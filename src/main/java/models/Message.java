package models;

import java.time.LocalDateTime;
import java.util.Objects;

public class Message {
    private int id;
    private String content;
    private User user;
    private Channel channel;
    private LocalDateTime date;

    public Message(int id,String content,User user,Channel channel, LocalDateTime date){
        this.id = id;
        this.content = content;
        this.user = user;
        this.channel = channel;
        this.date = date;
    }

    public Message(String content,User user,Channel channel, LocalDateTime date){
        this.content = content;
        this.user = user;
        this.channel = channel;
        this.date = date;
    }

    public Message(String content) {
        this.content=content;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
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

    @Override
    public String toString() {
        return "[ " + user + " - "+ date + " ]" + content ;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Message message = (Message) o;
        return id == message.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
