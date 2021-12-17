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

    public Channel(){}

    public Channel(User admin,String channelName,String channelDescription,boolean isPublic){
        this.admin = admin;
        this.channelName = channelName;
        this.channelDescription = channelDescription;
        this.isPublic = isPublic;
    }

}
