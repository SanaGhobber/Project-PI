package com.example.sportify;

import entities.Evenement;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import services.evenementService;

import java.io.File;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class AdminEventController {

    @FXML private TextField nomEvenementField;
    @FXML private TextField typeEvenementField;
    @FXML private DatePicker dateEvenementPicker;
    @FXML private TextField localisationField;
    @FXML private TextField prixField;
    @FXML private Button importImageBtn;
    @FXML private ImageView eventImageView;
    @FXML private ListView<Evenement> eventsListView;

    private final evenementService evenementService = new evenementService();
    private final ObservableList<Evenement> observableEvenements = FXCollections.observableArrayList();
    private String selectedImagePath = null;

    private Evenement selectedEvenement = null;

    @FXML
    public void initialize() {
        refreshList();
        eventsListView.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            selectedEvenement = newVal;
            if (newVal != null) {
                populateFields(newVal);
            }
        });
    }

    private void refreshList() {
        observableEvenements.setAll(evenementService.afficherEvenement());
        eventsListView.setItems(observableEvenements);
    }

    private void populateFields(Evenement e) {
        nomEvenementField.setText(e.getNomEvenement());
        typeEvenementField.setText(e.getTypeEvenement());
        dateEvenementPicker.setValue(e.getDateEvenement().toLocalDate());
        localisationField.setText(e.getLocalisation());
        prixField.setText(e.getPrix().toString());

        if (e.getImage() != null && !e.getImage().isEmpty()) {
            File file = new File(e.getImage());
            if (file.exists()) {
                Image image = new Image(file.toURI().toString());
                eventImageView.setImage(image);
                selectedImagePath = e.getImage();
            }
        }
    }

    @FXML
    private void handleImportImage(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choisir une image");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Images", "*.png", "*.jpg", "*.jpeg"));
        File file = fileChooser.showOpenDialog(null);

        if (file != null) {
            selectedImagePath = file.getAbsolutePath();
            Image image = new Image(file.toURI().toString());
            eventImageView.setImage(image);
        }
    }

    @FXML
    private void handleAjouter(ActionEvent event) {
        try {
            Evenement e = new Evenement(
                    nomEvenementField.getText(),
                    typeEvenementField.getText(),
                    localisationField.getText(),
                    selectedImagePath,
                    LocalDateTime.from(dateEvenementPicker.getValue().atStartOfDay()),
                    Double.parseDouble(prixField.getText())
            );
            evenementService.ajouterEvenement(e);
            refreshList();
            clearFields();
        } catch (Exception ex) {
            showAlert("Erreur", "Vérifiez les champs saisis.");
        }
    }

    @FXML
    private void handleModifier(ActionEvent event) {
        if (selectedEvenement != null) {
            try {
                selectedEvenement.setNomEvenement(nomEvenementField.getText());
                selectedEvenement.setTypeEvenement(typeEvenementField.getText());
                selectedEvenement.setLocalisation(localisationField.getText());
                selectedEvenement.setImage(selectedImagePath);
                selectedEvenement.setDateEvenement(LocalDateTime.from(dateEvenementPicker.getValue().atStartOfDay()));
                selectedEvenement.setPrix(Double.parseDouble(prixField.getText()));

                evenementService.modifierEvenement(selectedEvenement);
                refreshList();
                clearFields();
            } catch (Exception ex) {
                showAlert("Erreur", "Erreur lors de la modification.");
            }
        }
    }

    @FXML
    private void handleSupprimer(ActionEvent event) {
        if (selectedEvenement != null) {
            evenementService.supprimerEvenement(selectedEvenement.getId());
            refreshList();
            clearFields();
        }
    }

    private void clearFields() {
        nomEvenementField.clear();
        typeEvenementField.clear();
        localisationField.clear();
        prixField.clear();
        dateEvenementPicker.setValue(null);
        eventImageView.setImage(null);
        selectedImagePath = null;
        selectedEvenement = null;
        eventsListView.getSelectionModel().clearSelection();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.show();
    }
}
