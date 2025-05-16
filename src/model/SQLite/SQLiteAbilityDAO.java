package model.SQLite;

import Exceptions.*;
import model.constructor.Ability;
import model.dao.DAO;

import java.sql.*;

public class SQLiteAbilityDAO implements DAO<Ability, Integer> {
    private Connection connection;

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void insertTable(Ability ability) {
        if (ability.getNom() == null) {
            throw new InvalidDataException("No se puede insertar una habilidad nula o sin nombre");
        }
        if (ability.getId_habilitat() <= 0) {
            throw new InvalidDataException("No se puede insertar una habilidad con ID menor o igual a 0");
        }
        String sql = "INSERT INTO habilitats (id_habilitat, nom, efecte) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, ability.getId_habilitat());
            stmt.setString(2, ability.getNom());
            stmt.setString(3, ability.getEfecte());
            stmt.executeUpdate();
        } catch (SQLException e) {
            if (e.getMessage().contains("UNIQUE constraint failed")) {
                throw new DuplicateEntryException("Ya existe una habilidad con ID " + ability.getId_habilitat());
            }
            throw new DataAccessException("Error during database operation", e);
        }
    }

    @Override
    public void readTable(Integer id) {
        String sql = "SELECT * FROM habilitats WHERE id_habilitat = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                System.out.printf("%-5s %-20s %-30s%n", "ID", "Nom", "Efecte");
                System.out.println("---------------------------------------------");
                System.out.printf("%-5d %-20s %-30s%n",
                        rs.getInt("id_habilitat"),
                        rs.getString("nom"),
                        rs.getString("efecte"));
            } else {
                throw new PropertyNotFound("No existe la habilidad con ID " + id);
            }
        } catch (PropertyNotFound | SQLException e) {
            System.err.println("Error al leer la habilidad: " + e.getMessage());
        }
    }

    @Override
    public void updateTable(Ability ability) {
        if (ability.getNom() == null) {
            throw new InvalidDataException("No se puede actualizar una habilidad nula o sin nombre");
        }
        String sql = "UPDATE habilitats SET nom = ?, efecte = ? WHERE id_habilitat = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, ability.getNom());
            stmt.setString(2, ability.getEfecte());
            stmt.setInt(3, ability.getId_habilitat());
            stmt.executeUpdate();
            if (stmt.getUpdateCount() == -1) {
                throw new PropertyNotFound("No existe la habilidad con ID " + ability.getId_habilitat());
            }
        } catch (PropertyNotFound e) {
            System.err.println("Error al actualizar la habilidad: " + e.getMessage());
        } catch (SQLException e) {
            if (e.getMessage().contains("UNIQUE constraint failed")) {
                throw new DuplicateEntryException("Ya existe una habilidad con ID " + ability.getId_habilitat());
            }
            throw new DataAccessException("Error during database operation", e);
        }
    }

    @Override
    public void deleteTable(Integer id) {
        String sql = "DELETE FROM habilitats WHERE id_habilitat = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
            if (stmt.getUpdateCount() == -1) {
                throw new PropertyNotFound("No existe la habilidad con ID " + id);
            }
        } catch (PropertyNotFound e) {
            System.err.println("Error al eliminar la habilidad: " + e.getMessage());
        } catch (SQLException e) {
            if (e.getMessage().contains("FOREIGN KEY constraint failed")) {
                throw new ForeignKeyConstraintException("No se puede eliminar la habilidad con ID " + id + " porque está referenciada por otra tabla.");
            }
            throw new DataAccessException("Error during database operation", e);
        }
    }

    @Override
    public void readAll() {
        String sql = "SELECT * FROM habilitats";
        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            if (!rs.isBeforeFirst()) { // Check if the result set is empty
                throw new EmptyResultSetException("Ninguna habilidad encontrada en la base de datos.");
            }
            System.out.printf("%-5s %-20s %-30s%n", "ID", "Nom", "Efecte");
            System.out.println("---------------------------------------------");
            while (rs.next()) {
                System.out.printf("%-5d %-20s %-30s%n",
                        rs.getInt("id_habilitat"),
                        rs.getString("nom"),
                        rs.getString("efecte"));
            }
        } catch (EmptyResultSetException e) {
            System.err.println(e.getMessage());
        } catch (SQLException e) {
            throw new DataAccessException("Error during database operation", e);
        }
    }
}