package services;
import entities.Participer;
import utils.DB;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class participerService {
    private Connection connection;

    public participerService() {
        connection = DB.getInstance().getConnection();
    }

    // Ajouter une participation
    public void ajouterParticipation(Participer participation) {
        String query = "INSERT INTO participer (idEvenement, status, role) VALUES (?, ?, ?)";

        try (PreparedStatement pst = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            pst.setInt(1, participation.getIdEvenement());
            pst.setString(2, participation.getStatus());
            pst.setString(3, participation.getRole());

            pst.executeUpdate();

            // Récupérer l'ID généré
            ResultSet generatedKeys = pst.getGeneratedKeys();
            if (generatedKeys.next()) {
                participation.setId(generatedKeys.getInt(1));
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de l'ajout de la participation: " + e.getMessage());
        }
    }

    // Mettre à jour une participation
    public void modifierParticipation(Participer participation) {
        String query = "UPDATE participer SET idEvenement=?, status=?, role=? WHERE id=?";

        try (PreparedStatement pst = connection.prepareStatement(query)) {
            pst.setInt(1, participation.getIdEvenement());
            pst.setString(2, participation.getStatus());
            pst.setString(3, participation.getRole());
            pst.setInt(4, participation.getId());

            pst.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Erreur lors de la modification de la participation: " + e.getMessage());
        }
    }

    // Supprimer une participation
    public void supprimerParticipation(int id) {
        String query = "DELETE FROM participer WHERE id=?";

        try (PreparedStatement pst = connection.prepareStatement(query)) {
            pst.setInt(1, id);
            pst.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Erreur lors de la suppression de la participation: " + e.getMessage());
        }
    }

    // Obtenir toutes les participations
    public List<Participer> afficherToutesParticipations() {
        List<Participer> participations = new ArrayList<>();
        String query = "SELECT * FROM participer";

        try (Statement st = connection.createStatement();
             ResultSet rs = st.executeQuery(query)) {

            while (rs.next()) {
                Participer p = new Participer();
                p.setId(rs.getInt("id"));
                p.setIdEvenement(rs.getInt("idEvenement"));
                p.setStatus(rs.getString("status"));
                p.setRole(rs.getString("role"));

                participations.add(p);
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération des participations: " + e.getMessage());
        }

        return participations;
    }

    // Obtenir les participations pour un événement spécifique
    public List<Participer> getParticipationsParEvenement(int idEvenement) {
        List<Participer> participations = new ArrayList<>();
        String query = "SELECT * FROM participer WHERE idEvenement=?";

        try (PreparedStatement pst = connection.prepareStatement(query)) {
            pst.setInt(1, idEvenement);
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                Participer p = new Participer();
                p.setId(rs.getInt("id"));
                p.setIdEvenement(rs.getInt("idEvenement"));
                p.setStatus(rs.getString("status"));
                p.setRole(rs.getString("role"));

                participations.add(p);
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération des participations: " + e.getMessage());
        }

        return participations;
    }

    // Obtenir une participation par son ID
    public Participer getParticipationById(int id) {
        String query = "SELECT * FROM participer WHERE id=?";
        Participer participation = null;

        try (PreparedStatement pst = connection.prepareStatement(query)) {
            pst.setInt(1, id);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                participation = new Participer();
                participation.setId(rs.getInt("id"));
                participation.setIdEvenement(rs.getInt("idEvenement"));
                participation.setStatus(rs.getString("status"));
                participation.setRole(rs.getString("role"));
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération de la participation: " + e.getMessage());
        }

        return participation;
    }
}