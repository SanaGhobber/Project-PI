package com.esprit.services;

import com.esprit.models.Salle;
import java.util.List;

public interface IServiceSalle {
    void ajouterSalle(Salle salle);
    void modifierSalle(Salle salle);
    void supprimerSalle(int id);
    Salle getSalleById(int id);
    List<Salle> getAllSalles();
}
