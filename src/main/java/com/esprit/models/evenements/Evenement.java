package com.esprit.models.evenements;

public class Evenement {
    private int id;
    private String nom;
    private String lieu;
    private int capaciteMax;
    private String sport;

    public Evenement() {
    }

    public Evenement(String nom, String lieu, int capaciteMax, String sport) {
        this.nom = nom;
        this.lieu = lieu;
        this.capaciteMax = capaciteMax;
        this.sport = sport;
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

    public String getLieu() {
        return lieu;
    }

    public void setLieu(String lieu) {
        this.lieu = lieu;
    }

    public int getCapaciteMax() {
        return capaciteMax;
    }

    public void setCapaciteMax(int capaciteMax) {
        this.capaciteMax = capaciteMax;
    }

    public String getSport() {
        return sport;
    }

    public void setSport(String sport) {
        this.sport = sport;
    }



}