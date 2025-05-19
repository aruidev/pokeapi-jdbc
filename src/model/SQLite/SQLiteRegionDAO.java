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
            throw new InvalidDataException("No es pot insertar una regió nul·la o sense nom");
        }
        if (region.getId_regio() <= 0) {
            throw new InvalidDataException("No es pot insertar una regió amb ID menor o igual a 0");
        }
        String sql = "INSERT INTO regions (id_regio, nom) VALUES (?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, region.getId_regio());
            stmt.setString(2, region.getNom());
            stmt.executeUpdate();
        } catch (SQLException e) {
            if (e.getMessage().contains("UNIQUE constraint failed")) {
                throw new DuplicateEntryException("Ja existeix una regió amb ID " + region.getId_regio());
            }
            throw new DataAccessException("Error durant la operació a la DB.", e);
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
                throw new PropertyNotFound("No existix la regió a la DB " + id);
            }
        } catch (PropertyNotFound | SQLException e) {
            System.err.println("Error al llegir la DB: " + e.getMessage());
        }
    }

    @Override
    public void updateTable(Region region) {
        if (region.getNom() == null) {
            throw new InvalidDataException("No es pot actualitzar una regió nul·la o sense nom");
        }
        String sql = "UPDATE regions SET nom = ? WHERE id_regio = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, region.getNom());
            stmt.setInt(2, region.getId_regio());
            stmt.executeUpdate();
            if (stmt.getUpdateCount() == -1) {
                throw new PropertyNotFound("No existeix la regió amb ID " + region.getId_regio());
            }
        } catch (PropertyNotFound e) {
            System.err.println("Error al actualitzar la regió: " + e.getMessage());
        } catch (SQLException e) {
            if (e.getMessage().contains("UNIQUE constraint failed")) {
                throw new DuplicateEntryException("Ja existeix la regió amb ID " + region.getId_regio());
            }
            throw new DataAccessException("Error durant la operació a la DB", e);
        }
    }

    @Override
    public void deleteTable(Integer id) {
        String sql = "DELETE FROM regions WHERE id_regio = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
            if (stmt.getUpdateCount() == -1) {
                throw new PropertyNotFound("No existeix la regió amb ID " + id);
            }
        } catch (PropertyNotFound e) {
            System.err.println("Error al eliminar la regió: " + e.getMessage());
        } catch (SQLException e) {
            if (e.getMessage().contains("FOREIGN KEY constraint failed")) {
                throw new ForeignKeyConstraintException("No es pot eliminar la regió amb ID " + id + " perque està referenciant una altre taula.");
            }
            throw new DataAccessException("Error durant la operació a la DB.", e);
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
            throw new DataAccessException("Error durant la operació a la DB.", e);
        }
    }
}