package database;

public class SQLStatements {
    public static String fetchMessageFromChannel = "SELECT * FROM Client where username=? and password = ?";
    private SQLStatements(){}
}
