package com.esprit.controllers;

import com.esprit.services.ServicePersonne;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class ForgotPasswordController {

    @FXML
    private TextField emailField;

    @FXML
    private Button resetButton;

    private final ServicePersonne servicePersonne = new ServicePersonne();

    @FXML
    public void initialize() {
        resetButton.setOnAction(event -> handleReset());
    }

    private void handleReset() {
        String email = emailField.getText();
        if (email == null || email.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Veuillez saisir votre adresse email.");
            return;
        }

        boolean userExists = servicePersonne.existsByEmail(email);
        if (userExists) {
            // Here you would implement sending a reset email or other reset logic
            showAlert(Alert.AlertType.INFORMATION, "Succès", "Un lien de réinitialisation a été envoyé à votre adresse email.");
        } else {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Aucun utilisateur trouvé avec cette adresse email.");
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
