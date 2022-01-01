package client;

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
        ((AuthController) this.controller).authFailed(responseData);
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
        ((AuthController) this.controller).authFailed(responseData);
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

    }

    @Override
    public void joinChannelSucceed(String responseData) {

    }

    @Override
    public void joinChannelFailed(String responseData) {

    }

    @Override
    public void joinChannelBroadcastSucceeded(String responseData) {

    }

    @Override
    public void joinChannelBroadcastFailed(String responseData) {

    }

    @Override
    public void deleteMessageSucceeded(String responseData) {

    }

    @Override
    public void deleteMessageFailed(String responseData) {

    }

    @Override
    public void modifyMessageSucceeded(String responseData) {

    }

    @Override
    public void modifyMessageFailed(String responseData) {

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
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Authentication Failed");
            alert.setContentText("Deleting channel failed");
            alert.showAndWait();
        });
    }

    @Override
    public void listChannelsInServerSucceeded(String responseData) {

    }

    @Override
    public void listChannelsInServerFailed(String responseData) {

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

    }

    @Override
    public void listOfRequestsSucceeded(String responseData) {
        List<Map<String,String>> listOfRequests = GsonConfiguration.gson.fromJson(responseData,
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

    }

    @Override
    public void messageBroadcastSucceed(String responseData) {
        Message message = GsonConfiguration.gson.fromJson(responseData, Message.class);
        this.user.addMessage(message);
        ((SlockController) this.controller).addMessageToListOfMessages(message);
    }

    @Override
    public void messageBroadcastFailed(String responseData) {

    }

    @Override
    public void joinPrivateChannel(String responseData) {
        //TODO pop up
    }

    @Override
    public void requestAlreadySent(String responseData) {
        //TODO pop up
    }
}
