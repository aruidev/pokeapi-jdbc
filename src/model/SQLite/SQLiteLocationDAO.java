package model.SQLite;

import Exceptions.*;
import model.constructor.Location;
import model.dao.DAO;

import java.sql.*;

public class SQLiteLocationDAO implements DAO<Location, Integer> {
    private Connection connection;

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void insertTable(Location location) {
        if (location.getNom() == null) {
            throw new InvalidDataException("No se puede insertar una localización nula o sin nombre");
        }
        if (location.getId_location() <= 0) {
            throw new InvalidDataException("No se puede insertar una localización con ID menor o igual a 0");
        }
        if (location.getId_regio() <= 0) {
            throw new InvalidDataException("No se puede insertar una localización con ID de región menor o igual a 0");
        }
        String sql = "INSERT INTO localitzacions (id_localitzacio, nom, id_regio) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, location.getId_location());
            stmt.setString(2, location.getNom());
            stmt.setInt(3, location.getId_regio());
            stmt.executeUpdate();
        } catch (SQLException e) {
            if (e.getMessage().contains("UNIQUE constraint failed")) {
                throw new DuplicateEntryException("Ya existe una localización con ID " + location.getId_location());
            }
            throw new DataAccessException("Error during database operation", e);
        }
    }

    @Override
    public void readTable(Integer id) {
        String sql = "SELECT * FROM localitzacions WHERE id_localitzacio = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                System.out.printf("%-5s %-20s %-20s%n", "ID", "Nom", "ID_Regio");
                System.out.println("---------------------------------------------");
                System.out.printf("%-5d %-20s %-20s%n",
                        rs.getInt("id_localitzacio"),
                        rs.getString("nom"),
                        rs.getString("id_regio"));
            } else {
                throw new PropertyNotFound("No existe la localización con ID " + id);
            }
        } catch (PropertyNotFound | SQLException e) {
            System.err.println("Error al leer la localización: " + e.getMessage());
        }
    }

    @Override
    public void updateTable(Location location) {
        if (location.getNom() == null) {
            throw new InvalidDataException("No se puede actualizar una localización nula o sin nombre");
        }
        String sql = "UPDATE localitzacions SET nom = ?, id_regio = ? WHERE id_localitzacio = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, location.getNom());
            stmt.setInt(2, location.getId_regio());
            stmt.setInt(3, location.getId_location());
            stmt.executeUpdate();
            if (stmt.getUpdateCount() == -1) {
                throw new PropertyNotFound("No existe la localización con ID " + location.getId_location());
            }
        } catch (PropertyNotFound e) {
            System.err.println("Error al actualizar la localización: " + e.getMessage());
        } catch (SQLException e) {
            if (e.getMessage().contains("UNIQUE constraint failed")) {
                throw new DuplicateEntryException("Ya existe una localización con ID " + location.getId_location());
            }
            throw new DataAccessException("Error during database operation", e);
        }
    }

    @Override
    public void deleteTable(Integer id) {
        String sql = "DELETE FROM localitzacions WHERE id_localitzacio = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
            if (stmt.getUpdateCount() == -1) {
                throw new PropertyNotFound("No existe la localización con ID " + id);
            }
        } catch (PropertyNotFound e) {
            System.err.println("Error al eliminar la localización: " + e.getMessage());
        } catch (SQLException e) {
            if (e.getMessage().contains("FOREIGN KEY constraint failed")) {
                throw new ForeignKeyConstraintException("No se puede eliminar la localización con ID " + id + " porque está referenciada por otra tabla.");
            }
            throw new DataAccessException("Error during database operation", e);
        }
    }

    @Override
    public void readAll() {
        String sql = "SELECT * FROM localitzacions";
        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            if (!rs.isBeforeFirst()) { // Check if the result set is empty
                throw new EmptyResultSetException("Ninguna localización encontrada en la base de datos.");
            }
            System.out.printf("%-5s %-20s %-20s%n", "ID", "Nom", "ID_Regio");
            System.out.println("---------------------------------------------");
            while (rs.next()) {
                System.out.printf("%-5d %-20s %-20s%n",
                        rs.getInt("id_localitzacio"),
                        rs.getString("nom"),
                        rs.getString("id_regio"));
            }
        } catch (EmptyResultSetException e) {
            System.err.println(e.getMessage());
        } catch (SQLException e) {
            throw new DataAccessException("Error during database operation", e);
        }
    }
}