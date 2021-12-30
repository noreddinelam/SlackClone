package client;

import front.controllers.AuthController;
import front.controllers.SlockController;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import models.Channel;
import models.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import shared.CommunicationTypes;
import shared.FieldsRequestName;
import shared.communication.Response;
import shared.gson_configuration.GsonConfiguration;

import java.util.List;
import java.util.Map;

public class GraphicalClientImpl extends ClientImpl{
    private static GraphicalClientImpl instance = new GraphicalClientImpl();
    private static final Logger logger = LoggerFactory.getLogger(GraphicalClientImpl.class);

    private GraphicalClientImpl(){}

    public static GraphicalClientImpl getUniqueInstanceOfGraphicalClientImpl(){
        return instance;
    }

    @Override
    public void connectSucceeded(String responseData) {
        try {
            this.user = GsonConfiguration.gson.fromJson(responseData, User.class);
            logger.info("[Graphic] Login succeeded {}",user);
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
            logger.info("[Graphic] Register succeeded {}",user);
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
            Map<String,String> response = GsonConfiguration.gson.fromJson(responseData, CommunicationTypes.mapJsonTypeData);
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
    public void listOfJoinedChannelsSucceeded(String responseData) {
        Map<String, List<Channel>> listOfChannels = GsonConfiguration.gson.fromJson(responseData, CommunicationTypes.mapListChannelJsonTypeData);
        List<Channel> channels = listOfChannels.get(FieldsRequestName.listChannels);
        this.user.setChannels(channels);
        System.out.println("lksdhfklqdshfqljsdhfqjldmshfqlmksdfh");
        ((SlockController) this.controller).initListJoinedChannels(channels);
    }

    @Override
    public void listOfJoinedChannelsFailed(String responseData) {

    }

    @Override
    public void listOfUnJoinedChannelsSucceeded(String responseData) {

    }

    @Override
    public void listOfUnJoinedChannelsFailed(String responseData) {

    }

    @Override
    public void listOfUserInChannelSucceeded(String responseData) {

    }

    @Override
    public void listOfUserInChannelFailed(String responseData) {

    }

    @Override
    public void messageConsumed(String responseData) {

    }

    @Override
    public void messageConsumptionError(String responseData) {

    }

    @Override
    public void messageBroadcastSucceed(String responseData) {

    }

    @Override
    public void messageBroadcastFailed(String responseData) {

    }
}
