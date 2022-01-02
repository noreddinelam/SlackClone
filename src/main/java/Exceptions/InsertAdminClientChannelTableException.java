package Exceptions;

public class InsertAdminClientChannelTableException extends Exception{
    public InsertAdminClientChannelTableException(){
        super("Insert admin to clientchannel table when channel is created error");
    }
}