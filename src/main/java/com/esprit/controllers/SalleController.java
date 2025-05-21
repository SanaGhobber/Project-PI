package com.esprit.controllers;

import com.esprit.models.Salle;
import com.esprit.services.IServiceSalle;
import com.esprit.services.ServiceSalle2;

import java.util.List;

public class SalleController {

    private IServiceSalle serviceSalle;

    public SalleController() {
        this.serviceSalle = new ServiceSalle2();
    }

    public void ajouterSalle(Salle salle) {
        serviceSalle.ajouterSalle(salle);
    }

    public void modifierSalle(Salle salle) {
        serviceSalle.modifierSalle(salle);
    }

    public void supprimerSalle(int id) {
        serviceSalle.supprimerSalle(id);
    }

    public Salle getSalleById(int id) {
        return serviceSalle.getSalleById(id);
    }

    public List<Salle> getAllSalles() {
        return serviceSalle.getAllSalles();
    }
}
