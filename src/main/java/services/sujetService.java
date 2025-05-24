package services;

import entities.Sujet;
import utils.DB;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class sujetService {
    private Connection connection;

    public sujetService() {
        connection = DB.getInstance().getConnection();
    }

    // Create (Ajouter un nouveau sujet)
    public void add(Sujet sujet) throws SQLException {
            String req = "INSERT INTO sujet (titre, contenu, categorie, dateCreation) VALUES (?, ?, ?, ?)";
        PreparedStatement pst = connection.prepareStatement(req, Statement.RETURN_GENERATED_KEYS);

        pst.setString(1, sujet.getTitre());
        pst.setString(2, sujet.getContenu());
        pst.setString(3, sujet.getCategorie());
        pst.setTimestamp(4, Timestamp.valueOf(LocalDateTime.now()));

        pst.executeUpdate();

        // Récupérer l'ID généré
        ResultSet rs = pst.getGeneratedKeys();
        if (rs.next()) {
            sujet.setId(rs.getInt(1));
        }
    }

    // Read (Récupérer tous les sujets)
    public List<Sujet> getAll() throws SQLException {
        List<Sujet> sujets = new ArrayList<>();
        String req = "SELECT * FROM sujet";
        Statement st = connection.createStatement();
        ResultSet rs = st.executeQuery(req);

        while (rs.next()) {
            Sujet s = new Sujet(
                    rs.getInt("id"),
                    rs.getString("titre"),
                    rs.getString("contenu"),
                    rs.getString("categorie"),
                    rs.getTimestamp("dateCreation").toLocalDateTime()
            );
            sujets.add(s);
        }
        return sujets;
    }

    public List<Sujet> getByCategory(String category) throws SQLException {
        List<Sujet> results = new ArrayList<>();
        String req = "SELECT * FROM sujet WHERE categorie = ?";
        PreparedStatement pst = connection.prepareStatement(req);
        pst.setString(1, category);

        ResultSet rs = pst.executeQuery();
        while (rs.next()) {
            Sujet s = new Sujet(
                    rs.getInt("id"),
                    rs.getString("titre"),
                    rs.getString("contenu"),
                    rs.getString("categorie"),
                    rs.getTimestamp("dateCreation").toLocalDateTime()
            );
            results.add(s);
        }
        return results;
    }

    public List<String> getAllCategories() throws SQLException {
        List<String> categories = new ArrayList<>();
        String req = "SELECT DISTINCT categorie FROM sujet";
        Statement st = connection.createStatement();
        ResultSet rs = st.executeQuery(req);

        while (rs.next()) {
            categories.add(rs.getString("categorie"));
        }
        return categories;
    }


    // Read (Récupérer un sujet par son ID)
    public Sujet getById(int id) throws SQLException {
        String req = "SELECT * FROM sujet WHERE id = ?";
        PreparedStatement pst = connection.prepareStatement(req);
        pst.setInt(1, id);

        ResultSet rs = pst.executeQuery();
        if (rs.next()) {
            return new Sujet(
                    rs.getInt("id"),
                    rs.getString("titre"),
                    rs.getString("contenu"),
                    rs.getString("categorie"),
                    rs.getTimestamp("dateCreation").toLocalDateTime()
            );
        }
        return null;
    }

    // Update (Modifier un sujet)
    public void update(Sujet sujet) throws SQLException {
        String req = "UPDATE sujet SET titre = ?, contenu = ?, categorie = ? WHERE id = ?";
        PreparedStatement pst = connection.prepareStatement(req);

        pst.setString(1, sujet.getTitre());
        pst.setString(2, sujet.getContenu());
        pst.setString(3, sujet.getCategorie());
        pst.setInt(4, sujet.getId());

        pst.executeUpdate();
    }

    // Delete (Supprimer un sujet)
    public void delete(int id) throws SQLException {
        String req = "DELETE FROM sujet WHERE id = ?";
        PreparedStatement pst = connection.prepareStatement(req);
        pst.setInt(1, id);
        pst.executeUpdate();
    }

    public List<Sujet> search(String keyword) throws SQLException {
        List<Sujet> results = new ArrayList<>();
        String req = "SELECT * FROM sujet WHERE titre LIKE ? OR categorie LIKE ?";
        PreparedStatement pst = connection.prepareStatement(req);
        pst.setString(1, "%" + keyword + "%");
        pst.setString(2, "%" + keyword + "%");

        ResultSet rs = pst.executeQuery();
        while (rs.next()) {
            Sujet s = new Sujet(
                    rs.getInt("id"),
                    rs.getString("titre"),
                    rs.getString("contenu"),
                    rs.getString("categorie"),
                    rs.getTimestamp("dateCreation").toLocalDateTime()
            );
            results.add(s);
        }
        return results;
    }

    // Recherche avancée multi-critères
    public List<Sujet> advancedSearch(String titre, String categorie, String contenu) throws SQLException {
        List<Sujet> results = new ArrayList<>();
        String req = "SELECT * FROM sujet WHERE (? IS NULL OR titre LIKE ?) AND (? IS NULL OR categorie LIKE ?) AND (? IS NULL OR contenu LIKE ?)";
        PreparedStatement pst = connection.prepareStatement(req);
        pst.setString(1, titre == null || titre.isEmpty() ? null : titre);
        pst.setString(2, titre == null || titre.isEmpty() ? "%%" : "%" + titre + "%");
        pst.setString(3, categorie == null || categorie.isEmpty() ? null : categorie);
        pst.setString(4, categorie == null || categorie.isEmpty() ? "%%" : "%" + categorie + "%");
        pst.setString(5, contenu == null || contenu.isEmpty() ? null : contenu);
        pst.setString(6, contenu == null || contenu.isEmpty() ? "%%" : "%" + contenu + "%");

        ResultSet rs = pst.executeQuery();
        while (rs.next()) {
            Sujet s = new Sujet(
                    rs.getInt("id"),
                    rs.getString("titre"),
                    rs.getString("contenu"),
                    rs.getString("categorie"),
                    rs.getTimestamp("dateCreation").toLocalDateTime()
            );
            // Si tu veux charger les vues depuis la base, ajoute une colonne et ici : s.setVues(rs.getInt("vues"));
            results.add(s);
        }
        return results;
    }
}