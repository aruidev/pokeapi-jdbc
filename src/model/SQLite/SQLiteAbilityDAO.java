package model.SQLite;

import Exceptions.DBNotFound;
import Exceptions.PropertyNotFound;
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
        String sql = "INSERT INTO habilitats (id_habilitat, nom, efecte) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, ability.getId_habilitat());
            stmt.setString(2, ability.getNom());
            stmt.setString(3, ability.getEfecte());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
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
        String sql = "UPDATE habilitats SET nom = ?, efecte = ? WHERE id_habilitat = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, ability.getNom());
            stmt.setString(2, ability.getEfecte());
            stmt.setInt(3, ability.getId_habilitat());
            stmt.executeUpdate();
            if (stmt.getUpdateCount() == 0) {
                throw new PropertyNotFound("No existe la habilidad con ID " + ability.getId_habilitat());
            }
        } catch (PropertyNotFound e) {
            System.err.println("Error al actualizar la habilidad: " + e.getMessage());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteTable(Integer id) {
        String sql = "DELETE FROM habilitats WHERE id_habilitat = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
            if (stmt.getUpdateCount() == 0) {
                throw new PropertyNotFound("No existe la habilidad con ID " + id);
            }
        } catch(PropertyNotFound e) {
            System.err.println("Error al eliminar la habilidad: " + e.getMessage());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void readAll() {
        String sql = "SELECT * FROM habilitats";
        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            System.out.printf("%-5s %-20s %-30s%n", "ID", "Nom", "Efecte");
            System.out.println("---------------------------------------------");
            while (rs.next()) {
                System.out.printf("%-5d %-20s %-30s%n",
                        rs.getInt("id_habilitat"),
                        rs.getString("nom"),
                        rs.getString("efecte"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}