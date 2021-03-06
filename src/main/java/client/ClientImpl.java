package client;

import front.Others.FailureMessages;
import front.controllers.Controller;
import front.controllers.RequestToJoinChannelController;
import front.controllers.SlockController;
import front.controllers.UnJoinedChannelsController;
import models.Channel;
import models.Message;
import models.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import shared.CommunicationTypes;
import shared.FieldsRequestName;
import shared.NetCodes;
import shared.Properties;
import shared.communication.Request;
import shared.communication.Response;
import shared.gson_configuration.GsonConfiguration;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.function.Consumer;

public abstract class ClientImpl {
    protected static final Hashtable<String, Consumer<String>> listOfFunctions = new Hashtable<>();
    private static final Logger logger = LoggerFactory.getLogger(ClientImpl.class);
    protected User user;
    protected String ipAddress;
    protected AsynchronousSocketChannel client;
    protected Controller controller;
    protected RequestToJoinChannelController rqController;
    protected UnJoinedChannelsController ucController;

    public static Consumer<String> getFunctionWithRequestCode(Response response) {
        return listOfFunctions.get(response.getNetCode());
    }

    public void initThreadReader() {
        Thread reader = new Thread(() -> {
            ByteBuffer buffer = ByteBuffer.allocate(Properties.BUFFER_SIZE);
            try {
                while (client.isOpen()) {
                    int nb = client.read(buffer).get();
                    String jsonRes = new String(buffer.array()).substring(0, nb);
                    logger.info("The received response \n{}", jsonRes);
                    Response response = GsonConfiguration.gson.fromJson(jsonRes, Response.class);
                    ClientImpl.getFunctionWithRequestCode(response).accept(response.getResponse());
                    buffer = ByteBuffer.allocate(Properties.BUFFER_SIZE);
                }
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        });
        reader.start();
    }

    public abstract void logoutSucceeded(String responseData);

    public abstract void connectSucceeded(String responseData);

    public abstract void connectFailed(String responseData);

    public abstract void registerSucceeded(String responseData);

    public abstract void registerFailed(String responseData);

    public abstract void createChannelSucceeded(String responseData);

    public abstract void createChannelFailed(String responseData);

    public abstract void joinChannelSucceed(String responseData);

    public abstract void joinChannelFailed(String responseData);

    public abstract void joinChannelBroadcastSucceeded(String responseData);

    public abstract void joinChannelBroadcastFailed(String responseData);

    public abstract void leaveChannelSucceeded(String responseData);

    public abstract void leaveChannelFailed(String responseData);

    public abstract void leaveChannelBroadcastSucceeded(String responseData);

    public abstract void leaveChannelBroadcastFailed(String responseData);

    public abstract void deleteMessageSucceeded(String responseData);

    public abstract void deleteMessageFailed(String responseData);

    public abstract void deleteMessageBroadcastSucceeded(String responseData);

    public abstract void deleteMessageBroadcastFailed(String responseData);



    public abstract void modifyMessageBroadcastSucceeded(String responseData);

    public abstract void modifyMessageBroadcastFailed(String responseData);

    public abstract void deleteChannelBroadcastSucceeded(String responseData);

    public abstract void deleteChannelBroadcastFailed(String responseData);

    public abstract void modifyChannelSucceeded(String responseData);

    public abstract void modifyChannelFailed(String responseData);

    public abstract void modifyChannelBroadcastSucceeded(String responseData);

    public abstract void modifyChannelBroadcastFailed(String responseData);

    public abstract void modifyMessageSucceeded(String responseData);

    public abstract void modifyMessageFailed(String responseData);

    public abstract void deleteChannelSucceeded(String responseData);

    public abstract void deleteChannelFailed(String responseData);

    public abstract void listChannelsInServerSucceeded(String responseData);

    public abstract void listChannelsInServerFailed(String responseData);

    public abstract void listOfMessageInChannelSucceeded(String responseData);

    public abstract void listOfMessageInChannelFailed(String responseData);

    public abstract void listOfJoinedChannelsSucceeded(String responseData);

    public abstract void listOfJoinedChannelsFailed(String responseData);

    public abstract void listOfUnJoinedChannelsSucceeded(String responseData);

    public abstract void listOfUnJoinedChannelsFailed(String responseData);

    public abstract void listOfUserInChannelSucceeded(String responseData);

    public abstract void listOfUserInChannelFailed(String responseData);

    public abstract void listOfRequestsSucceeded(String responseData);

    public abstract void listOfRequestsFailed(String responseData);

    public abstract void messageConsumed(String responseData);

    public abstract void messageConsumptionError(String responseData);

    public abstract void messageBroadcastSucceed(String responseData);

    public abstract void messageBroadcastFailed(String responseData);

    public abstract void joinPrivateChannel(String responseData);

    public abstract void requestAlreadySent(String responseData);

    public abstract void responseRequestJoinChannelSucceeded(String responseData);

    public abstract void responseRequestJoinChannelFailed(String responseData);

    public abstract void deleteUserSucceeded(String responseData);

    public abstract void deleteUserFailed(String responseData);

    public abstract void deleteUserBroadcastFailed(String responseData);

    public abstract void deleteUserBroadcastSucceeded(String responseData);

    public void initListOfFunctions() {
        listOfFunctions.put(NetCodes.LOGOUT_SUCCEED, this::logoutSucceeded);

        listOfFunctions.put(NetCodes.CONNECT_SUCCEED, this::connectSucceeded);
        listOfFunctions.put(NetCodes.CONNECT_FAILED, this::connectFailed);

        listOfFunctions.put(NetCodes.REGISTER_SUCCEED, this::registerSucceeded);
        listOfFunctions.put(NetCodes.REGISTER_FAILED, this::registerFailed);

        listOfFunctions.put(NetCodes.CREATE_CHANNEL_SUCCEED, this::createChannelSucceeded);
        listOfFunctions.put(NetCodes.CREATE_CHANNEL_FAILED, this::createChannelFailed);

        listOfFunctions.put(NetCodes.JOIN_CHANNEL_SUCCEED, this::joinChannelSucceed);
        listOfFunctions.put(NetCodes.JOIN_CHANNEL_FAILED, this::joinChannelFailed);
        listOfFunctions.put(NetCodes.JOIN_CHANNEL_BROADCAST_SUCCEED, this::joinChannelBroadcastSucceeded);
        listOfFunctions.put(NetCodes.JOIN_CHANNEL_BROADCAST_FAILED, this::joinChannelBroadcastFailed);
        listOfFunctions.put(NetCodes.JOIN_PRIVATE_CHANNEL, this::joinPrivateChannel);
        listOfFunctions.put(NetCodes.REQUEST_JOIN_FAILED, this::requestAlreadySent);

        listOfFunctions.put(NetCodes.LEAVE_CHANNEL_SUCCEED, this::leaveChannelSucceeded);
        listOfFunctions.put(NetCodes.LEAVE_CHANNEL_FAILED, this::leaveChannelFailed);
        listOfFunctions.put(NetCodes.LEAVE_CHANNEL_BROADCAST_SUCCEED, this::leaveChannelBroadcastSucceeded);
        listOfFunctions.put(NetCodes.LEAVE_CHANNEL_BROADCAST_FAILED, this::leaveChannelBroadcastFailed);

        listOfFunctions.put(NetCodes.DELETE_MESSAGE_SUCCEED, this::deleteMessageSucceeded);
        listOfFunctions.put(NetCodes.DELETE_MESSAGE_FAILED, this::deleteMessageFailed);
        listOfFunctions.put(NetCodes.DELETE_MESSAGE_BROADCAST_SUCCEED, this::deleteMessageBroadcastSucceeded);
        listOfFunctions.put(NetCodes.DELETE_MESSAGE_BROADCAST_FAILED, this::deleteMessageBroadcastFailed);

        listOfFunctions.put(NetCodes.MODIFY_MESSAGE_SUCCEED, this::modifyMessageSucceeded);
        listOfFunctions.put(NetCodes.MODIFY_MESSAGE_FAILED, this::modifyMessageFailed);
        listOfFunctions.put(NetCodes.MODIFY_MESSAGE_BROADCAST_SUCCEEDED, this::modifyMessageBroadcastSucceeded);
        listOfFunctions.put(NetCodes.MODIFY_MESSAGE_BROADCAST_FAILED, this::modifyMessageBroadcastFailed);

        listOfFunctions.put(NetCodes.DELETE_CHANNEL_SUCCEED, this::deleteChannelSucceeded);
        listOfFunctions.put(NetCodes.DELETE_CHANNEL_FAILED, this::deleteChannelFailed);
        listOfFunctions.put(NetCodes.DELETE_CHANNEL_BROADCAST_SUCCEEDED, this::deleteChannelBroadcastSucceeded);
        listOfFunctions.put(NetCodes.DELETE_CHANNEL_BROADCAST_FAILED, this::deleteChannelBroadcastFailed);

        listOfFunctions.put(NetCodes.DELETE_USER_FROM_CHANNEL_SUCCEED, this::deleteUserSucceeded);
        listOfFunctions.put(NetCodes.DELETE_USER_FROM_CHANNEL_FAILED, this::deleteUserFailed);
        listOfFunctions.put(NetCodes.DELETE_USER_FROM_CHANNEL_BRODCAST_SUCCEED, this::deleteUserBroadcastSucceeded);
        listOfFunctions.put(NetCodes.DELETE_USER_FROM_CHANNEL_BRODCAST_FAILED, this::deleteUserBroadcastFailed);

        listOfFunctions.put(NetCodes.MODIFY_CHANNEL_SUCCEED, this::modifyChannelSucceeded);
        listOfFunctions.put(NetCodes.MODIFY_CHANNEL_FAILED, this::modifyChannelFailed);
        listOfFunctions.put(NetCodes.MODIFY_CHANNEL_BROADCAST_SUCCEED, this::modifyChannelBroadcastSucceeded);
        listOfFunctions.put(NetCodes.MODIFY_CHANNEL_BROADCAST_FAILED, this::modifyChannelBroadcastFailed);

        listOfFunctions.put(NetCodes.LIST_CHANNELS_IN_SERVER_SUCCEED, this::listChannelsInServerSucceeded);
        listOfFunctions.put(NetCodes.LIST_CHANNELS_IN_SERVER_FAILED, this::listChannelsInServerFailed);

        listOfFunctions.put(NetCodes.List_Of_MESSAGE_IN_CHANNEL_SUCCEED, this::listOfMessageInChannelSucceeded);
        listOfFunctions.put(NetCodes.List_Of_MESSAGE_IN_CHANNEL_FAILED, this::listOfMessageInChannelFailed);

        listOfFunctions.put(NetCodes.LIST_OF_JOINED_CHANNELS_SUCCEEDED, this::listOfJoinedChannelsSucceeded);
        listOfFunctions.put(NetCodes.LIST_OF_JOINED_CHANNELS_FAILED, this::listOfJoinedChannelsFailed);
        listOfFunctions.put(NetCodes.LIST_OF_UN_JOINED_CHANNELS_SUCCEEDED, this::listOfUnJoinedChannelsSucceeded);
        listOfFunctions.put(NetCodes.LIST_OF_UN_JOINED_CHANNELS_FAILED, this::listOfUnJoinedChannelsFailed);

        listOfFunctions.put(NetCodes.LIST_OF_USER_IN_CHANNEL_SUCCEED, this::listOfUserInChannelSucceeded);
        listOfFunctions.put(NetCodes.LIST_OF_USER_IN_CHANNEL_FAILED, this::listOfUserInChannelFailed);

        listOfFunctions.put(NetCodes.LIST_REQUEST_JOIN_CHANNEL_SUCCEED, this::listOfRequestsSucceeded);
        listOfFunctions.put(NetCodes.LIST_REQUEST_JOIN_CHANNEL_FAILED, this::listOfRequestsFailed);

        listOfFunctions.put(NetCodes.MESSAGE_CONSUMED, this::messageConsumed);
        listOfFunctions.put(NetCodes.MESSAGE_CONSUMPTION_ERROR, this::messageConsumptionError);
        listOfFunctions.put(NetCodes.MESSAGE_BROADCAST_SUCCEED, this::messageBroadcastSucceed);
        listOfFunctions.put(NetCodes.MESSAGE_BROADCAST_FAILED, this::messageBroadcastFailed);

        listOfFunctions.put(NetCodes.RESPONSE_JOIN_SUCCEED, this::responseRequestJoinChannelSucceeded);
        listOfFunctions.put(NetCodes.RESPONSE_JOIN_FAILED, this::responseRequestJoinChannelFailed);


    }

    public void logout() {
        Map<String, String> data = new HashMap<>();
        data.put(FieldsRequestName.userName, this.user.getUsername());
        data.put(FieldsRequestName.guest, this.ipAddress);
        String requestData = GsonConfiguration.gson.toJson(data, CommunicationTypes.mapJsonTypeData);
        Request request = new Request(NetCodes.LOGOUT, requestData);
        ByteBuffer buffer = ByteBuffer.wrap(GsonConfiguration.gson.toJson(request).getBytes());
        this.client.write(buffer, buffer, new ClientWriterCompletionHandler());
    }

    public void modifyChannel(String newChannelName, boolean isPublic, String channelName) {
        Map<String, String> data = new HashMap<>();
        data.put(FieldsRequestName.channelName, channelName);
        data.put(FieldsRequestName.newChannelName, newChannelName);
        data.put(FieldsRequestName.channelPublic, isPublic ? "true" : "false");
        data.put(FieldsRequestName.userName, this.user.getUsername());
        String requestData = GsonConfiguration.gson.toJson(data);
        Request request = new Request(NetCodes.MODIFY_CHANNEL, requestData);
        ByteBuffer buffer = ByteBuffer.wrap(GsonConfiguration.gson.toJson(request).getBytes());
        this.client.write(buffer, buffer, new ClientWriterCompletionHandler());
    }

    public void login(String username, String password) {
        Map<String, String> data = new HashMap<>();
        data.put(FieldsRequestName.userName, username);
        data.put(FieldsRequestName.password, password);
        data.put(FieldsRequestName.guest, this.ipAddress);
        String requestData = GsonConfiguration.gson.toJson(data, CommunicationTypes.mapJsonTypeData);
        Request request = new Request(NetCodes.CONNECTION, requestData);
        ByteBuffer buffer = ByteBuffer.wrap(GsonConfiguration.gson.toJson(request).getBytes());
        this.client.write(buffer, buffer, new ClientWriterCompletionHandler());
    }

    public void register(String username, String password) {
        Map<String, String> data = new HashMap<>();
        data.put(FieldsRequestName.userName, username);
        data.put(FieldsRequestName.password, password);
        data.put(FieldsRequestName.guest, this.ipAddress);
        String requestData = GsonConfiguration.gson.toJson(data, CommunicationTypes.mapJsonTypeData);
        Request request = new Request(NetCodes.REGISTER, requestData);
        ByteBuffer buffer = ByteBuffer.wrap(GsonConfiguration.gson.toJson(request).getBytes());
        this.client.write(buffer, buffer, new ClientWriterCompletionHandler());
    }

    public void listOfJoinedChannels() {
        Map<String, String> data = new HashMap<>();
        data.put(FieldsRequestName.userName, this.user.getUsername());
        String requestData = GsonConfiguration.gson.toJson(data, CommunicationTypes.mapJsonTypeData);
        Request request = new Request(NetCodes.LIST_OF_JOINED_CHANNELS, requestData);
        ByteBuffer buffer = ByteBuffer.wrap(GsonConfiguration.gson.toJson(request).getBytes());
        this.client.write(buffer, buffer, new ClientWriterCompletionHandler());
    }

    public void listOfUnJoinedChannels() {
        Map<String, String> data = new HashMap<>();
        data.put(FieldsRequestName.userName, this.user.getUsername());
        String requestData = GsonConfiguration.gson.toJson(data, CommunicationTypes.mapJsonTypeData);
        Request request = new Request(NetCodes.LIST_OF_UN_JOINED_CHANNELS, requestData);
        ByteBuffer buffer = ByteBuffer.wrap(GsonConfiguration.gson.toJson(request).getBytes());
        this.client.write(buffer, buffer, new ClientWriterCompletionHandler());
    }

    public void createChannel(String channelName, boolean isPublic) {
        Channel data = new Channel(this.user, channelName, "", isPublic);
        String requestData = GsonConfiguration.gson.toJson(data);
        Request request = new Request(NetCodes.CREATE_CHANNEL, requestData);
        ByteBuffer buffer = ByteBuffer.wrap(GsonConfiguration.gson.toJson(request).getBytes());
        this.client.write(buffer, buffer, new ClientWriterCompletionHandler());
    }

    public void deleteChannel(String channelName) {
        if (this.user.getChannelByName(channelName).getAdmin().getUsername().equalsIgnoreCase(this.user.getUsername())) {
            Map<String, String> data = new HashMap<>();
            data.put(FieldsRequestName.channelName, channelName);
            data.put(FieldsRequestName.userName, this.user.getUsername());
            String requestData = GsonConfiguration.gson.toJson(data, CommunicationTypes.mapJsonTypeData);
            Request request = new Request(NetCodes.DELETE_CHANNEL, requestData);
            ByteBuffer buffer = ByteBuffer.wrap(GsonConfiguration.gson.toJson(request).getBytes());
            this.client.write(buffer, buffer, new ClientWriterCompletionHandler());
        } else {
            this.controller.commandFailed(FailureMessages.deleteChannelNotAdminTitle,
                    FailureMessages.deleteChannelNotAdminMessage);
        }
    }

    public void deleteUser(String channelName, String userName) {
        String adminUsername = this.user.getChannelByName(channelName).getAdmin().getUsername();
        if (adminUsername.equalsIgnoreCase(this.user.getUsername())) {
            if (!adminUsername.equalsIgnoreCase(userName)) {
                Map<String, String> data = new HashMap<>();
                data.put(FieldsRequestName.channelName, channelName);
                data.put(FieldsRequestName.userName, userName);
                data.put(FieldsRequestName.adminName, adminUsername);
                String requestData = GsonConfiguration.gson.toJson(data, CommunicationTypes.mapJsonTypeData);
                Request request = new Request(NetCodes.DELETE_USER_FROM_CHANNEL, requestData);
                ByteBuffer buffer = ByteBuffer.wrap(GsonConfiguration.gson.toJson(request).getBytes());
                this.client.write(buffer, buffer, new ClientWriterCompletionHandler());
            } else {
                this.controller.commandFailed(FailureMessages.deleteUserNotAdminTitle,
                        FailureMessages.deleteUserAdminMessage);
            }
        } else {
            this.controller.commandFailed(FailureMessages.deleteUserNotAdminTitle,
                    FailureMessages.deleteUserNotAdminMessage);
        }
    }

    public void leaveChannel(String channelName) {
        if (!this.user.getChannelByName(channelName).getAdmin().getUsername().equalsIgnoreCase(this.user.getUsername())) {
            Map<String, String> data = new HashMap<>();
            data.put(FieldsRequestName.channelName, channelName);
            data.put(FieldsRequestName.userName, this.user.getUsername());
            String requestData = GsonConfiguration.gson.toJson(data, CommunicationTypes.mapJsonTypeData);
            Request request = new Request(NetCodes.LEAVE_CHANNEL, requestData);
            ByteBuffer buffer = ByteBuffer.wrap(GsonConfiguration.gson.toJson(request).getBytes());
            this.client.write(buffer, buffer, new ClientWriterCompletionHandler());
        } else {
            this.controller.commandFailed(FailureMessages.leaveChannelAdminTitle,
                    FailureMessages.leaveChannelAdminMessage);
        }
    }

    public void getMessagesOfChannel(String channelName) {
        Channel channel = this.user.getChannelByName(channelName);
        List<Message> messages = channel.getMessages();
        if(messages.isEmpty()) {
            Map<String, String> data = new HashMap<>();
            data.put(FieldsRequestName.userName, this.user.getUsername());
            data.put(FieldsRequestName.channelName, channel.getChannelName());
            String requestData = GsonConfiguration.gson.toJson(data, CommunicationTypes.mapJsonTypeData);
            Request request = new Request(NetCodes.List_Of_MESSAGE_IN_CHANNEL, requestData);
            ByteBuffer buffer = ByteBuffer.wrap(GsonConfiguration.gson.toJson(request).getBytes());
            this.client.write(buffer, buffer, new ClientWriterCompletionHandler());
        }
        else{
            ((SlockController) this.controller).initListMessagesInChannel(channelName,messages);
        }
    }

    public void getUsersForChannel(String channelName) {
        List<User> users = this.user.getListOfUsersFromChannel(channelName);
        //if (users.isEmpty()) {
            Map<String, String> data = new HashMap<>();
            data.put(FieldsRequestName.userName, this.user.getUsername());
            data.put(FieldsRequestName.channelName, channelName);
            String requestData = GsonConfiguration.gson.toJson(data, CommunicationTypes.mapJsonTypeData);
            Request request = new Request(NetCodes.LIST_OF_USER_IN_CHANNEL, requestData);
            ByteBuffer buffer = ByteBuffer.wrap(GsonConfiguration.gson.toJson(request).getBytes());
            this.client.write(buffer, buffer, new ClientWriterCompletionHandler());
//        } else {
//            ((SlockController) this.controller).setJoinedUsersToChannel(users,channelName);
//        }
    }

    public void sendMessage(String messageContent, String channelName) {
        Message data = new Message(messageContent, new User(this.user.getUsername()), new Channel(channelName),
                LocalDateTime.now());
        String requestData = GsonConfiguration.gson.toJson(data);
        Request request = new Request(NetCodes.CONSUME_MESSAGE, requestData);
        ByteBuffer buffer = ByteBuffer.wrap(GsonConfiguration.gson.toJson(request).getBytes());
        this.client.write(buffer, buffer, new ClientWriterCompletionHandler());
    }

    public void modifyMessage(Message message, String newContent) {
        Map<String, String> data = new HashMap<>();
        data.put(FieldsRequestName.userName, this.user.getUsername());
        data.put(FieldsRequestName.messageContent, message.getContent());
        data.put(FieldsRequestName.messageID, String.valueOf(message.getId()));
        data.put(FieldsRequestName.newMessageContent, newContent);
        data.put(FieldsRequestName.channelName, message.getChannel().getChannelName());
        Request request = new Request(NetCodes.MODIFY_MESSAGE, GsonConfiguration.gson.toJson(data));
        ByteBuffer buffer = ByteBuffer.wrap(GsonConfiguration.gson.toJson(request).getBytes());
        this.client.write(buffer, buffer, new ClientWriterCompletionHandler());
    }

    public void deleteMessage(int idMessage,String channelName) {
        Map<String, String> data = new HashMap<>();
        data.put(FieldsRequestName.messageID, String.valueOf(idMessage));
        data.put(FieldsRequestName.userName, this.user.getUsername());
        data.put(FieldsRequestName.channelName, channelName);
        Request request = new Request(NetCodes.DELETE_MESSAGE, GsonConfiguration.gson.toJson(data));
        ByteBuffer buffer = ByteBuffer.wrap(GsonConfiguration.gson.toJson(request).getBytes());
        this.client.write(buffer, buffer, new ClientWriterCompletionHandler());
    }

    public void requestJoinChannel() {
        Map<String, String> data = new HashMap<>();
        data.put(FieldsRequestName.adminName, this.user.getUsername());
        String requestData = GsonConfiguration.gson.toJson(data);
        Request request = new Request(NetCodes.LIST_REQUEST_JOIN_CHANNEL, requestData);
        ByteBuffer buffer = ByteBuffer.wrap(GsonConfiguration.gson.toJson(request).getBytes());
        this.client.write(buffer, buffer, new ClientWriterCompletionHandler());
    }

    public void joinChannel(String channelName, String adminUsername) {
        Map<String, String> data = new HashMap<>();
        data.put(FieldsRequestName.userName, this.user.getUsername());
        data.put(FieldsRequestName.channelName, channelName);
        data.put(FieldsRequestName.adminName, adminUsername);
        String requestData = GsonConfiguration.gson.toJson(data);
        Request request = new Request(NetCodes.JOIN_CHANNEL, requestData);
        ByteBuffer buffer = ByteBuffer.wrap(GsonConfiguration.gson.toJson(request).getBytes());
        this.client.write(buffer, buffer, new ClientWriterCompletionHandler());
    }

    public void responseRequestJoinChannel(String channelName, String username, String status) {
        Map<String, String> data = new HashMap<>();
        data.put(FieldsRequestName.adminName, this.user.getUsername());
        data.put(FieldsRequestName.channelName, channelName);
        data.put(FieldsRequestName.userName, username);
        data.put(FieldsRequestName.accept, status);
        String requestData = GsonConfiguration.gson.toJson(data);
        Request request = new Request(NetCodes.RESPONSE_JOIN_CHANNEL, requestData);
        ByteBuffer buffer = ByteBuffer.wrap(GsonConfiguration.gson.toJson(request).getBytes());
        this.client.write(buffer, buffer, new ClientWriterCompletionHandler());
    }

    // Functions that don't do sql requests :

    public void setAsynchronousSocketChannel(AsynchronousSocketChannel client) {
        this.client = client;
    }

    public void setController(Controller controller) {
        this.controller = controller;
    }

    public void setRqController(RequestToJoinChannelController rqController) {
        this.rqController = rqController;
    }

    public void setUcController(UnJoinedChannelsController ucController) {
        this.ucController = ucController;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public AsynchronousSocketChannel getClient() {
        return client;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }


}
