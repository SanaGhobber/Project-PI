package com.esprit.services;

import com.esprit.models.Salle;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ServiceSalle implements IServiceSalle {
    private List<Salle> salles = new ArrayList<>();

    @Override
    public void ajouterSalle(Salle salle) {
        salles.add(salle);
    }

    @Override
    public void modifierSalle(Salle salle) {
        Optional<Salle> existingSalle = salles.stream()
            .filter(s -> s.getId() == salle.getId())
            .findFirst();
        if (existingSalle.isPresent()) {
            salles.remove(existingSalle.get());
            salles.add(salle);
        }
    }

    @Override
    public void supprimerSalle(int id) {
        salles.removeIf(s -> s.getId() == id);
    }

    @Override
    public Salle getSalleById(int id) {
        return salles.stream()
            .filter(s -> s.getId() == id)
            .findFirst()
            .orElse(null);
    }

    @Override
    public List<Salle> getAllSalles() {
        return new ArrayList<>(salles);
    }
}
