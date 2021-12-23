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
    public User(String username,String password){this.username=username; this.password=password;}

    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username=username;
    }

    public String getPassword() {return password;}
}
