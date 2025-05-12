package model.SQLite;

import Exceptions.DBNotFound;
import Exceptions.PropertyNotFound;
import model.constructor.Generation;
import model.dao.DAO;

import java.sql.*;

public class SQLiteGenerationDAO implements DAO<Generation, Integer> {
    private Connection connection;

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void insertTable(Generation generation) {
        String sql = "INSERT INTO generacions (id_generacio, nom, id_regio) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, generation.getId_generation());
            stmt.setString(2, generation.getNom());
            stmt.setString(3, generation.getId_regio());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void readTable(Integer id) {
        String sql = "SELECT * FROM generacions WHERE id_generacio = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                System.out.printf("%-5s %-20s %-20s%n", "ID", "Nom", "ID_Regio");
                System.out.println("---------------------------------------------");
                System.out.printf("%-5d %-20s %-20s%n",
                        rs.getInt("id_generacio"),
                        rs.getString("nom"),
                        rs.getString("id_regio"));
            } else {
                throw new PropertyNotFound("No existe la generación con ID " + id);
            }
        } catch (PropertyNotFound | SQLException e) {
            System.err.println("Error al leer la generación: " + e.getMessage());
        }
    }

    @Override
    public void updateTable(Generation generation) {
        String sql = "UPDATE generacions SET nom = ?, id_regio = ? WHERE id_generacio = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, generation.getNom());
            stmt.setString(2, generation.getId_regio());
            stmt.setInt(3, generation.getId_generation());
            stmt.executeUpdate();
            if (stmt.getUpdateCount() == -1) {
                throw new PropertyNotFound("No existe la generación con ID " + generation.getId_generation());
            }
        } catch (PropertyNotFound e) {
            System.err.println("Error al actualizar la generación: " + e.getMessage());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteTable(Integer id) {
        String sql = "DELETE FROM generacions WHERE id_generacio = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
            if (stmt.getUpdateCount() == -1) {
                throw new PropertyNotFound("No existe la generación con ID " + id);
            }
        } catch (PropertyNotFound e) {
            System.err.println("Error al eliminar la generación: " + e.getMessage());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void readAll() {
        String sql = "SELECT * FROM generacions";
        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            System.out.printf("%-5s %-20s %-20s%n", "ID", "Nom", "ID_Regio");
            System.out.println("---------------------------------------------");
            while (rs.next()) {
                System.out.printf("%-5d %-20s %-20s%n",
                        rs.getInt("id_generacio"),
                        rs.getString("nom"),
                        rs.getString("id_regio"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}