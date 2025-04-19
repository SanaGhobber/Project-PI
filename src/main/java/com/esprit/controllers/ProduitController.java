package com.esprit.controllers;


import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.event.ActionEvent;
import javafx.collections.ObservableList;
import javafx.collections.FXCollections;
import javafx.fxml.Initializable;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import com.esprit.models.Produit;
import com.esprit.services.ProduitService;

import java.net.URL;
import java.util.ResourceBundle;

public class ProduitController implements Initializable {

    @FXML private TextField tfId;
    @FXML private TextField tfNom;
    @FXML private TextField tfDescription;
    @FXML private TextField tfPrix;
    @FXML private TextField tfQuantite;
    @FXML private TextField tfCategorie;
    @FXML private TextField tfImagePath;

    @FXML private Button btnAjouter;
    @FXML private Button btnModifier;
    @FXML private Button btnSupprimer;
    @FXML private Button btnClear;

    @FXML private TableView<Produit> tableProduits;
    @FXML private TableColumn<Produit, Integer> colId;
    @FXML private TableColumn<Produit, String> colNom;
    @FXML private TableColumn<Produit, String> colDescription;
    @FXML private TableColumn<Produit, Double> colPrix;
    @FXML private TableColumn<Produit, Integer> colQuantite;
    @FXML private TableColumn<Produit, String> colCategorie;
    @FXML private TableColumn<Produit, String> colImagePath;

    private ProduitService produitService = new ProduitService();
    private ObservableList<Produit> produitList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Initialisation des colonnes
        colId.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getId()));
        colNom.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getNom()));
        colDescription.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getDescription()));
        colPrix.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getPrix()));
        colQuantite.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getQuantiteEnStock()));
        colCategorie.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getCategorie()));
        colImagePath.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getImagePath()));

        // Charger les données
        loadProducts();

        // Sélection d'une ligne pour affichage
        tableProduits.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldSel, newSel) -> showProductDetails(newSel)
        );
    }

    private void loadProducts() {
        produitList.setAll(produitService.getTousLesProduits());
        tableProduits.setItems(produitList);
    }

    private void showProductDetails(Produit p) {
        if (p != null) {
            tfId.setText(String.valueOf(p.getId()));
            tfNom.setText(p.getNom());
            tfDescription.setText(p.getDescription());
            tfPrix.setText(String.valueOf(p.getPrix()));
            tfQuantite.setText(String.valueOf(p.getQuantiteEnStock()));
            tfCategorie.setText(p.getCategorie());
            tfImagePath.setText(p.getImagePath());
        }
    }

    @FXML
    private void handleAjouter(ActionEvent event) {
        Produit p = new Produit();
        p.setNom(tfNom.getText());
        p.setDescription(tfDescription.getText());
        p.setPrix(Double.parseDouble(tfPrix.getText()));
        p.setQuantiteEnStock(Integer.parseInt(tfQuantite.getText()));
        p.setCategorie(tfCategorie.getText());
        p.setImagePath(tfImagePath.getText());
        produitService.ajouterProduit(p);
        loadProducts();
        clearFields();
    }

    @FXML
    private void handleModifier(ActionEvent event) {
        Produit p = new Produit();
        p.setId(Integer.parseInt(tfId.getText()));
        p.setNom(tfNom.getText());
        p.setDescription(tfDescription.getText());
        p.setPrix(Double.parseDouble(tfPrix.getText()));
        p.setQuantiteEnStock(Integer.parseInt(tfQuantite.getText()));
        p.setCategorie(tfCategorie.getText());
        p.setImagePath(tfImagePath.getText());
        produitService.modifierProduit(p);
        loadProducts();
        clearFields();
    }

    @FXML
    private void handleSupprimer(ActionEvent event) {
        int id = Integer.parseInt(tfId.getText());
        produitService.supprimerProduit(id);
        loadProducts();
        clearFields();
    }

    @FXML
    private void handleClear(ActionEvent event) {
        clearFields();
    }

    private void clearFields() {
        tfId.clear();
        tfNom.clear();
        tfDescription.clear();
        tfPrix.clear();
        tfQuantite.clear();
        tfCategorie.clear();
        tfImagePath.clear();
        tableProduits.getSelectionModel().clearSelection();
    }
}
