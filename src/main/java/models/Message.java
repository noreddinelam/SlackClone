package models;

import java.time.LocalDateTime;

public class Message {
    private int id;
    private String content;
    private User user;
    private Channel channel;
    private LocalDateTime date;

    public Message(String content,User user,Channel channel, LocalDateTime date){
        this.content = content;
        this.user = user;
        this.channel = channel;
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public String getContent() {
        return content;
    }

    public User getUser() {
        return user;
    }

    public Channel getChannel() {
        return channel;
    }

    public LocalDateTime getDate() {
        return date;
    }
}
