package test;

import model.SQLite.SQLiteAbilityDAO;
import model.constructor.Ability;

import java.sql.Connection;
import java.util.Scanner;

import static model.dbconnection.openCon;

public class DAOTest {
    public static void main(String[] args) {
        try (Connection connection = openCon("jdbc:sqlite:db/pokeapiDB.db");
             Scanner scanner = new Scanner(System.in)) {

            SQLiteAbilityDAO abilityDAO = new SQLiteAbilityDAO();
            abilityDAO.setConnection(connection);

            int option;
            do {
                System.out.println("\n--- Ability CRUD Menu ---");
                System.out.println("1. Create Ability");
                System.out.println("2. Read Ability by ID");
                System.out.println("3. Update Ability");
                System.out.println("4. Delete Ability");
                System.out.println("5. Read All Abilities");
                System.out.println("0. Exit");
                System.out.print("Choose an option: ");
                option = scanner.nextInt();
                scanner.nextLine(); // Consume newline

                switch (option) {
                    case 1 -> {
                        System.out.print("Enter Ability ID: ");
                        int id = scanner.nextInt();
                        scanner.nextLine(); // Consume newline
                        System.out.print("Enter Ability Name: ");
                        String name = scanner.nextLine();
                        System.out.print("Enter Ability Effect: ");
                        String effect = scanner.nextLine();
                        Ability newAbility = new Ability(id, name, effect);
                        abilityDAO.insertTable(newAbility);
                        System.out.println("Ability created successfully.");
                    }
                    case 2 -> {
                        System.out.print("Enter Ability ID to read: ");
                        int id = scanner.nextInt();
                        abilityDAO.readTable(id);
                    }
                    case 3 -> {
                        System.out.print("Enter Ability ID to update: ");
                        int id = scanner.nextInt();
                        scanner.nextLine(); // Consume newline
                        System.out.print("Enter New Ability Name: ");
                        String name = scanner.nextLine();
                        System.out.print("Enter New Ability Effect: ");
                        String effect = scanner.nextLine();
                        Ability updatedAbility = new Ability(id, name, effect);
                        abilityDAO.updateTable(updatedAbility);
                        System.out.println("Ability updated successfully.");
                    }
                    case 4 -> {
                        System.out.print("Enter Ability ID to delete: ");
                        int id = scanner.nextInt();
                        abilityDAO.deleteTable(id);
                        System.out.println("Ability deleted successfully.");
                    }
                    case 5 -> abilityDAO.readAll();
                    case 0 -> System.out.println("Exiting...");
                    default -> System.out.println("Invalid option. Please try again.");
                }
            } while (option != 0);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            System.out.println("Program terminated.");
        }
    }
}
