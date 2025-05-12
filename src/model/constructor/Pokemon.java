package model.constructor;

public class Pokemon {
    private int id_pokemon;
    private String nom;
    private int pes;
    private int altura;
    private int id_generacio;
    private int id_tipus;

    public Pokemon (int id_pokemon, String nom, int pes, int altura, int id_generacio, int id_tipus) {
        this.id_pokemon = id_pokemon;
        this.nom = nom;
        this.pes = pes;
        this.altura = altura;
        this.id_generacio = id_generacio;
        this.id_tipus = id_tipus;
    }

    public int getId_pokemon() {
        return id_pokemon;
    }

    public void setId_pokemon(int id_pokemon) {
        this.id_pokemon = id_pokemon;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public int getPes() {
        return pes;
    }

    public void setPes(int pes) {
        this.pes = pes;
    }

    public int getAltura() {
        return altura;
    }

    public void setAltura(int altura) {
        this.altura = altura;
    }

    public int getId_generacio() {
        return id_generacio;
    }

    public void setId_generacio(int id_generacio) {
        this.id_generacio = id_generacio;
    }

    public int getId_tipus() {
        return id_tipus;
    }

    public void setId_tipus(int id_tipus) {
        this.id_tipus = id_tipus;
    }
}
