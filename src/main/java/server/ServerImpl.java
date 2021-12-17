package server;

import database.Repository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import shared.FieldsRequestName;
import shared.NetCodes;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;

public class ServerImpl {

    private static Repository repository = Repository.getRepository();

    private static Hashtable<String, Consumer<Map<String,String>>> listOfFunctions = new Hashtable<>();
    private static Hashtable<String, Function<String[], Map<String,String>>> listOfParsers = new Hashtable<>();
    private static Logger logger = LoggerFactory.getLogger(Server.class);

    public static void connect( Map<String,String> data){
        logger.info("Function : Connection to server");
    }
    public static void createChannel( Map<String,String> data){

    }
    public static void joinChannel( Map<String,String> data){}
    public static void deleteMessage( Map<String,String> data){}
    public static void modifyMessage( Map<String,String> data){}
    public static void deleteChannel( Map<String,String> data){}
    public static void listChannelsInServer( Map<String,String> data){}
    public static void listOfUserInChannel( Map<String,String> data){}
    public static void consumeMessage( Map<String,String> data){}

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
        listOfParsers.put(NetCodes.CONNECTION,ServerImpl::connexionParser);
        listOfParsers.put(NetCodes.CREATE_CHANNEL,ServerImpl::creationChannelParser);
    }

    public static Consumer<Map<String,String>> getFunctionWithRequestCode(String code){
        return listOfFunctions.get(code);
    }

    public static  Map<String,String> requestParser(String request){
        String[] dataArray = request.split(" ");
        logger.info("Received request {}",request);
        return listOfParsers.get(dataArray[0]).apply(dataArray);// TODO: change this after.
    }

    //TODO : define the parsers;
    private static Map<String,String> connexionParser(String[] dataArray){
        HashMap<String,String> data = new HashMap<>();
        data.put(FieldsRequestName.netCode,dataArray[0]);
        data.put(FieldsRequestName.userId,dataArray[1]);
        return data;
    }

    private static Map<String,String> creationChannelParser(String[] dataArray){
        HashMap<String,String> data = new HashMap<>();
        data.put(FieldsRequestName.netCode,dataArray[0]);
        data.put(FieldsRequestName.userId,dataArray[1]);
        data.put(FieldsRequestName.channelName,dataArray[2]);
        data.put(FieldsRequestName.channelDescription,dataArray[3]);
        return data;
    }
}
