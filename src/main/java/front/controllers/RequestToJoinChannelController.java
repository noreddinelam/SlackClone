package front.controllers;

import client.GraphicalClientImpl;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;

import java.net.URL;
import java.util.ResourceBundle;

public class RequestToJoinChannelController extends Controller{

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
    }
}
