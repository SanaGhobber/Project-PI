package com.esprit.services;


import com.esprit.models.Client;
import com.esprit.models.SupplierProduct;
import com.esprit.utils.DataSource;

import java.sql.*;
import java.util.List;

public class SeviceSupplierProduct implements IService<SupplierProduct>{
    private Connection connection;

    public SeviceSupplierProduct() {
        connection = DataSource.getInstance().getConnection();
    }

    public void ajouter(SupplierProduct supplierproduct) {
        String req = "INSERT INTO supplierproduct (nom, prenom, email, password, role) VALUES (?, ?, ?, ?, ?)";
        try {
            PreparedStatement pst = connection.prepareStatement(req, Statement.RETURN_GENERATED_KEYS);

            pst.setString(1, supplierproduct.getNom());
            pst.setString(2, supplierproduct.getPrenom());
            pst.setString(3, supplierproduct.getEmail());
            pst.setString(4, supplierproduct.getPassword());
            pst.setString(5, "SUPPLIERPRODUCT"); // Rôle forcé

            pst.executeUpdate();

            // Récupération de l'ID auto-généré
            try (ResultSet generatedKeys = pst.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    supplierproduct.setId(generatedKeys.getInt(1));
                }
            }

            System.out.println("supplierproduct ajouté avec ID: " + supplierproduct.getId());
        } catch (SQLException e) {
            System.err.println("Erreur lors de l'ajout du client: " + e.getMessage());
        }
    }

    @Override
    public void modifier(SupplierProduct supplierProduct) {

    }

    @Override
    public void supprimer(SupplierProduct supplierProduct) {

    }

    @Override
    public List<SupplierProduct> recuperer() {
        return List.of();
    }
}
