package model.SQLite;

import Exceptions.*;
import model.constructor.Type;
import model.dao.DAO;

import java.sql.*;

public class SQLiteTypeDAO implements DAO<Type, Integer> {
    private Connection connection;

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void insertTable(Type type) {
        if (type.getNom() == null) {
            throw new InvalidDataException("No es pot insertar un tipus nul o sense nom");
        }
        if (type.getId_tipus() <= 0) {
            throw new InvalidDataException("No es pot insertar un tipus amb ID menor o igual a 0");
        }
        if (type.getRelacions_dany() == null) {
            throw new InvalidDataException("No es pot insertar un tipus sense relacions de dany");
        }
        String sql = "INSERT INTO tipus (id_tipus, nom, relacions_dany) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, type.getId_tipus());
            stmt.setString(2, type.getNom());
            stmt.setString(3, type.getRelacions_dany());
            stmt.executeUpdate();
        } catch (SQLException e) {
            if (e.getMessage().contains("UNIQUE constraint failed")) {
                throw new DuplicateEntryException("Ja existeix un tipus amb ID " + type.getId_tipus());
            }
            throw new DataAccessException("Error durant la operació a la DB.", e);
        }
    }

    @Override
    public void readTable(Integer id) {
        String sql = "SELECT * FROM tipus WHERE id_tipus = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                System.out.printf("%-5s %-20s %-30s%n", "ID", "Nom", "Relacions Dany");
                System.out.println("-------------------------------------------------------------");
                System.out.printf("%-5d %-20s %-30s%n",
                        rs.getInt("id_tipus"),
                        rs.getString("nom"),
                        rs.getString("relacions_dany"));
            } else {
                throw new PropertyNotFound("No existeix el tipus amb ID " + id);
            }
        } catch (PropertyNotFound | SQLException e) {
            System.err.println("Error al llegir el tipus: " + e.getMessage());
        }
    }

    @Override
    public void updateTable(Type type) {
        if (type.getNom() == null) {
            throw new InvalidDataException("No es pot actualitzar un tipus nul o sense nom");
        }
        if (type.getRelacions_dany() == null) {
            throw new InvalidDataException("No es pot insertar un tipus sense relacions de dany");
        }
        String sql = "UPDATE tipus SET nom = ?, relacions_dany = ? WHERE id_tipus = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, type.getNom());
            stmt.setString(2, type.getRelacions_dany());
            stmt.setInt(3, type.getId_tipus());
            stmt.executeUpdate();
            if (stmt.getUpdateCount() == -1) {
                throw new PropertyNotFound("No existeix el tipus amb ID " + type.getId_tipus());
            }
        } catch (PropertyNotFound e) {
            System.err.println("Error al actualitzar el tipus: " + e.getMessage());
        } catch (SQLException e) {
            if (e.getMessage().contains("UNIQUE constraint failed")) {
                throw new DuplicateEntryException("Ja existeix un tipus amb ID " + type.getId_tipus());
            }
            throw new DataAccessException("Error durant la operació a la DB.", e);
        }
    }

    @Override
    public void deleteTable(Integer id) {
        String sql = "DELETE FROM tipus WHERE id_tipus = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
            if (stmt.getUpdateCount() == -1) {
                throw new PropertyNotFound("No existeix el tipus amb ID " + id);
            }
        } catch (PropertyNotFound e) {
            System.err.println("Error al eliminar el tipus: " + e.getMessage());
        } catch (SQLException e) {
            if (e.getMessage().contains("FOREIGN KEY constraint failed")) {
                throw new ForeignKeyConstraintException("No es pot eliminar el tipus amb ID " + id + " perque està referenciant una altre taula.");
            }
            throw new DataAccessException("Error durant la operació a la DB.", e);
        }
    }

    @Override
    public void readAll() {
        String sql = "SELECT * FROM tipus";
        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            if (!rs.isBeforeFirst()) { // Check if the result set is empty
                throw new EmptyResultSetException("Ningún tipus trobat a la DB.");
            }
            System.out.printf("%-5s %-20s %-30s%n", "ID", "Nom", "Relacions Dany");
            System.out.println("-------------------------------------------------------------");
            while (rs.next()) {
                System.out.printf("%-5d %-20s %-30s%n",
                        rs.getInt("id_tipus"),
                        rs.getString("nom"),
                        rs.getString("relacions_dany"));
            }
        } catch (EmptyResultSetException e) {
            System.err.println(e.getMessage());
        } catch (SQLException e) {
            throw new DataAccessException("Error durant la operació a la DB.", e);
        }
    }
}