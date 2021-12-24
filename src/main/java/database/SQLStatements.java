package database;

public class SQLStatements {
    public static String joinChannel =
            "INSERT INTO client_channel " +
                    "(idChannel,username) " +
                    "VALUES (?,?) ;";
    public static String createChannel =
            "INSERT INTO channel " +
                    "(name,idAdmin,description,isPublic) " +
                    "VALUES (?,?,?,?) ;";

    public static String createUser =
            "INSERT INTO client " +
                    "(username,password) " +
                    "VALUES (?,?) ;";

    public static String addMessage =
            "INSERT INTO message " +
                    "(content,idChannel,username,date) " +
                    "VALUES (?,?,?,?) ;";

    public static String fetchMessageFromChannel = "SELECT * FROM Message where idChannel=? ;";

    public static String fetchAllUsersWithChannelName = "SELECT username " +
            "FROM  client_channel" +
            "where channelName= ? " +
            ";";

    public static String deleteMessage =
            "DELETE FROM MESSAGE " +
                    "WHERE id=? ;";

    private SQLStatements() {
    }
}

