package models;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class User {
    private String username;
    private String password;
    private List<Channel> channels = new ArrayList<>();

    public User() {
    }

    public User(String username) {
        this.username = username;
    }

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public List<Channel> getChannels() {
        return channels;
    }

    public void setChannels(List<Channel> channels) {
        this.channels = channels;
        this.channels.forEach(System.out::println);
    }

    public void addChannel(Channel channel) {
        this.channels.add(channel);
    }

    public void removeChannel(Channel channel) {
        this.channels.remove(channel);
    }

    public void addListOfMessagesToChannel(String channelName, List<Message> messages) {
        this.channels.stream().filter((channel) -> channel.getChannelName().equalsIgnoreCase(channelName))
                .forEach((channel) -> channel.addMessages(messages));
    }

    public List<Message> getListOfMessagesFromChannel(String channelName) {
        return this.channels.stream().filter((channel) -> channel.getChannelName().equalsIgnoreCase(channelName))
                .flatMap((channel) -> channel.getMessages().stream()).collect(Collectors.toList());
    }

    public List<User> getListOfUsersFromChannel(String channelName) {
        return this.channels.stream().filter((channel) -> channel.getChannelName().equalsIgnoreCase(channelName))
                .flatMap((channel) -> channel.getUsers().stream()).collect(Collectors.toList());
    }

    public Channel getChannelByName(String channelName) {
        List<Channel> list =
                this.channels.stream().filter((channel) -> channel.getChannelName().equalsIgnoreCase(channelName)).collect(Collectors.toList());
        if (!list.isEmpty()) return list.get(0);
        return null;
    }

    public void addMessage(Message message) {
        this.channels.stream().filter((channel) -> channel.getChannelName().equalsIgnoreCase(message.getChannel().getChannelName())).forEach((channel) -> {
            channel.addMessage(message);
        });
    }

    @Override
    public String toString() {
        return " " + username + " ";
    }
}
