package controller;

        import controller.apicontroller.CopyFromApi;
        import controller.apicontroller.DisplayFromApi;
        import controller.json.DisplayFromJson;
        import view.ViewMenu;
        import java.util.Scanner;

        public class HandleMenu {
            private final ViewMenu viewMenu = new ViewMenu();
            private final Scanner scanner = new Scanner(System.in);
            private final Controller controller;
            private final DisplayFromApi displayFromApi;
            private final DisplayFromJson displayFromJson;

            public HandleMenu(Controller controller, DisplayFromApi displayFromApi) {
                this.controller = controller;
                this.displayFromApi = displayFromApi;
                this.displayFromJson = new DisplayFromJson("src/json");
            }

            public void start() {
                int option;
                do {
                    viewMenu.displayMenu();
                    option = readOption();
                    switch (option) {
                        case 1 -> handlePokemonListMenu();
                        case 2 -> handleEndpointMenu();
                        case 3 -> handleModifyFromApi();
                        case 4 -> handleCopyEndpointMenu();
                        case 5 -> handleJsonListMenu();
                        case 6 -> handleModifyFromJson();
                        case 7 -> handleCopyJsonMenu();
                        case 8 -> handleUpdateLogsMenu();
                        case 0 -> System.out.println("Sortint......");
                        default -> System.out.println("Entrada no vàlida. Introdueix la opció de nou.");
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
                            handleListAllMenu();
                        }
                        case 2 -> {
                            System.out.print("\nIntrodueix l'ID del Pokemon: ");
                            int id = readOption();
                            controller.showPokemonDetails(id);
                        }
                        case 3 -> {
                            System.out.print("\nIntrodueix l'ID de l'habilitat: ");
                            int id = readOption();
                            controller.showAbilityDetails(id);
                        }
                        case 4 -> {
                            System.out.print("\nIntrodueix l'ID del tipus: ");
                            int id = readOption();
                            controller.showTypeDetails(id);
                        }
                        case 5 -> {
                            System.out.print("\nIntrodueix l'ID del moviment: ");
                            int id = readOption();
                            controller.showMoveDetails(id);
                        }
                        case 6 -> {
                            System.out.print("\nIntrodueix l'ID de la generació: ");
                            int id = readOption();
                            controller.showGenerationDetails(id);
                        }
                        case 7 -> {
                            System.out.print("\nIntrodueix l'ID de la localizació: ");
                            int id = readOption();
                            controller.showLocationDetails(id);
                        }
                        case 8 -> {
                            System.out.print("\nIntrodueix l'ID de la regió: ");
                            int id = readOption();
                            controller.showRegionDetails(id);
                        }
                        case 0 -> System.out.println("Tornant al menú principal...");
                        default -> System.out.println("Entrada no vàlida. Introdueix la opció de nou.");
                    }
                } while (option != 0);
            }


            private void handleEndpointMenu() {
                int option;
                do {
                    viewMenu.displayPokemonListMenu(); // Reutilizamos el mismo menú
                    option = readOption();
                    switch (option) {
                        case 1 -> {
                            System.out.println("\nLlistat de tots els pokemons de l'API:");
                            int currentPage = 1;
                            char option2;
                            do {
                                displayFromApi.listAllPokemons(currentPage);
                                System.out.print("\nPremeu 'a' per anterior, 's' per següent, 'x' per tornar: ");
                                option2 = scanner.next().charAt(0);
                                if (option2 == 'a' && currentPage > 1) currentPage--;
                                else if (option2 == 's') currentPage++;
                            } while (option2 != 'x');
                        }
                        case 2 -> {
                            System.out.print("\nIntrodueix l'ID del Pokemon: ");
                            int id = readOption();
                            displayFromApi.showPokemonDetails(id);
                        }
                        case 3 -> {
                            System.out.print("\nIntrodueix l'ID de l'habilitat: ");
                            int id = readOption();
                            displayFromApi.showAbilityDetails(id);
                        }
                        case 4 -> {
                            System.out.print("\nIntrodueix l'ID del tipus: ");
                            int id = readOption();
                            displayFromApi.showTypeDetails(id);
                        }
                        case 5 -> {
                            System.out.print("\nIntrodueix l'ID del moviment: ");
                            int id = readOption();
                            displayFromApi.showMoveDetails(id);
                        }
                        case 6 -> {
                            System.out.print("\nIntrodueix l'ID de la generació: ");
                            int id = readOption();
                            displayFromApi.showGenerationDetails(id);
                        }
                        case 7 -> {
                            System.out.print("\nIntrodueix l'ID de la localització: ");
                            int id = readOption();
                            displayFromApi.showLocationDetails(id);
                        }
                        case 8 -> {
                            System.out.print("\nIntrodueix l'ID de la regió: ");
                            int id = readOption();
                            displayFromApi.showRegionDetails(id);
                        }
                        case 0 -> System.out.println("Tornant al menú principal...");
                        default -> System.out.println("Entrada no vàlida. Introdueix la opció de nou.");
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
                            System.out.println("\nIniciant còpia parcial des de l'API (només pokemons NOUS)...");
                            System.out.print("Introdueix la ID inicial: ");
                            int startId = readOption();
                            System.out.print("Introdueix la ID final: ");
                            int endId = readOption();
                            controller.importPokemonRangeFromApi(startId, endId, false);
                        }
                        case 2 -> {
                            System.out.println("\nIniciant còpia total des de l'API (TOTS els pokemons)...");
                            System.out.println("ALERTA: Aquesta acció pot sobrescriure dades existents.");
                            System.out.print("Introdueix la ID inicial: ");
                            int startId = readOption();
                            System.out.print("Introdueix la ID final: ");
                            int endId = readOption();
                            controller.importPokemonRangeFromApi(startId, endId, true);
                        }
                        case 0 -> System.out.println("Tornant al menú principal...");
                        default -> System.out.println("Entrada no vàlida. Introdueix la opció de nou.");
                    }
                } while (option != 0);
            }

            private void handleJsonListMenu() {
                int option;
                do {
                    viewMenu.displayPokemonListMenu();
                    option = readOption();
                    switch (option) {
                        case 1 -> {
                            System.out.println("\nLlistat de tots els pokemons del JSON:");
                            displayFromJson.listAllPokemons();
                        }
                        case 2 -> {
                            System.out.print("\nIntrodueix l'ID del Pokemon: ");
                            int id = readOption();
                            displayFromJson.showPokemonDetails(id);
                        }
                        case 3 -> {
                            System.out.print("\nIntrodueix l'ID de l'habilitat: ");
                            int id = readOption();
                            displayFromJson.showAbilityDetails(id);
                        }
                        case 4 -> {
                            System.out.print("\nIntrodueix l'ID del tipus: ");
                            int id = readOption();
                            displayFromJson.showTypeDetails(id);
                        }
                        case 5 -> {
                            System.out.print("\nIntrodueix l'ID del moviment: ");
                            int id = readOption();
                            displayFromJson.showMoveDetails(id);
                        }
                        case 6 -> {
                            System.out.print("\nIntrodueix l'ID de la generació: ");
                            int id = readOption();
                            displayFromJson.showGenerationDetails(id);
                        }
                        case 7 -> {
                            System.out.print("\nIntrodueix l'ID de la localització: ");
                            int id = readOption();
                            displayFromJson.showLocationDetails(id);
                        }
                        case 8 -> {
                            System.out.print("\nIntrodueix l'ID de la regió: ");
                            int id = readOption();
                            displayFromJson.showRegionDetails(id);
                        }
                        case 0 -> System.out.println("Tornant al menú principal...");
                        default -> System.out.println("Entrada no vàlida. Introdueix la opció de nou.");
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
                            System.out.println("\nIniciant còpia parcial des de JSON (només pokemons NOUS)...");
                            System.out.println("ALERTA: S'afegiràn les ID's encara que no existeixi l'arxiu JSON de la entitat corresponent.");
                            controller.importAllPokemonsFromJson(false);
                        }
                        case 2 -> {
                            System.out.println("\nIniciant còpia total des de JSON (TOTS els pokemons)...");
                            System.out.println("ALERTA: Aquesta acció pot sobrescriure dades existents.");
                            System.out.println("ALERTA: S'afegiràn les ID's encara que no existeixi l'arxiu JSON de la entitat corresponent.");
                            controller.importAllPokemonsFromJson(true);
                        }
                        case 0 -> System.out.println("Tornant al menú principal...");
                        default -> System.out.println("Entrada no vàlida. Introdueix la opció de nou.");
                    }
                } while (option != 0);
            }

            private void handleModifyFromApi() {
                System.out.println("Introdueix la ID del pokemon:");
                int id = readOption();
                System.out.println();
                System.out.println("Detalls del pokemon a la DB local:");
                controller.showPokemonDetails(id);
                System.out.println();
                System.out.println("Detalls del pokemon a l'API:");
                displayFromApi.showPokemonDetails(id);
                System.out.println("Vols modificar el pokemon a la DB local amb les dades de l'API? (s/n)");
                if (scanner.next().equalsIgnoreCase("s")) {
                    System.out.println("Modificant pokemon a la DB local amb les dades de la API...");
                    controller.importPokemonRangeFromApi(id, id, true);
                } else {
                    System.out.println("No s'ha realitzat cap modificació.");
                }
            }

            private void handleModifyFromJson() {
                System.out.println("Introdueix la ID del pokemon:");
                int id = readOption();
                System.out.println();
                System.out.println("Detalls del pokemon a la DB local:");
                controller.showPokemonDetails(id);
                System.out.println();
                System.out.println("Detalls del pokemon al JSON:");
                displayFromJson.showPokemonDetails(id);
                System.out.println("Vols modificar el pokemon a la DB local amb les dades del JSON? (s/n)");
                if (scanner.next().equalsIgnoreCase("s")) {
                    System.out.println("Modificant pokemon a la DB local amb les dades del JSON...");
                    controller.importSinglePokemonFromJson(id, true);
                }
            }

            private void handleUpdateLogsMenu() {
                int option;
                do {
                    viewMenu.displayUpdateLogMenu();
                    option = readOption();
                    switch (option) {
                        case 1 -> {
                            System.out.println("\nLlistat de tots els logs d'actualització:");
                            controller.printAllUpdateLogs();
                        }
                        case 0 -> System.out.println("Tornant al menú principal...");
                        default -> System.out.println("Entrada no vàlida. Introdueix la opció de nou.");
                    }
                } while (option != 0);
            }

            private void handleListAllMenu() {
                int option;
                do {
                    viewMenu.displayListAllMenu();
                    option = readOption();
                    switch (option) {
                        case 1 -> controller.listAllPokemons();
                        case 2 -> controller.listAllTypes();
                        case 3 -> controller.listAllAbilities();
                        case 4 -> controller.listAllMoves();
                        case 5 -> controller.listAllGenerations();
                        case 6 -> controller.listAllLocations();
                        case 7 -> controller.listAllRegions();
                        case 0 -> System.out.println("Tornant al menú principal...");
                        default -> System.out.println("Entrada no vàlida. Introdueix la opció de nou.");
                    }
                } while (option != 0);
            }

            private int readOption() {
                while (!scanner.hasNextInt()) {
                    System.out.println("Si us plau, introdueix un número vàlid");
                    scanner.next();
                }
                return scanner.nextInt();
            }
        }