package Exceptions;

public class ModifyChannelException extends Exception{
    public ModifyChannelException(){
        super("Insert admin to clientchannel table when channel is created error");
    }
}
