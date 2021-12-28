package front.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.text.Text;

public class SlockController {

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

}
