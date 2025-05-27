package com.esprit.models;

public class SupplierEvent extends Personne {

    public SupplierEvent(int id, String nom, String prenom, String email, String password) {
        super(id, nom, prenom, email, password, "SUPPLIEREVENT");
    }

    public SupplierEvent(String nom, String prenom, String email, String password) {
        super(nom, prenom, email, password, "SUPPLIEREVENT");
    }

    @Override
    public String toString() {
        return "SupplierEvent{" +
                "id=" + getId() +
                ", nom='" + getNom() + '\'' +
                ", prenom='" + getPrenom() + '\'' +
                ", email='" + getEmail() + '\'' +
                ", password='" + getPassword() + '\'' +
                ", role='" + getRole() + '\'' +
                '}';
    }
}
