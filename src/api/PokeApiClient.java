package api;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class PokeApiClient {
    private static final String BASE_URL = "https://pokeapi.co/api/v2/";
    private final HttpClient httpClient;
    private final Gson gson;

    public PokeApiClient() {
        this.httpClient = HttpClient.newHttpClient();
        this.gson = new Gson();
    }

    /**
     * Realitza una petició GET a l'API de Pokémon
     */
    private JsonObject makeGetRequest(String endpoint) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + endpoint))
                .GET()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            System.out.println("Dades recuperades correctament");
            return gson.fromJson(response.body(), JsonObject.class);
        } else {
            System.out.println("Error al recuperar dades: " + response.statusCode());
            return null;
        }
    }

    /**
     * Obté informació d'un Pokémon per ID
     */
    public JsonObject getPokemonInfo(int id) {
        try {
            return makeGetRequest("pokemon/" + id);
        } catch (Exception e) {
            System.err.println("Error al obtenir informació del pokemon: " + e.getMessage());
            return null;
        }
    }

    /**
     * Obté informació d'una habilitat per ID
     */
    public JsonObject getAbilityInfo(int id) {
        try {
            return makeGetRequest("ability/" + id);
        } catch (Exception e) {
            System.err.println("Error al obtenir informació de l'habilitat: " + e.getMessage());
            return null;
        }
    }

    /**
     * Obté informació d'un tipus per ID
     */
    public JsonObject getTypeInfo(int id) {
        try {
            return makeGetRequest("type/" + id);
        } catch (Exception e) {
            System.err.println("Error al obtener informació del tipus: " + e.getMessage());
            return null;
        }
    }

    /**
     * Obté informació d'un moviment per ID
     */
    public JsonObject getMoveInfo(int id) {
        try {
            return makeGetRequest("move/" + id);
        } catch (Exception e) {
            System.err.println("Error al obtenir informació del moviment: " + e.getMessage());
            return null;
        }
    }

    /**
     * Obté informació d'una generació per ID
     */
    public JsonObject getGenerationInfo(int id) {
        try {
            return makeGetRequest("generation/" + id);
        } catch (Exception e) {
            System.err.println("Error al obtenir informació de la generació: " + e.getMessage());
            return null;
        }
    }

    /**
     * Obté informació d'una localització per ID
     */
    public JsonObject getLocationInfo(int id) {
        try {
            return makeGetRequest("location/" + id);
        } catch (Exception e) {
            System.err.println("Error al obtenir informació de la localització: " + e.getMessage());
            return null;
        }
    }

    /**
     * Obté informació d'una regió per ID
     */
    public JsonObject getRegionInfo(int id) {
        try {
            return makeGetRequest("region/" + id);
        } catch (Exception e) {
            System.err.println("Error al obtenir informació de la regió: " + e.getMessage());
            return null;
        }
    }

    /**
     * Obté informació d'una llista de Pokémon amb paginació
     */
    public JsonObject listPokemons(int limit, int offset) {
        try {
            return makeGetRequest("pokemon?limit=" + limit + "&offset=" + offset);
        } catch (Exception e) {
            System.err.println("Error al obtenir la llista de pokemon: " + e.getMessage());
            return null;
        }
    }

    /**
     * Obté informació d'una espècie de Pokémon per ID
     */
    public JsonObject getPokemonSpeciesInfo(int id) {
        try {
            return makeGetRequest("pokemon-species/" + id);
        } catch (Exception e) {
            System.err.println("Error al obtenir informació de l'espècie de pokemon: " + e.getMessage());
            return null;
        }
    }
}