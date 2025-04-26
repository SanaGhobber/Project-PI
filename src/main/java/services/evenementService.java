package services;
import entities.Evenement;
import utils.DB;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class evenementService {
    private Connection connection;

    public evenementService() {
        connection = DB.getInstance().getConnection();
    }

    // Create (Ajouter un événement)
    public void ajouterEvenement(Evenement evenement) {
        String query = "INSERT INTO evenement (nomEvenement, typeEvenement, localisation, image, dateEvenement, Prix) VALUES (?, ?, ?, ?, ?, ?)";

        try (PreparedStatement pst = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            pst.setString(1, evenement.getNomEvenement());
            pst.setString(2, evenement.getTypeEvenement());
            pst.setString(3, evenement.getLocalisation());
            pst.setString(4, evenement.getImage());
            pst.setTimestamp(5, Timestamp.valueOf(evenement.getDateEvenement()));
            pst.setDouble(6, evenement.getPrix());

            pst.executeUpdate();

            // Récupérer l'ID généré
            ResultSet generatedKeys = pst.getGeneratedKeys();
            if (generatedKeys.next()) {
                evenement.setId(generatedKeys.getInt(1));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    // Read (Récupérer tous les événements)
    public List<Evenement> afficherEvenement() {
        List<Evenement> evenements = new ArrayList<>();
        String query = "SELECT * FROM evenement";

        try (Statement st = connection.createStatement();
             ResultSet rs = st.executeQuery(query)) {

            while (rs.next()) {
                Evenement e = new Evenement();
                e.setId(rs.getInt("id"));
                e.setNomEvenement(rs.getString("nomEvenement"));
                e.setTypeEvenement(rs.getString("typeEvenement"));
                e.setLocalisation(rs.getString("localisation"));
                e.setImage(rs.getString("image"));
                e.setDateEvenement(rs.getTimestamp("dateEvenement").toLocalDateTime());
                e.setPrix(rs.getDouble("Prix"));

                evenements.add(e);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return evenements;
    }

    // Update (Modifier un événement)
    public void modifierEvenement(Evenement evenement) {
        String query = "UPDATE evenement SET nomEvenement=?, typeEvenement=?, localisation=?, image=?, dateEvenement=?, Prix=? WHERE id=?";

        try (PreparedStatement pst = connection.prepareStatement(query)) {
            pst.setString(1, evenement.getNomEvenement());
            pst.setString(2, evenement.getTypeEvenement());
            pst.setString(3, evenement.getLocalisation());
            pst.setString(4, evenement.getImage());
            pst.setTimestamp(5, Timestamp.valueOf(evenement.getDateEvenement()));
            pst.setDouble(6, evenement.getPrix());
            pst.setInt(7, evenement.getId());

            pst.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    // Delete (Supprimer un événement)
    public void supprimerEvenement(int id) {
        String query = "DELETE FROM evenement WHERE id=?";

        try (PreparedStatement pst = connection.prepareStatement(query)) {
            pst.setInt(1, id);
            pst.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    // Rechercher un événement par ID
    public Evenement getEvenementById(int id) {
        String query = "SELECT * FROM evenement WHERE id=?";
        Evenement evenement = null;

        try (PreparedStatement pst = connection.prepareStatement(query)) {
            pst.setInt(1, id);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                evenement = new Evenement();
                evenement.setId(rs.getInt("id"));
                evenement.setNomEvenement(rs.getString("nomEvenement"));
                evenement.setTypeEvenement(rs.getString("typeEvenement"));
                evenement.setLocalisation(rs.getString("localisation"));
                evenement.setImage(rs.getString("image"));
                evenement.setDateEvenement(rs.getTimestamp("dateEvenement").toLocalDateTime());
                evenement.setPrix(rs.getDouble("Prix"));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return evenement;
    }
}