package controller.json;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class JsonFileReader {
    private final Gson gson;

    public JsonFileReader() {
        this.gson = new Gson();
    }

    /**
     * Llegeix un arxiu JSON i retorna un objecte JsonObject
     */
    public JsonObject readJsonFile(String filePath) {
        try (FileReader reader = new FileReader(filePath)) {
            return gson.fromJson(reader, JsonObject.class);
        } catch (IOException e) {
            System.err.println("Error al llegir l'arxiu JSON: " + e.getMessage());
            return null;
        }
    }

    /**
     * Verifica si l'arxiu existeix
     */
    public boolean fileExists(String filePath) {
        Path path = Paths.get(filePath);
        return Files.exists(path);
    }

    /**
     * Obté informació d'un pokemon des d'un arxiu JSON
     */
    public JsonObject getPokemonInfo(String basePath, int id) {
        String filePath = basePath + "/pokemon_" + id + ".json";
        if (!fileExists(filePath)) {
            System.err.println("El archivo " + filePath + " no existe");
            return null;
        }
        return readJsonFile(filePath);
    }

    /**
     * Obté informació d'una habilitat des d'un arxiu JSON
     */
    public JsonObject getAbilityInfo(String basePath, int id) {
        String filePath = basePath + "/ability_" + id + ".json";
        if (!fileExists(filePath)) {
            System.err.println("El archivo " + filePath + " no existe");
            return null;
        }
        return readJsonFile(filePath);
    }

    /**
     * Obté informació d'un tipus des d'un arxiu JSON
     */
    public JsonObject getTypeInfo(String basePath, int id) {
        String filePath = basePath + "/type_" + id + ".json";
        if (!fileExists(filePath)) {
            System.err.println("El archivo " + filePath + " no existe");
            return null;
        }
        return readJsonFile(filePath);
    }

    /**
     * Obté informació d'un moviment des d'un arxiu JSON
     */
    public JsonObject getMoveInfo(String basePath, int id) {
        String filePath = basePath + "/move_" + id + ".json";
        if (!fileExists(filePath)) {
            System.err.println("El archivo " + filePath + " no existe");
            return null;
        }
        return readJsonFile(filePath);
    }

    /**
     * Obté informació d'una generació des d'un arxiu JSON
     */
    public JsonObject getGenerationInfo(String basePath, int id) {
        String filePath = basePath + "/generation_" + id + ".json";
        if (!fileExists(filePath)) {
            System.err.println("El archivo " + filePath + " no existe");
            return null;
        }
        return readJsonFile(filePath);
    }

    /**
     * Obté informació d'una localització des d'un arxiu JSON
     */
    public JsonObject getLocationInfo(String basePath, int id) {
        String filePath = basePath + "/location_" + id + ".json";
        if (!fileExists(filePath)) {
            System.err.println("El archivo " + filePath + " no existe");
            return null;
        }
        return readJsonFile(filePath);
    }

    /**
     * Obté informació d'una regió des d'un arxiu JSON
     */
    public JsonObject getRegionInfo(String basePath, int id) {
        String filePath = basePath + "/region_" + id + ".json";
        if (!fileExists(filePath)) {
            System.err.println("El archivo " + filePath + " no existe");
            return null;
        }
        return readJsonFile(filePath);
    }

    /**
     * Obté tots els arxius pokemon d'un directori
     */
    public String[] listAllPokemonFiles(String basePath) {
        try (Stream<Path> pathStream = Files.list(Paths.get(basePath))) {
            return pathStream
                    .map(Path::getFileName)
                    .map(Path::toString)
                    .filter(name -> name.startsWith("pokemon_") && name.endsWith(".json"))
                    .toArray(String[]::new);
        } catch (IOException e) {
            System.err.println("Error al llistar arxius: " + e.getMessage());
            return new String[0];
        }
    }
}