package model.SQLite.updateLogs;

import Exceptions.DBNotFound;
import Exceptions.DataAccessException;
import Exceptions.PropertyNotFound;
import model.constructor.UpdateLog;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UpdateLogReader {

    private Connection connection;

    // Constructor que recibe la conexión
    public UpdateLogReader(Connection connection) {
        this.connection = connection;
    }

    public List<UpdateLog> fetchAllLogs() throws SQLException {
        if (connection == null) {
            throw new SQLException("No hay conexión a la base de datos establecida");
        }

        String sql = "SELECT id, table_name, action, row_id, changed_at FROM update_logs ORDER BY id";
        List<UpdateLog> logs = new ArrayList<>();

        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                UpdateLog log = new UpdateLog(
                        rs.getInt("id"),
                        rs.getString("table_name"),
                        rs.getString("action"),
                        rs.getInt("row_id"),
                        rs.getString("changed_at")
                );
                logs.add(log);
            }
        }

        return logs;
    }

    public void printLogs() {
        try {
            List<UpdateLog> logs = fetchAllLogs();
            System.out.printf("%-5s %-20s %-10s %-10s %-20s%n", "ID", "Nom Taula", "Acció", "ID Fila", "Modificació a");
            for (UpdateLog log : logs) {
                System.out.printf("%-5d %-20s %-10s %-10d %-20s%n",
                        log.getId(), log.getTableName(), log.getAction(), log.getRowId(), log.getChangedAt());
            }
        } catch (DBNotFound | PropertyNotFound | DataAccessException | SQLException e) {
            System.err.println("Error al accedir als logs de la DB local. " + e.getMessage());;
        }
    }
}