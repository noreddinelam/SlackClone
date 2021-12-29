package front.controllers;

import client.GraphicalClientImpl;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.ResourceBundle;

public class SlockController extends Controller {

    @FXML
    private Text clientUsername;

    @FXML
    private TextField createChannelTextField;

    @FXML
    private ListView<?> listOfJoinedChannels;

    @FXML
    private ListView<?> listOfMessages;

    @FXML
    private TextField messageTextField;

    @FXML
    private ListView<?> usersListView;

    @FXML
    void onCreateChannel(ActionEvent event) {

    }

    @FXML
    void onDeleteCurrentChannel(ActionEvent event) {

    }

    @FXML
    void onKeyPressed(KeyEvent event) {

    }

    @FXML
    void onLeaveCurrentChannel(ActionEvent event) {
        System.out.println("jdghjkfhqsdkjfhqksjdfhklqjsdfh");

    }

    @FXML
    void onPrintAllChannels(ActionEvent event) {

    }

    @FXML
    void onPrintRequestsChannels(ActionEvent event) {

    }

    @FXML
    void onPrivateChannel(ActionEvent event) {

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.clientImpl = GraphicalClientImpl.getUniqueInstanceOfGraphicalClientImpl();
        this.clientUsername.setText(this.clientImpl.getUser().getUsername());
    }
}
