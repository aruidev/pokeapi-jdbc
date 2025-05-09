package controller;

import view.ViewMenu;
import java.util.Scanner;

public class HandleMenu {
    private final ViewMenu viewMenu = new ViewMenu();
    private final Scanner scanner = new Scanner(System.in);

    public void start() {
        int option;
        do {
            viewMenu.displayMenu();
            option = readOption();
            switch (option) {
                case 1 -> handlePokemonListMenu();
                case 2 -> {
                    // Lógica para mostrar el contenido del Endpoint
                }
                case 3 -> {
                    // Lógica para modificar pokemons según el Endpoint
                }
                case 4 -> handleCopyEndpointMenu();
                case 5 -> {
                    // Lógica para mostrar el contenido del JSON
                }
                case 6 -> {
                    // Lógica para modificar pokemons según el JSON
                }
                case 7 -> handleCopyJsonMenu();
                case 0 -> System.out.println("Saliendo del programa...");
                default -> System.out.println("Opción no válida. Inténtalo de nuevo.");
            }
        } while (option != 0);
    }

    private void handlePokemonListMenu() {
        int option;
        do {
            viewMenu.displayPokemonListMenu();
            option = readOption();
            switch (option) {
                case 1 -> {
                    // Lógica para listar todos los pokemons
                }
                case 2 -> {
                    // Lógica para mostrar detalles de un pokemon
                }
                case 3 -> {
                    // Lógica para mostrar detalles de una habilidad
                }
                case 4 -> {
                    // Lógica para mostrar detalles de un tipo de pokemon
                }
                case 5 -> {
                    // Lógica para mostrar detalles de un movimiento
                }
                case 6 -> {
                    // Lógica para mostrar detalles de una generación
                }
                case 7 -> {
                    // Lógica para mostrar detalles de una localización
                }
                case 8 -> {
                    // Lógica para mostrar detalles de una región
                }
                case 0 -> System.out.println("Volviendo al menú principal...");
                default -> System.out.println("Opción no válida. Inténtalo de nuevo.");
            }
        } while (option != 0);
    }

    private void handleCopyEndpointMenu() {
        int option;
        do {
            viewMenu.displayCopyEndpointMenu();
            option = readOption();
            switch (option) {
                case 1 -> {
                    // Lógica para realizar copia parcial de datos del Endpoint
                }
                case 2 -> {
                    // Lógica para realizar copia total de datos del Endpoint
                }
                case 0 -> System.out.println("Volviendo al menú principal...");
                default -> System.out.println("Opción no válida. Inténtalo de nuevo.");
            }
        } while (option != 0);
    }

    private void handleCopyJsonMenu() {
        int option;
        do {
            viewMenu.displayCopyJsonMenu();
            option = readOption();
            switch (option) {
                case 1 -> {
                    // Lógica para realizar copia parcial de datos del JSON
                }
                case 2 -> {
                    // Lógica para realizar copia total de datos del JSON
                }
                case 0 -> System.out.println("Volviendo al menú principal...");
                default -> System.out.println("Opción no válida. Inténtalo de nuevo.");
            }
        } while (option != 0);
    }

    private int readOption() {
        while (!scanner.hasNextInt()) {
            System.out.println("Por favor, introduce un número válido.");
            scanner.next();
        }
        return scanner.nextInt();
    }
}