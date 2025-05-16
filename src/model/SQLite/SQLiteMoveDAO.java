package model.SQLite;

import Exceptions.*;
import model.constructor.Move;
import model.dao.DAO;

import java.sql.*;

public class SQLiteMoveDAO implements DAO<Move, Integer> {
    private Connection connection;

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void insertTable(Move move) {
        if (move.getNom() == null) {
            throw new InvalidDataException("No se puede insertar un movimiento nulo o sin nombre");
        }
        if (move.getId_move() <= 0) {
            throw new InvalidDataException("No se puede insertar un movimiento con ID menor o igual a 0");
        }
        if (move.getPoder() <= 0) {
            throw new InvalidDataException("No se puede insertar un movimiento con poder menor o igual a 0");
        }
        if (move.getPrecisio() <= 0) {
            throw new InvalidDataException("No se puede insertar un movimiento con precisión menor o igual a 0");
        }
        if (move.getPp() <= 0) {
            throw new InvalidDataException("No se puede insertar un movimiento con PP menor o igual a 0");
        }
        if (move.getTipus() == null) {
            throw new InvalidDataException("No se puede insertar un movimiento con tipo nulo");
        }
        String sql = "INSERT INTO moviments (id_moviment, nom, poder, precisio, pp, tipus) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, move.getId_move());
            stmt.setString(2, move.getNom());
            stmt.setInt(3, move.getPoder());
            stmt.setInt(4, move.getPrecisio());
            stmt.setInt(5, move.getPp());
            stmt.setString(6, move.getTipus());
            stmt.executeUpdate();
        } catch (SQLException e) {
            if (e.getMessage().contains("UNIQUE constraint failed")) {
                throw new DuplicateEntryException("Ya existe un movimiento con ID " + move.getId_move());
            }
            throw new DataAccessException("Error during database operation", e);
        }
    }

    @Override
    public void readTable(Integer id) {
        String sql = "SELECT * FROM moviments WHERE id_moviment = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                System.out.printf("%-5s %-20s %-10s %-10s %-5s %-10s%n", "ID", "Nom", "Poder", "Precisio", "PP", "Tipus");
                System.out.println("-------------------------------------------------------------");
                System.out.printf("%-5d %-20s %-10d %-10d %-5d %-10s%n",
                        rs.getInt("id_moviment"),
                        rs.getString("nom"),
                        rs.getInt("poder"),
                        rs.getInt("precisio"),
                        rs.getInt("pp"),
                        rs.getString("tipus"));
            } else {
                throw new PropertyNotFound("No existe el movimiento con ID " + id);
            }
        } catch (PropertyNotFound | SQLException e) {
            System.err.println("Error al leer el movimiento: " + e.getMessage());
        }
    }

    @Override
    public void updateTable(Move move) {
        if (move.getNom() == null) {
            throw new InvalidDataException("No se puede actualizar un movimiento nulo o sin nombre");
        }
        if (move.getPoder() <= 0) {
            throw new InvalidDataException("No se puede insertar un movimiento con poder menor o igual a 0");
        }
        if (move.getPrecisio() <= 0) {
            throw new InvalidDataException("No se puede insertar un movimiento con precisión menor o igual a 0");
        }
        if (move.getPp() <= 0) {
            throw new InvalidDataException("No se puede insertar un movimiento con PP menor o igual a 0");
        }
        if (move.getTipus() == null) {
            throw new InvalidDataException("No se puede insertar un movimiento con tipo nulo");
        }
        String sql = "UPDATE moviments SET nom = ?, poder = ?, precisio = ?, pp = ?, tipus = ? WHERE id_moviment = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, move.getNom());
            stmt.setInt(2, move.getPoder());
            stmt.setInt(3, move.getPrecisio());
            stmt.setInt(4, move.getPp());
            stmt.setString(5, move.getTipus());
            stmt.setInt(6, move.getId_move());
            stmt.executeUpdate();
            if (stmt.getUpdateCount() == -1) {
                throw new PropertyNotFound("No existe el movimiento con ID " + move.getId_move());
            }
        } catch (PropertyNotFound e) {
            System.err.println("Error al actualizar el movimiento: " + e.getMessage());
        } catch (SQLException e) {
            if (e.getMessage().contains("UNIQUE constraint failed")) {
                throw new DuplicateEntryException("Ya existe un movimiento con ID " + move.getId_move());
            }
            throw new DataAccessException("Error during database operation", e);
        }
    }

    @Override
    public void deleteTable(Integer id) {
        String sql = "DELETE FROM moviments WHERE id_moviment = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
            if (stmt.getUpdateCount() == -1) {
                throw new PropertyNotFound("No existe el movimiento con ID " + id);
            }
        } catch (PropertyNotFound e) {
            System.err.println("Error al eliminar el movimiento: " + e.getMessage());
        } catch (SQLException e) {
            if (e.getMessage().contains("FOREIGN KEY constraint failed")) {
                throw new ForeignKeyConstraintException("No se puede eliminar el movimiento con ID " + id + " porque está referenciado por otra tabla.");
            }
            throw new DataAccessException("Error during database operation", e);
        }
    }

    @Override
    public void readAll() {
        String sql = "SELECT * FROM moviments";
        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            if (!rs.isBeforeFirst()) { // Check if the result set is empty
                throw new EmptyResultSetException("Ningún movimiento encontrado en la base de datos.");
            }
            System.out.printf("%-5s %-20s %-10s %-10s %-5s %-10s%n", "ID", "Nom", "Poder", "Precisio", "PP", "Tipus");
            System.out.println("-------------------------------------------------------------");
            while (rs.next()) {
                System.out.printf("%-5d %-20s %-10d %-10d %-5d %-10s%n",
                        rs.getInt("id_moviment"),
                        rs.getString("nom"),
                        rs.getInt("poder"),
                        rs.getInt("precisio"),
                        rs.getInt("pp"),
                        rs.getString("tipus"));
            }
        } catch (EmptyResultSetException e) {
            System.err.println(e.getMessage());
        } catch (SQLException e) {
            throw new DataAccessException("Error during database operation", e);
        }
    }
}