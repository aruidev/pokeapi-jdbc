package controller.apicontroller;

import api.PokeApiClient;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class DisplayFromApi {
    private final PokeApiClient apiClient;

    public DisplayFromApi() {
        this.apiClient = new PokeApiClient();
    }

    // Mètode per llistar tots els pokemons desde la API amb limit i suport per paginació
    public void listAllPokemons(int page) {
        int limit = 20;
        int offset = (page - 1) * limit;

        JsonObject response = apiClient.listPokemons(limit, offset);
        if (response != null) {
            JsonArray results = response.getAsJsonArray("results");
            System.out.printf("%-5s %-20s %-30s%n", "Núm.", "Nom", "URL");
            System.out.println("-------------------------------------------------------------");
            int count = offset + 1;
            for (JsonElement element : results) {
                JsonObject pokemon = element.getAsJsonObject();
                System.out.printf("%-5d %-20s %-30s%n",
                        count++,
                        pokemon.get("name").getAsString(),
                        pokemon.get("url").getAsString());
            }

            // Mostrar información de navegación
            System.out.println("\n-------------------------------------------------------------");
            System.out.println("Pàgina: " + page);
            System.out.println("< Anterior (p" + (page > 1 ? page - 1 : 1) + ") | Següent (p" + (page + 1) + ") >");
        }
    }

    // Mètode original per llistar tots els pokemons
    public void listAllPokemons() {
        listAllPokemons(1); // Mostrar la primera pàgina per defecte
    }

    // Mètode per mostrar detalls d'un pokemon específic
    public void showPokemonDetails(int id) {
        JsonObject pokemonInfo = apiClient.getPokemonInfo(id);
        if (pokemonInfo != null) {
            System.out.println("\nDetalls del Pokémon:");
            System.out.println("ID: " + pokemonInfo.get("id").getAsInt());
            System.out.println("Nom: " + pokemonInfo.get("name").getAsString());
            System.out.println("Altura: " + pokemonInfo.get("height").getAsInt());
            System.out.println("Pes: " + pokemonInfo.get("weight").getAsInt());

            // Mostrar tipus
            System.out.println("\nTipus:");
            JsonArray types = pokemonInfo.getAsJsonArray("types");
            for (JsonElement typeElement : types) {
                JsonObject typeObj = typeElement.getAsJsonObject();
                System.out.println("- " + typeObj.getAsJsonObject("type").get("name").getAsString());
            }

            // Mostrar habilitats
            System.out.println("\nHabilitats:");
            JsonArray abilities = pokemonInfo.getAsJsonArray("abilities");
            for (JsonElement abilityElement : abilities) {
                JsonObject abilityObj = abilityElement.getAsJsonObject();
                System.out.println("- " + abilityObj.getAsJsonObject("ability").get("name").getAsString());
            }
        }
    }

    // Mètode per mostrar detalls d'una habilitat
    public void showAbilityDetails(int id) {
        JsonObject abilityInfo = apiClient.getAbilityInfo(id);
        if (abilityInfo != null) {
            System.out.println("\nDetalls de l'Habilitat:");
            System.out.println("ID: " + abilityInfo.get("id").getAsInt());
            System.out.println("Nom: " + abilityInfo.get("name").getAsString());

            // Mostrar efectes
            JsonArray effectEntries = abilityInfo.getAsJsonArray("effect_entries");
            for (JsonElement effectElement : effectEntries) {
                JsonObject effectObj = effectElement.getAsJsonObject();
                if (effectObj.getAsJsonObject("language").get("name").getAsString().equals("en")) {
                    System.out.println("\nEfecte: " + effectObj.get("effect").getAsString());
                    break;
                }
            }

            // Mostrar pokemons que tenen aquesta habilitat
            System.out.println("\nPokemons amb aquesta habilitat:");
            JsonArray pokemonList = abilityInfo.getAsJsonArray("pokemon");
            for (int i = 0; i < Math.min(5, pokemonList.size()); i++) {
                JsonObject pokemonObj = pokemonList.get(i).getAsJsonObject();
                System.out.println("- " + pokemonObj.getAsJsonObject("pokemon").get("name").getAsString());
            }
        }
    }

    // Mètode per mostrar detalls d'un tipus
    public void showTypeDetails(int id) {
        JsonObject typeInfo = apiClient.getTypeInfo(id);
        if (typeInfo != null) {
            System.out.println("\nDetalls del Tipus:");
            System.out.println("ID: " + typeInfo.get("id").getAsInt());
            System.out.println("Nom: " + typeInfo.get("name").getAsString());

            // Mostrar relacions de dany
            JsonObject damageRelations = typeInfo.getAsJsonObject("damage_relations");

            System.out.println("\nDoble dany a:");
            JsonArray doubleDamageTo = damageRelations.getAsJsonArray("double_damage_to");
            for (JsonElement element : doubleDamageTo) {
                System.out.println("- " + element.getAsJsonObject().get("name").getAsString());
            }

            System.out.println("\nMeitat de dany a:");
            JsonArray halfDamageTo = damageRelations.getAsJsonArray("half_damage_to");
            for (JsonElement element : halfDamageTo) {
                System.out.println("- " + element.getAsJsonObject().get("name").getAsString());
            }
        }
    }

    // Mètode per mostrar detalls d'un moviment
    public void showMoveDetails(int id) {
        JsonObject moveInfo = apiClient.getMoveInfo(id);
        if (moveInfo != null) {
            System.out.println("\nDetalls del Moviment:");
            System.out.println("ID: " + moveInfo.get("id").getAsInt());
            System.out.println("Nom: " + moveInfo.get("name").getAsString());

            // Potencia, precisión y PP pueden ser null
            if (moveInfo.has("power") && !moveInfo.get("power").isJsonNull()) {
                System.out.println("Potència: " + moveInfo.get("power").getAsInt());
            } else {
                System.out.println("Potència: N/A");
            }

            if (moveInfo.has("accuracy") && !moveInfo.get("accuracy").isJsonNull()) {
                System.out.println("Precisió: " + moveInfo.get("accuracy").getAsInt());
            } else {
                System.out.println("Precisió: N/A");
            }

            if (moveInfo.has("pp") && !moveInfo.get("pp").isJsonNull()) {
                System.out.println("PP: " + moveInfo.get("pp").getAsInt());
            } else {
                System.out.println("PP: N/A");
            }

            // Tipus del moviment
            System.out.println("Tipus: " + moveInfo.getAsJsonObject("type").get("name").getAsString());
        }
    }

    // Mètode per mostrar detalls d'una generació
    public void showGenerationDetails(int id) {
        JsonObject generationInfo = apiClient.getGenerationInfo(id);
        if (generationInfo != null) {
            System.out.println("\nDetalls de la Generació:");
            System.out.println("ID: " + generationInfo.get("id").getAsInt());
            System.out.println("Nom: " + generationInfo.get("name").getAsString());
            System.out.println("Regió principal: " + generationInfo.getAsJsonObject("main_region").get("name").getAsString());
        }
    }

    // Mètode per mostrar detalls d'una localització
    public void showLocationDetails(int id) {
        JsonObject locationInfo = apiClient.getLocationInfo(id);
        if (locationInfo != null) {
            System.out.println("\nDetalls de la Localització:");
            System.out.println("ID: " + locationInfo.get("id").getAsInt());
            System.out.println("Nom: " + locationInfo.get("name").getAsString());
        }
    }

    // Mètode per mostrar detalls d'una regió
    public void showRegionDetails(int id) {
        JsonObject regionInfo = apiClient.getRegionInfo(id);
        if (regionInfo != null) {
            System.out.println("\nDetalls de la Regió:");
            System.out.println("ID: " + regionInfo.get("id").getAsInt());
            System.out.println("Nom: " + regionInfo.get("name").getAsString());

            // Mostrar localitzacions
            System.out.println("\nLocalitzacions:");
            JsonArray locations = regionInfo.getAsJsonArray("locations");
            for (int i = 0; i < Math.min(5, locations.size()); i++) {
                JsonObject locationObj = locations.get(i).getAsJsonObject();
                System.out.println("- " + locationObj.get("name").getAsString());
            }

            // Mostrar generació principal
            System.out.println("\nGeneració principal: " + regionInfo.getAsJsonObject("main_generation").get("name").getAsString());
        }
    }
}