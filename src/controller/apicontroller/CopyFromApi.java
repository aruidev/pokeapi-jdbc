package controller.apicontroller;

import api.PokeApiClient;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import model.SQLite.*;
import model.constructor.Ability;
import model.constructor.Pokemon;
import model.constructor.Type;
import model.constructor.Move;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CopyFromApi {
    private final PokeApiClient apiClient;
    private final SQLitePokemonDAO pokemonDAO;
    private final SQLiteTypeDAO typeDAO;
    private final SQLiteAbilityDAO abilityDAO;
    private final SQLiteMoveDAO moveDAO;
    private final Connection connection;

    public CopyFromApi(SQLitePokemonDAO pokemonDAO, SQLiteTypeDAO typeDAO,
                      SQLiteAbilityDAO abilityDAO, SQLiteMoveDAO moveDAO,
                      Connection connection) {
        this.apiClient = new PokeApiClient();
        this.pokemonDAO = pokemonDAO;
        this.typeDAO = typeDAO;
        this.abilityDAO = abilityDAO;
        this.moveDAO = moveDAO;
        this.connection = connection;
    }

    /**
     * Importa un interval de pokemons desde la API
     * @param startId ID inicial per importar
     * @param endId ID final per importar
     * @param overwriteExisting si es true, sobreescriu els pokemons existents
     */
    public void importPokemonRange(int startId, int endId, boolean overwriteExisting) {
        if (startId <= 0 || endId < startId) {
            System.out.println("El rang d'IDs no és vàlid. Assegura't que l'ID inicial és major que 0 i l'ID final és major o igual a l'ID inicial.");
            return;
        }

        System.out.println("Iniciant importació de pokemons desde la ID " + startId + " a " + endId + "...");
        int countAdded = 0;
        int countSkipped = 0;

        for (int id = startId; id <= endId; id++) {
            System.out.print("Important pokemon #" + id + "... ");

            JsonObject pokemonJson = apiClient.getPokemonInfo(id);
            if (pokemonJson == null) {
                System.out.println("No trobat");
                continue;
            }

            boolean exists = checkIfPokemonExists(id);

            if (exists && !overwriteExisting) {
                System.out.println("Omitint (ja existeix)");
                countSkipped++;
                continue;
            }

            importPokemon(pokemonJson, exists);
            System.out.println(exists ? "Actualitzat" : "Afegit");
            countAdded++;
        }

        System.out.println("Importació completada:");
        System.out.println("Pokemons afegits/actualitzats: " + countAdded);
        System.out.println("Pokemons omitits (ja existeixen): " + countSkipped);
    }

    /**
     * Importa un pokemon des de la API
     */
    private void importPokemon(JsonObject pokemonJson, boolean exists) {
        // dades bàsiques pokemon
        int id = pokemonJson.get("id").getAsInt();
        String name = pokemonJson.get("name").getAsString();
        int height = pokemonJson.get("height").getAsInt();
        int weight = pokemonJson.get("weight").getAsInt();

        // Obtenir tipus principal per la taula pokemon
        int typeId = 1; // Valor por defecte
        if (pokemonJson.has("types") && pokemonJson.getAsJsonArray("types").size() > 0) {
            JsonObject typeObj = pokemonJson.getAsJsonArray("types").get(0)
                    .getAsJsonObject().getAsJsonObject("type");
            String typeUrl = typeObj.get("url").getAsString();
            typeId = extractIdFromUrl(typeUrl);

            // Importar tipus associats al pokemon quan sigui possible
            importTypesFromPokemon(pokemonJson);
        }

        // Obtenir generació del pokemon
        int genId = 1; // Valor por defecte
        if (pokemonJson.has("species")) {
            String speciesUrl = pokemonJson.getAsJsonObject("species").get("url").getAsString();
            int speciesId = extractIdFromUrl(speciesUrl);

            // Obtenir informació de l'espècie des de la API
            JsonObject speciesJson = apiClient.getPokemonSpeciesInfo(speciesId);
            if (speciesJson != null && speciesJson.has("generation")) {
                String generationUrl = speciesJson.getAsJsonObject("generation").get("url").getAsString();
                genId = extractIdFromUrl(generationUrl);
            }
        }

        // Crear objecte pokemon i insertar/actualitzar
        Pokemon pokemon = new Pokemon(id, name, weight, height, genId, typeId);
        if (exists) {
            pokemonDAO.updateTable(pokemon);
        } else {
            pokemonDAO.insertTable(pokemon);
        }

        // Importar habilitats si estàn disponibles
        if (pokemonJson.has("abilities")) {
            importAbilitiesFromPokemon(pokemonJson);
        }

        // Importar moviments si estàn actualitzats
        if (pokemonJson.has("moves")) {
            importMovesFromPokemon(pokemonJson);
        }
    }

    /**
     * Importa tipus associats a un pokemon
     */
    private void importTypesFromPokemon(JsonObject pokemonJson) {
        JsonArray types = pokemonJson.getAsJsonArray("types");
        for (JsonElement typeElement : types) {
            JsonObject typeObj = typeElement.getAsJsonObject().getAsJsonObject("type");
            String typeUrl = typeObj.get("url").getAsString();
            int typeId = extractIdFromUrl(typeUrl);

            // Verificar si el tipus ja existeix
            if (!checkIfTypeExists(typeId)) {
                // Obtenir informació completa del tipus desde la API
                JsonObject typeJson = apiClient.getTypeInfo(typeId);
                if (typeJson != null) {
                    String typeName = typeJson.get("name").getAsString();
                    // Procesar las relaciones de daño de forma legible
                    String typeRelations = formatDamageRelations(typeJson.getAsJsonObject("damage_relations"));

                    Type type = new Type(typeId, typeName, typeRelations);
                    typeDAO.insertTable(type);
                }
            }
        }
    }

    // Mètode per formatar el String retornat de "damage_relations" i passar-lo a un String llegible
    private String formatDamageRelations(JsonObject damageRelations) {
        StringBuilder sb = new StringBuilder();

        // Doble daño a
        sb.append("Doble dany a: ");
        JsonArray doubleDamageTo = damageRelations.getAsJsonArray("double_damage_to");
        if (doubleDamageTo.size() > 0) {
            for (int i = 0; i < doubleDamageTo.size(); i++) {
                if (i > 0) sb.append(", ");
                sb.append(doubleDamageTo.get(i).getAsJsonObject().get("name").getAsString());
            }
        } else {
            sb.append("Ningún");
        }

        sb.append("; ");

        // Doble daño de
        sb.append("Doble dany de: ");
        JsonArray doubleDamageFrom = damageRelations.getAsJsonArray("double_damage_from");
        if (doubleDamageFrom.size() > 0) {
            for (int i = 0; i < doubleDamageFrom.size(); i++) {
                if (i > 0) sb.append(", ");
                sb.append(doubleDamageFrom.get(i).getAsJsonObject().get("name").getAsString());
            }
        } else {
            sb.append("Ningún");
        }

        sb.append("; ");

        // Mitad de daño a
        sb.append("Meitat de dany a: ");
        JsonArray halfDamageTo = damageRelations.getAsJsonArray("half_damage_to");
        if (halfDamageTo.size() > 0) {
            for (int i = 0; i < halfDamageTo.size(); i++) {
                if (i > 0) sb.append(", ");
                sb.append(halfDamageTo.get(i).getAsJsonObject().get("name").getAsString());
            }
        } else {
            sb.append("Ningún");
        }

        return sb.toString();
    }

    /**
     * Importa habilitats associades a un pokemon
     */
    private void importAbilitiesFromPokemon(JsonObject pokemonJson) {
        JsonArray abilities = pokemonJson.getAsJsonArray("abilities");
        for (JsonElement abilityElement : abilities) {
            JsonObject abilityObj = abilityElement.getAsJsonObject().getAsJsonObject("ability");
            String abilityUrl = abilityObj.get("url").getAsString();
            int abilityId = extractIdFromUrl(abilityUrl);

            // Verifica si la habilitat ja existeix
            if (!checkIfAbilityExists(abilityId)) {
                // Obtenir informació completa de la habilitat desde la API
                JsonObject abilityJson = apiClient.getAbilityInfo(abilityId);
                if (abilityJson != null) {
                    String abilityName = abilityJson.get("name").getAsString();
                    String effect = "Efecte no disponible";

                    if (abilityJson.has("effect_entries")) {
                        JsonArray effects = abilityJson.getAsJsonArray("effect_entries");
                        for (JsonElement effectElem : effects) {
                            JsonObject effectObj = effectElem.getAsJsonObject();
                            if (effectObj.getAsJsonObject("language").get("name").getAsString().equals("en")) {
                                effect = effectObj.get("effect").getAsString();
                                break;
                            }
                        }
                    }

                    Ability ability = new Ability(abilityId, abilityName, effect);
                    abilityDAO.insertTable(ability);
                }
            }
        }
    }

    /**
     * Importa els moviments associats a un pokemon
     */
    private void importMovesFromPokemon(JsonObject pokemonJson) {
        JsonArray moves = pokemonJson.getAsJsonArray("moves");

        // Limita a 5 moviments per no sobrecarregar la API o la DB
        int limit = Math.min(5, moves.size());

        for (int i = 0; i < limit; i++) {
            JsonObject moveObj = moves.get(i).getAsJsonObject().getAsJsonObject("move");
            String moveUrl = moveObj.get("url").getAsString();
            int moveId = extractIdFromUrl(moveUrl);

            // Verificar si moviment ja existeix
            if (!checkIfMoveExists(moveId)) {
                // Obtenir informació del moviment desde la API
                JsonObject moveJson = apiClient.getMoveInfo(moveId);
                if (moveJson != null) {
                    String moveName = moveJson.get("name").getAsString();

                    int power = moveJson.has("power") && !moveJson.get("power").isJsonNull() ?
                            moveJson.get("power").getAsInt() : 0;

                    int accuracy = moveJson.has("accuracy") && !moveJson.get("accuracy").isJsonNull() ?
                            moveJson.get("accuracy").getAsInt() : 0;

                    int pp = moveJson.has("pp") && !moveJson.get("pp").isJsonNull() ?
                            moveJson.get("pp").getAsInt() : 0;

                    // Valors no poden ser 0
                    power = power <= 0 ? 1 : power;
                    accuracy = accuracy <= 0 ? 100 : accuracy;
                    pp = pp <= 0 ? 10 : pp;

                    String type = moveJson.getAsJsonObject("type").get("name").getAsString();

                    Move move = new Move(moveId, moveName, power, accuracy, pp, type);
                    try {
                        moveDAO.insertTable(move);
                    } catch (Exception e) {
                        System.err.println("Error al insertar movimiento " + moveId + ": " + e.getMessage());
                    }
                }
            }
        }
    }

    /**
     * Comprova si el pokemon existeix a la DB local
     */
    private boolean checkIfPokemonExists(int id) {
        try {
            String query = "SELECT COUNT(*) FROM pokemons WHERE id_pokemon = ?";
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
            return false;
        } catch (SQLException e) {
            System.err.println("Error al verificar si existeix el pokemon a la DB local: " + e.getMessage());
            return false;
        }
    }

    /**
     * Comprova si un tipus existeix a la DB local
     */
    private boolean checkIfTypeExists(int id) {
        try {
            String query = "SELECT COUNT(*) FROM tipus WHERE id_tipus = ?";
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
            return false;
        } catch (SQLException e) {
            System.err.println("Error al verificar si existeix el tipus a la DB local: " + e.getMessage());
            return false;
        }
    }

    /**
     * Comprova si una habilitat existeix a la DB local
     */
    private boolean checkIfAbilityExists(int id) {
        try {
            String query = "SELECT COUNT(*) FROM habilitats WHERE id_habilitat = ?";
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
            return false;
        } catch (SQLException e) {
            System.err.println("Error al verificar si existeix la habilitat a la DB local: " + e.getMessage());
            return false;
        }
    }

    /**
     * Comprova si un moviment existeix a la DB local
     */
    private boolean checkIfMoveExists(int id) {
        try {
            String query = "SELECT COUNT(*) FROM moviments WHERE id_moviment = ?";
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
            return false;
        } catch (SQLException e) {
            System.err.println("Error al verificar si existeix el moviment a la DB local: " + e.getMessage());
            return false;
        }
    }

    /**
     * Extrau la ID de una URL
     */
    private int extractIdFromUrl(String url) {
        try {
            String[] parts = url.split("/");

            for (int i = parts.length - 1; i >= 0; i--) {
                if (!parts[i].isEmpty()) {
                    return Integer.parseInt(parts[i]);
                }
            }

            System.err.println("No s'ha pogut extreure la ID de la URL: " + url);
        } catch (NumberFormatException e) {
            System.err.println("La URL no conté un ID vàlid: " + url);
        } catch (Exception e) {
            System.err.println("Error al processar URL: " + url);
        }

        return 1; // Valor por defecte
    }
}