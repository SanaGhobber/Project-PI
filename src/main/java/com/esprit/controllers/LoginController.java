package com.esprit.controllers;

import com.esprit.models.Personne;
import com.esprit.services.ServicePersonne;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginController {
    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;
    @FXML private Button loginButton;
    @FXML private Hyperlink signupLink;
    @FXML private Hyperlink forgetPasswordLink;

    private final ServicePersonne servicePersonne = new ServicePersonne();

    @FXML
    public void initialize() {
        // Configure login button
        loginButton.setOnAction(event -> handleLogin());

        // Set signup link text and action
        signupLink.setText("signup");
        signupLink.setOnAction(event -> redirectToSignup());

        // Set forget password link text and action
        forgetPasswordLink.setText("mot de passe oublié");
        forgetPasswordLink.setOnAction(event -> redirectToForgetPassword());
    }

    private void handleLogin() {
        try {
            String email = emailField.getText();
            String password = passwordField.getText();

            if (email.isEmpty() || password.isEmpty()) {
                showAlert(Alert.AlertType.ERROR, "Erreur", "Email et mot de passe requis");
                return;
            }

            Personne personne = servicePersonne.getByEmail(email);
            if (personne != null && personne.getPassword().equals(password)) {
                showAlert(Alert.AlertType.INFORMATION, "Succès", "Connexion réussie!");
                redirectToDashboard(personne.getRole());
            } else {
                showAlert(Alert.AlertType.ERROR, "Erreur", "Email ou mot de passe incorrect");
            }
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Erreur lors de la connexion: " + e.getMessage());
        }
    }

    private void redirectToSignup() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/AjoutPersonne.fxml"));
            Stage stage = (Stage) signupLink.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Inscription");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void redirectToForgetPassword() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/ForgotPassword.fxml"));
            Stage stage = (Stage) forgetPasswordLink.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Mot de passe oublié");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void redirectToDashboard(String role) {
        try {
            String fxmlPath = "/" + role.toLowerCase() + "_dashboard.fxml";
            Parent root = FXMLLoader.load(getClass().getResource(fxmlPath));
            Stage stage = (Stage) loginButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Tableau de bord " + role);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
