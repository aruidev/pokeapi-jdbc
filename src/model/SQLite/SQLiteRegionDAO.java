package model.SQLite;

import Exceptions.*;
import model.constructor.Region;
import model.dao.DAO;

import java.sql.*;

public class SQLiteRegionDAO implements DAO<Region, Integer> {
    private Connection connection;

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void insertTable(Region region) {
        if (region.getNom() == null) {
            throw new InvalidDataException("No se puede insertar una región nula o sin nombre");
        }
        if (region.getId_regio() <= 0) {
            throw new InvalidDataException("No se puede insertar una región con ID menor o igual a 0");
        }
        String sql = "INSERT INTO regions (id_regio, nom) VALUES (?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, region.getId_regio());
            stmt.setString(2, region.getNom());
            stmt.executeUpdate();
        } catch (SQLException e) {
            if (e.getMessage().contains("UNIQUE constraint failed")) {
                throw new DuplicateEntryException("Ya existe una región con ID " + region.getId_regio());
            }
            throw new DataAccessException("Error during database operation", e);
        }
    }

    @Override
    public void readTable(Integer id) {
        String sql = "SELECT * FROM regions WHERE id_regio = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                System.out.printf("%-5s %-20s%n", "ID", "Nom");
                System.out.println("-------------------------");
                System.out.printf("%-5d %-20s%n",
                        rs.getInt("id_regio"),
                        rs.getString("nom"));
            } else {
                throw new PropertyNotFound("No existe la región con ID " + id);
            }
        } catch (PropertyNotFound | SQLException e) {
            System.err.println("Error al leer la región: " + e.getMessage());
        }
    }

    @Override
    public void updateTable(Region region) {
        if (region.getNom() == null) {
            throw new InvalidDataException("No se puede actualizar una región nula o sin nombre");
        }
        String sql = "UPDATE regions SET nom = ? WHERE id_regio = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, region.getNom());
            stmt.setInt(2, region.getId_regio());
            stmt.executeUpdate();
            if (stmt.getUpdateCount() == -1) {
                throw new PropertyNotFound("No existe la región con ID " + region.getId_regio());
            }
        } catch (PropertyNotFound e) {
            System.err.println("Error al actualizar la región: " + e.getMessage());
        } catch (SQLException e) {
            if (e.getMessage().contains("UNIQUE constraint failed")) {
                throw new DuplicateEntryException("Ya existe una región con ID " + region.getId_regio());
            }
            throw new DataAccessException("Error during database operation", e);
        }
    }

    @Override
    public void deleteTable(Integer id) {
        String sql = "DELETE FROM regions WHERE id_regio = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
            if (stmt.getUpdateCount() == -1) {
                throw new PropertyNotFound("No existe la región con ID " + id);
            }
        } catch (PropertyNotFound e) {
            System.err.println("Error al eliminar la región: " + e.getMessage());
        } catch (SQLException e) {
            if (e.getMessage().contains("FOREIGN KEY constraint failed")) {
                throw new ForeignKeyConstraintException("No se puede eliminar la región con ID " + id + " porque está referenciada por otra tabla.");
            }
            throw new DataAccessException("Error during database operation", e);
        }
    }

    @Override
    public void readAll() {
        String sql = "SELECT * FROM regions";
        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            if (!rs.isBeforeFirst()) { // Check if the result set is empty
                throw new EmptyResultSetException("Ninguna región encontrada en la base de datos.");
            }
            System.out.printf("%-5s %-20s%n", "ID", "Nom");
            System.out.println("-------------------------");
            while (rs.next()) {
                System.out.printf("%-5d %-20s%n",
                        rs.getInt("id_regio"),
                        rs.getString("nom"));
            }
        } catch (EmptyResultSetException e) {
            System.err.println(e.getMessage());
        } catch (SQLException e) {
            throw new DataAccessException("Error during database operation", e);
        }
    }
}