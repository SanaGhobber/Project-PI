package com.esprit.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import javafx.event.ActionEvent;

import java.io.IOException;

public class MainMenuController {

    @FXML
    private Button btnAddPerson;

    @FXML
    private Button btnAddEvent;

    @FXML
    private void handleAddPerson(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/AjoutPersonne.fxml"));
            Stage stage = (Stage) btnAddPerson.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Add Person");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleAddEvent(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/AjoutEvenement.fxml"));
            Stage stage = (Stage) btnAddEvent.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Add Event");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
