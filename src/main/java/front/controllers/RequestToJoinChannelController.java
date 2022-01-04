package front;

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
    private String selectedRequest;
    @FXML
    private ListView<String> requestsToJoinChannel;

    @FXML
    void onAccept(ActionEvent event) {
        if (this.selectedRequest != null) {
            String[] data = this.selectedRequest.split("-");
            this.clientImpl.responseRequestJoinChannel(data[0].trim(), data[1].trim(), "true");
        }

    }

    @FXML
    void onRefuse(ActionEvent event) {
        if (this.selectedRequest != null) {
            String[] data = this.selectedRequest.split("-");
            this.clientImpl.responseRequestJoinChannel(data[0].trim(), data[1].trim(), "false");
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.clientImpl = GraphicalClientImpl.getUniqueInstanceOfGraphicalClientImpl();
        this.clientImpl.setRqController(this);
        this.clientImpl.requestJoinChannel();
        this.requestsToJoinChannel.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            this.selectedRequest = newValue;

        });
    }

    public void initRequestToJoinChannelListView(List<Map<String, String>> request) {
        Platform.runLater(() -> {
            request.forEach((e) -> {
                this.requestsToJoinChannel.getItems().add(e.get(FieldsRequestName.channelName) + " - " + e.get(FieldsRequestName.userName));
            });
        });
    }

    public void removeRequestChannel(Map<String, String> request) {
        Platform.runLater(() -> {
            this.requestsToJoinChannel.getItems().remove(request.get(FieldsRequestName.channelName) + " - " + request.get(FieldsRequestName.userName));
        });
    }
}
