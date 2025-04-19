package com.esprit.services;

import com.esprit.models.Personne;
import com.esprit.utils.DataSource;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServicePersonne implements IService<Personne> {

    private Connection connection;

    public ServicePersonne() {
        connection = DataSource.getInstance().getConnection();
    }

    @Override
    public void ajouter(Personne personne) {
        String req = "INSERT INTO personne(nom, prenom, email, password, role) VALUES (?, ?, ?, ?, ?)";
        try {
            PreparedStatement pst = connection.prepareStatement(req);
            pst.setString(1, personne.getNom());
            pst.setString(2, personne.getPrenom());
            pst.setString(3, personne.getEmail());
            pst.setString(4, personne.getPassword());
            pst.setString(5, personne.getRole());
            pst.executeUpdate();
            System.out.println("Personne ajoutée avec succès !");
        } catch (SQLException e) {
            System.err.println("Erreur lors de l'ajout de la personne: " + e.getMessage());
        }
    }

    @Override
    public void modifier(Personne personne) {
        String req = "UPDATE personne SET nom=?, prenom=?, email=?, password=?, role=? WHERE id=?";
        try {
            PreparedStatement pst = connection.prepareStatement(req);
            pst.setString(1, personne.getNom());
            pst.setString(2, personne.getPrenom());
            pst.setString(3, personne.getEmail());
            pst.setString(4, personne.getPassword());
            pst.setString(5, personne.getRole());
            pst.setInt(6, personne.getId());
            int rowsUpdated = pst.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Personne modifiée avec succès !");
            } else {
                System.out.println("Aucune personne trouvée avec cet ID");
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la modification de la personne: " + e.getMessage());
        }
    }

    @Override
    public void supprimer(Personne personne) {
        String req = "DELETE FROM personne WHERE id=?";
        try {
            PreparedStatement pst = connection.prepareStatement(req);
            pst.setInt(1, personne.getId());
            int rowsDeleted = pst.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println("Personne supprimée avec succès !");
            } else {
                System.out.println("Aucune personne trouvée avec cet ID");
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la suppression de la personne: " + e.getMessage());
        }
    }

    @Override
    public List<Personne> recuperer() {
        List<Personne> personnes = new ArrayList<>();
        String req = "SELECT * FROM personne";
        try {
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery(req);
            while (rs.next()) {
                personnes.add(new Personne(
                        rs.getInt("id"),
                        rs.getString("nom"),
                        rs.getString("prenom"),
                        rs.getString("email"),
                        rs.getString("password"),
                        rs.getString("role") // Rôle en tant que String
                ));
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des personnes: " + e.getMessage());
        }
        return personnes;
    }

    // Méthode supplémentaire pour trouver par email
    public Personne getByEmail(String email) {
        String req = "SELECT * FROM personne WHERE email = ?";
        try {
            PreparedStatement pst = connection.prepareStatement(req);
            pst.setString(1, email);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                return new Personne(
                        rs.getInt("id"),
                        rs.getString("nom"),
                        rs.getString("prenom"),
                        rs.getString("email"),
                        rs.getString("password"),
                        rs.getString("role")
                );
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la recherche par email: " + e.getMessage());
        }
        return null;
    }

    // New method to get Personne by ID
    public Personne getById(int id) {
        String req = "SELECT * FROM personne WHERE id = ?";
        try {
            PreparedStatement pst = connection.prepareStatement(req);
            pst.setInt(1, id);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                return new Personne(
                        rs.getInt("id"),
                        rs.getString("nom"),
                        rs.getString("prenom"),
                        rs.getString("email"),
                        rs.getString("password"),
                        rs.getString("role")
                );
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la recherche par ID: " + e.getMessage());
        }
        return null;
    }
}
