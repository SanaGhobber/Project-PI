package com.esprit.services;

import com.esprit.models.EmailVerificationToken;
import com.esprit.models.Personne;
import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;

import java.io.IOException;
import java.sql.*;
import java.util.Date;

public class EmailVerificationService {
    private final Connection connection;
    private final ServicePersonne userService;
    private final String apiKey = ""; // TODO: Replace with actual API key
    private final SendGrid sendGrid;

    // Default constructor without UserService dependency
    public EmailVerificationService() {
        this.connection = com.esprit.utils.DataSource.getInstance().getConnection();
        this.userService = null; // No userService dependency
        this.sendGrid = new SendGrid(apiKey);
    }

    public EmailVerificationToken createVerificationToken(Personne user) throws SQLException, IOException {
        // Generate 6-digit random numeric code
        java.security.SecureRandom random = new java.security.SecureRandom();
        int codeInt = 100000 + random.nextInt(900000);
        String code = String.valueOf(codeInt);
        Date expiryDate = new Date(System.currentTimeMillis() + 24 * 60 * 60 * 1000); // 24 hours

        // Remove existing tokens for this user
        String deleteSql = "DELETE FROM email_verification_tokens WHERE user_id = ?";
        try (PreparedStatement deletePs = connection.prepareStatement(deleteSql)) {
            deletePs.setInt(1, user.getId());
            deletePs.executeUpdate();
        }

        // Insert new token
        String insertSql = "INSERT INTO email_verification_tokens (user_id, token, expiry_date) VALUES (?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(insertSql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, user.getId());
            ps.setString(2, code);
            ps.setTimestamp(3, new Timestamp(expiryDate.getTime()));

            int affectedRows = ps.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating verification code failed, no rows affected.");
            }

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    EmailVerificationToken verificationToken = new EmailVerificationToken(user.getId(), code, expiryDate);
                    verificationToken.setId(rs.getInt(1));

                    sendVerificationEmail(user, code); // Send email with the code
                    return verificationToken;
                } else {
                    throw new SQLException("Creating verification code failed, no ID obtained.");
                }
            }
        }
    }

    public boolean validateVerificationToken(String token) throws SQLException {
        String sql = "SELECT user_id, expiry_date FROM email_verification_tokens WHERE token = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, token);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    int userId = rs.getInt("user_id");
                    Timestamp expiryDate = rs.getTimestamp("expiry_date");

                    if (expiryDate.before(new Timestamp(System.currentTimeMillis()))) {
                        deleteToken(token);
                        return false; // Token expired
                    } else {
                        // Without userService, just return true here
                        // User status update should be handled elsewhere
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public void deleteToken(String token) throws SQLException {
        String sql = "DELETE FROM email_verification_tokens WHERE token = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, token);
            ps.executeUpdate();
        }
    }

    public void sendVerificationEmail(Personne user, String code) throws IOException {
        String toEmail = user.getEmail();
        String subject = "Email Verification";
        String contentText = "Dear " + user.getPrenom() + ",\n\n"
                + "Your email verification code is: " + code + "\n\n"
                + "Please enter this code in the application to verify your email address.\n\n"
                + "If you did not register, please ignore this email.\n\n"
                + "Best regards,\nYour Company";

        Email from = new Email("esprit.solaris@gmail.com"); // TODO: Replace with your sender email
        Email to = new Email(toEmail);
        Content content = new Content("text/plain", contentText);
        Mail mail = new Mail(from, subject, to, content);

        Request request = new Request();
        request.setMethod(Method.POST);
        request.setEndpoint("mail/send");
        request.setBody(mail.build());

        Response response = sendGrid.api(request);
        System.out.println("→ Status: " + response.getStatusCode());
        System.out.println("→ Body:   " + response.getBody());
        System.out.println("→ Hdrs:   " + response.getHeaders());

        if (response.getStatusCode() >= 200 && response.getStatusCode() < 300) {
            System.out.println("Verification email sent successfully to " + toEmail);
        } else {
            System.err.println("Failed to send verification email. Status Code: " + response.getStatusCode());
            System.err.println("Response Body: " + response.getBody());
        }
    }}