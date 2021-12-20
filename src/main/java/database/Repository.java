package database;

import models.Channel;
import models.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
//test


//The repository to use for using requests on the database.
public class Repository {
    private static final Repository repository = new Repository();
    private static Logger logger = LoggerFactory.getLogger(Repository.class);
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
    // Fonction qui retourne de la DB le champs content de la table message
    // du channel passé en argument
    //TODO: test this function
    public static Optional<ResultSet> fetchMessageFromChannelDB(Channel channel) {
        try(PreparedStatement stmt = connectionDB.prepareCall(SQLStatements.fetchMessageFromChannel)){
            stmt.setNString(1, String.valueOf(channel.getChannelName()));
            return Optional.of(stmt.executeQuery());
        } catch (SQLException sqlE) {
            sqlE.printStackTrace();
            return Optional.empty();
        }
    }


    public static Optional<ResultSet> fetchMessageFromClientDB(User user)
    {
        try (PreparedStatement stmt = connectionDB.prepareCall(SQLStatements.fetchMessageFromChannel)) {
            stmt.setNString(1, String.valueOf(user.getUsername()));
            return Optional.of(stmt.executeQuery());
        } catch (SQLException e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }public static Optional<ResultSet> createChannelDB(Channel channel)
    {
        try (PreparedStatement stmt = connectionDB.prepareStatement(SQLStatements.createChannel)) {
            stmt.setNString(1, String.valueOf(channel.getChannelName()));
            stmt.setNString(2, String.valueOf(channel.getAdmin().getUsername()));
            stmt.setNString(3, String.valueOf(channel.getChannelDescription()));
            stmt.setBoolean(4, true);
            return Optional.of(stmt.executeQuery());
        } catch (SQLException e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }



}
