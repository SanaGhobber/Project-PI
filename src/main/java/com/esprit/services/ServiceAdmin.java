package com.esprit.services;

import com.esprit.models.Admin;
import com.esprit.utils.DataSource;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceAdmin implements IService<Admin> {

    private Connection connection;

    public ServiceAdmin() {
        connection = DataSource.getInstance().getConnection();
    }

    @Override
    public void ajouter(Admin admin) {
        String query = "INSERT INTO admin(nom, prenom, email, password, matricule_admin, departement) VALUES (?, ?, ?, ?, ?, ?)";
        try {
            PreparedStatement pst = connection.prepareStatement(query);
            pst.setString(1, admin.getNom());
            pst.setString(2, admin.getPrenom());
            pst.setString(3, admin.getEmail());
            pst.setString(4, admin.getPassword());
            pst.setString(5, admin.getMatriculeAdmin());
            pst.setString(6, admin.getDepartement());
            pst.executeUpdate();
            System.out.println("Admin ajouté avec succès");
        } catch (SQLException e) {
            System.err.println("Erreur lors de l'ajout de l'admin: " + e.getMessage());
        }
    }

    @Override
    public void modifier(Admin admin) {
        String query = "UPDATE admin SET nom = ?, prenom = ?, email = ?, password = ?, matricule_admin = ?, departement = ? WHERE id = ?";
        try {
            PreparedStatement pst = connection.prepareStatement(query);
            pst.setString(1, admin.getNom());
            pst.setString(2, admin.getPrenom());
            pst.setString(3, admin.getEmail());
            pst.setString(4, admin.getPassword());
            pst.setString(5, admin.getMatriculeAdmin());
            pst.setString(6, admin.getDepartement());
            pst.setInt(7, admin.getId());
            int rowsAffected = pst.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Admin modifié avec succès");
            } else {
                System.out.println("Aucun admin trouvé avec cet ID");
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la modification de l'admin: " + e.getMessage());
        }
    }

    @Override
    public void supprimer(Admin admin) {
        String query = "DELETE FROM admin WHERE id = ?";
        try {
            PreparedStatement pst = connection.prepareStatement(query);
            pst.setInt(1, admin.getId());
            int rowsAffected = pst.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Admin supprimé avec succès");
            } else {
                System.out.println("Aucun admin trouvé avec cet ID");
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la suppression de l'admin: " + e.getMessage());
        }
    }

    @Override
    public List<Admin> recuperer() {
        List<Admin> admins = new ArrayList<>();
        String query = "SELECT * FROM admin";
        try {
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery(query);
            while (rs.next()) {
                admins.add(new Admin(
                        rs.getInt("id"),
                        rs.getString("nom"),
                        rs.getString("prenom"),
                        rs.getString("email"),
                        rs.getString("password"),
                        rs.getString("matricule_admin"),
                        rs.getString("departement")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des admins: " + e.getMessage());
        }
        return admins;
    }
}