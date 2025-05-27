package com.esprit.models;

import java.time.LocalDate;

public class Client extends Personne {
    private String numeroClient;
    private LocalDate dateInscription;

    public Client(int id, String nom, String prenom, String email, String password) {
        super(id, nom, prenom, email, password, "CLIENT");
        this.dateInscription = LocalDate.now();
        this.numeroClient = genererNumeroClient();
    }

    public Client(String nom, String prenom, String email, String password) {
        super(nom, prenom, email, password, "CLIENT");
        this.dateInscription = LocalDate.now();
        this.numeroClient = genererNumeroClient();
    }

    // Méthode pour générer le numéro client
    private String genererNumeroClient() {
        return "CLI-" + LocalDate.now().getYear() + "-" + (int)(Math.random() * 10000);
    }

    // Getters et Setters


    public String getNumeroClient() {
        return numeroClient;
    }

    public LocalDate getDateInscription() {
        return dateInscription;
    }

    public void setNumeroClient(String numeroClient) {
        this.numeroClient = numeroClient;
    }

    public void setDateInscription(LocalDate dateInscription) {
        this.dateInscription = dateInscription;
    }

    @Override
    public String toString() {
        return super.toString() +
                " Client{" +
                "numeroClient='" + numeroClient + '\'' +
                ", dateInscription=" + dateInscription +
                '}';
    }
}