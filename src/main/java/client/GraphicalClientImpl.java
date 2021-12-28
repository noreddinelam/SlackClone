package client;

import front.controllers.AuthController;
import models.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import shared.communication.Response;
import shared.gson_configuration.GsonConfiguration;

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

    }

    @Override
    public void registerSucceeded(String responseData) {

    }

    @Override
    public void registerFailed(String responseData) {

    }

    @Override
    public void createChannelSucceeded(String responseData) {

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

    }

    @Override
    public void deleteChannelFailed(String responseData) {

    }

    @Override
    public void listChannelsInServerSucceeded(String responseData) {

    }

    @Override
    public void listChannelsInServerFailed(String responseData) {

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
