package models;

import java.util.List;

public class Channel {
    private int id;
    private User admin;
    private List<User> allowedUsers;
    private String channelName;
    private List<Message> messages;
    private boolean publicState;//true = a public channel.


}
