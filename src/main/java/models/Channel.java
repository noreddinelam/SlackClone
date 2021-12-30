package models;

import java.util.List;
import java.util.Objects;

public class Channel {
    private User admin;
    private List<User> allowedUsers;
    private String channelName;
    private String channelDescription;
    private List<Message> messages;
    private boolean isPublic;//true = a public channel.

    public Channel() {
    }

    public Channel(String channelName, boolean isPublic){
        this.channelName = channelName;
        this.isPublic=isPublic;
    }


    public Channel(User admin, String channelName, String channelDescription, boolean isPublic) {
        this.admin = admin;
        this.channelName = channelName;
        this.channelDescription = channelDescription;
        this.isPublic = isPublic;
    }

    public Channel(String channelName) {
        this.channelName = channelName;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Channel channel = (Channel) o;
        return channelName.equals(channel.channelName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(channelName);
    }
}
