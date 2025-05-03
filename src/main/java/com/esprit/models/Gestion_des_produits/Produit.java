package com.esprit.models.Gestion_des_produits;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Produit {
    @Id
    @GeneratedValue
    private int id;
    private String nom;
    private String description;
    private double prix;
    private int quantiteEnStock;
    private String categorie;
    private String imagePath;
    private boolean Etat;
    // Constructeurs, getters, setters...
}