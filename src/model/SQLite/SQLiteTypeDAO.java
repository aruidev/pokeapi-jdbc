package model.SQLite;

import Exceptions.PropertyNotFound;
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
        String sql = "INSERT INTO tipus (id_tipus, nom, relacions_dany) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, type.getId_tipus());
            stmt.setString(2, type.getNom());
            stmt.setString(3, type.getRelacions_dany());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
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
                throw new PropertyNotFound("No existe el tipo con ID " + id);
            }
        } catch (PropertyNotFound | SQLException e) {
            System.err.println("Error al leer el tipo: " + e.getMessage());
        }
    }

    @Override
    public void updateTable(Type type) {
        String sql = "UPDATE tipus SET nom = ?, relacions_dany = ? WHERE id_tipus = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, type.getNom());
            stmt.setString(2, type.getRelacions_dany());
            stmt.setInt(3, type.getId_tipus());
            stmt.executeUpdate();
            if (stmt.getUpdateCount() == -1) {
                throw new PropertyNotFound("No existe el tipo con ID " + type.getId_tipus());
            }
        } catch (PropertyNotFound e) {
            System.err.println("Error al actualizar el tipo: " + e.getMessage());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteTable(Integer id) {
        String sql = "DELETE FROM tipus WHERE id_tipus = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
            if (stmt.getUpdateCount() == -1) {
                throw new PropertyNotFound("No existe el tipo con ID " + id);
            }
        } catch (PropertyNotFound e) {
            System.err.println("Error al eliminar el tipo: " + e.getMessage());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void readAll() {
        String sql = "SELECT * FROM tipus";
        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            System.out.printf("%-5s %-20s %-30s%n", "ID", "Nom", "Relacions Dany");
            System.out.println("-------------------------------------------------------------");
            while (rs.next()) {
                System.out.printf("%-5d %-20s %-30s%n",
                        rs.getInt("id_tipus"),
                        rs.getString("nom"),
                        rs.getString("relacions_dany"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}