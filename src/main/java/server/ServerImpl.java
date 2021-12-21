package server;

import Exceptions.AddMessageException;
import Exceptions.CreateChannelException;
import database.Repository;
import models.Channel;
import models.Message;
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
import java.util.Hashtable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;
import java.util.function.Function;

public class ServerImpl {

    private static int cpt = 0;

    private static ConcurrentHashMap<String, AsynchronousSocketChannel> listOfClients = new ConcurrentHashMap<>();

    private static Repository repository = Repository.getRepository();

    private static Hashtable<String, Consumer<String>> listOfFunctions = new Hashtable<>();
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
    public static void joinChannel(String data) {

        Map<String, String> requestData = GsonConfiguration.gson.fromJson(data, CommunicationTypes.mapJsonTypeData);
//        listOfClients.values().forEach((client)->{
//            client.write(attachment, attachment, new ServerWriterCompletionHandler(client));
//        });
        logger.info("Joining channel data {}", requestData);

    }

    public static void deleteMessage(String data) {
        //message id
        Map<String, String> requestData = GsonConfiguration.gson.fromJson(data, CommunicationTypes.mapJsonTypeData);
        String messageId = requestData.get(FieldsRequestName.messageID);
        logger.info("Message delated{}", requestData);
    }

    public static void modifyMessage(String data) {
        //message id
        Map<String, String> requestData = GsonConfiguration.gson.fromJson(data, CommunicationTypes.mapJsonTypeData);
        int idMessage = Integer.valueOf(requestData.get(FieldsRequestName.messageID));
        logger.info("Message delated {}", requestData);
    }

    public static void deleteChannel(String data) {
        Map<String, String> requestData = GsonConfiguration.gson.fromJson(data, CommunicationTypes.mapJsonTypeData);
        int idMessage = Integer.valueOf(requestData.get(FieldsRequestName.messageID));
        logger.info("Message delated {}", requestData);
    }

    public static void listChannelsInServer(String data) {

    }

    public static void listOfUserInChannel(String data) {

    }

    public static void listOfMessageInChannel(String data) {
    }

    public static void consumeMessage(String data) {
        Message messageReceived = GsonConfiguration.gson.fromJson(data,Message.class);
        try {
            repository.addMessageDB(messageReceived).orElseThrow(AddMessageException::new);
            listOfClients.entrySet().forEach((entry)->{
                AsynchronousSocketChannel client = entry.getValue();
                String responseJson;
                if(entry.getKey().equalsIgnoreCase(messageReceived.getUser().getUsername())){
                    Response responseSucceed = new Response(NetCodes.MESSAGE_CONSUMED, "Message consumption succeed");
                    responseJson = GsonConfiguration.gson.toJson(responseSucceed);
                    ByteBuffer bufferReader = ByteBuffer.allocate(1024);
                    client.read(bufferReader,bufferReader,new ServerReaderCompletionHandler());
                }
                else{
                    Response responseBroadcast = new Response(NetCodes.MESSAGE_BROADCAST,data);
                    responseJson = GsonConfiguration.gson.toJson(responseBroadcast);
                }
                ByteBuffer buffer = ByteBuffer.wrap(responseJson.getBytes());
                client.write(buffer,buffer,new ServerWriterCompletionHandler(client));
            });
        } catch (AddMessageException e) {
            e.printStackTrace();
            Response response = new Response(NetCodes.MESSAGE_CONSUMPTION_ERROR, "Message consumption error");
            String responseJson = GsonConfiguration.gson.toJson(response);
            AsynchronousSocketChannel client = listOfClients.get(messageReceived.getUser().getUsername());
            ByteBuffer buffer = ByteBuffer.wrap(responseJson.getBytes());
            client.write(buffer,buffer,new ServerWriterCompletionHandler(client));
            ByteBuffer bufferReader = ByteBuffer.allocate(1024);
            client.read(bufferReader,bufferReader,new ServerReaderCompletionHandler());
        }
    }

    public static void initListOfFunctionsAndParsers() {
        // initialisation of methods;
        listOfFunctions.put(NetCodes.CONNECTION, ServerImpl::connect);
        listOfFunctions.put(NetCodes.CREATE_CHANNEL, ServerImpl::createChannel);
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

    public static Consumer<String> getFunctionWithRequestCode(Request request) {
        return listOfFunctions.get(request.getNetCode());
    }

    public static void addConnectedClients(AsynchronousSocketChannel client) throws IOException {
        listOfClients.put("nouredine", client);
    }

}
