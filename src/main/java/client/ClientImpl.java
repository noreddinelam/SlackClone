package client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import shared.NetCodes;
import shared.communication.Request;
import shared.communication.Response;

import java.util.Hashtable;
import java.util.function.Consumer;

public class ClientImpl {
    private static final Hashtable<String, Consumer<String>> listOfFunctions = new Hashtable<>();
    private static final Logger logger = LoggerFactory.getLogger(ClientImpl.class);

    private ClientImpl() {
    }

    public static void createChannelSucceeded(String responseData){}
    public static void createChannelFailed(String responseData){}

    public static void joinChannelSucceed(String responseData){}
    public static void joinChannelFailed(String responseData){}
    public static void joinChannelBroadcastSucceeded(String responseData){}
    public static void joinChannelBroadcastFailed(String responseData){}

    public static void deleteMessageSucceeded(String responseData){}
    public static void deleteMessageFailed(String responseData){}

    public static void modifyMessageSucceeded(String responseData){}
    public static void modifyMessageFailed(String responseData){}

    public static void deleteChannelSucceeded(String responseData){}
    public static void deleteChannelFailed(String responseData){}

    public static void listChannelsInServerSucceeded(String responseData){}
    public static void listChannelsInServerFailed(String responseData){}

    public static void listOfUserInChannelSucceeded(String responseData){}
    public static void listOfUserInChannelFailed(String responseData){}

    public static void messageConsumed(String responseData){}
    public static void messageConsumptionError(String responseData){}
    public static void messageBroadcastSucceed(String responseData){}
    public static void messageBroadcastFailed(String responseData){}

    public static void initListOfFunctions(){

        listOfFunctions.put(NetCodes.CREATE_CHANNEL_SUCCEED,ClientImpl::createChannelSucceeded);
        listOfFunctions.put(NetCodes.CREATE_CHANNEL_FAILED,ClientImpl::createChannelFailed);

        listOfFunctions.put(NetCodes.JOIN_CHANNEL_SUCCEED,ClientImpl::joinChannelSucceed);
        listOfFunctions.put(NetCodes.JOIN_CHANNEL_FAILED,ClientImpl::joinChannelFailed);
        listOfFunctions.put(NetCodes.JOIN_CHANNEL_BROADCAST_SUCCEED,ClientImpl::joinChannelBroadcastSucceeded);
        listOfFunctions.put(NetCodes.JOIN_CHANNEL_BROADCAST_FAILED,ClientImpl::joinChannelBroadcastFailed);

        listOfFunctions.put(NetCodes.DELETE_MESSAGE_SUCCEED,ClientImpl::deleteMessageSucceeded);
        listOfFunctions.put(NetCodes.DELETE_MESSAGE_FAILED,ClientImpl::deleteMessageFailed);

        listOfFunctions.put(NetCodes.MODIFY_MESSAGE_SUCCEED,ClientImpl::modifyMessageSucceeded);
        listOfFunctions.put(NetCodes.MODIFY_MESSAGE_FAILED,ClientImpl::modifyMessageFailed);

        listOfFunctions.put(NetCodes.DELETE_CHANNEL_SUCCEED,ClientImpl::deleteChannelSucceeded);
        listOfFunctions.put(NetCodes.DELETE_CHANNEL_FAILED,ClientImpl::deleteChannelFailed);

        listOfFunctions.put(NetCodes.LIST_CHANNELS_IN_SERVER_SUCCEED,ClientImpl::listChannelsInServerSucceeded);
        listOfFunctions.put(NetCodes.LIST_CHANNELS_IN_SERVER_FAILED,ClientImpl::listChannelsInServerFailed);

        listOfFunctions.put(NetCodes.LIST_OF_USER_IN_CHANNEL_SUCCEED,ClientImpl::listOfUserInChannelSucceeded);
        listOfFunctions.put(NetCodes.LIST_OF_USER_IN_CHANNEL_FAILED,ClientImpl::listOfUserInChannelFailed);

        listOfFunctions.put(NetCodes.MESSAGE_CONSUMED,ClientImpl::messageConsumed);
        listOfFunctions.put(NetCodes.MESSAGE_CONSUMPTION_ERROR,ClientImpl::messageConsumptionError);
        listOfFunctions.put(NetCodes.MESSAGE_BROADCAST_SUCCEED,ClientImpl::messageBroadcastSucceed);
        listOfFunctions.put(NetCodes.MESSAGE_BROADCAST_FAILED,ClientImpl::messageBroadcastFailed);

    }

    public static Consumer<String> getFunctionWithRequestCode(Response response) {
        return listOfFunctions.get(response.getNetCode());
    }

}
