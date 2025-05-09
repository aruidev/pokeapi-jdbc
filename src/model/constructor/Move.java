package model.constructor;

public class Move {
    private int id_move;
    private String nom;
    private int poder;
    private int precisio;
    private int pp;
    private String tipus;

    public Move (int id_move, String nom, int poder, int precisio, int pp, String tipus) {
        this.id_move = id_move;
        this.nom = nom;
        this.poder = poder;
        this.precisio = precisio;
        this.pp = pp;
        this.tipus = tipus;
    }

    public int getId_move() {
        return id_move;
    }

    public void setId_move(int id_move) {
        this.id_move = id_move;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public int getPoder() {
        return poder;
    }

    public void setPoder(int poder) {
        this.poder = poder;
    }

    public int getPrecisio() {
        return precisio;
    }

    public void setPrecisio(int precisio) {
        this.precisio = precisio;
    }

    public int getPp() {
        return pp;
    }

    public void setPp(int pp) {
        this.pp = pp;
    }

    public String getTipus() {
        return tipus;
    }

    public void setTipus(String tipus) {
        this.tipus = tipus;
    }
}
