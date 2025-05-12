import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Scanner;

import controller.HandleMenu;
import model.SQLite.*;
import view.*;
import model.dbconnection;

public class Main {
    public static void main(String[] args) {
        try (Connection connection = DriverManager.getConnection("jdbc:sqlite:db/pokeapiDB.db");
             Scanner scan = new Scanner(System.in)) {

            SQLiteAbilityDAO abilityDAO = new SQLiteAbilityDAO();
            SQLiteGenerationDAO generationDAO = new SQLiteGenerationDAO();
            SQLiteLocationDAO locationDAO = new SQLiteLocationDAO();
            SQLiteMoveDAO moveDAO = new SQLiteMoveDAO();
            SQLitePokemonDAO pokemonDAO = new SQLitePokemonDAO();
            SQLiteRegionDAO regionDAO = new SQLiteRegionDAO();
            SQLiteTypeDAO typeDAO = new SQLiteTypeDAO();

            abilityDAO.setConnection(connection);
            generationDAO.setConnection(connection);
            locationDAO.setConnection(connection);
            moveDAO.setConnection(connection);
            pokemonDAO.setConnection(connection);
            regionDAO.setConnection(connection);
            typeDAO.setConnection(connection);

            HandleMenu menu = new HandleMenu();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
