package com.esprit.tests;

import com.esprit.models.Personne;
import com.esprit.services.ServicePersonne;
import com.esprit.services.ServicePersonne2;

public class MainProg {
    public static void main(String[] args) {
//        ServicePersonne sp = new ServicePersonne();
//        sp.ajouter(new Personne("Ahmed", "Ghassen"));
//        sp.modifier(new Personne(1, "Hamza", "Hosni"));
//        sp.supprimer(new Personne(1, "", ""));
//        System.out.println(sp.recuperer());
        ServicePersonne2 sp2 = new ServicePersonne2();
        sp2.ajouter(new Personne("Arbi", "Ammar"));
    }
}
