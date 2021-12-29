package models;

import java.util.List;

public class Channel {
    private User admin;
    private List<User> allowedUsers;
    private String channelName;
    private String channelDescription;
    private List<Message> messages;
    private boolean isPublic;//true = a public channel.

    public Channel() {
    }

    public Channel(String channelName){
        this.channelName = channelName;
    }


    public Channel(User admin, String channelName, String channelDescription, boolean isPublic) {
        this.admin = admin;
        this.channelName = channelName;
        this.channelDescription = channelDescription;
        this.isPublic = isPublic;
    }


    public User getAdmin() {
        return admin;
    }

    public String getChannelName() {
        return channelName;
    }

    public String getChannelDescription() {
        return channelDescription;
    }

    public boolean isPublic() {
        return isPublic;
    }

    @Override
    public String toString() {
        return "Channel{" +
                ", admin=" + admin +
                ", channelName='" + channelName + '\'' +
                ", channelDescription='" + channelDescription + '\'' +
                ", isPublic=" + isPublic +
                '}';
    }
}
