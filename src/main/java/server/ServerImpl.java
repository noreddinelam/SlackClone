package server;

import database.Repository;
import models.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import shared.NetCodes;
import shared.communication.Request;
import shared.gson_configuration.GsonConfiguration;

import java.util.Hashtable;
import java.util.function.Function;

public class ServerImpl {

    private static Repository repository = Repository.getRepository();

    private static Hashtable<String, Function<String,String>> listOfFunctions = new Hashtable<>();
    private static Logger logger = LoggerFactory.getLogger(Server.class);

    public static String connect( String data){
        logger.info("Function : Connection to server");
        return "";
    }
    public static String createChannel( String data){
        Channel requestData = GsonConfiguration.gson.fromJson(data,Channel.class);
        logger.info("Create channel data received {}",requestData);
        return "";
    }
    //data simple
    public static String joinChannel( String data){

        return "";
    }
    public static String deleteMessage( String data){
        return "";
    }
    public static String modifyMessage( String data){
        return "";
    }
    public static String deleteChannel( String data){
        return "";
    }
    public static String listChannelsInServer( String data){
        return "";
    }
    public static String listOfUserInChannel( String data){
        return "";
    }
    public static String listOfMessageInChannel( String data){
        return "";
    }
    public static String consumeMessage( String data){
        return "";
    }

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
        listOfFunctions.put(NetCodes.LIST_OF_USER_IN_CHANNEL, ServerImpl::listOfUserInChannel);
        listOfFunctions.put(NetCodes.CONSUME_MESSAGE, ServerImpl::consumeMessage);
        listOfFunctions.put(NetCodes.List_Of_MESSAGE_IN_CHANNEL, ServerImpl::listOfMessageInChannel);
    }

    public static Function<String,String> getFunctionWithRequestCode(Request request){
        return listOfFunctions.get(request.getNetCode());
    }

//    public static  Map<String,String> requestParser(Request request){
//        String[] dataArray = request.split(" ");
//        logger.info("Received request {}",request);
//        return listOfParsers.get(dataArray[0]).apply(dataArray);// TODO: change this after.
//    }

}
