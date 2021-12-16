package database;

import shared.Properties;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {
    private static Connection database;

    private Database() {
    }

    public static Connection getDatabaseConnection() throws SQLException {
        if(database == null)
            database = DriverManager.getConnection(Properties.DATABASE_URL,
                    Properties.DATABASE_USER, Properties.DATABASE_PASSWORD);
        return database;
    }
}
