package front.controllers;

import client.GraphicalClientImpl;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import models.Channel;
import models.Message;
import shared.FieldsRequestName;

import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

public class UnJoinedChannelsController extends Controller {

    private String selectedChannelName;
    @FXML
    private TextField joinChannelTextFailed;

    @FXML
    private ListView<String> unJoinedChannelList;

    @FXML
    void onJoin(ActionEvent event) {
        if (this.selectedChannelName != null) {
            String[] data = this.selectedChannelName.split("-");
            this.clientImpl.joinChannel(data[0].trim(), data[2].trim());
        }

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.clientImpl = GraphicalClientImpl.getUniqueInstanceOfGraphicalClientImpl();
        this.clientImpl.setUcController(this);
        this.clientImpl.listOfUnJoinedChannels();
        this.unJoinedChannelList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            this.selectedChannelName = newValue;

        });

    }

    public void initRequestToJoinChannelListView(List<Channel> request) {
        Platform.runLater(() -> {
            request.forEach((e) -> {
                this.unJoinedChannelList.getItems().add(e.getChannelName() + " - " + (e.isPublic() ? "Public" : "Private") + " - " + e.getAdmin().getUsername());
            });
        });
    }

    public void removeJoinedChannel(Channel channel) {
        Platform.runLater(() -> {
            this.unJoinedChannelList.getItems().remove(channel.getChannelName() + " - " + (channel.isPublic() ? "Public" : "Private") + " - " + channel.getAdmin().getUsername());
        });
    }


}
