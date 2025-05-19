package model.SQLite;

import Exceptions.*;
import model.constructor.Pokemon;
import model.dao.DAO;

import java.sql.*;

public class SQLitePokemonDAO implements DAO<Pokemon, Integer> {
    private Connection connection;

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void insertTable(Pokemon pokemon) {
        if (pokemon.getNom() == null) {
            throw new InvalidDataException("No es pot insertar un pokemon nul o sense nom");
        }
        if (pokemon.getId_pokemon() <= 0) {
            throw new InvalidDataException("No es pot insertar un pokemon amb ID menor o igual a 0");
        }
        if (pokemon.getPes() <= 0) {
            throw new InvalidDataException("No es pot insertar un pokemon amb pes menor o igual a 0");
        }
        if (pokemon.getAltura() <= 0) {
            throw new InvalidDataException("No es pot insertar un pokemon amb altura menor o igual a 0");
        }
        if (pokemon.getId_generacio() <= 0) {
            throw new InvalidDataException("No es pot insertar un pokemon amb ID de generació menor o igual a 0");
        }
        if (pokemon.getId_tipus() <= 0) {
            throw new InvalidDataException("No es pot insertar un pokemon amb ID de tipus menor o igual a 0");
        }
        String sql = "INSERT INTO pokemons (id_pokemon, nom, pes, altura, id_generacio, id_tipus) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, pokemon.getId_pokemon());
            stmt.setString(2, pokemon.getNom());
            stmt.setInt(3, pokemon.getPes());
            stmt.setInt(4, pokemon.getAltura());
            stmt.setInt(5, pokemon.getId_generacio());
            stmt.setInt(6, pokemon.getId_tipus());
            stmt.executeUpdate();
        } catch (SQLException e) {
            if (e.getMessage().contains("UNIQUE constraint failed")) {
                throw new DuplicateEntryException("Ja existeix un pokemon amb ID " + pokemon.getId_pokemon());
            }
            throw new DataAccessException("Error durant la operació a la DB.", e);
        }
    }

    @Override
    public void readTable(Integer id) {
        String sql = "SELECT * FROM pokemons WHERE id_pokemon = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                System.out.printf("%-5s %-20s %-10s %-10s %-15s %-10s%n", "ID", "Nom", "Pes", "Altura", "Generacio", "Tipus");
                System.out.println("-------------------------------------------------------------");
                System.out.printf("%-5d %-20s %-10d %-10d %-15d %-10d%n",
                        rs.getInt("id_pokemon"),
                        rs.getString("nom"),
                        rs.getInt("pes"),
                        rs.getInt("altura"),
                        rs.getInt("id_generacio"),
                        rs.getInt("id_tipus"));
            } else {
                throw new PropertyNotFound("No existeix el pokemon amb la ID " + id);
            }
        } catch (PropertyNotFound | SQLException e) {
            System.err.println("Error al llegir el pokemon: " + e.getMessage());
        }
    }

    @Override
    public void updateTable(Pokemon pokemon) {
        if (pokemon.getNom() == null) {
            throw new InvalidDataException("No es pot actualitzar un pokemon nul o sense nom");
        }
        if (pokemon.getPes() <= 0) {
            throw new InvalidDataException("No es pot insertar un pokemon amb pes menor o igual a 0");
        }
        if (pokemon.getAltura() <= 0) {
            throw new InvalidDataException("No es pot actualitzar un pokemon amb altura menor o igual a 0");
        }
        if (pokemon.getId_generacio() <= 0) {
            throw new InvalidDataException("No es pot insertar un pokemon amb ID de generació menor o igual a 0");
        }
        if (pokemon.getId_tipus() <= 0) {
            throw new InvalidDataException("No es pot actualitzar un pokemon amb ID de tipus menor o igual a 0");
        }
        String sql = "UPDATE pokemons SET nom = ?, pes = ?, altura = ?, id_generacio = ?, id_tipus = ? WHERE id_pokemon = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, pokemon.getNom());
            stmt.setInt(2, pokemon.getPes());
            stmt.setInt(3, pokemon.getAltura());
            stmt.setInt(4, pokemon.getId_generacio());
            stmt.setInt(5, pokemon.getId_tipus());
            stmt.setInt(6, pokemon.getId_pokemon());
            stmt.executeUpdate();
            if (stmt.getUpdateCount() == -1) {
                throw new PropertyNotFound("No existeix el pokemon amb ID " + pokemon.getId_pokemon());
            }
        } catch (PropertyNotFound e) {
            System.err.println("Error al actualitzar el pokemon: " + e.getMessage());
        } catch (SQLException e) {
            if (e.getMessage().contains("UNIQUE constraint failed")) {
                throw new DuplicateEntryException("Ja existeix un pokemon amb ID " + pokemon.getId_pokemon());
            }
            throw new DataAccessException("Error durant la operació a la DB.", e);
        }
    }

    @Override
    public void deleteTable(Integer id) {
        String sql = "DELETE FROM pokemons WHERE id_pokemon = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
            if (stmt.getUpdateCount() == -1) {
                throw new PropertyNotFound("No existeix el pokemon amb la ID " + id);
            }
        } catch (PropertyNotFound e) {
            System.err.println("Error al eliminar el pokemon: " + e.getMessage());
        } catch (SQLException e) {
            if (e.getMessage().contains("FOREIGN KEY constraint failed")) {
                throw new ForeignKeyConstraintException("No es pot eliminar el pokemon amb la ID " + id + " perque s'està referenciant una altre taula.");
            }
            throw new DataAccessException("Error durant la operació a la DB.", e);
        }
    }

    @Override
    public void readAll() {
        String sql = "SELECT * FROM pokemons";
        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            if (!rs.isBeforeFirst()) { // Si el resultat està buit
                throw new EmptyResultSetException("Ningún pokemon trobat a la DB.");
            }
            System.out.printf("%-5s %-20s %-10s %-10s %-15s %-10s%n", "ID", "Nom", "Pes", "Altura", "Generacio", "Tipus");
            System.out.println("-------------------------------------------------------------");
            while (rs.next()) {
                System.out.printf("%-5d %-20s %-10d %-10d %-15d %-10d%n",
                        rs.getInt("id_pokemon"),
                        rs.getString("nom"),
                        rs.getInt("pes"),
                        rs.getInt("altura"),
                        rs.getInt("id_generacio"),
                        rs.getInt("id_tipus"));
            }
        } catch (EmptyResultSetException e) {
            System.err.println(e.getMessage());
        } catch (SQLException e) {
            throw new DataAccessException("Error durant la operació a la DB", e);
        }
    }
}