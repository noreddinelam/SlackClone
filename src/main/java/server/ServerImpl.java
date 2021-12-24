package server;

import Exceptions.*;
import database.Repository;
import models.Channel;
import models.Message;
import models.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import shared.CommunicationTypes;
import shared.FieldsRequestName;
import shared.Mapper;
import shared.NetCodes;
import shared.communication.Request;
import shared.communication.Response;
import shared.gson_configuration.GsonConfiguration;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.util.Hashtable;
import java.util.Map;
import java.util.Optional;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

public class ServerImpl {

    private static ConcurrentHashMap<String, AsynchronousSocketChannel> listOfClients = new ConcurrentHashMap<>();

    private static Repository repository = Repository.getRepository();
    private static Mapper mapper = Mapper.getMapper();
    private static Hashtable<String, Consumer<String>> listOfFunctions = new Hashtable<>();
    private static Logger logger = LoggerFactory.getLogger(Server.class);

    public static void connect(String data) {
        logger.info("Function : Connection to server");
    }

    public static void createChannel(String data) {
        Channel requestData = GsonConfiguration.gson.fromJson(data, Channel.class);
        logger.info("Create channel data received {}", requestData);
        try {
            repository.createChannelDB(requestData).orElseThrow(CreateChannelException::new);
            Response response = new Response(NetCodes.CREATE_CHANNEL_SUCCEED, "Channel created");
            String responseJson = GsonConfiguration.gson.toJson(response);
            ByteBuffer attachment = ByteBuffer.wrap(responseJson.getBytes());
            logger.info("username {}", requestData.getAdmin().getUsername());
            AsynchronousSocketChannel client = listOfClients.get(requestData.getAdmin().getUsername());
            client.write(attachment, attachment, new ServerWriterCompletionHandler());
            attachment.clear();
            ByteBuffer newByteBuffer = ByteBuffer.allocate(1024);
            client.read(newByteBuffer, newByteBuffer, new ServerReaderCompletionHandler());
        } catch (CreateChannelException e) {
            e.printStackTrace();
            Response response = new Response(NetCodes.CREATE_CHANNEL_FAILED, "Channel creation failed");
            AsynchronousSocketChannel client = listOfClients.get(requestData.getAdmin().getUsername());
            requestFailure(response,client);
        }
    }

    public static void joinChannel(String data) {
        logger.info("joining channel {} ", data);
        Map<String, String> requestData = GsonConfiguration.gson.fromJson(data, CommunicationTypes.mapJsonTypeData);
        String username = requestData.get(FieldsRequestName.userName);
        try {
            repository.joinChannelDB(requestData.get(FieldsRequestName.channelName), username).orElseThrow(JoinChannelException::new);
            Response response = new Response(NetCodes.JOIN_CHANNEL, "Channel joined");
            String responseJson = GsonConfiguration.gson.toJson(response);
            ByteBuffer attachment = ByteBuffer.wrap(responseJson.getBytes());
            logger.info("username {}", username);
            AsynchronousSocketChannel client = listOfClients.get(username);
            client.write(attachment, attachment, new ServerWriterCompletionHandler());
            attachment.clear();
            ByteBuffer newByteBuffer = ByteBuffer.allocate(1024);
            client.read(newByteBuffer, newByteBuffer, new ServerReaderCompletionHandler());
        } catch (JoinChannelException e) {
            e.printStackTrace();
            Response response = new Response(NetCodes.JOIN_CHANNEL, "joining channel failed");
            AsynchronousSocketChannel client = listOfClients.get(username);
            requestFailure(response, client);
        }
    }

    public static String deleteMessage(String data) {
        //message id
        Response fail = new Response(NetCodes.DELETE_MESSAGE_FAILED, "Message deletion failed");
        Map<String, String> requestData = GsonConfiguration.gson.fromJson(data, CommunicationTypes.mapJsonTypeData);
        int idMessage =Integer.parseInt(requestData.get(FieldsRequestName.messageID));
        Optional<Boolean> result= repository.deleteMessageDB(idMessage);
        if(result.isPresent())
        {
            if(result.get()) return GsonConfiguration.gson.toJson(
                    new Response(NetCodes.DELETE_MESSAGE_SUCCEED, "Message deleted"));
        }
        return GsonConfiguration.gson.toJson(fail);
    }

