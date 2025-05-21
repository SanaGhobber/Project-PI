package com.esprit.controllers;

import com.esprit.models.Activite;

import java.util.ArrayList;
import java.util.List;

public class ActiviteController {
    private List<Activite> activites;

    public ActiviteController() {
        this.activites = new ArrayList<>();
    }

    public void ajouterActivite(Activite activite) {
        activites.add(activite);
    }

    public void modifierActivite(Activite activite) {
        for (int i = 0; i < activites.size(); i++) {
            if (activites.get(i).getId() == activite.getId()) {
                activites.set(i, activite);
                return;
            }
        }
    }

    public void supprimerActivite(int id) {
        activites.removeIf(a -> a.getId() == id);
    }

    public Activite getActiviteById(int id) {
        return activites.stream()
                .filter(a -> a.getId() == id)
                .findFirst()
                .orElse(null);
    }

    public List<Activite> getAllActivites() {
        return new ArrayList<>(activites);
    }
}
