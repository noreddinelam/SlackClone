package shared;

import models.Channel;
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
        System.out.println("2");
        List<Channel> channels = new ArrayList<>();
        while (resultSet.next()) {
            System.out.println("1");
            channels.add(new Channel(new User(resultSet.getString("username")),
                    resultSet.getString("name"),
                    resultSet.getString("description"),
                    resultSet.getBoolean("isPublic")));
        }
        return channels;
    }
}
