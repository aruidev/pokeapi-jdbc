import java.sql.Connection;
import java.util.Scanner;

import controller.apicontroller.DisplayFromApi;
import controller.Controller;
import controller.HandleMenu;
import model.SQLite.*;

import static model.dbconnection.openCon;

public class Main {
    public static void main(String[] args) {
        try (Connection connection = openCon("jdbc:sqlite:db/pokeapiDB.db");
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

            Controller controller = new Controller(abilityDAO, generationDAO, locationDAO,
                                                 moveDAO, pokemonDAO, regionDAO, typeDAO, connection);

            DisplayFromApi displayFromApi = new DisplayFromApi();

            HandleMenu menu = new HandleMenu(controller, displayFromApi);
            menu.start();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            System.out.println("Programa finalitzat.");
            System.out.println("Connexió amb la base de dades finalitzada.");
        }
    }
}