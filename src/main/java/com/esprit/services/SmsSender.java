package com.esprit.services;

import com.twilio.Twilio;
import com.twilio.exception.ApiException;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

import java.sql.Connection;

public class SmsSender {
    // Configuration (à remplacer par vos valeurs)
    private static final String ACCOUNT_SID = "";
    private static final String AUTH_TOKEN = "";
    private static final String TWILIO_NUMBER = ""; // Votre numéro Twilio

    static {
        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
    }

    private final Connection connection;


    public SmsSender() {
        this.connection = com.esprit.utils.DataSource.getInstance().getConnection();
        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
    }
    public static void sendUniversalSMS(String phoneNumber, String message) {
        try {
            validatePhoneNumber(phoneNumber);

            Message twilioMessage = Message.creator(
                    new PhoneNumber(phoneNumber),
                    new PhoneNumber(TWILIO_NUMBER),
                    message
            ).create();

            logSuccess(phoneNumber, twilioMessage.getSid());

        } catch (ApiException e) {
            handleTwilioError(e, phoneNumber);
        } catch (Exception e) {
            System.err.println("❌ Erreur générale : " + e.getMessage());
        }
    }

    private static void validatePhoneNumber(String number) {
        if (!number.matches("^\\+[1-9]\\d{1,14}$")) {
            throw new IllegalArgumentException("Format E.164 invalide. Ex: +21612345678");
        }
    }

    private static void logSuccess(String number, String sid) {
        System.out.printf("✅ SMS envoyé à %s\nSID: %s\nCoût: ~%s\n",
                number,
                sid,
                getEstimatedCost(number.substring(0, 3))); // Estimation du coût
    }

    private static void handleTwilioError(ApiException e, String number) {
        System.err.printf("❌ Échec pour %s\nCode: %d\nMessage: %s\n",
                number,
                e.getStatusCode(),
                e.getMessage());

        if (e.getStatusCode() == 21408) {
            System.err.println("Solution: https://twil.io/verify-number");
        }
    }

    private static String getEstimatedCost(String countryCode) {
        // Exemples de coûts (à vérifier sur https://twil.io/sms-pricing)
        return switch (countryCode) {

            case "+216" -> "0.025$ (Tunisie)";
            default -> "Consultez twil.io/sms-pricing";
        };
    }

    public static void main(String[] args) {
        // Exemple d'utilisation
        sendUniversalSMS("+21699283413", " hellooooooooooooooooooooo");
    }
}
