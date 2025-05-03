package com.esprit.tests;
import jakarta.persistence.EntityManager;
import com.esprit.utils.JpaUtil;

import com.esprit.services.ServiceAdmin;

public class MainProg {
    public static void main(String[] args) {
        EntityManager em = JpaUtil.getEntityManager();
       // ServicePersonne sp = new ServicePersonne();
      //  sp.ajouter(new Personne("Ahmed", "Ghassen","ghassen@gmail.com", "ghassen00", "admin"));
       // sp.modifier(new Personne(2, "Hamza", "Hosni", "ghassen@gmail.com", "ghassen00", "admin"));
        //sp.supprimer(new Personne(1, "","", "" ,"",""));
        //System.out.println(sp.recuperer());

       // ServiceClient c1 = new ServiceClient();
       // c1.ajouter(new Client("sana","ghober","sana@gmail.com","sana123"));
        //c1.supprimer(new Client(1,"","","",""));
       // c1.modifier(new Client(2,"malk","hamdi","",""));
        //System.out.println(c1.recuperer());

        ServiceAdmin a = new ServiceAdmin(em);
       // a.ajouter(new Admin("sana","ghober","sana@gmail.com","sana123","matricule" ,"hguef"));
        //a.supprimer(new Admin(1 ,"sana","ghober","sana@gmail.com","sana123","matricule" ,"hguef"));
        //a.modifier(new Admin(2,"malek","wael","sana@gmail.com","sana123","matriculee" ,"hgeeuef"));
        //System.out.println(a.recuperer());


    }
    }