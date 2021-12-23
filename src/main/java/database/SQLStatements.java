package database;

public class SQLStatements {
    public static String listChannelsInServer =
            "SELECT * "
                    + "FROM channel "
                    + "INNER JOIN client ON channel.idAdmin=client.username";
    public static String listOfUserInChannel =
            "SELECT username"
                    + " FROM client_channel"
                    + " where idChannel= ?";
    public static String listOfMessageInChannel =
            "SELECT content"
                    + " FROM message"
                    + " where idChannel= ?";
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
  
    public static String modifyMessage =
            "UPDATE message " +
                    "set content=? " +
                    "WHERE id = ? ;";

    public static String fetchMessageFromChannel = "SELECT * FROM Message where idChannel=? ;";

    public static String fetchMessageFromUser =
            "SELECT m.content " +
                    "FROM Message m" +
                    "where m.id = ( Select idMessage" +
                    "FROM Client_Channel_Message" +
                    "where username=? )" +
                    ";";

    public static String fetchAllUsersWithIdChannel = "SELECT username " +
            "FROM  client_channel" +
            "where idChannel= ? " +
            ";";

    public static String deleteMessage =
            "DELETE FROM MESSAGE " +
                    "WHERE id=? ;";

    private SQLStatements() {
    }
}

