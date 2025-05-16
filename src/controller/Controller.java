package controller;

import model.SQLite.*;

public class Controller {
    private SQLiteAbilityDAO abilityDAO;
    private SQLiteGenerationDAO generationDAO;
    private SQLiteLocationDAO locationDAO;
    private SQLiteMoveDAO moveDAO;
    private SQLitePokemonDAO pokemonDAO;
    private SQLiteRegionDAO regionDAO;
    private SQLiteTypeDAO typeDAO;

    public Controller(SQLiteAbilityDAO abilityDAO, SQLiteGenerationDAO generationDAO,
                     SQLiteLocationDAO locationDAO, SQLiteMoveDAO moveDAO,
                     SQLitePokemonDAO pokemonDAO, SQLiteRegionDAO regionDAO,
                     SQLiteTypeDAO typeDAO) {
        this.abilityDAO = abilityDAO;
        this.generationDAO = generationDAO;
        this.locationDAO = locationDAO;
        this.moveDAO = moveDAO;
        this.pokemonDAO = pokemonDAO;
        this.regionDAO = regionDAO;
        this.typeDAO = typeDAO;
    }

    // Mètode per llistar tots els pokemons
    public void listAllPokemons() {
        pokemonDAO.readAll();
    }

    // Mètode per mostrar detalls d'un pokemon
    public void showPokemonDetails(int id) {
        pokemonDAO.readTable(id);
    }

    // Mètode per mostrar detalls d'una habilitat
    public void showAbilityDetails(int id) {
        abilityDAO.readTable(id);
    }

    // Mètode per mostrar detalls d'un tipus
    public void showTypeDetails(int id) {
        typeDAO.readTable(id);
    }

    // Mètode per mostrar detalls d'un moviment
    public void showMoveDetails(int id) {
        moveDAO.readTable(id);
    }

    // Mètode per mostrar detalls d'una generació
    public void showGenerationDetails(int id) {
        generationDAO.readTable(id);
    }

    // Mètode per mostrar detalls d'una localització
    public void showLocationDetails(int id) {
        locationDAO.readTable(id);
    }

    // Mètode per mostrar detalls d'una regió
    public void showRegionDetails(int id) {
        regionDAO.readTable(id);
    }
}