    public static void modifyMessage(String data) {
        logger.info("modify message {} ", data);
        Map<String, String> requestData = GsonConfiguration.gson.fromJson(data, CommunicationTypes.mapJsonTypeData);
        String idmessage = requestData.get(FieldsRequestName.messageID);
        String username = requestData.get(FieldsRequestName.userName);
        try {
            repository.modifyMessageDB(requestData.get(FieldsRequestName.messageContent), idmessage).orElseThrow(ModifyMessageException::new);
            Response response = new Response(NetCodes.MODIFY_MESSAGE_SUCCEED, "message modified");
            String responseJson = GsonConfiguration.gson.toJson(response);
            ByteBuffer attachment = ByteBuffer.wrap(responseJson.getBytes());
            logger.info("idmessage{}", idmessage);
            AsynchronousSocketChannel client = listOfClients.get(username);
            client.write(attachment, attachment, new ServerWriterCompletionHandler());
            attachment.clear();
            ByteBuffer newByteBuffer = ByteBuffer.allocate(1024);
            client.read(newByteBuffer, newByteBuffer, new ServerReaderCompletionHandler());

        }
        catch (ModifyMessageException e){

        }

    }

    public static void deleteChannel(String data) {
        Map<String, String> requestData = GsonConfiguration.gson.fromJson(data, CommunicationTypes.mapJsonTypeData);
        int idMessage = Integer.valueOf(requestData.get(FieldsRequestName.messageID));
        logger.info("Message delated {}", requestData);
    }

    public static void listChannelsInServer(String data) {
        logger.info("list of channel in the server {} ", data);
        Map<String, String> requestData = GsonConfiguration.gson.fromJson(data, CommunicationTypes.mapJsonTypeData);
        String username = requestData.get(FieldsRequestName.userName);
        try {
            ResultSet resultSet =repository.listChannelsInServerDB().orElseThrow(ListOfUserInChannelException::new);
            List<Channel> channels = mapper.resultSetToChannel(resultSet);
            Map<String,List<Channel>> responseData = new HashMap<>();
            responseData.put(FieldsRequestName.listChannels,channels);
            Response response = new Response(NetCodes.LIST_CHANNELS_IN_SERVER_SUCCEED, GsonConfiguration.gson.toJson(responseData,CommunicationTypes.mapListChannelJsonTypeData));
            String responseJson = GsonConfiguration.gson.toJson(response);
            ByteBuffer attachment = ByteBuffer.wrap(responseJson.getBytes());
            AsynchronousSocketChannel client = listOfClients.get(username);
            client.write(attachment, attachment, new ServerWriterCompletionHandler());
            attachment.clear();
            ByteBuffer newByteBuffer = ByteBuffer.allocate(1024);
            client.read(newByteBuffer, newByteBuffer, new ServerReaderCompletionHandler());

        }
        catch (SQLException e) {
            e.printStackTrace();
        } catch (ListOfUserInChannelException e) {
            e.printStackTrace();
        }


    }

    public static void listOfUserInChannel(String data) {
        logger.info("list of user in channel {} ", data);
        Map<String, String> requestData = GsonConfiguration.gson.fromJson(data, CommunicationTypes.mapJsonTypeData);
        String username = requestData.get(FieldsRequestName.userName);
        String channelName =  requestData.get(FieldsRequestName.channelName);
        try {
            ResultSet resultSet =repository.listOfUserInChannelDB(channelName).orElseThrow(ListOfUserInChannelException::new);
            //List<Channel> channels = mapper.resultSetToChannel(resultSet);
            List<User> users = mapper.resultSetToUser(resultSet);
            Map<String,List<User>> responseData = new HashMap<>();
            responseData.put(FieldsRequestName.userName,users);
            Response response = new Response(NetCodes.LIST_OF_USER_IN_CHANNEL_SUCCEED, GsonConfiguration.gson.toJson(responseData,CommunicationTypes.mapListUserJsonTypeData));
            String responseJson = GsonConfiguration.gson.toJson(response);
            ByteBuffer attachment = ByteBuffer.wrap(responseJson.getBytes());
            AsynchronousSocketChannel client = listOfClients.get(username);
            client.write(attachment, attachment, new ServerWriterCompletionHandler());
            attachment.clear();
            ByteBuffer newByteBuffer = ByteBuffer.allocate(1024);
            client.read(newByteBuffer, newByteBuffer, new ServerReaderCompletionHandler());

        }
         catch (SQLException e) {
            e.printStackTrace();
        } catch (ListOfUserInChannelException e) {
            e.printStackTrace();
            Response response = new Response(NetCodes.LIST_OF_USER_IN_CHANNEL_FAILED, "list of user in channel failed");
            AsynchronousSocketChannel client = listOfClients.get(username);
            requestFailure(response, client);
        }

    }

