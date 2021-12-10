package server;

import java.util.Hashtable;
import java.util.function.Consumer;

public class ServerImpl {
    private static Hashtable<Integer, Consumer<String[]>> listeOfFunctions = new Hashtable<>();
    public static void connect(String[] data){}
    public static void createChannel(String[] data){}
    public static void joinChannel(String[] data){}
    public static void deleteMessage(String[] data){}
    public static void modifyMessage(String[] data){}
    public static void deleteChannel(String[] data){}
    public static void listChannelsInServer(String[] data){}
    public static void listOfUserInChannel(String[] data){}
    public static void consumeMessage(String[] data){}

    public static void initListOfFunctions(){
        listeOfFunctions.put(100,ServerImpl::connect);
        listeOfFunctions.put(200,ServerImpl::createChannel);
        listeOfFunctions.put(300,ServerImpl::joinChannel);
        listeOfFunctions.put(400,ServerImpl::deleteMessage);
        listeOfFunctions.put(500,ServerImpl::modifyMessage);
        listeOfFunctions.put(600,ServerImpl::deleteChannel);
        listeOfFunctions.put(700,ServerImpl::listChannelsInServer);
        listeOfFunctions.put(800,ServerImpl::listOfUserInChannel);
        listeOfFunctions.put(900,ServerImpl::consumeMessage);
    }

    public static Consumer<String[]> getFunctionWithRequestCode(int code){
        return listeOfFunctions.get(code);
    }

    public static String[] requestParser(String request){
        return request.split(" ");
    }
}
