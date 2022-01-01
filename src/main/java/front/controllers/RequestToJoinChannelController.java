package front.controllers;

import client.GraphicalClientImpl;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import shared.FieldsRequestName;

import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

public class RequestToJoinChannelController extends Controller {

    @FXML
    private ListView<String> requestsToJoinChannel;

    @FXML
    void onAccept(ActionEvent event) {

    }

    @FXML
    void onRefuse(ActionEvent event) {

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.clientImpl = GraphicalClientImpl.getUniqueInstanceOfGraphicalClientImpl();
        this.clientImpl.setRqController(this);
        this.clientImpl.requestJoinChannel();
    }

    public void initRequestToJoinChannelListView(List<Map<String, String>> request) {
        Platform.runLater(() -> {
            request.forEach((e) -> {
                this.requestsToJoinChannel.getItems().add(e.get(FieldsRequestName.channelName) + " | " + e.get(FieldsRequestName.userName));
            });
        });
    }

}
