package database;

import models.Channel;
import models.Message;
import models.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
//test


// The repository to use for using requests on the database.
// executeQuery : for select statements.
// execute : return boolean
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
    public Optional<ResultSet> fetchMessageFromChannelDB(Channel channel) {
        try (PreparedStatement stmt = connectionDB.prepareStatement(SQLStatements.fetchMessageFromChannel)) {
            stmt.setString(1, channel.getChannelName());
            return Optional.of(stmt.executeQuery());
        } catch (SQLException sqlE) {
            sqlE.printStackTrace();
            return Optional.empty();
        }
    }


    public Optional<ResultSet> fetchMessageFromClientDB(User user) {
        try (PreparedStatement stmt = connectionDB.prepareStatement(SQLStatements.fetchMessageFromChannel)) {
            stmt.setString(1, String.valueOf(user.getUsername()));
            return Optional.of(stmt.executeQuery());
        } catch (SQLException e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    public Optional<Boolean> createChannelDB(Channel channel) {
        try (PreparedStatement stmt = connectionDB.prepareStatement(SQLStatements.createChannel)) {
            stmt.setString(1, channel.getChannelName());
            stmt.setString(2, channel.getAdmin().getUsername());
            stmt.setString(3, channel.getChannelDescription());
            stmt.setBoolean(4, channel.isPublic());
            return Optional.of(stmt.execute());
        } catch (SQLException e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    public Optional<Boolean> createUserDB(User user)
    {
        try (PreparedStatement stmt = connectionDB.prepareStatement(SQLStatements.createUser)) {
            stmt.setString(1,user.getUsername());
            stmt.setString(2,user.getPassword());
            return Optional.of(stmt.execute());
        } catch(SQLException e)
        {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    public Optional<Boolean> addMessageDB(Message message)
    {
        try (PreparedStatement addMessage = connectionDB.prepareStatement(SQLStatements.addMessage)) {
            addMessage.setString(1,message.getContent());
            addMessage.setString(2,message.getChannel().getChannelName());
            addMessage.setString(3,message.getUser().getUsername());
            addMessage.setObject(4, message.getDate());
            return Optional.of(addMessage.execute());
        } catch(SQLException e)
        {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    public Optional<Boolean> deleteMessageDB(int idMessage)
    {
        try (PreparedStatement deleteMessage = connectionDB.prepareStatement(SQLStatements.deleteMessage)) {
            deleteMessage.setInt(1,idMessage);
            return Optional.of(deleteMessage.execute());
        } catch (SQLException e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }
}
