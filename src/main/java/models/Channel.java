package models;

import java.util.List;

public class Channel {
    private int id;
    private User admin;
    private List<User> allowedUsers;
    private String channelName;
    private String channelDescription;
    private List<Message> messages;
    private boolean isPublic;//true = a public channel.


    public int getId() {
        return id;
    public Channel(){}

    public Channel(User admin,String channelName,String channelDescription,boolean isPublic){
        this.admin = admin;
        this.channelName = channelName;
        this.channelDescription = channelDescription;
        this.isPublic = isPublic;
    }

    public String getChannelName() {
        return channelName;
    }

    @Override
    public String toString() {
        return "Channel{" +
                "id=" + id +
                ", admin=" + admin +
                ", channelName='" + channelName + '\'' +
                ", channelDescription='" + channelDescription + '\'' +
                ", isPublic=" + isPublic +
                '}';
    }
}
