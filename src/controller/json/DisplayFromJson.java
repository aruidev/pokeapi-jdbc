package controller.json;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class DisplayFromJson {
    private final JsonFileReader jsonFileReader;
    private final String basePath;

    public DisplayFromJson(String basePath) {
        this.jsonFileReader = new JsonFileReader();
        this.basePath = basePath;
    }

    /**
     * Llista tots els pokemons disponibles en el directori especificat
     */
    public void listAllPokemons() {
        String[] files = jsonFileReader.listAllPokemonFiles(basePath);

        if (files.length == 0) {
            System.out.println("No se encontraron archivos de pokémon en " + basePath);
            return;
        }

        System.out.printf("%-5s %-20s %-10s %-10s%n", "ID", "Nom", "Altura", "Peso");
        System.out.println("--------------------------------------------------");

        for (String file : files) {
            // Extraer ID del nombre del archivo (pokemon_X.json)
            int id = Integer.parseInt(file.substring(8, file.length() - 5));
            JsonObject pokemon = jsonFileReader.getPokemonInfo(basePath, id);

            if (pokemon != null) {
                System.out.printf("%-5d %-20s %-10d %-10d%n",
                        pokemon.get("id").getAsInt(),
                        pokemon.get("name").getAsString(),
                        pokemon.get("height").getAsInt(),
                        pokemon.get("weight").getAsInt());
            }
        }
    }

    /**
     * Mostra els detalls d'un pokemon específic
     */
    public void showPokemonDetails(int id) {
        JsonObject pokemon = jsonFileReader.getPokemonInfo(basePath, id);
        if (pokemon == null) return;

        System.out.println("\nDetalles del Pokémon:");
        System.out.println("ID: " + pokemon.get("id").getAsInt());
        System.out.println("Nombre: " + pokemon.get("name").getAsString());
        System.out.println("Altura: " + pokemon.get("height").getAsInt());
        System.out.println("Peso: " + pokemon.get("weight").getAsInt());

        // Mostrar tipus
        System.out.println("\nTipos:");
        JsonArray types = pokemon.getAsJsonArray("types");
        for (JsonElement type : types) {
            System.out.println("- " + type.getAsJsonObject()
                    .getAsJsonObject("type")
                    .get("name").getAsString());
        }

        // Mostrar habilitats
        System.out.println("\nHabilidades:");
        JsonArray abilities = pokemon.getAsJsonArray("abilities");
        for (JsonElement ability : abilities) {
            JsonObject abilityObj = ability.getAsJsonObject();
            System.out.println("- " + abilityObj.getAsJsonObject("ability")
                    .get("name").getAsString());
        }
    }

    /**
     * Mostra habilitats d'una habilitat específica
     */
    public void showAbilityDetails(int id) {
        JsonObject ability = jsonFileReader.getAbilityInfo(basePath, id);
        if (ability == null) return;

        System.out.println("\nDetalles de la Habilidad:");
        System.out.println("ID: " + ability.get("id").getAsInt());
        System.out.println("Nombre: " + ability.get("name").getAsString());

        // Mostrar efectos
        System.out.println("\nEfecto:");
        JsonArray effects = ability.getAsJsonArray("effect_entries");
        for (JsonElement effect : effects) {
            JsonObject effectObj = effect.getAsJsonObject();
            if (effectObj.getAsJsonObject("language").get("name").getAsString().equals("en")) {
                System.out.println(effectObj.get("effect").getAsString());
            }
        }
    }

    /**
     * Mostra detalls d'un tipus específic
     */
    public void showTypeDetails(int id) {
        JsonObject type = jsonFileReader.getTypeInfo(basePath, id);
        if (type == null) return;

        System.out.println("\nDetalles del Tipo:");
        System.out.println("ID: " + type.get("id").getAsInt());
        System.out.println("Nombre: " + type.get("name").getAsString());

        // Mostrar relaciones de daño
        System.out.println("\nRelaciones de daño:");
        JsonObject damageRelations = type.getAsJsonObject("damage_relations");

        System.out.println("Doble daño a:");
        for (JsonElement t : damageRelations.getAsJsonArray("double_damage_to")) {
            System.out.println("- " + t.getAsJsonObject().get("name").getAsString());
        }

        System.out.println("Mitad de daño a:");
        for (JsonElement t : damageRelations.getAsJsonArray("half_damage_to")) {
            System.out.println("- " + t.getAsJsonObject().get("name").getAsString());
        }
    }

    /**
     * Mostra detalls d'un moviment específic
     */
    public void showMoveDetails(int id) {
        JsonObject move = jsonFileReader.getMoveInfo(basePath, id);
        if (move == null) return;

        System.out.println("\nDetalles del Movimiento:");
        System.out.println("ID: " + move.get("id").getAsInt());
        System.out.println("Nombre: " + move.get("name").getAsString());
        System.out.println("Potencia: " + (move.get("power").isJsonNull() ? "N/A" : move.get("power").getAsInt()));
        System.out.println("Precisión: " + (move.get("accuracy").isJsonNull() ? "N/A" : move.get("accuracy").getAsInt()));
        System.out.println("PP: " + move.get("pp").getAsInt());
        System.out.println("Tipo: " + move.getAsJsonObject("type").get("name").getAsString());
    }

    /**
     * Mostra detalls d'una generació específica
     */
    public void showGenerationDetails(int id) {
        JsonObject generation = jsonFileReader.getGenerationInfo(basePath, id);
        if (generation == null) return;

        System.out.println("\nDetalles de la Generación:");
        System.out.println("ID: " + generation.get("id").getAsInt());
        System.out.println("Nombre: " + generation.get("name").getAsString());
        System.out.println("Región principal: " + generation.getAsJsonObject("main_region").get("name").getAsString());
    }

    /**
     * Mostra detalls d'una localització específica
     */
    public void showLocationDetails(int id) {
        JsonObject location = jsonFileReader.getLocationInfo(basePath, id);
        if (location == null) return;

        System.out.println("\nDetalles de la Localización:");
        System.out.println("ID: " + location.get("id").getAsInt());
        System.out.println("Nombre: " + location.get("name").getAsString());
        System.out.println("Región: " + location.getAsJsonObject("region").get("name").getAsString());
    }

    /**
     * Mostra detalls d'una regió específica
     */
    public void showRegionDetails(int id) {
        JsonObject region = jsonFileReader.getRegionInfo(basePath, id);
        if (region == null) return;

        System.out.println("\nDetalles de la Región:");
        System.out.println("ID: " + region.get("id").getAsInt());
        System.out.println("Nombre: " + region.get("name").getAsString());

        System.out.println("\nLocalizaciones:");
        JsonArray locations = region.getAsJsonArray("locations");
        for (JsonElement location : locations) {
            System.out.println("- " + location.getAsJsonObject().get("name").getAsString());
        }
    }
}