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

    public static String createUser=
            "INSERT INTO client " +
            "(username,password) " +
            "VALUES (?,?) ;";

    public static String addMessage =
            "INSERT INTO message " +
            "(content,idChannel,username,date) " +
            "VALUES (?,?,?,?) ;";

    public static String fetchMessageFromChannel = "SELECT * FROM Message where idChannel=? ;";

    public static String fetchMessageFromUser=
            "SELECT m.content " +
            "FROM Message m" +
            "where m.id = ( Select idMessage" +
                    "FROM Client_Channel_Message" +
                    "where username=? )" +
            "; ";
    private SQLStatements(){}
}

