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

    public void setChannels(List<Channel> channels) {
        this.channels = channels;
    }

    public void addChannel(Channel channel){
        this.channels.add(channel);
    }
    public void removeChannel(Channel channel){
        this.channels.remove(channel);
    }

    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                '}';
    }
}
