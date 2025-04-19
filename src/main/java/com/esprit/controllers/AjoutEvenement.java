package com.esprit.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

import com.esprit.models.evenements.Evenement;
import com.esprit.models.evenements.Sport;
import com.esprit.services.ServiceEvenement;

public class AjoutEvenement {

    @FXML
    private TextField nom;

    @FXML
    private TextField lieu;

    @FXML
    private TextField capaciteMax;

    @FXML
    private TextField sport;

    private ServiceEvenement serviceEvenement = new ServiceEvenement();

    @FXML
    private void handleAddButton() {
        try {
            String nomValue = nom.getText();
            String lieuValue = lieu.getText();
            String capaciteMaxValue = capaciteMax.getText();
            String sportValue = sport.getText();

            if (nomValue.isEmpty() || lieuValue.isEmpty() || capaciteMaxValue.isEmpty() || sportValue.isEmpty()) {
                Alert alert = new Alert(AlertType.WARNING);
                alert.setTitle("Validation Error");
                alert.setHeaderText(null);
                alert.setContentText("Please fill in all fields.");
                alert.showAndWait();
                return;
            }

            int capaciteMaxParsed = Integer.parseInt(capaciteMaxValue);

            Evenement evenement = new Evenement(nomValue, lieuValue, capaciteMaxParsed, sportValue);

            serviceEvenement.ajouter(evenement);

            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setHeaderText(null);
            alert.setContentText("Event added successfully!");
            alert.showAndWait();

            nom.clear();
            lieu.clear();
            capaciteMax.clear();
            sport.clear();

        } catch (Exception e) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("An error occurred while adding the event: " + e.getMessage());
            alert.showAndWait();
        }
    }
}
