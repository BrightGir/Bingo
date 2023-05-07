package me.bright.mbingo.database;

import me.bright.mbingo.BPlayer;
import me.bright.skylib.database.DataType;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;

public abstract class Database {

    protected Connection connection;

    public abstract Connection getSQLConnection();

    public abstract void load();

    private String dbName = "player_bingo_info";

    public void initialize() {
        connection = getSQLConnection();
        try {
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM " + dbName);
            ResultSet rs = ps.executeQuery();
            close(ps, rs);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public Connection getConnection() {
        return connection;
    }

    public void addGame(boolean win, OfflinePlayer player) {
        updateData(player,DataType.GAMES,(int)getData(player,DataType.GAMES) + 1);
        if(win) {
            updateData(player,DataType.WINS,(int)getData(player,DataType.WINS) + 1);
        }
    }

    public Object getData(OfflinePlayer player, DataType datatype) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = getSQLConnection();
            ps = conn.prepareStatement("SELECT " + datatype.name() + " FROM " + dbName + " WHERE player = ?;");
            ps.setString(1, String.valueOf(player.getUniqueId()));
            rs = ps.executeQuery();

            if (!rs.next()) {
                insertPlayer(player,0);
                return 0;
            }

            return rs.getObject(datatype.name());
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            try {
                if (ps != null)
                    ps.close();
                if (conn != null)
                    conn.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        return 0;
    }

    private void insertPlayer(OfflinePlayer player, Object defaultValue) {
        try (Connection conn = getSQLConnection(); PreparedStatement stmt = conn.prepareStatement(
                "INSERT INTO " + dbName + "(player, "+DataType.GAMES.name()+", " + DataType.WINS.name() + ") VALUES(?, ?, ?);")) {
            stmt.setString(1, player.getUniqueId().toString());
            stmt.setObject(2, defaultValue);
            stmt.setObject(3, defaultValue);
            stmt.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void updateData(OfflinePlayer player, DataType datatype, Object newValue) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = getSQLConnection().prepareStatement("UPDATE " + dbName + " SET " + datatype.name() + " = ? WHERE player = ?;");
            ps.setObject(1, newValue);
            ps.setString(2, String.valueOf(player.getUniqueId()));
            ps.execute();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            try {
                if (ps != null)
                    ps.close();
                if (conn != null)
                    conn.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }


    public void close(PreparedStatement ps, ResultSet rs) {
        try {
            if (ps != null)
                ps.close();
            if (rs != null)
                rs.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void closeConnection() {
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

