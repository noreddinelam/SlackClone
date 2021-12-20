package database;

public class SQLStatements {

    public static String createChannel = "Insert INTO channel (name,idAdmin,desc,isPublic) values(?,?,?,?) ;";

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
