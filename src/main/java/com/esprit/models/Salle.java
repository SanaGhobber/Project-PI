package com.esprit.models;

import java.util.List;

public class Salle {
    private int id;
    private String nom;
    private EtatSalle etat;
    private String localisation;
    private List<Activite> activites;

    public Salle() {
    }

    public Salle(int id, String nom, EtatSalle etat, String localisation, List<Activite> activites) {
        this.id = id;
        this.nom = nom;
        this.etat = etat;
        this.localisation = localisation;
        this.activites = activites;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public EtatSalle getEtat() {
        return etat;
    }

    public void setEtat(EtatSalle etat) {
        this.etat = etat;
    }

    public String getLocalisation() {
        return localisation;
    }

    public void setLocalisation(String localisation) {
        this.localisation = localisation;
    }

    public List<Activite> getActivites() {
        return activites;
    }

    public void setActivites(List<Activite> activites) {
        this.activites = activites;
    }
}
