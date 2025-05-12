package model.constructor;

public class Generation {
    private int id_generation;
    private String nom;
    private String id_regio;

    public Generation(int id_generation, String nom, String id_regio) {
        this.id_generation = id_generation;
        this.nom = nom;
        this.id_regio = id_regio;
    }

    public int getId_generation() {
        return id_generation;
    }

    public void setId_generation(int id_generation) {
        this.id_generation = id_generation;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getId_regio() {
        return id_regio;
    }

    public void setId_regio(String id_regio) {
        this.id_regio = id_regio;
    }
}
