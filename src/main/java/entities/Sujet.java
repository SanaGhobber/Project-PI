package entities;

import java.time.LocalDateTime;

public class Sujet {
    int id ;
    String titre, contenu, categorie;
    LocalDateTime dateCreation;

    public Sujet(int id, String titre, String contenu, String categorie, LocalDateTime dateCreation) {
        this.id = id;
        this.titre = titre;
        this.contenu = contenu;
        this.categorie = categorie;
        this.dateCreation = dateCreation;
    }
    public Sujet(String titre, String contenu, String categorie) {
        this.titre = titre;
        this.contenu = contenu;
        this.categorie = categorie;
    }
    public Sujet() {
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getTitre() {
        return titre;
    }
    public void setTitre(String titre) {
        this.titre = titre;
    }
    public String getContenu() {
        return contenu;
    }
    public void setContenu(String contenu) {
        this.contenu = contenu;
    }
    public String getCategorie() {
        return categorie;
    }
    public void setCategorie(String categorie) {
        this.categorie = categorie;
    }
    public LocalDateTime getDateCreation() {
        return dateCreation;
    }
    public void setDateCreation(LocalDateTime dateCreation) {
        this.dateCreation = dateCreation;
    }
    public int getNombreMots() {
        if (contenu == null || contenu.isEmpty()) return 0;
        return contenu.trim().split("\\s+").length;
    }
    @Override
    public String toString() {
        return "Sujet [id=" + id + ", titre=" + titre + ", contenu=" + contenu + ", categorie=" + categorie
                + ", dateCreation=" + dateCreation + "]";
    }
}
