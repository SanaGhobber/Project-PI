package entities;

import java.time.LocalDateTime;

public class Evenement {
int id ;
String nomEvenement, typeEvenement, localisation, image;
LocalDateTime dateEvenement;
Double Prix;

    public Evenement() {
    }

    public Evenement(int id, String nomEvenement, String typeEvenement, String localisation, String image, LocalDateTime dateEvenement, Double prix) {
        this.id = id;
        this.nomEvenement = nomEvenement;
        this.typeEvenement = typeEvenement;
        this.localisation = localisation;
        this.image = image;
        this.dateEvenement = dateEvenement;
        Prix = prix;
    }

    public Evenement(String nomEvenement, String typeEvenement, String localisation, String image, LocalDateTime dateEvenement, Double prix) {
        this.nomEvenement = nomEvenement;
        this.typeEvenement = typeEvenement;
        this.localisation = localisation;
        this.image = image;
        this.dateEvenement = dateEvenement;
        Prix = prix;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNomEvenement() {
        return nomEvenement;
    }

    public void setNomEvenement(String nomEvenement) {
        this.nomEvenement = nomEvenement;
    }

    public String getTypeEvenement() {
        return typeEvenement;
    }

    public void setTypeEvenement(String typeEvenement) {
        this.typeEvenement = typeEvenement;
    }

    public String getLocalisation() {
        return localisation;
    }

    public void setLocalisation(String localisation) {
        this.localisation = localisation;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public LocalDateTime getDateEvenement() {
        return dateEvenement;
    }

    public void setDateEvenement(LocalDateTime dateEvenement) {
        this.dateEvenement = dateEvenement;
    }

    public Double getPrix() {
        return Prix;
    }

    public void setPrix(Double prix) {
        Prix = prix;
    }

    @Override
    public String toString() {
        return "Evenement{" +
                "id=" + id +
                ", nomEvenement='" + nomEvenement + '\'' +
                ", typeEvenement='" + typeEvenement + '\'' +
                ", localisation='" + localisation + '\'' +
                ", image='" + image + '\'' +
                ", dateEvenement=" + dateEvenement +
                ", Prix=" + Prix +
                '}';
    }
}
