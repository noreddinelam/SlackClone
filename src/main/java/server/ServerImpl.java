package server;

import Exceptions.*;
import database.Repository;
import database.SQLTablesInformation;
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
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

public class ServerImpl {

    private static final String usernames[] = {"nouredine", "dola", "amine", "arthur"};
    private static final ConcurrentHashMap<String, AsynchronousSocketChannel> listOfClients = new ConcurrentHashMap<>();
    private static final Repository repository = Repository.getRepository();
    private static final Mapper mapper = Mapper.getMapper();
    private static final Hashtable<String, Consumer<String>> listOfFunctions = new Hashtable<>();
    private static final Logger logger = LoggerFactory.getLogger(Server.class);
    private static int cpt = 0;

    public static void connect(String data) {
        logger.info("Function : Connection to server");
    }

    public static void createChannel(String data) {
        Channel requestData = GsonConfiguration.gson.fromJson(data, Channel.class);
        AsynchronousSocketChannel client = listOfClients.get(requestData.getAdmin().getUsername());
        logger.info("Create channel data received {}", requestData);
        try {
            repository.createChannelDB(requestData).orElseThrow(CreateChannelException::new);
            Response response = new Response(NetCodes.CREATE_CHANNEL_SUCCEED, "Channel created");
            String responseJson = GsonConfiguration.gson.toJson(response);
            ByteBuffer attachment = ByteBuffer.wrap(responseJson.getBytes());
            logger.info("username {}", requestData.getAdmin().getUsername());
            client.write(attachment, attachment, new ServerWriterCompletionHandler());
            attachment.clear();
            ByteBuffer newByteBuffer = ByteBuffer.allocate(1024);
            client.read(newByteBuffer, newByteBuffer, new ServerReaderCompletionHandler());
        } catch (CreateChannelException e) {
            e.printStackTrace();
            Response response = new Response(NetCodes.CREATE_CHANNEL_FAILED, "Channel creation failed");
            requestFailure(response, client);
        }
    }

