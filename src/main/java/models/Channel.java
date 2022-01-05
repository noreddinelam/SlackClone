package models;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class Channel {
    private User admin;
    private List<User> users = new ArrayList<>();
    private String channelName;
    private String channelDescription;
    private List<Message> messages = new ArrayList<>();
    private boolean isPublic;//true = a public channel.

    public Channel() {
    }

    public Channel(String channelName, boolean isPublic) {
        this.channelName = channelName;
        this.isPublic = isPublic;
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

    public void setChannelName(String newChannelName) {
        this.channelName = newChannelName;
    }

    public String getChannelDescription() {
        return channelDescription;
    }

    public void addMessages(List<Message> listOfMessages) {
        this.messages.addAll(listOfMessages);
    }

    public List<Message> getMessages() {
        return messages;
    }

    public void addMessage(Message message) {
        this.messages.add(message);
    }


    public void removeMessage(Message message) {
        this.messages.remove(message);
    }

    public List<User> getUsers() {
        return users;
    }

    public boolean isPublic() {
        return isPublic;
    }

    public void setPublic(boolean isPublic) {
        this.isPublic = isPublic;
    }

    public void addUser(User user) {
        this.users.add(user);
    }

    public void removeUser(String username) {
        this.users.remove(new User(username));
    }

    public Message getMessageById(int idMessage){
        return this.messages.stream().filter((mes) -> mes.getId() == idMessage).findFirst().get();
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
