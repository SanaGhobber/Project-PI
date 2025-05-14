package entities;

import java.time.LocalDateTime;

public class Message {


    int id,sujetid ;
    String contenu;
    LocalDateTime datepublication;

    public Message(int id, int sujetid, String contenu, LocalDateTime datepublication) {
        this.id = id;
        this.sujetid = sujetid;
        this.contenu = contenu;
        this.datepublication = datepublication;
    }
    public Message(int sujetid, String contenu) {
        this.sujetid = sujetid;
        this.contenu = contenu;
    }
    public Message() {
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public int getSujetid() {
        return sujetid;
    }
    public void setSujetid(int sujetid) {
        this.sujetid = sujetid;
    }
    public String getContenu() {
        return contenu;
    }
    public void setContenu(String contenu) {
        this.contenu = contenu;
    }
    public LocalDateTime getDatepublication() {
        return datepublication;
    }
    public void setDatepublication(LocalDateTime datepublication) {
        this.datepublication = datepublication;
    }
    @Override
    public String toString() {
        return "Message [id=" + id + ", sujetid=" + sujetid + ", contenu=" + contenu + ", datepublication="
                + datepublication + "]";
    }



}
