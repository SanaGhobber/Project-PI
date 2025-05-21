package com.esprit.services;

import com.esprit.models.Salle;
import com.esprit.models.EtatSalle;
import com.esprit.utils.DataSource;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceSalle2 implements IServiceSalle {

    private Connection connection;

    public ServiceSalle2() {
        connection = DataSource.getInstance().getConnection();
    }

    @Override
    public void ajouterSalle(Salle salle) {
        String req = "INSERT INTO salle(nom, etat, localisation) VALUES (?, ?, ?)";
        try {
            PreparedStatement pst = connection.prepareStatement(req, Statement.RETURN_GENERATED_KEYS);
            pst.setString(1, salle.getNom());
            pst.setString(2, salle.getEtat().name());
            pst.setString(3, salle.getLocalisation());
            pst.executeUpdate();

            ResultSet rs = pst.getGeneratedKeys();
            if (rs.next()) {
                salle.setId(rs.getInt(1));
            }
            System.out.println("Salle ajoutée !");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void modifierSalle(Salle salle) {
        String req = "UPDATE salle SET nom=?, etat=?, localisation=? WHERE id=?";
        try {
            PreparedStatement pst = connection.prepareStatement(req);
            pst.setString(1, salle.getNom());
            pst.setString(2, salle.getEtat().name());
            pst.setString(3, salle.getLocalisation());
            pst.setInt(4, salle.getId());
            pst.executeUpdate();
            System.out.println("Salle modifiée !");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void supprimerSalle(int id) {
        String req = "DELETE FROM salle WHERE id=?";
        try {
            PreparedStatement pst = connection.prepareStatement(req);
            pst.setInt(1, id);
            pst.executeUpdate();
            System.out.println("Salle supprimée !");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public Salle getSalleById(int id) {
        Salle salle = null;
        String req = "SELECT * FROM salle WHERE id=?";
        try {
            PreparedStatement pst = connection.prepareStatement(req);
            pst.setInt(1, id);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                salle = new Salle();
                salle.setId(rs.getInt("id"));
                salle.setNom(rs.getString("nom"));
                salle.setEtat(EtatSalle.valueOf(rs.getString("etat")));
                salle.setLocalisation(rs.getString("localisation"));
                // Activities loading can be added here if needed
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return salle;
    }

    @Override
    public List<Salle> getAllSalles() {
        List<Salle> salles = new ArrayList<>();
        String req = "SELECT * FROM salle";
        try {
            PreparedStatement pst = connection.prepareStatement(req);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                Salle salle = new Salle();
                salle.setId(rs.getInt("id"));
                salle.setNom(rs.getString("nom"));
                salle.setEtat(EtatSalle.valueOf(rs.getString("etat")));
                salle.setLocalisation(rs.getString("localisation"));
                // Activities loading can be added here if needed
                salles.add(salle);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return salles;
    }
}
