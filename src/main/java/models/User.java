package models;

import java.util.List;

public class User {
    private String username;
    private String password;
    private List<Channel> channels;

    public User(){}

    public User(String username){
        this.username = username;
    }
    public String getUsername() {
        return username;
    }
}
