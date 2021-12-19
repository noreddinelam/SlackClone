package database;

public class SQLStatements {
    public static String fetchMessageFromChannel = "SELECT content FROM Message where idChannel=? ;";
    public static String fetchMessageFromUser=
            "SELECT m.content " +
            "FROM Message m" +
            "where m.id = ( Select id" +
                    "FROM Client_Channel_Message" +
                    "where username=? )" +
            "; ";
    private SQLStatements(){}
}
