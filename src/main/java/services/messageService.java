package services;

import entities.Message;
import utils.DB;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.*;

public class messageService {
    private Connection connection;

    private static final List<String> BAD_WORDS = Arrays.asList("putain", "merde", "con", "salope", "connard"); // ajoute d'autres mots si besoin

    public messageService() {
        connection = DB.getInstance().getConnection();
    }

    // Fonction de filtrage des mauvais mots
    public String filterBadWords(String content) {
        String filtered = content;
        for (String badWord : BAD_WORDS) {
            filtered = filtered.replaceAll("(?i)" + badWord, "****");
        }
        return filtered;
    }

    // Create - Ajouter un nouveau message
    public void add(Message message) throws SQLException {
        message.setContenu(filterBadWords(message.getContenu()));
        String req = "INSERT INTO message (contenu, sujetid, datepublication) VALUES (?, ?, ?)";
        PreparedStatement pst = connection.prepareStatement(req, Statement.RETURN_GENERATED_KEYS);

        pst.setString(1, message.getContenu());
        pst.setInt(2, message.getSujetid());
        pst.setTimestamp(3, Timestamp.valueOf(LocalDateTime.now()));

        pst.executeUpdate();

        // Récupérer l'ID généré
        ResultSet rs = pst.getGeneratedKeys();
        if (rs.next()) {
            message.setId(rs.getInt(1));
        }
    }

    // Read - Récupérer tous les messages d'un sujet (par défaut DESC)
    public List<Message> getBySujetId(int sujetId) throws SQLException {
        List<Message> messages = new ArrayList<>();
        String req = "SELECT * FROM message WHERE sujetid = ? ORDER BY datepublication DESC";
        PreparedStatement pst = connection.prepareStatement(req);
        pst.setInt(1, sujetId);

        ResultSet rs = pst.executeQuery();
        while (rs.next()) {
            Message m = new Message(
                    rs.getInt("id"),
                    rs.getInt("sujetid"),
                    rs.getString("contenu"),
                    rs.getTimestamp("datepublication").toLocalDateTime()
            );
            messages.add(m);
        }
        return messages;
    }

    // Read - Récupérer tous les messages d'un sujet en ASC
    public List<Message> getBySujetIdAsc(int sujetId) throws SQLException {
        List<Message> messages = new ArrayList<>();
        String req = "SELECT * FROM message WHERE sujetid = ? ORDER BY datepublication ASC";
        PreparedStatement pst = connection.prepareStatement(req);
        pst.setInt(1, sujetId);
        ResultSet rs = pst.executeQuery();
        while (rs.next()) {
            Message m = new Message(
                    rs.getInt("id"),
                    rs.getInt("sujetid"),
                    rs.getString("contenu"),
                    rs.getTimestamp("datepublication").toLocalDateTime()
            );
            messages.add(m);
        }
        return messages;
    }

    // Read - Récupérer tous les messages d'un sujet en DESC
    public List<Message> getBySujetIdDesc(int sujetId) throws SQLException {
        return getBySujetId(sujetId); // déjà DESC
    }

    // Read - Récupérer un message par son ID
    public Message getById(int id) throws SQLException {
        String req = "SELECT * FROM message WHERE id = ?";
        PreparedStatement pst = connection.prepareStatement(req);
        pst.setInt(1, id);

        ResultSet rs = pst.executeQuery();
        if (rs.next()) {
            return new Message(
                    rs.getInt("id"),
                    rs.getInt("sujetid"),
                    rs.getString("contenu"),
                    rs.getTimestamp("datepublication").toLocalDateTime()
            );
        }
        return null;
    }

    // Update - Modifier un message avec la date actuelle
    public void update(Message message) throws SQLException {
        message.setContenu(filterBadWords(message.getContenu()));
        String req = "UPDATE message SET contenu = ?, datepublication = ? WHERE id = ?";
        PreparedStatement pst = connection.prepareStatement(req);

        pst.setString(1, message.getContenu());
        pst.setTimestamp(2, Timestamp.valueOf(LocalDateTime.now())); // Met à jour avec la date/heure actuelle
        pst.setInt(3, message.getId());

        pst.executeUpdate();
    }

    // Delete - Supprimer un message
    public void delete(int id) throws SQLException {
        String req = "DELETE FROM message WHERE id = ?";
        PreparedStatement pst = connection.prepareStatement(req);
        pst.setInt(1, id);
        pst.executeUpdate();
    }

    // Supprimer tous les messages d'un sujet
    public void deleteBySujetId(int sujetId) throws SQLException {
        String req = "DELETE FROM message WHERE sujetid = ?";
        PreparedStatement pst = connection.prepareStatement(req);
        pst.setInt(1, sujetId);
        pst.executeUpdate();
    }

    // Recherche dans les messages
    public List<Message> search(String keyword) throws SQLException {
        List<Message> results = new ArrayList<>();
        String req = "SELECT * FROM message WHERE contenu LIKE ?";
        PreparedStatement pst = connection.prepareStatement(req);
        pst.setString(1, "%" + keyword + "%");

        ResultSet rs = pst.executeQuery();
        while (rs.next()) {
            Message m = new Message(
                    rs.getInt("id"),
                    rs.getInt("sujetid"),
                    rs.getString("contenu"),
                    rs.getTimestamp("datepublication").toLocalDateTime()
            );
            results.add(m);
        }
        return results;
    }
}