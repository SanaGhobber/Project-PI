package com.esprit.services;

import com.esprit.models.Gestion_des_produits.CategorieProduit;
import com.esprit.models.Gestion_des_produits.Produit;
import jakarta.persistence.EntityManager;
import org.hibernate.annotations.DialectOverride;

public class ServiceCategorieProduit extends AbstractService<CategorieProduit> {
    public ServiceCategorieProduit(EntityManager em) {
        super(em);
    }
    @Override
    protected Class<CategorieProduit> getEntityClass() {
        return CategorieProduit.class;
    }
}
