package database;

public class Database {
    private static final Database database = new Database();
    Database getDatabase(){
        return database;
    }
}
