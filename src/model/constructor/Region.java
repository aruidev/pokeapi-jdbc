package model.constructor;

public class Region {
    private int id_regio;
    private String nom;

    public Region(int id_regio, String nom) {
        this.id_regio = id_regio;
        this.nom = nom;
    }

    public int getId_regio() {
        return id_regio;
    }

    public void setId_regio(int id_regio) {
        this.id_regio = id_regio;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }
}
