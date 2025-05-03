package com.esprit.services;

import com.esprit.models.Client;
import com.esprit.models.Gestion_des_produits.Produit;
import com.esprit.utils.DataSource;
import jakarta.persistence.EntityManager;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public final class ServiceClient extends AbstractService<Client> {

    public ServiceClient(EntityManager em) {
        super(em);
    }
    @Override
    protected Class<Client> getEntityClass() {
        return Client.class;
    }

    /*public ServiceClient() {
        connection = DataSource.getInstance().getConnection();
    }

    @Override
    public void ajouter(Client client) {
        String req = "INSERT INTO client (nom, prenom, email, password, role, numero_client, date_inscription) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try {
            PreparedStatement pst = connection.prepareStatement(req, Statement.RETURN_GENERATED_KEYS);

            pst.setString(1, client.getNom());
            pst.setString(2, client.getPrenom());
            pst.setString(3, client.getEmail());
            pst.setString(4, client.getPassword());
            pst.setString(5, "CLIENT"); // Rôle forcé
            pst.setString(6, client.getNumeroClient());
            pst.setDate(7, Date.valueOf(client.getDateInscription()));

            pst.executeUpdate();

            // Récupération de l'ID auto-généré
            try (ResultSet generatedKeys = pst.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    client.setId(generatedKeys.getInt(1));
                }
            }

            System.out.println("Client ajouté avec ID: " + client.getId());
        } catch (SQLException e) {
            System.err.println("Erreur lors de l'ajout du client: " + e.getMessage());
        }
    }

    @Override
    public void modifier(Client client) {
        String req = "UPDATE client SET nom=?, prenom=?, email=?, numero_client=? WHERE id=?";
        try {
            PreparedStatement pst = connection.prepareStatement(req);

            pst.setString(1, client.getNom());
            pst.setString(2, client.getPrenom());
            pst.setString(3, client.getEmail());
            pst.setString(4, client.getNumeroClient());
            pst.setInt(5, client.getId());

            int rowsAffected = pst.executeUpdate();
            System.out.println(rowsAffected + " ligne(s) modifiée(s)");
        } catch (SQLException e) {
            System.err.println("Erreur lors de la modification: " + e.getMessage());
        }
    }

    @Override
    public void supprimer(Client client) {
        String req = "DELETE FROM client WHERE id=?";
        try {
            PreparedStatement pst = connection.prepareStatement(req);
            pst.setInt(1, client.getId());

            int rowsAffected = pst.executeUpdate();
            System.out.println(rowsAffected + " ligne(s) supprimée(s)");
        } catch (SQLException e) {
            System.err.println("Erreur lors de la suppression: " + e.getMessage());
        }
    }

    @Override
    public List<Client> recuperer() {
        List<Client> clients = new ArrayList<>();
        String req = "SELECT * FROM client";

        try (PreparedStatement pst = connection.prepareStatement(req);
             ResultSet rs = pst.executeQuery()) {

            while (rs.next()) {
                Client client = new Client(
                        rs.getInt("id"),
                        rs.getString("nom"),
                        rs.getString("prenom"),
                        rs.getString("email"),
                        rs.getString("password")
                );

                // Attributs spécifiques
                client.setNumeroClient(rs.getString("numero_client"));
                client.setDateInscription(rs.getDate("date_inscription").toLocalDate());

                clients.add(client);
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération: " + e.getMessage());
        }

        return clients;
    }

    // Méthode spécifique pour trouver un client par son numéro
    public Client trouverParNumero(String numeroClient) {
        String req = "SELECT * FROM client WHERE numero_client = ?";
        try {
            PreparedStatement pst = connection.prepareStatement(req);
            pst.setString(1, numeroClient);

            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                Client client = new Client(
                        rs.getInt("id"),
                        rs.getString("nom"),
                        rs.getString("prenom"),
                        rs.getString("email"),
                        rs.getString("password")
                );
                client.setNumeroClient(rs.getString("numero_client"));
                client.setDateInscription(rs.getDate("date_inscription").toLocalDate());
                return client;
            }
        } catch (SQLException e) {
            System.err.println("Erreur de recherche: " + e.getMessage());
        }
        return null;
    }
*/}