package com.example.sportify;

import entities.Evenement;
import entities.Participer;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import services.evenementService;
import services.participerService;

import java.io.File;
import java.time.LocalDateTime;

public class ParticiperFrontController {
    @FXML
    private FlowPane eventsFlowPane;

    private final evenementService evenementService = new evenementService();
    private final participerService participerService = new participerService();

    @FXML
    public void initialize() {
        loadEvents();
    }

    private void loadEvents() {
        eventsFlowPane.getChildren().clear();
        for (Evenement event : evenementService.afficherEvenement()) {
            VBox eventCard = createEventCard(event);
            eventsFlowPane.getChildren().add(eventCard);
        }
    }

    private VBox createEventCard(Evenement event) {
        VBox card = new VBox(10);
        card.setStyle("-fx-background-color: rgba(0, 0, 0, 0.5); -fx-padding: 15; -fx-background-radius: 10; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.3), 10, 0, 0, 0);");
        card.setPrefWidth(230);

        // Image de l'événement
        ImageView imageView = new ImageView();
        imageView.setFitWidth(200);
        imageView.setFitHeight(120);
        imageView.setPreserveRatio(true);
        imageView.setStyle("-fx-background-radius: 5;");
        if (event.getImage() != null && !event.getImage().isEmpty()) {
            File file = new File(event.getImage());
            if (file.exists()) {
                imageView.setImage(new Image(file.toURI().toString()));
            }
        }

        // Informations de l'événement
        Label nameLabel = new Label(event.getNomEvenement());
        nameLabel.setStyle("-fx-font-size: 18; -fx-font-weight: bold; -fx-text-fill: white;");
        
        Label typeLabel = new Label("Type: " + event.getTypeEvenement());
        typeLabel.setStyle("-fx-font-size: 14; -fx-text-fill: #64dd17;");
        
        Label locationLabel = new Label("Lieu: " + event.getLocalisation());
        locationLabel.setStyle("-fx-font-size: 14; -fx-text-fill: white;");
        
        Label dateLabel = new Label("Date: " + event.getDateEvenement().toString());
        dateLabel.setStyle("-fx-font-size: 14; -fx-text-fill: white;");
        
        Label priceLabel = new Label("Prix: " + event.getPrix() + " DT");
        priceLabel.setStyle("-fx-font-size: 14; -fx-text-fill: #64dd17; -fx-font-weight: bold;");

        // Bouton Participer
        Button participerBtn = new Button("Participer");
        participerBtn.setStyle("-fx-background-color: #64dd17; -fx-text-fill: white; -fx-font-size: 14; -fx-font-weight: bold; -fx-background-radius: 5; -fx-padding: 8 15;");
        participerBtn.setOnAction(e -> showParticipationDialog(event));

        card.getChildren().addAll(imageView, nameLabel, typeLabel, locationLabel, dateLabel, priceLabel, participerBtn);
        return card;
    }

    private void showParticipationDialog(Evenement event) {
        Stage dialogStage = new Stage();
        dialogStage.initModality(Modality.APPLICATION_MODAL);
        dialogStage.setTitle("Participer à l'événement");

        VBox dialogBox = new VBox(15);
        dialogBox.setStyle("-fx-padding: 25; -fx-background-color: rgba(0, 0, 0, 0.8); -fx-background-radius: 10;");

        Label titleLabel = new Label("Participer à " + event.getNomEvenement());
        titleLabel.setStyle("-fx-font-size: 18; -fx-font-weight: bold; -fx-text-fill: white;");

        TextField statusField = new TextField();
        statusField.setPromptText("Status");
        statusField.setStyle("-fx-background-color: rgba(255, 255, 255, 0.9); -fx-padding: 8; -fx-background-radius: 5;");
        
        TextField roleField = new TextField();
        roleField.setPromptText("Rôle");
        roleField.setStyle("-fx-background-color: rgba(255, 255, 255, 0.9); -fx-padding: 8; -fx-background-radius: 5;");

        Button submitButton = new Button("Confirmer la participation");
        submitButton.setStyle("-fx-background-color: #64dd17; -fx-text-fill: white; -fx-font-size: 14; -fx-font-weight: bold; -fx-background-radius: 5; -fx-padding: 10 20;");
        submitButton.setOnAction(e -> {
            if (!statusField.getText().isEmpty() && !roleField.getText().isEmpty()) {
                Participer participation = new Participer(
                    event.getId(),
                    statusField.getText(),
                    roleField.getText()
                );
                participerService.ajouterParticipation(participation);
                dialogStage.close();
                showAlert("Succès", "Vous avez participé à l'événement avec succès!");
            } else {
                showAlert("Erreur", "Veuillez remplir tous les champs");
            }
        });

        dialogBox.getChildren().addAll(
            titleLabel,
            new Label("Status:"),
            statusField,
            new Label("Rôle:"),
            roleField,
            submitButton
        );

        Scene dialogScene = new Scene(dialogBox);
        dialogStage.setScene(dialogScene);
        dialogStage.show();
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
