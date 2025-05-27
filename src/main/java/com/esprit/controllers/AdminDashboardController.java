package com.esprit.controllers;

import com.esprit.models.Personne;
import com.esprit.models.SupplierProduct;
import com.esprit.services.ServicePersonne;
import com.esprit.services.SeviceSupplierProduct;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;

public class AdminDashboardController {

    @FXML
    private TableView<Personne> usersTable;
    @FXML
    private TableColumn<Personne, String> colNom;
    @FXML
    private TableColumn<Personne, String> colPrenom;
    @FXML
    private TableColumn<Personne, String> colEmail;
    @FXML
    private TableColumn<Personne, String> colRole;
    @FXML
    private TableColumn<Personne, Void> colActionUser;

    @FXML
    private TableView<SupplierProduct> productsTable;
    @FXML
    private TableColumn<SupplierProduct, String> colProductName;
    @FXML
    private TableColumn<SupplierProduct, Double> colProductPrice;
    @FXML
    private TableColumn<SupplierProduct, String> colProductCategory;
    @FXML
    private TableColumn<SupplierProduct, Integer> colProductStock;
    @FXML
    private TableColumn<SupplierProduct, Void> colActionProduct;

    private final ServicePersonne servicePersonne = new ServicePersonne();
    private final SeviceSupplierProduct serviceSupplierProduct = new SeviceSupplierProduct();

    @FXML
    public void initialize() {
        // Initialize user table columns
        colNom.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getNom()));
        colPrenom.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getPrenom()));
        colEmail.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getEmail()));
        colRole.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getRole()));

        // Add action buttons to user table
        addUserActionButtons();

        // Load users data
        loadUsers();

        // Initialize product table columns
        //colProductName.setCellValueFactory(cellData -> javafx.beans.property.SimpleStringProperty.stringExpression(cellData.getValue().getName()));
        //colProductPrice.setCellValueFactory(cellData -> new javafx.beans.property.SimpleDoubleProperty(cellData.getValue().getPrice()).asObject());
        //colProductCategory.setCellValueFactory(cellData -> javafx.beans.property.SimpleStringProperty.stringExpression(cellData.getValue().getCategory()));
        //colProductStock.setCellValueFactory(cellData -> new javafx.beans.property.SimpleIntegerProperty(cellData.getValue().getStock()).asObject());

        // Add action buttons to product table
        addProductActionButtons();

        // Load products data
        //loadProducts();
    }

    private void loadUsers() {
        ObservableList<Personne> allUsers = FXCollections.observableArrayList(servicePersonne.recuperer());
        // Filter out users with role "admin"
        ObservableList<Personne> filteredUsers = allUsers.filtered(user -> !"admin".equalsIgnoreCase(user.getRole()));
        usersTable.setItems(filteredUsers);
    }

    private void addUserActionButtons() {
        colActionUser.setCellFactory(param -> new TableCell<>() {
            private final Button editBtn = new Button("Edit");
            private final Button deleteBtn = new Button("Delete");
            private final HBox pane = new HBox(5, editBtn, deleteBtn);

            {
                editBtn.setOnAction(event -> {
                    Personne user = getTableView().getItems().get(getIndex());
                    // Open edit dialog
                    UserEditDialog dialog = new UserEditDialog(user);
                    dialog.showAndWait().ifPresent(updatedUser -> {
                        servicePersonne.modifier(updatedUser);
                        loadUsers();
                    });
                });

                deleteBtn.setOnAction(event -> {
                    Personne user = getTableView().getItems().get(getIndex());
                    // Confirm deletion
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Confirmation");
                    alert.setHeaderText("Supprimer l'utilisateur");
                    alert.setContentText("Êtes-vous sûr de vouloir supprimer l'utilisateur " + user.getNom() + " ?");
                    alert.showAndWait().ifPresent(response -> {
                        if (response == ButtonType.OK) {
                            // Delete user from database
                            servicePersonne.supprimer(user);
                            // Refresh table
                            loadUsers();
                            System.out.println("Deleted user: " + user.getNom());
                        }
                    });
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(pane);
                }
            }
        });
    }

    private void addProductActionButtons() {
        colActionProduct.setCellFactory(param -> new TableCell<>() {
            private final Button btn = new Button("Action");

            {
                btn.setOnAction(event -> {
                    SupplierProduct product = getTableView().getItems().get(getIndex());
                    // Implement action for product here
                    //System.out.println("Action clicked for product: " + product.getName());
                });
                
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(btn);
                }
            }
        });
    }
}
