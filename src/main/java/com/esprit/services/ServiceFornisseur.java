package com.esprit.services;

import com.esprit.models.Gestion_des_produits.Fournisseur;
import com.esprit.models.Gestion_des_produits.Produit;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;

public class ServiceFornisseur extends AbstractService<Fournisseur>{
    public ServiceFornisseur(EntityManager em) {
        super(em);
    }
    @Override
    protected Class<Fournisseur> getEntityClass() {
        return Fournisseur.class;
    }

}