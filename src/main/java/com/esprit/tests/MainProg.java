package com.esprit.tests;

import com.esprit.controllers.ActiviteController;
import com.esprit.models.Activite;

public class MainProg {
    public static void main(String[] args) {
        ActiviteController activiteController = new ActiviteController();

        // Tester uniquement l'ajout d'une activité
        Activite activite1 = new Activite();
        activite1.setNom("Fitness");
        activite1.setType("Sport");
        activiteController.ajouterActivite(activite1);

        System.out.println("Activité ajoutée avec ID: " + activite1.getId());

        /*
        // Code commenté pour les autres opérations de test

        // SalleController salleController = new SalleController();

        // Ajouter une salle
        // Salle salle1 = new Salle();
        // salle1.setNom("Salle A");
        // salle1.setEtat(EtatSalle.DISPONIBLE);
        // salle1.setLocalisation("Building 1");
        // salle1.setActivites(Arrays.asList());

        // salleController.ajouterSalle(salle1);

        // Modifier une salle
        // salle1.setEtat(EtatSalle.OCCUPEE);
        // salleController.modifierSalle(salle1);

        // Récupérer toutes les salles
        // List<Salle> salles = salleController.getAllSalles();
        // System.out.println("Liste des salles:");
        // for (Activite a : activites) {
        //     System.out.println("ID: " + a.getId() + ", Nom: " + a.getNom() + ", Type: " + a.getType());
        // }

        // Supprimer une activité
        // activiteController.supprimerActivite(activite1.getId());
        */
    }
}
