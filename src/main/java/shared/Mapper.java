package shared;

import database.SQLTablesInformation;
import models.Channel;
import models.Message;
import models.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Mapper {
    private static Mapper mapper = new Mapper();

    private Mapper() {
    }

    public static Mapper getMapper() {
        return mapper;
    }

    public List<Channel> resultSetToChannel(ResultSet resultSet) throws SQLException {
        List<Channel> channels = new ArrayList<>();
        while (resultSet.next()) {
            channels.add(new Channel(new User(resultSet.getString("adminUsername")),
                    resultSet.getString("name"),
                    resultSet.getString("description"),
                    resultSet.getBoolean("isPublic")));
        }
        return channels;
    }

    public List<User> resultSetToUser(ResultSet resultSet) throws SQLException {
        List<User> users = new ArrayList<>();
        while (resultSet.next()) {
            users.add(new User(resultSet.getString("username")));
        }
        return users;
    }

    public List<Message> resultSetToMessage(ResultSet resultSet) throws SQLException {
        List<Message> messages = new ArrayList<>();
        String messageContent;
        User user;
        Channel channel;
        LocalDateTime messageTime;
        int id;
        while (resultSet.next()) {
            messageContent = resultSet.getString(SQLTablesInformation.messageContentColumn);
            user = new User(resultSet.getString(SQLTablesInformation.messageUsernameColumn));
            channel = new Channel(resultSet.getString(SQLTablesInformation.messageChannelNameColumn));
            messageTime = LocalDateTime.parse(resultSet.getString(SQLTablesInformation.messageDate),
                    DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").withLocale(Locale.ENGLISH));
            id = resultSet.getInt(SQLTablesInformation.messageIdMessageColumn);
            messages.add(new Message(id, messageContent, user, channel, messageTime));
        }
        return messages;
    }
}
