package com.esprit.controllers;

import com.esprit.models.Personne;
import com.esprit.services.ServicePersonne;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

public class AjoutPersonne {
    @FXML private TextField nom;
    @FXML private TextField prenom;
    @FXML private TextField email;
    @FXML private PasswordField password;
    @FXML private ComboBox<String> role;
    @FXML private Button addButton;
    @FXML private Hyperlink loginLink;

    private final ServicePersonne servicePersonne = new ServicePersonne();
    private final List<String> roles = Arrays.asList("CLIENT", "SUPPLIERPRODUCT", "SUPPLIEREVENT");
    private final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$");

    @FXML
    public void initialize() {
        role.setItems(FXCollections.observableArrayList(roles));
        role.getSelectionModel().selectFirst();
        
        // Add validation listeners
        nom.textProperty().addListener((obs, oldVal, newVal) -> validateForm());
        prenom.textProperty().addListener((obs, oldVal, newVal) -> validateForm());
        email.textProperty().addListener((obs, oldVal, newVal) -> validateForm());
        password.textProperty().addListener((obs, oldVal, newVal) -> validateForm());
        role.valueProperty().addListener((obs, oldVal, newVal) -> validateForm());

        addButton.setOnAction(event -> handleSignup());
        loginLink.setText("Connexion");
        loginLink.setOnAction(event -> redirectToLogin());
    }

    private void validateForm() {
        boolean isValid = !nom.getText().isEmpty() 
            && !prenom.getText().isEmpty() 
            && !email.getText().isEmpty() 
            && !password.getText().isEmpty() 
            && role.getValue() != null;
        addButton.setDisable(!isValid);
    }

    private void handleSignup() {
        try {
            String nomValue = nom.getText();
            String prenomValue = prenom.getText();
            String emailValue = email.getText();
            String passwordValue = password.getText();
            String roleValue = role.getValue();

            // Validate all fields are filled
            if (nomValue.isEmpty() || prenomValue.isEmpty() || emailValue.isEmpty() || passwordValue.isEmpty() || roleValue == null) {
                showAlert(Alert.AlertType.ERROR, "Erreur", "Tous les champs sont obligatoires");
                return;
            }

            // Validate email format
            if (!EMAIL_PATTERN.matcher(emailValue).matches()) {
                showAlert(Alert.AlertType.ERROR, "Erreur", "Format d'email invalide. Exemple: exemple@gmail.com");
                return;
            }

            // Validate password length (minimum 6 characters)
            if (passwordValue.length() < 6) {
                showAlert(Alert.AlertType.ERROR, "Erreur", "Le mot de passe doit contenir au moins 6 caractères");
                return;
            }

            Personne personne = new Personne(nomValue, prenomValue, emailValue, passwordValue, roleValue);
            servicePersonne.ajouter(personne);
            
            showAlert(Alert.AlertType.INFORMATION, "Succès", "Compte créé avec succès!");
            
            // Clear form after successful registration
            nom.clear();
            prenom.clear();
            email.clear();
            password.clear();
            role.setValue(null);
            
            // Redirect to login page
            redirectToLogin();
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Erreur lors de la création du compte: " + e.getMessage());
        }
    }

    private void redirectToLogin() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Login.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) loginLink.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Connexion - Sportify");
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Erreur lors du chargement de la page de connexion");
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
