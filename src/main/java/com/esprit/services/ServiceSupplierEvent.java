package com.esprit.services;

import com.esprit.models.SupplierEvent;
import com.esprit.utils.DataSource;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceSupplierEvent implements IService<SupplierEvent> {

    private Connection connection;

    public ServiceSupplierEvent() {
        connection = DataSource.getInstance().getConnection();
    }

    @Override
    public void ajouter(SupplierEvent supplierEvent) {
        String query = "INSERT INTO supplierevent (nom, prenom, email, password, role) VALUES (?, ?, ?, ?, ?)";
        try {
            PreparedStatement pst = connection.prepareStatement(query);
            pst.setString(1, supplierEvent.getNom());
            pst.setString(2, supplierEvent.getPrenom());
            pst.setString(3, supplierEvent.getEmail());
            pst.setString(4, supplierEvent.getPassword());
            pst.setString(5, "SUPPLIEREVENT");
            pst.executeUpdate();
            System.out.println("SupplierEvent ajouté avec succès");
        } catch (SQLException e) {
            System.err.println("Erreur lors de l'ajout du SupplierEvent: " + e.getMessage());
        }
    }

    @Override
    public void modifier(SupplierEvent supplierEvent) {
        String query = "UPDATE supplierevent SET nom = ?, prenom = ?, email = ?, password = ? WHERE id = ?";
        try {
            PreparedStatement pst = connection.prepareStatement(query);
            pst.setString(1, supplierEvent.getNom());
            pst.setString(2, supplierEvent.getPrenom());
            pst.setString(3, supplierEvent.getEmail());
            pst.setString(4, supplierEvent.getPassword());
            pst.setInt(5, supplierEvent.getId());
            int rowsAffected = pst.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("SupplierEvent modifié avec succès");
            } else {
                System.out.println("Aucun SupplierEvent trouvé avec cet ID");
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la modification du SupplierEvent: " + e.getMessage());
        }
    }

    @Override
    public void supprimer(SupplierEvent supplierEvent) {
        String query = "DELETE FROM supplierevent WHERE id = ?";
        try {
            PreparedStatement pst = connection.prepareStatement(query);
            pst.setInt(1, supplierEvent.getId());
            int rowsAffected = pst.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("SupplierEvent supprimé avec succès");
            } else {
                System.out.println("Aucun SupplierEvent trouvé avec cet ID");
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la suppression du SupplierEvent: " + e.getMessage());
        }
    }

    @Override
    public List<SupplierEvent> recuperer() {
        List<SupplierEvent> supplierEvents = new ArrayList<>();
        String query = "SELECT * FROM supplierevent";
        try {
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery(query);
            while (rs.next()) {
                supplierEvents.add(new SupplierEvent(
                        rs.getInt("id"),
                        rs.getString("nom"),
                        rs.getString("prenom"),
                        rs.getString("email"),
                        rs.getString("password")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des SupplierEvents: " + e.getMessage());
        }
        return supplierEvents;
    }
}