    public static void joinChannel(String data) {
        logger.info("joining channel {} ", data);
        Map<String, String> requestData = GsonConfiguration.gson.fromJson(data, CommunicationTypes.mapJsonTypeData);
        String username = requestData.get(FieldsRequestName.userName);
        String channelName = requestData.get(FieldsRequestName.channelName);
        AsynchronousSocketChannel client = listOfClients.get(username);
        try {
            repository.joinChannelDB(channelName, username).orElseThrow(JoinChannelException::new);
            ResultSet resultSet =
                    repository.fetchAllUsersWithChannelName(channelName).orElseThrow(FetchAllUsersWithChannelNameException::new);
            Response response = new Response(NetCodes.JOIN_CHANNEL_SUCCEED, "Channel joined");
            Response broadcastResponse = new Response(NetCodes.JOIN_CHANNEL_BROADCAST, username + " has joined the " +
                    "channel");
            String responseJson = GsonConfiguration.gson.toJson(response);
            ByteBuffer attachment = ByteBuffer.wrap(responseJson.getBytes());
            logger.info("username {}", username);
            client.write(attachment, attachment, new ServerWriterCompletionHandler());
            attachment.clear();
            String broadcastUsername;
            AsynchronousSocketChannel broadcastClient;
            while (resultSet.next()) {
                broadcastUsername = resultSet.getString("username");
                broadcastClient = listOfClients.get(broadcastUsername);
                if (broadcastClient != null && !broadcastUsername.equalsIgnoreCase(username))
                    broadcastResponseClient(broadcastClient, broadcastResponse);
            }
            ByteBuffer newByteBuffer = ByteBuffer.allocate(1024);
            client.read(newByteBuffer, newByteBuffer, new ServerReaderCompletionHandler());
        } catch (JoinChannelException e) {
            e.printStackTrace();
            Response response = new Response(NetCodes.JOIN_CHANNEL_FAILED, "joining channel failed");
            requestFailure(response, client);
        } catch (FetchAllUsersWithChannelNameException e) {
            e.printStackTrace();
            Response response = new Response(NetCodes.JOIN_CHANNEL_BROADCAST_FAILED, "broadcasting message error");
            requestFailure(response, client);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    //TODO : there is enhancements in future
    public static void deleteMessage(String data) {
        Map<String, String> requestData = GsonConfiguration.gson.fromJson(data, CommunicationTypes.mapJsonTypeData);
        int idMessage = Integer.parseInt(requestData.get(FieldsRequestName.messageID));
        String username = requestData.get(FieldsRequestName.userName);
        AsynchronousSocketChannel client = listOfClients.get(username);
        try {
            Response response = new Response(NetCodes.DELETE_MESSAGE_SUCCEED, "Message deletion succeeded");
            repository.deleteMessageDB(idMessage).orElseThrow(DeleteMessageException::new);
            ByteBuffer buffer = ByteBuffer.wrap(GsonConfiguration.gson.toJson(response).getBytes());
            client.write(buffer,buffer,new ServerWriterCompletionHandler());
            buffer.clear();
            ByteBuffer newByteBuffer = ByteBuffer.allocate(1024);
            client.read(newByteBuffer, newByteBuffer, new ServerReaderCompletionHandler());
        } catch (DeleteMessageException e) {
            e.printStackTrace();
            Response response = new Response(NetCodes.DELETE_MESSAGE_FAILED, "Message deletion failed");
            requestFailure(response, client);
        }
    }

    //TODO : there is enhancements in future
    public static void modifyMessage(String data) {
        logger.info("modify message {} ", data);
        Map<String, String> requestData = GsonConfiguration.gson.fromJson(data, CommunicationTypes.mapJsonTypeData);
        String idMessage = requestData.get(FieldsRequestName.messageID);
        String username = requestData.get(FieldsRequestName.userName);
        AsynchronousSocketChannel client = listOfClients.get(username);
        try {
            repository.modifyMessageDB(requestData.get(FieldsRequestName.messageContent), idMessage).orElseThrow(ModifyMessageException::new);
            Response response = new Response(NetCodes.MODIFY_MESSAGE_SUCCEED, "message modified");
            String responseJson = GsonConfiguration.gson.toJson(response);
            ByteBuffer attachment = ByteBuffer.wrap(responseJson.getBytes());
            logger.info("idMessage {}", idMessage);
            client.write(attachment, attachment, new ServerWriterCompletionHandler());
            attachment.clear();
            ByteBuffer newByteBuffer = ByteBuffer.allocate(1024);
            client.read(newByteBuffer, newByteBuffer, new ServerReaderCompletionHandler());
        } catch (ModifyMessageException e) {
            Response response = new Response(NetCodes.MODIFY_MESSAGE_FAILED, "Message modification failed");
            requestFailure(response, client);
        }

    }

    //TODO : It's not implemented yet
    public static void deleteChannel(String data) {
        Map<String, String> requestData = GsonConfiguration.gson.fromJson(data, CommunicationTypes.mapJsonTypeData);
        int idMessage = Integer.valueOf(requestData.get(FieldsRequestName.messageID));
        logger.info("Message delated {}", requestData);
    }


    public static void listChannelsInServer(String data) {
        logger.info("list of channel in the server {} ", data);
        Map<String, String> requestData = GsonConfiguration.gson.fromJson(data, CommunicationTypes.mapJsonTypeData);
        String username = requestData.get(FieldsRequestName.userName);
        AsynchronousSocketChannel client = listOfClients.get(username);
        try {
            ResultSet resultSet = repository.listChannelsInServerDB().orElseThrow(ListOfUserInChannelException::new);
            List<Channel> channels = mapper.resultSetToChannel(resultSet);
            Map<String, List<Channel>> responseData = new HashMap<>();
            responseData.put(FieldsRequestName.listChannels, channels);
            Response response = new Response(NetCodes.LIST_CHANNELS_IN_SERVER_SUCCEED,
                    GsonConfiguration.gson.toJson(responseData, CommunicationTypes.mapListChannelJsonTypeData));
            String responseJson = GsonConfiguration.gson.toJson(response);
            ByteBuffer attachment = ByteBuffer.wrap(responseJson.getBytes());
            client.write(attachment, attachment, new ServerWriterCompletionHandler());
            attachment.clear();
            ByteBuffer newByteBuffer = ByteBuffer.allocate(1024);
            client.read(newByteBuffer, newByteBuffer, new ServerReaderCompletionHandler());
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ListOfUserInChannelException e) {
            e.printStackTrace();
            Response response = new Response(NetCodes.LIST_CHANNELS_IN_SERVER_FAILED, "list of channels failed");
            requestFailure(response, client);
        }


    }

    public static void listOfUserInChannel(String data) {
        logger.info("list of user in channel {} ", data);
        Map<String, String> requestData = GsonConfiguration.gson.fromJson(data, CommunicationTypes.mapJsonTypeData);
        String username = requestData.get(FieldsRequestName.userName);
        String channelName = requestData.get(FieldsRequestName.channelName);
        AsynchronousSocketChannel client = listOfClients.get(username);
        try {
            ResultSet resultSet =
                    repository.listOfUserInChannelDB(channelName).orElseThrow(ListOfUserInChannelException::new);
            //List<Channel> channels = mapper.resultSetToChannel(resultSet);
            List<User> users = mapper.resultSetToUser(resultSet);
            Map<String, List<User>> responseData = new HashMap<>();
            responseData.put(FieldsRequestName.userName, users);
            Response response = new Response(NetCodes.LIST_OF_USER_IN_CHANNEL_SUCCEED,
                    GsonConfiguration.gson.toJson(responseData, CommunicationTypes.mapListUserJsonTypeData));
            String responseJson = GsonConfiguration.gson.toJson(response);
            ByteBuffer attachment = ByteBuffer.wrap(responseJson.getBytes());
            client.write(attachment, attachment, new ServerWriterCompletionHandler());
            attachment.clear();
            ByteBuffer newByteBuffer = ByteBuffer.allocate(1024);
            client.read(newByteBuffer, newByteBuffer, new ServerReaderCompletionHandler());

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ListOfUserInChannelException e) {
            e.printStackTrace();
            Response response = new Response(NetCodes.LIST_OF_USER_IN_CHANNEL_FAILED, "list of user in channel failed");
            requestFailure(response, client);
        }

    }

    public static void listOfMessageInChannel(String data) {
        logger.info("list of message in channel {} ", data);

        Map<String, String> requestData = GsonConfiguration.gson.fromJson(data, CommunicationTypes.mapJsonTypeData);
        String username = requestData.get(FieldsRequestName.userName);
        String channelName = requestData.get(FieldsRequestName.channelName);
        AsynchronousSocketChannel client = listOfClients.get(username);
        try {
            ResultSet resultSet =
                    repository.listOfMessageInChannelDB(channelName).orElseThrow(ListOfMessageInChannelException::new);
            List<Message> messages = mapper.resultSetToMessage(resultSet);
            Map<String, List<Message>> responseData = new HashMap<>();
            responseData.put(FieldsRequestName.channelName, messages);
            Response response = new Response(NetCodes.List_Of_MESSAGE_IN_CHANNEL_SUCCEED,
                    GsonConfiguration.gson.toJson(responseData, CommunicationTypes.mapListMessageJsonTypeData));
            String responseJson = GsonConfiguration.gson.toJson(response);
            ByteBuffer attachment = ByteBuffer.wrap(responseJson.getBytes());
            client.write(attachment, attachment, new ServerWriterCompletionHandler());
            attachment.clear();
            ByteBuffer newByteBuffer = ByteBuffer.allocate(1024);
            client.read(newByteBuffer, newByteBuffer, new ServerReaderCompletionHandler());

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ListOfMessageInChannelException e) {
            e.printStackTrace();
            Response response = new Response(NetCodes.List_Of_MESSAGE_IN_CHANNEL_FAILED, "list of message in channel " +
                    "failed");
            requestFailure(response, client);
        }
    }

    //TODO : change the broadcast methodology which is static here.
    public static void consumeMessage(String data) {
        Message messageReceived = GsonConfiguration.gson.fromJson(data, Message.class);
        String channelName = messageReceived.getChannel().getChannelName();
        String username = messageReceived.getUser().getUsername();
        AsynchronousSocketChannel client = listOfClients.get(username);
        try {
            repository.addMessageDB(messageReceived).orElseThrow(AddMessageException::new);
            ResultSet result =  repository.listOfUserInChannelDB(channelName).orElseThrow(ListOfUserInChannelException::new);
            Response responseSucceed = new Response(NetCodes.MESSAGE_CONSUMED, "Message consumption succeed");
            Response broadcastResponse = new Response(NetCodes.MESSAGE_BROADCAST, data);
            ByteBuffer buffer = ByteBuffer.wrap(GsonConfiguration.gson.toJson(responseSucceed).getBytes());
            client.write(buffer, buffer, new ServerWriterCompletionHandler());
            AsynchronousSocketChannel broadcastClient;
            String broadcastUsername;
            while(result.next()){
                broadcastUsername = result.getString(SQLTablesInformation.clientChannelUsernameColumn);
                broadcastClient = listOfClients.get(broadcastUsername);
                if (broadcastClient != null && !broadcastUsername.equalsIgnoreCase(username))
                    broadcastResponseClient(broadcastClient, broadcastResponse);
            }
        } catch (AddMessageException e) {
            e.printStackTrace();
            Response response = new Response(NetCodes.MESSAGE_CONSUMPTION_ERROR, "Message consumption error");
            requestFailure(response, client);
        } catch (ListOfUserInChannelException e) {
            e.printStackTrace();
            Response response = new Response(NetCodes.MESSAGE_BROADCAST_FAILED,"Message broadcast failed");
            requestFailure(response, client);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
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
        listOfClients.put(usernames[cpt++], client);
    }

    private static void requestFailure(Response response, AsynchronousSocketChannel client) {
        String responseJson = GsonConfiguration.gson.toJson(response);
        ByteBuffer buffer = ByteBuffer.wrap(responseJson.getBytes());
        client.write(buffer, buffer, new ServerWriterCompletionHandler());
        ByteBuffer bufferReader = ByteBuffer.allocate(1024);
        client.read(bufferReader, bufferReader, new ServerReaderCompletionHandler());
    }

    private static void broadcastResponseClient(AsynchronousSocketChannel broadcastClient, Response broadcastResponse) {
        String responseJson = GsonConfiguration.gson.toJson(broadcastResponse);
        ByteBuffer buffer = ByteBuffer.wrap(responseJson.getBytes());
        broadcastClient.write(buffer, buffer, new ServerWriterCompletionHandler());
    }

}
