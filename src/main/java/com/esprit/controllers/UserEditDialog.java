package com.esprit.controllers;

import com.esprit.models.Personne;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;

import java.util.Optional;

public class UserEditDialog extends Dialog<Personne> {

    private final TextField nomField = new TextField();
    private final TextField prenomField = new TextField();
    private final TextField emailField = new TextField();
    private final TextField roleField = new TextField();

    public UserEditDialog(Personne user) {
        setTitle("Edit User");
        setHeaderText("Edit user details");

        ButtonType saveButtonType = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
        getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        nomField.setText(user.getNom());
        prenomField.setText(user.getPrenom());
        emailField.setText(user.getEmail());
        roleField.setText(user.getRole());

        grid.add(new Label("Nom:"), 0, 0);
        grid.add(nomField, 1, 0);
        grid.add(new Label("Prenom:"), 0, 1);
        grid.add(prenomField, 1, 1);
        grid.add(new Label("Email:"), 0, 2);
        grid.add(emailField, 1, 2);
        grid.add(new Label("Role:"), 0, 3);
        grid.add(roleField, 1, 3);

        getDialogPane().setContent(grid);

        setResultConverter(dialogButton -> {
            if (dialogButton == saveButtonType) {
                user.setNom(nomField.getText());
                user.setPrenom(prenomField.getText());
                user.setEmail(emailField.getText());
                user.setRole(roleField.getText());
                return user;
            }
            return null;
        });
    }
}
