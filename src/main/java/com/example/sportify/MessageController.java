package com.example.sportify;

import entities.Message;
import entities.Sujet;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldListCell;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Callback;
import services.messageService;
import services.sujetService;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

// ZXing
import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.Result;
import com.google.zxing.NotFoundException;
import com.google.zxing.LuminanceSource;
import com.google.zxing.ReaderException;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import javafx.stage.FileChooser;
import javafx.scene.image.Image;
import javafx.embed.swing.SwingFXUtils;

import org.json.JSONObject; // Ajout pour gestion JSON
public class MessageController {

    @FXML private ListView<Sujet> sujetListView;
    @FXML private ListView<Message> messageListView;
    @FXML private TextArea messageTextArea;
    @FXML private Button envoyerBtn;
    @FXML private Button ascBtn; // bouton pour trier ASC
    @FXML private Button descBtn; // bouton pour trier DESC

    // QR Code
    @FXML private Button btnAfficherQR;
    @FXML private ImageView qrImageView;
    @FXML private Button btnScannerQR;

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

        // QR Code handlers
        if (btnAfficherQR != null) {
            btnAfficherQR.setOnAction(e -> afficherQRCodeSujet());
        }
        if (btnScannerQR != null) {
            btnScannerQR.setOnAction(e -> scannerQRCodeSujet());
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

    // Générer et afficher le QR code du sujet sélectionné
    private void afficherQRCodeSujet() {
        Sujet sujet = sujetListView.getSelectionModel().getSelectedItem();
        if (sujet == null) {
            showAlert("Avertissement", "Aucun sujet sélectionné", "Veuillez sélectionner un sujet pour générer son QR code.", Alert.AlertType.WARNING);
            return;
        }
        try {
            // Encodage JSON de toutes les infos du sujet
            JSONObject json = new JSONObject();
            json.put("titre", sujet.getTitre());
            json.put("contenu", sujet.getContenu());
            json.put("categorie", sujet.getCategorie());
            json.put("dateCreation", sujet.getDateCreation() != null ? sujet.getDateCreation().toString() : "");
            String qrContent = json.toString();
            int size = 200;
            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            BitMatrix bitMatrix = qrCodeWriter.encode(qrContent, BarcodeFormat.QR_CODE, size, size);
            BufferedImage bufferedImage = MatrixToImageWriter.toBufferedImage(bitMatrix);
            Image fxImage = SwingFXUtils.toFXImage(bufferedImage, null);
            qrImageView.setImage(fxImage);
        } catch (WriterException e) {
            showAlert("Erreur QR", "Erreur de génération du QR code", e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    // Scanner un QR code depuis une image et afficher le sujet correspondant
    private void scannerQRCodeSujet() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Sélectionner une image QR code");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Images", "*.png", "*.jpg", "*.jpeg", "*.bmp")
        );
        File file = fileChooser.showOpenDialog(btnScannerQR.getScene().getWindow());
        if (file != null) {
            try {
                BufferedImage bufferedImage = ImageIO.read(file);
                LuminanceSource source = new BufferedImageLuminanceSource(bufferedImage);
                BinaryBitmap bitmap = new BinaryBitmap(new com.google.zxing.common.HybridBinarizer(source));
                Result result = new MultiFormatReader().decode(bitmap);
                String qrText = result.getText();
                // On suppose que le QR code contient un JSON avec toutes les infos
                JSONObject json = new JSONObject(qrText);
                String titre = json.optString("titre", "");
                String contenu = json.optString("contenu", "");
                String categorie = json.optString("categorie", "");
                String dateCreation = json.optString("dateCreation", "");
                String info = "Titre : " + titre + "\nCatégorie : " + categorie + "\nDate : " + dateCreation + "\nContenu : " + contenu;
                showAlert("Sujet scanné", "Informations du sujet", info, Alert.AlertType.INFORMATION);
            } catch (IOException | NotFoundException | org.json.JSONException e) {
                showAlert("Erreur QR", "Erreur lors du scan du QR code", e.getMessage(), Alert.AlertType.ERROR);
            }
        }
    }
}