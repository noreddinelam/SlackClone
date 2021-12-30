package Exceptions;

public class ModifyChannelStatusException extends Exception{
    public  ModifyChannelStatusException  (){
        super("Insert admin to clientchannel table when channel is created error");
    }
}

