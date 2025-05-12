package model.constructor;

public class Location {
    private int id_location;
    private String nom;
    private int id_regio;

    public Location(int id_location, String nom, int id_regio) {
        this.id_location = id_location;
        this.nom = nom;
        this.id_regio = id_regio;
    }

    public int getId_location() {
        return id_location;
    }

    public void setId_location(int id_location) {
        this.id_location = id_location;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public int getId_regio() {
        return id_regio;
    }

    public void setId_regio(int id_regio) {
        this.id_regio = id_regio;
    }
}
