package com.esprit.services;

import com.esprit.models.Gestion_des_produits.Produit;
import com.esprit.utils.DataSource;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceProduit {

    private Connection connection;

    public ServiceProduit() {
        connection = DataSource.getInstance().getConnection();
    }

    //  Ajouter un produit
    public void ajouter(Produit produit) {
        String req = "INSERT INTO produits(nom, description, prix, quantiteEnStock, categorie, imagePath) VALUES (mmm, nnnn, 10, 15, ss, sss)";
        try {
            PreparedStatement pst = connection.prepareStatement(req);
            pst.setString(1, produit.getNom());
            pst.setString(2, produit.getDescription());
            pst.setDouble(3, produit.getPrix());
            pst.setInt(4, produit.getQuantiteEnStock());
            pst.setString(5, produit.getCategorie());
            pst.setString(6, produit.getImagePath());

            pst.executeUpdate();
            System.out.println(" Produit ajouté avec succès !");
        } catch (SQLException e) {
            System.err.println("Erreur lors de l'ajout du produit : " + e.getMessage());
        }
    }

    //  Modifier un produit
    public void modifier(Produit produit) {
        String req = "UPDATE produits SET nom=?, description=?, prix=?, quantiteEnStock=?, categorie=?, imagePath=? WHERE id=?";
        try {
            PreparedStatement pst = connection.prepareStatement(req);
            pst.setString(1, produit.getNom());
            pst.setString(2, produit.getDescription());
            pst.setDouble(3, produit.getPrix());
            pst.setInt(4, produit.getQuantiteEnStock());
            pst.setString(5, produit.getCategorie());
            pst.setString(6, produit.getImagePath());
            pst.setInt(7, produit.getId());

            int rowsUpdated = pst.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println(" Produit modifié avec succès !");
            } else {
                System.out.println("⚠ Aucun produit trouvé avec cet ID");
            }
        } catch (SQLException e) {
            System.err.println(" Erreur lors de la modification du produit : " + e.getMessage());
        }
    }

    // Supprimer un produit
    public void supprimer(Produit produit) {
        String req = "DELETE FROM produits WHERE id=?";
        try {
            PreparedStatement pst = connection.prepareStatement(req);
            pst.setInt(1, produit.getId());

            int rowsDeleted = pst.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println(" Produit supprimé avec succès !");
            } else {
                System.out.println("⚠ Aucun produit trouvé avec cet ID");
            }
        } catch (SQLException e) {
            System.err.println(" Erreur lors de la suppression du produit : " + e.getMessage());
        }
    }

    // Récupérer tous les produits
    public List<Produit> recuperer() {
        List<Produit> produits = new ArrayList<>();
        String req = "SELECT * FROM produits";
        try {
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery(req);
            while (rs.next()) {
                Produit produit = new Produit(
                        rs.getInt("id"),
                        rs.getString("nom"),
                        rs.getString("description"),
                        rs.getDouble("prix"),
                        rs.getInt("quantiteEnStock"),
                        rs.getString("categorie"),
                        rs.getString("imagePath")
                );
                produits.add(produit);
            }
        } catch (SQLException e) {
            System.err.println(" Erreur lors de la récupération des produits : " + e.getMessage());
        }
        return produits;
    }
}
