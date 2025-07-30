package me.tormented.farmmancy;

import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseManager {
    private static DatabaseManager instance;
    @NotNull
    private final Connection connection;

    public static DatabaseManager getInstance() {
        return instance;
    }

    public void uncheckedCloseConnection() {
        try {
            connection.close();
        } catch (SQLException e) {
            FarmMancy.getInstance().getLogger().warning(e.toString());
        }
    }

    public static DatabaseManager createSQLiteConnection(String URL) throws ClassNotFoundException, SQLException {
        if (getInstance() != null) getInstance().uncheckedCloseConnection();
        Class.forName("org.sqlite.JDBC");

        Connection connection = DriverManager.getConnection(URL);

        instance = new DatabaseManager(connection);

        FarmMancy.getInstance().getLogger().info("Connection to SQLite database established.");

        instance.createSchemaIfEmpty();

        return instance;
    }

    private boolean shuttingDown = false;

    public void shutdown() {
        shuttingDown = true;
        uncheckedCloseConnection();
    }

    private DatabaseManager(@NotNull Connection connection) {
        this.connection = connection;
    }

    private void createSchemaIfEmpty() throws SQLException {
        Statement statement = connection.createStatement();
        String sql_farmmancy_profiles = "CREATE TABLE IF NOT EXISTS farmmancy_profiles ("
                + "	id TEXT PRIMARY KEY,"
                + ");";
        String sql_abilities = "CREATE TABLE IF NOT EXISTS abilities ("
                + "	id TEXT PRIMARY KEY,"
                + " owner TEXT,"
                + "	registry_id TEXT,"
                + "	slot INT,"
                + " metadata BLOB,"
                + " FOREIGN KEY(owner) REFERENCES farmmancy_profiles(id)"
                + ");";
        String sql_heads = "CREATE TABLE IF NOT EXISTS ability_heads ("
                + "	id TEXT PRIMARY KEY,"
                + " variant TEXT,"
                + "	entity_type TEXT,"
                + " metadata BLOB,"
                + " FOREIGN KEY(owner) REFERENCES abilities(id)"
                + ");";

        statement.addBatch(sql_farmmancy_profiles);
        statement.addBatch(sql_abilities);
        statement.executeBatch();
    }
}
