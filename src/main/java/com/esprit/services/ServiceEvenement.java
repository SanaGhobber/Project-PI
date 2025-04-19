package com.esprit.services;

import com.esprit.models.evenements.Evenement;
import com.esprit.utils.DataSource;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceEvenement {

    private Connection connection;

    public ServiceEvenement() {
        connection = DataSource.getInstance().getConnection();
    }

    // ✅ Ajouter un événement
    public void ajouter(Evenement evenement) {
        String req = "INSERT INTO evenement(nom, lieu, capaciteMax, sport) VALUES (?, ?, ?, ?)";
        try {
            PreparedStatement pst = connection.prepareStatement(req);
            pst.setString(1, evenement.getNom());
            pst.setString(2, evenement.getLieu());
            pst.setInt(3, evenement.getCapaciteMax());
            pst.setString(4, evenement.getSport());

            pst.executeUpdate();
            System.out.println("✅ Événement ajouté avec succès !");
        } catch (SQLException e) {
            System.err.println(" Erreur lors de l'ajout de l'événement: " + e.getMessage());
        }
    }

    // ✅ Modifier un événement
    public void modifier(Evenement evenement) {
        String req = "UPDATE evenement SET nom=?, lieu=?, capaciteMax=?, sport=? WHERE id=?";
        try {
            PreparedStatement pst = connection.prepareStatement(req);
            pst.setString(1, evenement.getNom());
            pst.setString(2, evenement.getLieu());
            pst.setInt(3, evenement.getCapaciteMax());
            pst.setString(4, evenement.getSport());
            pst.setInt(5, evenement.getId());

            int rowsUpdated = pst.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("✅ Événement modifié avec succès !");
            } else {
                System.out.println("⚠️ Aucun événement trouvé avec cet ID");
            }
        } catch (SQLException e) {
            System.err.println(" Erreur lors de la modification de l'événement: " + e.getMessage());
        }
    }

    // ✅ Supprimer un événement
    public void supprimer(Evenement evenement) {
        String req = "DELETE FROM evenement WHERE id=?";
        try {
            PreparedStatement pst = connection.prepareStatement(req);
            pst.setInt(1, evenement.getId());

            int rowsDeleted = pst.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println("✅ Événement supprimé avec succès !");
            } else {
                System.out.println("⚠️ Aucun événement trouvé avec cet ID");
            }
        } catch (SQLException e) {
            System.err.println(" Erreur lors de la suppression de l'événement: " + e.getMessage());
        }
    }

    // ✅ Récupérer tous les événements
    public List<Evenement> recuperer() {
        List<Evenement> evenements = new ArrayList<>();
        String req = "SELECT * FROM evenement";
        try {
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery(req);
            while (rs.next()) {
                Evenement evenement = new Evenement();
                evenements.add(evenement);
            }
        } catch (SQLException e) {
            System.err.println(" Erreur lors de la récupération des événements: " + e.getMessage());
        }
        return evenements;
    }
}
