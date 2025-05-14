package com.example.sportify;

import entities.Sujet;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import services.sujetService;

import java.sql.SQLException;
import java.util.Comparator;
import java.util.List;

public class SujetStatsController {
    @FXML private Label totalSujetsLabel;
    @FXML private Label totalMotsLabel;
    @FXML private Label sujetMaxLabel;
    @FXML private Label moyenneMotsLabel;

    private sujetService sujetService = new sujetService();

    @FXML
    public void initialize() {
        try {
            List<Sujet> sujets = sujetService.getAll();
            int totalSujets = sujets.size();
            int totalMots = sujets.stream().mapToInt(Sujet::getNombreMots).sum();
            Sujet sujetMax = sujets.stream().max(Comparator.comparingInt(Sujet::getNombreMots)).orElse(null);
            double moyenne = totalSujets == 0 ? 0 : (double) totalMots / totalSujets;

            totalSujetsLabel.setText("Total Sujets: " + totalSujets);
            totalMotsLabel.setText("Total Mots: " + totalMots);
            sujetMaxLabel.setText("Sujet le plus long: " + (sujetMax != null ? sujetMax.getTitre() + " (" + sujetMax.getNombreMots() + " mots)" : "Aucun"));
            moyenneMotsLabel.setText("Moyenne mots/sujet: " + String.format("%.2f", moyenne));
        } catch (SQLException e) {
            totalSujetsLabel.setText("Erreur de stats");
            totalMotsLabel.setText("");
            sujetMaxLabel.setText("");
            moyenneMotsLabel.setText("");
        }
    }
}
