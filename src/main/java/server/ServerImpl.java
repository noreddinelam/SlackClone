package server;

import database.Repository;
import models.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import shared.CommunicationTypes;
import shared.FieldsRequestName;
import shared.NetCodes;
import shared.communication.Request;
import shared.communication.Response;
import shared.gson_configuration.GsonConfiguration;


import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;


import java.util.function.Function;
import java.util.stream.Collectors;

public class ServerImpl {

    private static int cpt = 0;

    private static ConcurrentHashMap<String, AsynchronousSocketChannel> listOfClients = new ConcurrentHashMap<>();

    private static Repository repository = Repository.getRepository();

    private static Hashtable<String, Function<String,String>> listOfFunctions = new Hashtable<>();
    private static Logger logger = LoggerFactory.getLogger(Server.class);

    public static String connect( String data){
        logger.info("Function : Connection to server");
        return " ";
    }
    public static String createChannel( String data){
        Channel requestData = GsonConfiguration.gson.fromJson(data,Channel.class);
        logger.info("Create channel data received {}",requestData);
        Response response = new Response("","Salut");
        String responseJson = GsonConfiguration.gson.toJson(response);
        ByteBuffer attachment = ByteBuffer.wrap(responseJson.getBytes());
        listOfClients.values().forEach((client)->{
            client.write(attachment, attachment, new ServerWriterCompletionHandler(client));
        });
        attachment.clear();
        ByteBuffer newByteBuffer = ByteBuffer.allocate(1024);
        new ArrayList<>(listOfClients.values()).get(1).read(newByteBuffer,newByteBuffer,new ServerReaderCompletionHandler(null));
        return GsonConfiguration.gson.toJson(response);
    }
    //data simple
    public static String joinChannel( String data){

        Map<String,String> requestData = GsonConfiguration.gson.fromJson(data, CommunicationTypes.mapJsonTypeData);

        logger.info("Joining channel data {}",requestData);
        return " ";

    }
    public static String deleteMessage( String data){
        //message id
        Map<String,String> requestData = GsonConfiguration.gson.fromJson(data, CommunicationTypes.mapJsonTypeData);

        logger.info("Message delated{}",requestData);
        return " ";
    }
    public static String modifyMessage( String data){
        //message id
        Map<String,String> requestData = GsonConfiguration.gson.fromJson(data, CommunicationTypes.mapJsonTypeData);
        int idMessage = Integer.valueOf(requestData.get(FieldsRequestName.messageID));
        logger.info("Message delated {}",requestData);
        return " ";
    }
    public static String deleteChannel( String data){
        Map<String,String> requestData = GsonConfiguration.gson.fromJson(data, CommunicationTypes.mapJsonTypeData);
        int idMessage = Integer.valueOf(requestData.get(FieldsRequestName.messageID));
        logger.info("Message delated {}",requestData);
        return " ";
    }
    public static String listChannelsInServer( String data){

        return " ";
    }
    public static String listOfUserInChannel( String data){

        return " ";
    }
    public static String listOfMessageInChannel( String data){
        return "";
    }
    public static String consumeMessage( String data){
        return " ";
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

    public static void addConnectedClients(AsynchronousSocketChannel client) throws IOException {
        listOfClients.put(String.valueOf(cpt++), client);
    }

}
