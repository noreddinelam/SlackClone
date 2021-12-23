package server;

import Exceptions.CreateChannelException;
import Exceptions.JoinChannelException;
import database.Repository;
import models.Channel;
import models.User;
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
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

public class ServerImpl {

    private static int cpt = 0;

    private static ConcurrentHashMap<String, AsynchronousSocketChannel> listOfClients = new ConcurrentHashMap<>();

    private static Repository repository = Repository.getRepository();

    private static Hashtable<String, Function<String, String>> listOfFunctions = new Hashtable<>();
    private static Logger logger = LoggerFactory.getLogger(Server.class);

    public static String connect(String data) {
        logger.info("Function : Connection to server");
        return " ";
    }

    public static String createChannel(String data) {
        Channel requestData = GsonConfiguration.gson.fromJson(data, Channel.class);
        logger.info("Create channel data received {}", requestData);
        try {
            repository.createChannelDB(requestData).orElseThrow(CreateChannelException::new);
            Response response = new Response(NetCodes.CREATE_CHANNEL_SUCCEED, "Channel created");
            String responseJson = GsonConfiguration.gson.toJson(response);
            ByteBuffer attachment = ByteBuffer.wrap(responseJson.getBytes());
            logger.info("username {}", requestData.getAdmin().getUsername());
            AsynchronousSocketChannel client = listOfClients.get(requestData.getAdmin().getUsername());
            client.write(attachment, attachment, new ServerWriterCompletionHandler(client));
            attachment.clear();
            ByteBuffer newByteBuffer = ByteBuffer.allocate(1024);
            client.read(newByteBuffer, newByteBuffer, new ServerReaderCompletionHandler());
            return GsonConfiguration.gson.toJson(response);
        } catch (CreateChannelException e) {
            e.printStackTrace();
            Response response = new Response(NetCodes.CREATE_CHANNEL_FAILED, "Channel creation failed");
            return GsonConfiguration.gson.toJson(response);
        }
    }

    //data simple
    public static String joinChannel(String data) {
        logger.info("joining channel {} ", data);
        //Channel requestDataChannel = GsonConfiguration.gson.fromJson(dataChannel, Channel.class);
            Map<String,String> requestData = GsonConfiguration.gson.fromJson(data, CommunicationTypes.mapJsonTypeData);

        try {
            String username = requestData.get(FieldsRequestName.userName);
            repository.joinChannelDB(requestData.get(FieldsRequestName.channelName), username).orElseThrow(JoinChannelException::new);
            Response response = new Response(NetCodes.JOIN_CHANNEL, "Channel joined");
            String responseJson = GsonConfiguration.gson.toJson(response);
            ByteBuffer attachment = ByteBuffer.wrap(responseJson.getBytes());
            logger.info("username {}",username);
            AsynchronousSocketChannel client = listOfClients.get(username);
            client.write(attachment, attachment, new ServerWriterCompletionHandler(client));
            attachment.clear();
            ByteBuffer newByteBuffer = ByteBuffer.allocate(1024);
            client.read(newByteBuffer, newByteBuffer, new ServerReaderCompletionHandler());
            return GsonConfiguration.gson.toJson(response);
        } catch ( JoinChannelException e) {
            e.printStackTrace();
            Response response = new Response(NetCodes.JOIN_CHANNEL, "joining channel failed");
            return GsonConfiguration.gson.toJson(response);
        }

    }

    public static String deleteMessage(String data) {
        //message id
        Map<String, String> requestData = GsonConfiguration.gson.fromJson(data, CommunicationTypes.mapJsonTypeData);

        logger.info("Message delated{}", requestData);
        return " ";
    }

    public static String modifyMessage(String data) {
        //message id
        Map<String, String> requestData = GsonConfiguration.gson.fromJson(data, CommunicationTypes.mapJsonTypeData);
        int idMessage = Integer.valueOf(requestData.get(FieldsRequestName.messageID));
        logger.info("Message delated {}", requestData);
        return " ";
    }

    public static String deleteChannel(String data) {
        Map<String, String> requestData = GsonConfiguration.gson.fromJson(data, CommunicationTypes.mapJsonTypeData);
        int idMessage = Integer.valueOf(requestData.get(FieldsRequestName.messageID));
        logger.info("Message delated {}", requestData);
        return " ";
    }

    public static String listChannelsInServer(String data) {

        return " ";
    }

    public static String listOfUserInChannel(String data) {

        return " ";
    }

    public static String listOfMessageInChannel(String data) {
        return "";
    }

    public static String consumeMessage(String data) {
        return " ";
    }

    public static void initListOfFunctionsAndParsers() {
        // initialisation of methods;
        listOfFunctions.put(NetCodes.CONNECTION, ServerImpl::connect);
        listOfFunctions.put(NetCodes.CREATE_CHANNEL, ServerImpl::createChannel);
        listOfFunctions.put(NetCodes.JOIN_CHANNEL, ServerImpl:: joinChannel);
        listOfFunctions.put(NetCodes.DELETE_MESSAGE, ServerImpl::deleteMessage);
        listOfFunctions.put(NetCodes.MODIFY_MESSAGE, ServerImpl::modifyMessage);
        listOfFunctions.put(NetCodes.DELETE_CHANNEL, ServerImpl::deleteChannel);
        listOfFunctions.put(NetCodes.LIST_CHANNELS_IN_SERVER, ServerImpl::listChannelsInServer);
        listOfFunctions.put(NetCodes.LIST_OF_USER_IN_CHANNEL, ServerImpl::listOfUserInChannel);
        listOfFunctions.put(NetCodes.LIST_OF_USER_IN_CHANNEL, ServerImpl::listOfUserInChannel);
        listOfFunctions.put(NetCodes.CONSUME_MESSAGE, ServerImpl::consumeMessage);
        listOfFunctions.put(NetCodes.List_Of_MESSAGE_IN_CHANNEL, ServerImpl::listOfMessageInChannel);
    }



    public static Function<String, String> getFunctionWithRequestCode(Request request) {
        return listOfFunctions.get(request.getNetCode());
    }

    public static void addConnectedClients(AsynchronousSocketChannel client) throws IOException {
        listOfClients.put("dola", client);
    }

}
