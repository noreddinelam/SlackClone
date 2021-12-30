package Exceptions;

public class ModifyChannelNameException extends Exception{
    public  ModifyChannelNameException (){
        super("Insert admin to clientchannel table when channel is created error");
    }
}
