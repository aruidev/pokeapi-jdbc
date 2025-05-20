package controller;

import controller.apicontroller.CopyFromApi;
import controller.json.CopyFromJson;
import model.SQLite.*;

import java.sql.Connection;

public class Controller {
    private SQLiteAbilityDAO abilityDAO;
    private SQLiteGenerationDAO generationDAO;
    private SQLiteLocationDAO locationDAO;
    private SQLiteMoveDAO moveDAO;
    private SQLitePokemonDAO pokemonDAO;
    private SQLiteRegionDAO regionDAO;
    private SQLiteTypeDAO typeDAO;
    private CopyFromJson copyFromJson;
    private Connection connection;

    public Controller(SQLiteAbilityDAO abilityDAO, SQLiteGenerationDAO generationDAO,
                     SQLiteLocationDAO locationDAO, SQLiteMoveDAO moveDAO,
                     SQLitePokemonDAO pokemonDAO, SQLiteRegionDAO regionDAO,
                     SQLiteTypeDAO typeDAO, Connection connection) {
        this.abilityDAO = abilityDAO;
        this.generationDAO = generationDAO;
        this.locationDAO = locationDAO;
        this.moveDAO = moveDAO;
        this.pokemonDAO = pokemonDAO;
        this.regionDAO = regionDAO;
        this.typeDAO = typeDAO;
        this.copyFromJson = new CopyFromJson("src/json", pokemonDAO, typeDAO, abilityDAO, connection);
        this.connection = connection;
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

    // Mètode per importar tots els pokemons des d'arxius JSON
    public void importAllPokemonsFromJson(boolean overwriteExisting) {
        copyFromJson.importAllPokemons(overwriteExisting);
    }

    // Mètode per importar un pokemon des d'un arxiu JSON
    public void importSinglePokemonFromJson(int id, boolean overwriteExisting) {
        copyFromJson.importSinglePokemonFromJson(id, overwriteExisting);
    }

    // Mètode per importar pokemons desde la API
    public void importPokemonRangeFromApi(int startId, int endId, boolean overwriteExisting) {
        CopyFromApi copyFromApi = new CopyFromApi(pokemonDAO, typeDAO, abilityDAO, moveDAO,
                                                 connection);
        copyFromApi.importPokemonRange(startId, endId, overwriteExisting);
    }
}