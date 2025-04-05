package com.esprit.controllers;

import com.esprit.models.Personne;
import com.esprit.services.ServicePersonne2;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;

import java.io.IOException;

public class AjoutPersonne {

    @FXML
    private TextField tfNom;

    @FXML
    private TextField tfPrenom;

    @FXML
    void addPerson(ActionEvent event) throws IOException {
        ServicePersonne2 sp = new ServicePersonne2();
        sp.ajouter(new Personne(tfNom.getText(), tfPrenom.getText()));

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation d'ajout");
        alert.setContentText("Personne ajoutée !");
        alert.show();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/DetailsPersonne.fxml"));
        Parent root = loader.load();
        tfPrenom.getScene().setRoot(root);

        DetailsPersonne dp = loader.getController();
        dp.setLbNom(tfNom.getText());
        dp.setLbPrenom(tfPrenom.getText());
    }

}
