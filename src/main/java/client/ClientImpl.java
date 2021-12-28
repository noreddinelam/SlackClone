package client;

import front.controllers.Controller;
import models.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import shared.CommunicationTypes;
import shared.FieldsRequestName;
import shared.NetCodes;
import shared.communication.Request;
import shared.communication.Response;
import shared.gson_configuration.GsonConfiguration;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.function.Consumer;

public abstract class ClientImpl {
    protected static final Hashtable<String, Consumer<String>> listOfFunctions = new Hashtable<>();
    protected User user;
    protected String ipAddress;
    protected AsynchronousSocketChannel client;
    protected Controller controller;
    private static final Logger logger = LoggerFactory.getLogger(ClientImpl.class);

    public static Consumer<String> getFunctionWithRequestCode(Response response) {
        return listOfFunctions.get(response.getNetCode());
    }

    public void initThreadReader(){
        Thread reader = new Thread(() -> {
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            try {
                while (client.isOpen()) {
                    int nb = client.read(buffer).get();
                    String jsonRes = new String(buffer.array()).substring(0, nb);
                    logger.info("The received response \n{}", jsonRes);
                    Response response = GsonConfiguration.gson.fromJson(jsonRes, Response.class);
                    ClientImpl.getFunctionWithRequestCode(response).accept(response.getResponse());
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        });
        reader.start();
    }

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

    public abstract void deleteMessageSucceeded(String responseData);

    public abstract void deleteMessageFailed(String responseData);

    public abstract void modifyMessageSucceeded(String responseData);

    public abstract void modifyMessageFailed(String responseData);

    public abstract void deleteChannelSucceeded(String responseData);

    public abstract void deleteChannelFailed(String responseData);

    public abstract void listChannelsInServerSucceeded(String responseData);

    public abstract void listChannelsInServerFailed(String responseData);

    public abstract void listOfUserInChannelSucceeded(String responseData);

    public abstract void listOfUserInChannelFailed(String responseData);

    public abstract void messageConsumed(String responseData);

    public abstract void messageConsumptionError(String responseData);

    public abstract void messageBroadcastSucceed(String responseData);

    public abstract void messageBroadcastFailed(String responseData);

    public void initListOfFunctions() {

        listOfFunctions.put(NetCodes.CONNECT_SUCCEED,this::connectSucceeded);
        listOfFunctions.put(NetCodes.CONNECT_FAILED,this::connectFailed);

        listOfFunctions.put(NetCodes.REGISTER_SUCCEED,this::registerSucceeded);
        listOfFunctions.put(NetCodes.REGISTER_FAILED,this::registerFailed);

        listOfFunctions.put(NetCodes.CREATE_CHANNEL_SUCCEED, this::createChannelSucceeded);
        listOfFunctions.put(NetCodes.CREATE_CHANNEL_FAILED, this::createChannelFailed);

        listOfFunctions.put(NetCodes.JOIN_CHANNEL_SUCCEED, this::joinChannelSucceed);
        listOfFunctions.put(NetCodes.JOIN_CHANNEL_FAILED, this::joinChannelFailed);
        listOfFunctions.put(NetCodes.JOIN_CHANNEL_BROADCAST_SUCCEED, this::joinChannelBroadcastSucceeded);
        listOfFunctions.put(NetCodes.JOIN_CHANNEL_BROADCAST_FAILED, this::joinChannelBroadcastFailed);

        listOfFunctions.put(NetCodes.DELETE_MESSAGE_SUCCEED, this::deleteMessageSucceeded);
        listOfFunctions.put(NetCodes.DELETE_MESSAGE_FAILED, this::deleteMessageFailed);

        listOfFunctions.put(NetCodes.MODIFY_MESSAGE_SUCCEED, this::modifyMessageSucceeded);
        listOfFunctions.put(NetCodes.MODIFY_MESSAGE_FAILED, this::modifyMessageFailed);

        listOfFunctions.put(NetCodes.DELETE_CHANNEL_SUCCEED, this::deleteChannelSucceeded);
        listOfFunctions.put(NetCodes.DELETE_CHANNEL_FAILED, this::deleteChannelFailed);

        listOfFunctions.put(NetCodes.LIST_CHANNELS_IN_SERVER_SUCCEED, this::listChannelsInServerSucceeded);
        listOfFunctions.put(NetCodes.LIST_CHANNELS_IN_SERVER_FAILED, this::listChannelsInServerFailed);

        listOfFunctions.put(NetCodes.LIST_OF_USER_IN_CHANNEL_SUCCEED, this::listOfUserInChannelSucceeded);
        listOfFunctions.put(NetCodes.LIST_OF_USER_IN_CHANNEL_FAILED, this::listOfUserInChannelFailed);

        listOfFunctions.put(NetCodes.MESSAGE_CONSUMED, this::messageConsumed);
        listOfFunctions.put(NetCodes.MESSAGE_CONSUMPTION_ERROR, this::messageConsumptionError);
        listOfFunctions.put(NetCodes.MESSAGE_BROADCAST_SUCCEED, this::messageBroadcastSucceed);
        listOfFunctions.put(NetCodes.MESSAGE_BROADCAST_FAILED, this::messageBroadcastFailed);
    }

    public void login(String username,String password){
        Map<String,String> data = new HashMap<>();
        data.put(FieldsRequestName.userName,username);
        data.put(FieldsRequestName.password,password);
        data.put(FieldsRequestName.guest,this.ipAddress);
        String requestData = GsonConfiguration.gson.toJson(data, CommunicationTypes.mapJsonTypeData);
        Request request = new Request(NetCodes.CONNECTION,requestData);
        ByteBuffer buffer = ByteBuffer.wrap(GsonConfiguration.gson.toJson(request).getBytes());
        this.client.write(buffer,buffer,new ClientWriterCompletionHandler());
    }

    public void register(String username,String password){
        Map<String,String> data = new HashMap<>();
        data.put(FieldsRequestName.userName,username);
        data.put(FieldsRequestName.password,password);
        data.put(FieldsRequestName.guest,this.ipAddress);
        String requestData = GsonConfiguration.gson.toJson(data, CommunicationTypes.mapJsonTypeData);
        Request request = new Request(NetCodes.REGISTER,requestData);
        ByteBuffer buffer = ByteBuffer.wrap(GsonConfiguration.gson.toJson(request).getBytes());
        this.client.write(buffer,buffer,new ClientWriterCompletionHandler());
    }

    public void setAsynchronousSocketChannel(AsynchronousSocketChannel client) {
        this.client = client;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setController(Controller controller) {
        this.controller = controller;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public AsynchronousSocketChannel getClient() {
        return client;
    }
}
