package model.constructor;

public class Ability {
    private int id_habilitat;
    private String nom;
    private String efecte;

    public Ability(int id_habilitat, String nom, String efecte) {
        this.id_habilitat = id_habilitat;
        this.nom = nom;
        this.efecte = efecte;
    }

    public int getId_habilitat() {
        return id_habilitat;
    }

    public void setId_habilitat(int id_habilitat) {
        this.id_habilitat = id_habilitat;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getEfecte() {
        return efecte;
    }

    public void setEfecte(String efecte) {
        this.efecte = efecte;
    }
}
