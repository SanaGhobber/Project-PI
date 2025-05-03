package com.esprit.models;


public class Admin extends Personne {
    private String matriculeAdmin;
    private String departement;

    public Admin(int id, String nom, String prenom, String email, String password,
                 String matriculeAdmin, String departement) {
        super(id, nom, prenom, email, password, "ADMIN");
        this.matriculeAdmin = matriculeAdmin;
        this.departement = departement;
    }

    public Admin(String nom, String prenom, String email, String password,
                 String matriculeAdmin, String departement) {
        super(nom, prenom, email, password, "ADMIN");
        this.matriculeAdmin = matriculeAdmin;
        this.departement = departement;
    }

    // Getters et Setters
    public String getMatriculeAdmin() {
        return matriculeAdmin;
    }

    public void setMatriculeAdmin(String matriculeAdmin) {
        this.matriculeAdmin = matriculeAdmin;
    }

    public String getDepartement() {
        return departement;
    }

    public void setDepartement(String departement) {
        this.departement = departement;
    }

    @Override
    public String toString() {
        return super.toString() +
                " Admin{" +
                "matriculeAdmin='" + matriculeAdmin + '\'' +
                ", departement='" + departement + '\'' +
                '}';
    }
}