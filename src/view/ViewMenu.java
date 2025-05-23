package view;

public class ViewMenu {
    public void displayMenu() {
        System.out.println("\n--------------------------------");
        System.out.println("Menú principal:");
        System.out.println();
        System.out.println("1. Mostrar contingut de DB local");
        System.out.println("2. Mostrar contingut de l'Endpoint");
        System.out.println("3. Modificar pokemons segons l’Endpoint");
        System.out.println("4. Còpia de les dades obtingudes de l’Endpoint");
        System.out.println("5. Mostrar contingut del JSON");
        System.out.println("6. Modificar pokemons segons el JSON");
        System.out.println("7. Còpia de les dades obtingudes del JSON");
        System.out.println("8. Mostrar logs d'actualització");
        System.out.println("0. Sortir");
        System.out.println("--------------------------------");
        System.out.print("Selecciona una opció: ");
    }

    public void displayPokemonListMenu() {
        System.out.println("\n--------------------------------");
        System.out.println("Llistat de pokemons:");
        System.out.println();
        System.out.println("1. Llistar tot");
        System.out.println("Veure detalls de...");
        System.out.println("2. ...pokemon");
        System.out.println("3. ...habilitat");
        System.out.println("4. ...tipus de pokemon");
        System.out.println("5. ...moviment");
        System.out.println("6. ...generació");
        System.out.println("7. ...localització");
        System.out.println("8. ...regió");
        System.out.println("0. Tornar al menú principal");
        System.out.println("--------------------------------");
        System.out.print("Selecciona una opció: ");
    }

    public void displayCopyEndpointMenu() {
        System.out.println("\n--------------------------------");
        System.out.println("Còpia de les dades obtingudes de l'Endpoint:");
        System.out.println();
        System.out.println("1. Còpia parcial de les dades obtingudes de l'Endpoint" +
                "\n(Copiar només aquells pokemons que no existeixen a la DB)");
        System.out.println("2. Còpia total de les dades obtingudes de l'Endpoint" +
                "\n(Copiar tots els pokemons de l'Endpoint a la DB)");
        System.out.println("0. Tornar al menú principal");
        System.out.println("--------------------------------");
        System.out.print("Selecciona una opció: ");
    }

    public void displayCopyJsonMenu() {
        System.out.println("\n--------------------------------");
        System.out.println("Còpia de les dades obtingudes del JSON:");
        System.out.println();
        System.out.println("1. Còpia parcial de les dades obtingudes del JSON" +
                "\n(Copiar només aquells pokemons que no existeixen a la DB)");
        System.out.println("2. Còpia total de les dades obtingudes del JSON" +
                "\n(Copiar tots els pokemons del JSON a la DB)");
        System.out.println("0. Tornar al menú principal");
        System.out.println("--------------------------------");
        System.out.print("Selecciona una opció: ");
    }

    public void displayUpdateLogMenu() {
        System.out.println("\n--------------------------------");
        System.out.println("Llistat de logs d'actualització:");
        System.out.println();
        System.out.println("1. Llistar tots els logs d'actualització");
        System.out.println("0. Tornar al menú principal");
        System.out.println("--------------------------------");
        System.out.print("Selecciona una opció: ");
    }

    public void displayListAllMenu() {
        System.out.println("\n--------------------------------");
        System.out.println("Llistat de pokemons:");
        System.out.println();
        System.out.println("1. Llistar tots els pokemons");
        System.out.println("2. Llistar tots els tipus");
        System.out.println("3. Llistar totes les habilitats");
        System.out.println("4. Llistar tots els moviments");
        System.out.println("5. Llistar totes les generacions");
        System.out.println("6. Llistar totes les localitzacions");
        System.out.println("7. Llistar totes les regions");
        System.out.println("0. Tornar al menú principal");
        System.out.println("--------------------------------");
        System.out.print("Selecciona una opció: ");
    }
}
