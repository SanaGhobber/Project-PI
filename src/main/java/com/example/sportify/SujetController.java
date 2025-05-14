package com.example.sportify;

import entities.Pdf;
import entities.Sujet;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import services.sujetService;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class SujetController {

    @FXML private TextField tit;
    @FXML private TextArea ctn;
    @FXML private TextField categ;
    @FXML private DatePicker datesuj;
    @FXML private ListView<Sujet> SujetListView;
    @FXML private Button ajouterBtn;
    @FXML private Button modifierBtn;
    @FXML private Button supprimerBtn;
    @FXML private TextField searchField;
    @FXML
    private Button pdff;

    @FXML private ComboBox<String> categoryFilter;

    private sujetService sujetService = new sujetService();
    private ObservableList<Sujet> sujetObservableList = FXCollections.observableArrayList();
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    @FXML
    public void initialize() {
        // Configuration du TextArea pour le contenu
        ctn.setWrapText(true);

        try {
            loadSujets();
            setupListViewSelection();
            setupInputValidation();
            setupSearchField();
        } catch (SQLException e) {
            showAlert("Erreur", "Erreur lors du chargement des sujets", e.getMessage(), Alert.AlertType.ERROR);
        }
        initializeCategoryFilter();
        setupCategoryFilterListener();
    }

    private void initializeCategoryFilter() {
        try {
            // Récupérer toutes les catégories distinctes
            List<String> categories = sujetService.getAllCategories();
            categoryFilter.getItems().addAll(categories);
            categoryFilter.getItems().add(0, "Toutes les catégories"); // Option par défaut
            categoryFilter.setValue("Toutes les catégories"); // Sélection par défaut
        } catch (SQLException e) {
            showAlert("Erreur", "Erreur de chargement des catégories", e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private void setupCategoryFilterListener() {
        categoryFilter.valueProperty().addListener((obs, oldVal, newVal) -> {
            try {
                if (newVal == null || newVal.equals("Toutes les catégories")) {
                    loadSujets(); // Charger tous les sujets
                } else {
                    // Filtrer par catégorie sélectionnée
                    List<Sujet> filtered = sujetService.getByCategory(newVal);
                    sujetObservableList.setAll(filtered);
                    SujetListView.setItems(sujetObservableList);
                }
            } catch (SQLException e) {
                showAlert("Erreur", "Erreur de filtrage", e.getMessage(), Alert.AlertType.ERROR);
            }
        });
    }
    private void setupInputValidation() {
        // Limiter la longueur des champs
        tit.textProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal.length() > 100) {
                tit.setText(oldVal);
            }
        });

        categ.textProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal.length() > 50) {
                categ.setText(oldVal);
            }
        });
        // Validation de la date - ne peut pas être dans le passé
        datesuj.setDayCellFactory(picker -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                setDisable(date.isBefore(LocalDate.now()));
            }
        });
    }

    private void loadSujets() throws SQLException {
        List<Sujet> sujets = sujetService.getAll();
        sujetObservableList.setAll(sujets);
        SujetListView.setItems(sujetObservableList);

        // Personnalisation de l'affichage des sujets
        SujetListView.setCellFactory(param -> new ListCell<Sujet>() {
            @Override
            protected void updateItem(Sujet sujet, boolean empty) {
                super.updateItem(sujet, empty);
                if (empty || sujet == null) {
                    setGraphic(null);
                    setText(null);
                } else {
                    // Création d'un conteneur pour afficher toutes les informations
                    VBox container = new VBox(5);

                    // Titre en gras
                    Text titleText = new Text(sujet.getTitre());
                    titleText.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");

                    // Catégorie avec icône
                    HBox categoryBox = new HBox(5);
                    Text categoryLabel = new Text("Catégorie: ");
                    categoryLabel.setStyle("-fx-font-weight: bold;");
                    Text categoryValue = new Text(sujet.getCategorie());
                    categoryBox.getChildren().addAll(categoryLabel, categoryValue);

                    // Date de création
                    Text dateText = new Text("Créé le: " + sujet.getDateCreation().format(dateFormatter));
                    dateText.setStyle("-fx-font-style: italic; -fx-fill: #666;");

                    // Contenu (tronqué pour l'affichage)
                    String shortContent = sujet.getContenu().length() > 100
                            ? sujet.getContenu().substring(0, 100) + "..."
                            : sujet.getContenu();
                    Text contentText = new Text(shortContent);
                    contentText.setWrappingWidth(SujetListView.getWidth() - 20);

                    // Statistiques
                    HBox statsBox = new HBox(15);
                    Text motsText = new Text("📝 " + sujet.getNombreMots() + " mots");
                    statsBox.getChildren().addAll(motsText);

                    container.getChildren().addAll(titleText, categoryBox, dateText, contentText, statsBox);
                    setGraphic(container);
                }
            }
        });
    }

    private void setupListViewSelection() {
        SujetListView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                fillFormWithSelectedSujet(newSelection);
                modifierBtn.setDisable(false);
                supprimerBtn.setDisable(false);
            } else {
                modifierBtn.setDisable(true);
                supprimerBtn.setDisable(true);
            }
        });
    }

    private void fillFormWithSelectedSujet(Sujet sujet) {
        tit.setText(sujet.getTitre());
        ctn.setText(sujet.getContenu());
        categ.setText(sujet.getCategorie());
        datesuj.setValue(sujet.getDateCreation().toLocalDate());
    }

    @FXML
    private void handleAjouter() {
        try {
            if (validateFields()) {
                Sujet nouveauSujet = new Sujet(
                        tit.getText().trim(),
                        ctn.getText().trim(),
                        categ.getText().trim()
                );
                sujetService.add(nouveauSujet);
                clearFields();
                loadSujets();
                showAlert("Succès", "Sujet ajouté", "Le sujet a été ajouté avec succès", Alert.AlertType.INFORMATION);
            }
        } catch (SQLException e) {
            showAlert("Erreur", "Erreur lors de l'ajout", e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void handleModifier() {
        Sujet selectedSujet = SujetListView.getSelectionModel().getSelectedItem();
        if (selectedSujet != null) {
            try {
                if (validateFields()) {
                    selectedSujet.setTitre(tit.getText().trim());
                    selectedSujet.setContenu(ctn.getText().trim());
                    selectedSujet.setCategorie(categ.getText().trim());
                    selectedSujet.setDateCreation(datesuj.getValue().atStartOfDay());

                    sujetService.update(selectedSujet);
                    loadSujets();
                    showAlert("Succès", "Sujet modifié", "Le sujet a été modifié avec succès", Alert.AlertType.INFORMATION);
                }
            } catch (SQLException e) {
                showAlert("Erreur", "Erreur lors de la modification", e.getMessage(), Alert.AlertType.ERROR);
            }
        }
    }

    @FXML
    private void handleSupprimer() {
        Sujet selectedSujet = SujetListView.getSelectionModel().getSelectedItem();
        if (selectedSujet != null) {
            Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
            confirmAlert.setTitle("Confirmation de suppression");
            confirmAlert.setHeaderText("Supprimer le sujet");
            confirmAlert.setContentText("Êtes-vous sûr de vouloir supprimer ce sujet ?");

            if (confirmAlert.showAndWait().get() == ButtonType.OK) {
                try {
                    sujetService.delete(selectedSujet.getId());
                    clearFields();
                    loadSujets();
                    showAlert("Succès", "Sujet supprimé", "Le sujet a été supprimé avec succès", Alert.AlertType.INFORMATION);
                } catch (SQLException e) {
                    showAlert("Erreur", "Erreur lors de la suppression", e.getMessage(), Alert.AlertType.ERROR);
                }
            }
        }
    }

    private boolean validateFields() {
        StringBuilder errors = new StringBuilder();

        if (tit.getText().trim().isEmpty()) {
            errors.append("- Le titre est obligatoire\n");
        } else if (tit.getText().trim().length() < 5) {
            errors.append("- Le titre doit contenir au moins 5 caractères\n");
        }

        if (ctn.getText().trim().isEmpty()) {
            errors.append("- Le contenu est obligatoire\n");
        } else if (ctn.getText().trim().length() < 10) {
            errors.append("- Le contenu doit contenir au moins 10 caractères\n");
        }

        if (categ.getText().trim().isEmpty()) {
            errors.append("- La catégorie est obligatoire\n");
        }

        if (datesuj.getValue() == null) {
            errors.append("- La date est obligatoire\n");
        } else if (datesuj.getValue().isBefore(LocalDate.now())) {
            errors.append("- La date ne peut pas être dans le passé\n");
        }

        if (errors.length() > 0) {
            showAlert("Erreurs de validation", "Veuillez corriger les erreurs suivantes", errors.toString(), Alert.AlertType.WARNING);
            return false;
        }

        return true;
    }

    private void clearFields() {
        tit.clear();
        ctn.clear();
        categ.clear();
        datesuj.setValue(null);
        SujetListView.getSelectionModel().clearSelection();
    }

    private void setupSearchField() {
        searchField.textProperty().addListener((obs, oldVal, newVal) -> {
            performLocalSearch(newVal);
        });
    }

    private void performLocalSearch(String query) {
        String selectedCategory = categoryFilter.getValue();
        boolean filterByCategory = selectedCategory != null && !selectedCategory.equals("Toutes les catégories");

        String lowerQuery = query == null ? "" : query.toLowerCase();

        ObservableList<Sujet> filtered = sujetObservableList.filtered(sujet ->
                (!filterByCategory || sujet.getCategorie().equals(selectedCategory)) &&
                        (lowerQuery.isEmpty() ||
                                (sujet.getTitre() != null && sujet.getTitre().toLowerCase().contains(lowerQuery)) ||
                                (sujet.getCategorie() != null && sujet.getCategorie().toLowerCase().contains(lowerQuery)) ||
                                (sujet.getContenu() != null && sujet.getContenu().toLowerCase().contains(lowerQuery)))
        );
        SujetListView.setItems(filtered);
    }

    @FXML
    void pdff(ActionEvent event) {
        // Créer une instance de la classe Pdf
        Pdf pdfGenerator = new Pdf();
        try {
            pdfGenerator.generateSujetsPdf("rapport_sujets");
            System.out.println("PDF généré avec succès!");
        } catch (IOException | SQLException e) {
            System.err.println("Erreur lors de la génération du PDF: " + e.getMessage());
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