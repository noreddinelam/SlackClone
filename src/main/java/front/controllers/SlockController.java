package front.controllers;

import client.GraphicalClientImpl;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import models.Channel;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class SlockController extends Controller {

    private String selectedChannelName;

    @FXML
    private Text clientUsername;

    @FXML
    private CheckBox isPrivate;

    @FXML
    private TextField createChannelTextField;

    @FXML
    private ListView<String> listOfJoinedChannels;

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
        System.out.println(event.getCode());
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

    @FXML
    void onItemSelected(MouseEvent event) {

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.clientImpl = GraphicalClientImpl.getUniqueInstanceOfGraphicalClientImpl();
        this.clientImpl.setController(this);
        this.clientUsername.setText(this.clientImpl.getUser().getUsername());
        this.clientImpl.listOfJoinedChannels();

        this.initListViewListeners();
    }

    public void initListViewListeners() {
        this.listOfJoinedChannels.getSelectionModel().selectedItemProperty().addListener((observable, oldValue,
                                                                                          newValue) -> {
            this.selectedChannelName = newValue;
        });
    }

    public void initListJoinedChannels(List<Channel> list) {
        List<String> channelsName = list.stream().map((c) -> c.getChannelName()).collect(Collectors.toList());
        this.listOfJoinedChannels.getItems().addAll(channelsName);
    }

    public void addChannelToListJoinedChannels(Channel channel) {
        this.listOfJoinedChannels.getItems().add(channel.getChannelName());
    }
}
