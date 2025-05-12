package model.constructor;

public class Type {
    private int id_tipus;
    private String nom;
    private String relacions_dany;

    public Type(int id_tipus, String nom, String relacions_dany) {
        this.id_tipus = id_tipus;
        this.nom = nom;
        this.relacions_dany = relacions_dany;
    }

    public int getId_tipus() {
        return id_tipus;
    }

    public void setId_tipus(int id_tipus) {
        this.id_tipus = id_tipus;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getRelacions_dany() {
        return relacions_dany;
    }

    public void setRelacions_dany(String relacions_dany) {
        this.relacions_dany = relacions_dany;
    }
}
