/***
 *    ooooooooo.   oooo                        ooooooooooooo  o8o
 *    `888   `Y88. `888                        8'   888   `8  `"'
 *     888   .d88'  888   .oooo.   oooo    ooo      888      oooo  ooo. .oo.  .oo.    .ooooo.
 *     888ooo88P'   888  `P  )88b   `88.  .8'       888      `888  `888P"Y88bP"Y88b  d88' `88b
 *     888          888   .oP"888    `88..8'        888       888   888   888   888  888ooo888
 *     888          888  d8(  888     `888'         888       888   888   888   888  888    .o
 *    o888o        o888o `Y888""8o     .8'         o888o     o888o o888o o888o o888o `Y8bod8P'
 *                                 .o..P'
 *                                 `Y8P'
 *
 *
 *    @name PlayTimePNX
 *    @author xArdaa49
 *    @version 1.0.0
 */
package com.xardaa49.playtimepnx.database;

import java.io.File;
import java.sql.*;

/**
 * DatabaseManager is responsible for handling database operations related to playtime tracking.
 * It manages the connection to the database, as well as creating tables, updating and retrieving player playtime data.
 */
public class DatabaseManager {

    private final String databasePath;
    private Connection connection;

    /**
     * Constructs a DatabaseManager with the specified database path.
     *
     * @param databasePath The file path to the SQLite database file.
     */
    public DatabaseManager(String databasePath) {
        this.databasePath = databasePath;
    }

    /**
     * Establishes and returns a connection to the SQLite database.
     * If the directory where the database is located doesn't exist, it will be created.
     *
     * @return A Connection object to the SQLite database.
     * @throws SQLException If an error occurs while establishing the connection.
     */
    public Connection getConnection() throws SQLException {
        // Check the directory of the database file
        File dbFile = new File(databasePath);
        File dbDir = dbFile.getParentFile();
        if (!dbDir.exists()) {
            dbDir.mkdirs(); // Create the directory if it doesn't exist
        }

        // Establish connection if not already open
        if (connection == null || connection.isClosed()) {
            connection = DriverManager.getConnection("jdbc:sqlite:" + databasePath);
        }
        return connection;
    }

    /**
     * Creates the playtime table in the database if it does not already exist.
     * The table includes player names, their playtime, and the last login time.
     */
    public void createDatabaseTable() {
        try (Connection connection = getConnection()) {
            String createTableSQL = "CREATE TABLE IF NOT EXISTS playtime (" +
                    "playerName TEXT PRIMARY KEY," +
                    "playtime INTEGER," +
                    "lastLoginTime INTEGER" +
                    ")";
            try (Statement statement = connection.createStatement()) {
                statement.execute(createTableSQL);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Retrieves the total playtime for a player from the database.
     * This includes the time since the player's last login.
     *
     * @param playerName The name of the player whose playtime is to be retrieved.
     * @return The total playtime in seconds.
     */
    public long getPlaytime(String playerName) {
        try (Connection connection = getConnection()) {
            String selectSQL = "SELECT playtime, lastLoginTime FROM playtime WHERE playerName = ?";
            try (PreparedStatement statement = connection.prepareStatement(selectSQL)) {
                statement.setString(1, playerName);
                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        long playtime = resultSet.getLong("playtime");
                        long lastLoginTime = resultSet.getLong("lastLoginTime");
                        long currentTime = System.currentTimeMillis();
                        // Calculate the elapsed time since the last login
                        long elapsedTime = currentTime - lastLoginTime;
                        long totalPlaytime = playtime + elapsedTime / 1000; // Convert to seconds
                        return totalPlaytime;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * Updates the player's playtime in the database.
     * This method also updates the last login time to the current time.
     *
     * @param playerName The name of the player whose playtime is to be updated.
     * @param playtime The new playtime value for the player.
     */
    public void updatePlaytime(String playerName, long playtime) {
        try (Connection connection = getConnection()) {
            String updateSQL = "UPDATE playtime SET playtime = ?, lastLoginTime = ? WHERE playerName = ?";
            try (PreparedStatement statement = connection.prepareStatement(updateSQL)) {
                statement.setLong(1, playtime);
                statement.setLong(2, System.currentTimeMillis());
                statement.setString(3, playerName);
                statement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Adds a new player to the database with initial playtime set to 0 and the current login time.
     *
     * @param playerName The name of the player to be added to the database.
     */
    public void addNewPlayer(String playerName) {
        try (Connection connection = getConnection()) {
            String insertSQL = "INSERT INTO playtime (playerName, playtime, lastLoginTime) VALUES (?, 0, ?)";
            try (PreparedStatement statement = connection.prepareStatement(insertSQL)) {
                statement.setString(1, playerName);
                statement.setLong(2, System.currentTimeMillis());
                statement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
