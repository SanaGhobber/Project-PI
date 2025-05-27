package com.esprit.models;

public class SupplierProduct extends Personne {
    public SupplierProduct(int id, String nom, String prenom, String email, String password) {
        super(id, nom, prenom, email, password, "SUPPLIERPRODUCT");
    }

    public SupplierProduct(String nom, String prenom, String email, String password) {
        super(nom, prenom, email, password, "SUPPLIERPRODUCT");
    }


    @Override
    public String toString() {
        return "SupplierProduct{" +
                "id=" + getId() +
                ", nom='" + getNom() + '\'' +
                ", prenom='" + getPrenom() + '\'' +
                ", email='" + getEmail() + '\'' +
                ", password='" + getPassword() + '\'' +
                ", role='" + getRole() + '\'' +
                '}';
    }
}
