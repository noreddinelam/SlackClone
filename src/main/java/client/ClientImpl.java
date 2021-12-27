package client;

import shared.NetCodes;
import shared.communication.Response;

import java.util.Hashtable;
import java.util.function.Consumer;

public abstract class ClientImpl {
    protected static final Hashtable<String, Consumer<String>> listOfFunctions = new Hashtable<>();

    public static Consumer<String> getFunctionWithRequestCode(Response response) {
        return listOfFunctions.get(response.getNetCode());
    }

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

}
