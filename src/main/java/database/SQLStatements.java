package database;

public class SQLStatements {

    public static String createChannel =
            "INSERT INTO Channel " +
            "(name,idAdmin,description,isPublic) " +
            "VALUES (?,?,?,?) ;";

    public static String createUser=
            "INSERT INTO CLIENT " +
            "(username,password) " +
            "VALUES (?,?) ;";

    public static String addMessage =
            "INSERT INTO Channel " +
            "(id,content,idChannel,username,date) " +
            "VALUES (?,?,?,?,?) ;";

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

