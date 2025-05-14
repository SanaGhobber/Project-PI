package entities;

public class Participer {

    int id, idEvenement ;
    String status, role;


    public Participer() {

    }

    public Participer(int id, int idEvenement, String status, String role) {
        this.id = id;
        this.idEvenement = idEvenement;
        this.status = status;
        this.role = role;
    }

    public Participer(int idEvenement, String status, String role) {
        this.idEvenement = idEvenement;
        this.status = status;
        this.role = role;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdEvenement() {
        return idEvenement;
    }

    public void setIdEvenement(int idEvenement) {
        this.idEvenement = idEvenement;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return "Participer{" +
                "id=" + id +
                ", idEvenement=" + idEvenement +
                ", status='" + status + '\'' +
                ", role='" + role + '\'' +
                '}';
    }
}
