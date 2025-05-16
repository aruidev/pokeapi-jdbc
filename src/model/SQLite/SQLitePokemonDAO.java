package model.SQLite;

import Exceptions.PropertyNotFound;
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
            e.printStackTrace();
        }
    }

    @Override
    public void readTable(Integer id) {
        String sql = "SELECT * FROM pokemons WHERE id_pokemon = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                System.out.printf("%-5s %-20s %-10s %-10s %-15s %-10s%n", "ID", "Nom", "Pes", "Altura", "ID Generacio", "ID Tipus");
                System.out.println("-------------------------------------------------------------");
                System.out.printf("%-5d %-20s %-10d %-10d %-15d %-10d%n",
                        rs.getInt("id_pokemon"),
                        rs.getString("nom"),
                        rs.getInt("pes"),
                        rs.getInt("altura"),
                        rs.getInt("id_generacio"),
                        rs.getInt("id_tipus"));
            } else {
                throw new PropertyNotFound("No existe el Pokémon con ID " + id);
            }
        } catch (PropertyNotFound | SQLException e) {
            System.err.println("Error al leer el Pokémon: " + e.getMessage());
        }
    }

    @Override
    public void updateTable(Pokemon pokemon) {
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
                throw new PropertyNotFound("No existeix el Pokemon amb ID " + pokemon.getId_pokemon());
            }
        } catch (PropertyNotFound e) {
            System.err.println("Error al actualitzar el Pokemon: " + e.getMessage());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteTable(Integer id) {
        String sql = "DELETE FROM pokemons WHERE id_pokemon = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
            if (stmt.getUpdateCount() == -1) {
                throw new PropertyNotFound("No existe el Pokémon con ID " + id);
            }
        } catch (PropertyNotFound e) {
            System.err.println("Error al eliminar el Pokémon: " + e.getMessage());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void readAll() {
        String sql = "SELECT * FROM pokemons";
        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            System.out.printf("%-5s %-20s %-10s %-10s %-15s %-10s%n", "ID", "Nom", "Pes", "Altura", "ID Generacio", "ID Tipus");
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
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}