    public static void listOfMessageInChannel(String data) {
        logger.info("list of user in channel {} ", data);
        Map<String, String> requestData = GsonConfiguration.gson.fromJson(data, CommunicationTypes.mapJsonTypeData);
        String username = requestData.get(FieldsRequestName.userName);
        String channelName =  requestData.get(FieldsRequestName.channelName);
        try {
            ResultSet resultSet =repository.listOfMessageInChanneleDB(channelName).orElseThrow(ListOfMessageInChannelException::new);
            //List<Channel> channels = mapper.resultSetToChannel(resultSet);
            List<Message> messages = mapper.resultSetToMessage(resultSet);
            Map<String,List<Message>> responseData = new HashMap<>();
            responseData.put(FieldsRequestName.channelName,messages);
            Response response = new Response(NetCodes.List_Of_MESSAGE_IN_CHANNEL_SUCCEED, GsonConfiguration.gson.toJson(responseData,CommunicationTypes.mapListMessageJsonTypeData));
            String responseJson = GsonConfiguration.gson.toJson(response);
            ByteBuffer attachment = ByteBuffer.wrap(responseJson.getBytes());
            AsynchronousSocketChannel client = listOfClients.get(username);
            client.write(attachment, attachment, new ServerWriterCompletionHandler());
            attachment.clear();
            ByteBuffer newByteBuffer = ByteBuffer.allocate(1024);
            client.read(newByteBuffer, newByteBuffer, new ServerReaderCompletionHandler());

        }
        catch (SQLException e) {
            e.printStackTrace();
        } catch (ListOfMessageInChannelException e) {
            e.printStackTrace();
            Response response = new Response(NetCodes.LIST_OF_USER_IN_CHANNEL_FAILED, "list of message in channel failed");
            AsynchronousSocketChannel client = listOfClients.get(username);
            requestFailure(response, client);
        }
    }

    public static void consumeMessage(String data) {
        Message messageReceived = GsonConfiguration.gson.fromJson(data, Message.class);
        try {
            repository.addMessageDB(messageReceived).orElseThrow(AddMessageException::new);
            listOfClients.entrySet().forEach((entry) -> {
                AsynchronousSocketChannel client = entry.getValue();
                String responseJson;
                if (entry.getKey().equalsIgnoreCase(messageReceived.getUser().getUsername())) {
                    Response responseSucceed = new Response(NetCodes.MESSAGE_CONSUMED, "Message consumption succeed");
                    responseJson = GsonConfiguration.gson.toJson(responseSucceed);
                    ByteBuffer bufferReader = ByteBuffer.allocate(1024);
                    client.read(bufferReader, bufferReader, new ServerReaderCompletionHandler());
                } else {
                    Response responseBroadcast = new Response(NetCodes.MESSAGE_BROADCAST, data);
                    responseJson = GsonConfiguration.gson.toJson(responseBroadcast);
                }
                ByteBuffer buffer = ByteBuffer.wrap(responseJson.getBytes());
                client.write(buffer, buffer, new ServerWriterCompletionHandler());
            });
        } catch (AddMessageException e) {
            e.printStackTrace();
            Response response = new Response(NetCodes.MESSAGE_CONSUMPTION_ERROR, "Message consumption error");
            AsynchronousSocketChannel client = listOfClients.get(messageReceived.getUser().getUsername());
            requestFailure(response, client);
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

    private static void requestFailure(Response response, AsynchronousSocketChannel client) {
        String responseJson = GsonConfiguration.gson.toJson(response);
        ByteBuffer buffer = ByteBuffer.wrap(responseJson.getBytes());
        client.write(buffer, buffer, new ServerWriterCompletionHandler());
        ByteBuffer bufferReader = ByteBuffer.allocate(1024);
        client.read(bufferReader, bufferReader, new ServerReaderCompletionHandler());
    }

}
