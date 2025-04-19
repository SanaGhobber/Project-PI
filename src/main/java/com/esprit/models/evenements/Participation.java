package com.esprit.models.evenements;

import com.esprit.models.Personne;

public class Participation {
    private int id;
    private String evenement;
    private String statut; // "CONFIRME", "EN_ATTENTE", "ANNULE"

    public Participation(int id, String evenement, String statut) {
        this.id = id;
        this.evenement = evenement;
        this.statut = statut;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEvenement() {
        return evenement;
    }

    public void setEvenement(String evenement) {
        this.evenement = evenement;
    }

    public String getStatut() {
        return statut;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }
}


