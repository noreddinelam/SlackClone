package shared;

import models.Channel;
import models.Message;
import models.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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
            channels.add(new Channel(new User(resultSet.getString("username")),
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
        while (resultSet.next()) {
            messages.add(new Message(resultSet.getString("content")));
        }
        return messages;
    }
}
