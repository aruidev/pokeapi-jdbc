package controller.json;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import model.SQLite.*;
import model.constructor.Ability;
import model.constructor.Pokemon;
import model.constructor.Type;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CopyFromJson {
    private final JsonFileReader jsonFileReader;
    private final String basePath;
    private final SQLitePokemonDAO pokemonDAO;
    private final SQLiteTypeDAO typeDAO;
    private final SQLiteAbilityDAO abilityDAO;
    private final Connection connection;

    public CopyFromJson(String basePath, SQLitePokemonDAO pokemonDAO,
                        SQLiteTypeDAO typeDAO, SQLiteAbilityDAO abilityDAO,
                        Connection connection) {
        this.jsonFileReader = new JsonFileReader();
        this.basePath = basePath;
        this.pokemonDAO = pokemonDAO;
        this.typeDAO = typeDAO;
        this.abilityDAO = abilityDAO;
        this.connection = connection;
    }

    /**
     * Importa tots els pokemons des d'arxius JSON
     * @param overwriteExisting si es true, sobreescriu els pokemons existents
     */
    public void importAllPokemons(boolean overwriteExisting) {
        String[] files = jsonFileReader.listAllPokemonFiles(basePath);

        if (files.length == 0) {
            System.out.println("No s'han trobat arxius JSON a " + basePath);
            return;
        }

        System.out.println("Iniciant importació de dades des d'els arxius JSON...");
        int countAdded = 0;
        int countSkipped = 0;

        for (String file : files) {
            // Extraer id del nombre del archivo (pokemon_X.json)
            int id = Integer.parseInt(file.substring(8, file.length() - 5));
            JsonObject pokemonJson = jsonFileReader.getPokemonInfo(basePath, id);

            if (pokemonJson == null) continue;

            boolean exists = checkIfPokemonExists(id);

            if (exists && !overwriteExisting) {
                System.out.println("Omitint pokemon #" + id + " (ja existeix)");
                countSkipped++;
                continue;
            }

            importPokemon(pokemonJson, exists);
            countAdded++;
        }

        System.out.println("Importació completada:");
        System.out.println("Pokemons afegits/actualitzats: " + countAdded);
        System.out.println("Pokemons omitits (ja existien: " + countSkipped);
    }

    /**
     * Importa un pokemon específic pel seu ID des d'arxiu JSON
     * @param id ID del pokemon a importar
     * @param overwriteExisting si es true, sobreescriu el pokemon si ja existeix
     * @return true si s'ha importat correctament, false en cas contrari
     */
    public boolean importSinglePokemonFromJson(int id, boolean overwriteExisting) {
        System.out.println("Iniciant importació del pokemon #" + id + " des de JSON...");

        JsonObject pokemonJson = jsonFileReader.getPokemonInfo(basePath, id);

        if (pokemonJson == null) {
            System.out.println("No s'ha trobat l'arxiu JSON per al pokemon #" + id);
            return false;
        }

        boolean exists = checkIfPokemonExists(id);

        if (exists && !overwriteExisting) {
            System.out.println("Omitint pokemon #" + id + " (ja existeix)");
            return false;
        }

        importPokemon(pokemonJson, exists);
        System.out.println("Pokemon #" + id + " importat correctament");
        return true;
    }

    /**
     * Importa un pokemon específic desde JSON
     */
    private void importPokemon(JsonObject pokemonJson, boolean exists) {
        // Dades bàsiques del pokemon
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

            // Importar tipus quan sigui posible
            importTypesFromPokemon(pokemonJson);
        }

        // Valor per defecte generació
        int genId = 1;

        // Crear objecte pokemon i insertar/actualitzar
        Pokemon pokemon = new Pokemon(id, name, weight, height, genId, typeId);
        if (exists) {
            pokemonDAO.updateTable(pokemon);
        } else {
            pokemonDAO.insertTable(pokemon);
        }

        // Importar habilitats si estan disponibles
        if (pokemonJson.has("abilities")) {
            importAbilitiesFromPokemon(pokemonJson);
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
            String typeName = typeObj.get("name").getAsString();

            // Obtenir relacions de dany del JSON complet del tipus
            JsonObject typeJson = jsonFileReader.getTypeInfo(basePath, typeId);
            String typeRelations = typeJson != null && typeJson.has("damage_relations")
                    ? typeJson.get("damage_relations").toString()
                    : "";

            // Verificar si el tipus ja existeix
            if (!checkIfTypeExists(typeId)) {
                if (typeJson != null) {
                    Type type = new Type(typeId, typeName, typeRelations);
                    typeDAO.insertTable(type);
                }
            }
        }
    }

    /**
     * Importa les habilitats associades a un pokemon
     */
    private void importAbilitiesFromPokemon(JsonObject pokemonJson) {
        JsonArray abilities = pokemonJson.getAsJsonArray("abilities");
        for (JsonElement abilityElement : abilities) {
            JsonObject abilityObj = abilityElement.getAsJsonObject().getAsJsonObject("ability");
            String abilityUrl = abilityObj.get("url").getAsString();
            int abilityId = extractIdFromUrl(abilityUrl);
            String abilityName = abilityObj.get("name").getAsString();

            // Verificar si l'habilitat ja existeix
            if (!checkIfAbilityExists(abilityId)) {
                // Obtenir més detalls de l'habilitat des de JSON
                JsonObject abilityJson = jsonFileReader.getAbilityInfo(basePath, abilityId);
                String effect = "Efecto no disponible";

                if (abilityJson != null && abilityJson.has("effect_entries")) {
                    JsonArray effects = abilityJson.getAsJsonArray("effect_entries");
                    for (JsonElement effectElem : effects) {
                        JsonObject effectObj = effectElem.getAsJsonObject();
                        if (effectObj.getAsJsonObject("language").get("name").getAsString().equals("en")) {
                            effect = effectObj.get("short_effect").getAsString();
                            break;
                        }
                    }
                }

                Ability ability = new Ability(abilityId, abilityName, effect);
                abilityDAO.insertTable(ability);
            }
        }
    }

    /**
     * Comprova si un pokemon existeix a la base de dades local
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
            System.err.println("Error al verificar si existeix el pokemon: " + e.getMessage());
            return false;
        }
    }

    /**
     * Comprova si un tipus existeix a la base de dades local
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
            System.err.println("Error al verificar si existeix el tipus: " + e.getMessage());
            return false;
        }
    }

    /**
     * Comprova si una habilitat existeix a la base de dades local
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
            System.err.println("Error al verificar si existeix l'habilitat: " + e.getMessage());
            return false;
        }
    }

    /**
     * Extreu la ID d'un URL utilitzant una expressió regular
     */
    private int extractIdFromUrl(String url) {
        try {
            // Dividir la URL por "/"
            String[] parts = url.split("/");

            // Buscar el último elemento no vacío (que debería ser el ID)
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

        return 1; // Valor per defecte
    }
}