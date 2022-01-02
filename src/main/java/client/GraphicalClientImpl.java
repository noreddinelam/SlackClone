package client;

import front.Others.FailureMessages;
import front.controllers.AuthController;
import front.controllers.SlockController;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import models.Channel;
import models.Message;
import models.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import shared.CommunicationTypes;
import shared.FieldsRequestName;
import shared.gson_configuration.GsonConfiguration;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class GraphicalClientImpl extends ClientImpl {
    private static final Logger logger = LoggerFactory.getLogger(GraphicalClientImpl.class);
    private static GraphicalClientImpl instance = new GraphicalClientImpl();

    private GraphicalClientImpl() {
    }

    public static GraphicalClientImpl getUniqueInstanceOfGraphicalClientImpl() {
        return instance;
    }

    @Override
    public void logoutSucceeded(String responseData) {
        try {
            ((SlockController) this.controller).onLogoutSucceeded();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void connectSucceeded(String responseData) {
        try {
            this.user = GsonConfiguration.gson.fromJson(responseData, User.class);
            logger.info("[Graphic] Login succeeded {}", user);
            ((AuthController) this.controller).authSucceeded();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void connectFailed(String responseData) {
        this.controller.commandFailed(FailureMessages.authTitle, responseData);
    }

    @Override
    public void registerSucceeded(String responseData) {
        try {
            this.user = GsonConfiguration.gson.fromJson(responseData, User.class);
            logger.info("[Graphic] Register succeeded {}", user);
            ((AuthController) this.controller).authSucceeded();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void registerFailed(String responseData) {
        this.controller.commandFailed(FailureMessages.authTitle, responseData);
    }

    @Override
    public void createChannelSucceeded(String responseData) {
        try {
            Channel channel = GsonConfiguration.gson.fromJson(responseData, Channel.class);
            this.user.addChannel(channel);
            ((SlockController) this.controller).addChannelToListJoinedChannels(channel);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void createChannelFailed(String responseData) {
        this.controller.commandFailed(FailureMessages.channelCreationTitle, responseData);
    }

    @Override
    public void joinChannelSucceed(String responseData) {
        Channel channel = GsonConfiguration.gson.fromJson(responseData, Channel.class);
        this.user.addChannel(channel);
        ((SlockController) this.controller).addChannelToListJoinedChannels(channel);
        if (this.ucController != null) this.ucController.removeJoinedChannel(channel);
    }

    @Override
    public void joinChannelFailed(String responseData) {
        this.controller.commandFailed(FailureMessages.joinChannelTitle, responseData);
    }

    @Override
    public void joinChannelBroadcastSucceeded(String responseData) {
        Map<String, String> response = GsonConfiguration.gson.fromJson(responseData, CommunicationTypes.mapJsonTypeData);
        String username = response.get(FieldsRequestName.userName);
        String channelName = response.get(FieldsRequestName.channelName);
        User user = new User(username);
        this.user.addUserToChannel(channelName, user);
        ((SlockController) this.controller).addUserToJoinedUsersChannel(user);
    }

    @Override
    public void joinChannelBroadcastFailed(String responseData) {
        this.controller.commandFailed(FailureMessages.joinChannelBroadcastTitle, responseData);
    }

    @Override
    public void leaveChannelSucceeded(String responseData) {
        this.user.removeChannelByName(responseData); // responseData is channelName;
        ((SlockController) this.controller).removeChannelFromListJoinedChannels(responseData);
    }

    @Override
    public void leaveChannelFailed(String responseData) {
        this.controller.commandFailed(FailureMessages.leaveChannelTitle, responseData);
    }


    @Override
    public void deleteMessageSucceeded(String responseData) {

    }

    @Override
    public void deleteMessageFailed(String responseData) {
        this.controller.commandFailed(FailureMessages.deleteMessageTitle, responseData);
    }

    @Override
    public void deleteChannelBroadcastSucceeded(String responseData) {
        this.user.removeChannelByName(responseData);
        ((SlockController) this.controller).removeChannelFromListJoinedChannels(responseData);
    }

    @Override
    public void deleteChannelBroadcastFailed(String responseData) {
        this.controller.commandFailed(FailureMessages.deleteChannelBroadcastTitle,responseData);
    }

    @Override
    public void modifyMessageSucceeded(String responseData) {

    }

    @Override
    public void modifyMessageFailed(String responseData) {
        this.controller.commandFailed(FailureMessages.modifyMessageTitle, responseData);
    }

    @Override
    public void deleteChannelSucceeded(String responseData) {
        try {
            Map<String, String> response = GsonConfiguration.gson.fromJson(responseData,
                    CommunicationTypes.mapJsonTypeData);
            this.user.removeChannel(new Channel(response.get(FieldsRequestName.channelName)));
            ((SlockController) this.controller).deleteChannelToListJoinedChannels(response.get(FieldsRequestName.channelName));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void deleteChannelFailed(String responseData) {
        this.controller.commandFailed(FailureMessages.deleteChannelTitle, responseData);
    }

    @Override
    public void listChannelsInServerSucceeded(String responseData) {

    }

    @Override
    public void listChannelsInServerFailed(String responseData) {
        this.controller.commandFailed(FailureMessages.listChannelsInServerFailed, responseData);
    }

    @Override
    public void listOfMessageInChannelSucceeded(String responseData) {
        Map<String, List<Message>> responseMap = GsonConfiguration.gson.fromJson(responseData,
                CommunicationTypes.mapListMessageJsonTypeData);
        List<Message> listOfMessages = responseMap.get(FieldsRequestName.listMessages);
        if (!listOfMessages.isEmpty())
            this.user.addListOfMessagesToChannel(listOfMessages.get(0).getChannel().getChannelName(), listOfMessages);
    }

    @Override
    public void listOfMessageInChannelFailed(String responseData) {
        this.controller.commandFailed(FailureMessages.listOfMessageInChannelTitle, responseData);
    }

    @Override
    public void listOfJoinedChannelsSucceeded(String responseData) {
        Map<String, List<Channel>> listOfChannels = GsonConfiguration.gson.fromJson(responseData,
                CommunicationTypes.mapListChannelJsonTypeData);
        List<Channel> channels = listOfChannels.get(FieldsRequestName.listChannels);
        this.user.setChannels(channels);
        ((SlockController) this.controller).initListJoinedChannels(channels);
    }

    @Override
    public void listOfJoinedChannelsFailed(String responseData) {
        this.controller.commandFailed(FailureMessages.listOfMessageInChannelTitle, responseData);
    }

    @Override
    public void listOfUnJoinedChannelsSucceeded(String responseData) {
        Map<String, List<Channel>> listOfChannels = GsonConfiguration.gson.fromJson(responseData,
                CommunicationTypes.mapListChannelJsonTypeData);
        List<Channel> channels = listOfChannels.get(FieldsRequestName.listChannels);
        this.ucController.initRequestToJoinChannelListView(channels);
    }

    @Override
    public void listOfUnJoinedChannelsFailed(String responseData) {

    }

    @Override
    public void listOfUserInChannelSucceeded(String responseData) {
        Map<String, List<User>> listOfUsers = GsonConfiguration.gson.fromJson(responseData,
                CommunicationTypes.mapListUserJsonTypeData);
        ((SlockController) this.controller).setJoinedUsersToChannel(listOfUsers.get(FieldsRequestName.listUsers));
    }

    @Override
    public void listOfUserInChannelFailed(String responseData) {
        this.controller.commandFailed(FailureMessages.listOfUserInChannelTitle, responseData);
    }

    @Override
    public void listOfRequestsSucceeded(String responseData) {
        List<Map<String, String>> listOfRequests = GsonConfiguration.gson.fromJson(responseData,
                CommunicationTypes.listMapChannelUsernameTypeData);
        this.rqController.initRequestToJoinChannelListView(listOfRequests);

    }

    @Override
    public void listOfRequestsFailed(String responseData) {

    }

    @Override
    public void messageConsumed(String responseData) {
        Message message = GsonConfiguration.gson.fromJson(responseData, Message.class);
        this.user.addMessage(message);
        ((SlockController) this.controller).addMessageToListOfMessages(message);
    }

    @Override
    public void messageConsumptionError(String responseData) {
        this.controller.commandFailed(FailureMessages.messageConsumptionTitle, responseData);
    }

    @Override
    public void messageBroadcastSucceed(String responseData) {
        Message message = GsonConfiguration.gson.fromJson(responseData, Message.class);
        this.user.addMessage(message);
        ((SlockController) this.controller).addMessageToListOfMessages(message);
    }

    @Override
    public void messageBroadcastFailed(String responseData) {
        this.controller.commandFailed(FailureMessages.messageBroadcastTitle, responseData);
    }

    @Override
    public void joinPrivateChannel(String responseData) {
        //TODO pop up
    }

    @Override
    public void requestAlreadySent(String responseData) {
        //TODO pop up
    }

    @Override
    public void responseRequestJoinChannelSucceeded(String responseData) {
        //TODO pop up
        Map<String, String> request = GsonConfiguration.gson.fromJson(responseData, CommunicationTypes.mapJsonTypeData);
        this.rqController.removeRequestChannel(request);
    }

    @Override
    public void responseRequestJoinChannelFailed(String responseData) {
        //TODO pop up
    }
}
