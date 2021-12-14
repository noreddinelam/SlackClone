package server;

import shared.FieldsRequestName;
import shared.NetCodes;
import shared.ParsersName;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.function.Consumer;
import java.util.function.Function;

public class ServerImpl {
    private static Hashtable<String, Consumer<HashMap<String,String>>> listOfFunctions = new Hashtable<>();
    private static Hashtable<String, Function<String[], HashMap<String,String>>> listOfParsers = new Hashtable<>();

    public static void connect( HashMap<String,String> data){}
    public static void createChannel( HashMap<String,String> data){}
    public static void joinChannel( HashMap<String,String> data){}
    public static void deleteMessage( HashMap<String,String> data){}
    public static void modifyMessage( HashMap<String,String> data){}
    public static void deleteChannel( HashMap<String,String> data){}
    public static void listChannelsInServer( HashMap<String,String> data){}
    public static void listOfUserInChannel( HashMap<String,String> data){}
    public static void consumeMessage( HashMap<String,String> data){}

    public static void initListOfFunctionsAndParsers(){
        // initialisation of methods;
        listOfFunctions.put(NetCodes.CONNECTION,ServerImpl::connect);
        listOfFunctions.put(NetCodes.CREATE_CHANNEL,ServerImpl::createChannel);
        listOfFunctions.put(NetCodes.JOIN_CHANNEL, ServerImpl::joinChannel);
        listOfFunctions.put(NetCodes.DELETE_MESSAGE, ServerImpl::deleteMessage);
        listOfFunctions.put(NetCodes.MODIFY_MESSAGE, ServerImpl::modifyMessage);
        listOfFunctions.put(NetCodes.DELETE_CHANNEL, ServerImpl::deleteChannel);
        listOfFunctions.put(NetCodes.LIST_CHANNELS_IN_SERVER, ServerImpl::listChannelsInServer);
        listOfFunctions.put(NetCodes.LIST_OF_USER_IN_CHANNEL, ServerImpl::listOfUserInChannel);
        listOfFunctions.put(NetCodes.CONSUME_MESSAGE, ServerImpl::consumeMessage);

        // initialisation of parsers;
        listOfParsers.put(ParsersName.connectionParser,ServerImpl::connexionParser);
        listOfParsers.put(ParsersName.createChannelParser,ServerImpl::creationChannelParser);
    }

    public static Consumer<HashMap<String,String>> getFunctionWithRequestCode(String code){
        return listOfFunctions.get(code);
    }

    public static  HashMap<String,String> requestParser(String request){
        String[] dataArray = request.split(" ");
        return listOfParsers.get(dataArray[0]).apply(dataArray);// TODO: change this after.
    }

    //TODO : define the parsers;
    private static HashMap<String,String> connexionParser(String[] dataArray){
        HashMap<String,String> data = new HashMap<String,String>();
        data.put(FieldsRequestName.netCode,dataArray[0]);
        data.put(FieldsRequestName.userId,dataArray[1]);
        return data;
    }

    private static HashMap<String,String> creationChannelParser(String[] dataArray){
        HashMap<String,String> data = new HashMap<String,String>();
        data.put(FieldsRequestName.netCode,dataArray[0]);
        data.put(FieldsRequestName.userId,dataArray[1]);
        data.put(FieldsRequestName.channelName,dataArray[2]);
        data.put(FieldsRequestName.channelDescription,dataArray[3]);
        return data;
    }
}
