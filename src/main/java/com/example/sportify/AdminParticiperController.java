package com.example.sportify;

import entities.Participer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import services.participerService;

public class AdminParticiperController {
    @FXML
    private TableView<Participer> participerTable;
    
    @FXML
    private TableColumn<Participer, Integer> idColumn;
    
    @FXML
    private TableColumn<Participer, Integer> eventColumn;
    
    @FXML
    private TableColumn<Participer, String> statusColumn;
    
    @FXML
    private TableColumn<Participer, String> roleColumn;

    private final participerService participerService = new participerService();
    private final ObservableList<Participer> participationsList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        // Configuration des colonnes
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        eventColumn.setCellValueFactory(new PropertyValueFactory<>("idEvenement"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        roleColumn.setCellValueFactory(new PropertyValueFactory<>("role"));

        // Chargement des données
        loadParticipations();
    }

    private void loadParticipations() {
        participationsList.clear();
        participationsList.addAll(participerService.afficherToutesParticipations());
        participerTable.setItems(participationsList);
    }
} 