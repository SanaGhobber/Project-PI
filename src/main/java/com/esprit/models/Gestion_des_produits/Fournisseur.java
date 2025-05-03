package com.esprit.models.Gestion_des_produits;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Fournisseur {
    @Id
    @GeneratedValue
    private int id;
    private String nom;
    private String adresse;
    private String email;
    private String telephone;

    // Constructeurs, getters, setters...
}