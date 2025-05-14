package com.example.sportify;

import entities.Message;
import entities.Sujet;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldListCell;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Callback;
import services.messageService;
import services.sujetService;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

public class MessageController {

    @FXML private ListView<Sujet> sujetListView;
    @FXML private ListView<Message> messageListView;
    @FXML private TextArea messageTextArea;
    @FXML private Button envoyerBtn;
    @FXML private Button ascBtn; // bouton pour trier ASC
    @FXML private Button descBtn; // bouton pour trier DESC

    private sujetService sService = new sujetService();
    private messageService mService = new messageService();
    private ObservableList<Sujet> sujets = FXCollections.observableArrayList();
    private ObservableList<Message> messages = FXCollections.observableArrayList();
    private Sujet currentSujet;
    private boolean isAsc = false; // par défaut DESC

    @FXML
    public void initialize() {
        try {
            loadSujets();
            setupSelectionListeners();
            setupMessageCellFactory();
            setupSortButtons();
        } catch (SQLException e) {
            showAlert("Erreur", "Erreur de chargement", e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private void setupSortButtons() {
        if (ascBtn != null) {
            ascBtn.setOnAction(e -> {
                isAsc = true;
                try {
                    refreshMessages();
                } catch (SQLException ex) {
                    showAlert("Erreur", "Erreur de tri", ex.getMessage(), Alert.AlertType.ERROR);
                }
            });
        }
        if (descBtn != null) {
            descBtn.setOnAction(e -> {
                isAsc = false;
                try {
                    refreshMessages();
                } catch (SQLException ex) {
                    showAlert("Erreur", "Erreur de tri", ex.getMessage(), Alert.AlertType.ERROR);
                }
            });
        }
    }

    private void setupMessageCellFactory() {
        messageListView.setCellFactory(param -> new MessageListCell());
    }

    private class MessageListCell extends ListCell<Message> {
        private final HBox container;
        private final Label contentLabel;
        private final Label dateLabel;
        private final Button editButton;
        private final Button deleteButton;
        private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

        public MessageListCell() {
            contentLabel = new Label();
            contentLabel.setWrapText(true);
            contentLabel.setMaxWidth(600);

            dateLabel = new Label();
            dateLabel.setStyle("-fx-font-size: 10px; -fx-text-fill: #666;");

            editButton = new Button("Modifier");
            editButton.setStyle("-fx-background-color: #3498db; -fx-text-fill: white;");
            editButton.setOnAction(event -> handleEditMessage(getItem()));

            deleteButton = new Button("Supprimer");
            deleteButton.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white;");
            deleteButton.setOnAction(event -> handleDeleteMessage(getItem()));

            VBox messageBox = new VBox(5, contentLabel, dateLabel);
            HBox buttonBox = new HBox(5, editButton, deleteButton);

            container = new HBox(10, messageBox, buttonBox);
            container.setStyle("-fx-padding: 10; -fx-background-color: #f9f9f9; -fx-background-radius: 5;");
        }

        @Override
        protected void updateItem(Message message, boolean empty) {
            super.updateItem(message, empty);

            if (empty || message == null) {
                setGraphic(null);
            } else {
                contentLabel.setText(message.getContenu());
                dateLabel.setText("Modifié le: " + message.getDatepublication().format(formatter));
                setGraphic(container);
            }
        }
    }

    private void handleEditMessage(Message message) {
        if (message == null) return;

        // Créer une boîte de dialogue pour la modification
        TextInputDialog dialog = new TextInputDialog(message.getContenu());
        dialog.setTitle("Modifier le message");
        dialog.setHeaderText("Modifiez le contenu de votre message");
        dialog.setContentText("Message:");

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(newContent -> {
            try {
                message.setContenu(newContent);
                // Le filtrage des mauvais mots est fait dans le service
                mService.update(message);
                refreshMessages();
            } catch (SQLException e) {
                showAlert("Erreur", "Erreur de modification", e.getMessage(), Alert.AlertType.ERROR);
            }
        });
    }

    private void handleDeleteMessage(Message message) {
        if (message == null) return;

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation de suppression");
        alert.setHeaderText("Supprimer ce message ?");
        alert.setContentText("Êtes-vous sûr de vouloir supprimer ce message ?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                mService.delete(message.getId());
                refreshMessages();
            } catch (SQLException e) {
                showAlert("Erreur", "Erreur de suppression", e.getMessage(), Alert.AlertType.ERROR);
            }
        }
    }

    private void refreshMessages() throws SQLException {
        if (currentSujet != null) {
            loadMessages(currentSujet.getId());
        }
    }

    private void loadSujets() throws SQLException {
        sujets.setAll(sService.getAll());
        sujetListView.setItems(sujets);
        sujetListView.setCellFactory(param -> new ListCell<Sujet>() {
            @Override
            protected void updateItem(Sujet sujet, boolean empty) {
                super.updateItem(sujet, empty);
                if (empty || sujet == null) {
                    setText(null);
                } else {
                    setText(sujet.getTitre() + " (" + sujet.getCategorie() + ")");
                }
            }
        });
    }

    private void loadMessages(int sujetId) throws SQLException {
        currentSujet = sujets.stream().filter(s -> s.getId() == sujetId).findFirst().orElse(null);
        if (isAsc) {
            messages.setAll(mService.getBySujetIdAsc(sujetId));
        } else {
            messages.setAll(mService.getBySujetIdDesc(sujetId));
        }
        messageListView.setItems(messages);
    }

    private void setupSelectionListeners() {
        sujetListView.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                try {
                    loadMessages(newVal.getId());
                } catch (SQLException e) {
                    showAlert("Erreur", "Erreur de chargement", e.getMessage(), Alert.AlertType.ERROR);
                }
            }
        });
    }

    @FXML
    private void handleEnvoyerMessage() {
        if (currentSujet != null && !messageTextArea.getText().isEmpty()) {
            try {
                Message nouveauMessage = new Message(currentSujet.getId(), messageTextArea.getText());
                mService.add(nouveauMessage);
                messageTextArea.clear();
                loadMessages(currentSujet.getId());
            } catch (SQLException e) {
                showAlert("Erreur", "Erreur d'envoi", e.getMessage(), Alert.AlertType.ERROR);
            }
        } else {
            showAlert("Avertissement", "Sélection requise", "Veuillez sélectionner un sujet et écrire un message", Alert.AlertType.WARNING);
        }
    }

    private void showAlert(String title, String header, String content, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
}