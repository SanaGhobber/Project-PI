package com.esprit.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

import com.esprit.models.Personne;
import com.esprit.services.ServicePersonne;

public class AjoutPersonne {

    @FXML
    private TextField email;

    @FXML
    private TextField nom;

    @FXML
    private TextField password;

    @FXML
    private TextField prenom;

    @FXML
    private TextField role;

    private ServicePersonne servicePersonne = new ServicePersonne();

    @FXML
    private void handleAddButton() {
        try {
            String nomValue = nom.getText();
            String prenomValue = prenom.getText();
            String passwordValue = password.getText();
            String emailValue = email.getText();
            String roleValue = role.getText();

            if (nomValue.isEmpty() || prenomValue.isEmpty() || passwordValue.isEmpty() || emailValue.isEmpty() || roleValue.isEmpty()) {
                Alert alert = new Alert(AlertType.WARNING);
                alert.setTitle("Validation Error");
                alert.setHeaderText(null);
                alert.setContentText("Please fill in all fields.");
                alert.showAndWait();
                return;
            }

            // Use constructor with parameters instead of no-arg constructor
            Personne p = new Personne(nomValue, prenomValue, emailValue, passwordValue, roleValue);

            servicePersonne.ajouter(p);

            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setHeaderText(null);
            alert.setContentText("Person added successfully!");
            alert.showAndWait();

            // Clear fields after adding
            nom.clear();
            prenom.clear();
            password.clear();
            email.clear();
            role.clear();

        } catch (Exception e) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("An error occurred while adding the person: " + e.getMessage());
            alert.showAndWait();
        }
    }
}
