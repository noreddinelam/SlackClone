package database;

import models.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import server.Server;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

//The repository to use for using requests on the database.
public class Repository {
    private static final Repository repository = new Repository();
    private static Logger logger = LoggerFactory.getLogger(Server.class);
    private static Connection connectionDB;

    private Repository() {
    }

    public static Repository getRepository() {
        initConnectionToDatabase();
        return repository;
    }

    private static void initConnectionToDatabase() {
        try {
            connectionDB = Database.getDatabaseConnection();
            logger.info("Initialisation of connection to database");
        } catch (SQLException e) {
            e.printStackTrace();
            logger.error("Error on getting connection from DB");
        }
    }

    public void initDB(){
        String createTable = "CREATE TABLE user(";
        try (PreparedStatement stmt = connectionDB.prepareStatement(createTable)) {
            this.connectionDB.setAutoCommit(false);

        } catch (SQLException throwable) {
            throwable.printStackTrace();
            logger.error("Error on creating tables");
        }
    }

    public static void saveChannelInDB(Channel channel) {
    }

}
