package database;

import java.sql.Connection;
import java.sql.SQLException;

//Pour faire les requetes vers la base de donnée.
public class Repository {
    private static Connection connectionDB;
    private static final Repository repository = new Repository();
    private Repository(){}
    public static Repository getRepository(){
        initConnectionToDatabase();
        return repository;
    }
    private static void initConnectionToDatabase(){
        try {
            connectionDB = Database.getDatabaseConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
