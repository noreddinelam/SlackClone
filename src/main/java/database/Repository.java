package database;

import models.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import server.Server;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

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
        }
    }

    public static void saveChannelInDB(Channel channel) { // TODO: explication of saveChannelInDB
        try(PreparedStatement stmt = connectionDB.prepareCall(SQLStatements.fetchMessageFromChannel)){
            stmt.setNString(1,"amine");
            stmt.setInt(2,2020);
            stmt.execute();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

}
