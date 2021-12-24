package Exceptions;

public class FetchAllUsersWithChannelNameException extends Exception {
    public FetchAllUsersWithChannelNameException() {
        super("Fetch all users with channelName error");
    }
}
