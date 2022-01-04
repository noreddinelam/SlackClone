package front.controllers;

import client.GraphicalClientImpl;
import front.Others.FailureMessages;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import models.Channel;
import models.Message;
import models.User;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class SlockController extends Controller {

    private String selectedChannelName;
    private String adminUsername;
    private boolean isPrivateChannel;
    private Message selectedMessage;

    @FXML
    private Text clientUsername;

    @FXML
    private CheckBox isPrivate;

    @FXML
    private TextField createChannelTextField;

    @FXML
    private ListView<String> listOfJoinedChannels;

    @FXML
    private ListView<Message> listOfMessages;

    @FXML
    private TextField messageTextField;

    @FXML
    private ListView<User> usersListView;

    @FXML
    private CheckBox modifyIsPrivate;

    @FXML
    private TextField channelNameModTextField;

    @FXML
    private TextField modifyMessageTextField;

    @FXML
    private Button deleteMessageButton;

    @FXML
    private Button modifyChannelButton;

    @FXML
    private Button modifyMessageButton;

    @FXML
    void onCreateChannel(ActionEvent event) {
        if (!createChannelTextField.getText().isEmpty())
            this.clientImpl.createChannel(createChannelTextField.getText(), !isPrivate.isSelected());
        else this.commandFailed(FailureMessages.emptyTextFieldNameTitle, FailureMessages.emptyTextFieldMessage);
    }

    @FXML
    void OnLogout(ActionEvent event) throws IOException {
        this.clientImpl.logout();
    }


    @FXML
    void onDeleteCurrentChannel(ActionEvent event) {
        this.clientImpl.deleteChannel(this.selectedChannelName);
    }

    @FXML
    void onDeleteMessage(ActionEvent event) {

    }

    @FXML
    void onKeyPressed(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER && !this.messageTextField.getText().isEmpty() && this.selectedChannelName != null) {
            this.clientImpl.sendMessage(this.messageTextField.getText(), this.selectedChannelName);
        }
    }

    @FXML
    void onLeaveCurrentChannel(ActionEvent event) {
        this.clientImpl.leaveChannel(this.selectedChannelName);
    }

    @FXML
    void onPrintAllChannels(ActionEvent event) throws IOException {
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/front/ressources/display_channel_list.fxml"));
        Parent root = (Parent) loader.load();
        stage.setTitle("List of channels");
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    @FXML
    void onPrintRequestsChannels(ActionEvent event) throws IOException {
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/front/ressources/list_of_requests.fxml"));
        Parent root = (Parent) loader.load();
        stage.setTitle("List of channels");
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    @FXML
    void onPrivateChannel(ActionEvent event) {

    }

    //TODO : delete from fxml and here
    @FXML
    void onItemSelected(MouseEvent event) {

    }

    @FXML
    void onModifyChannel(ActionEvent event) {
        if (!this.channelNameModTextField.getText().isEmpty())
            this.clientImpl.modifyChannel(this.channelNameModTextField.getText().trim(),
                    !this.modifyIsPrivate.isSelected(), this.selectedChannelName);
        else this.commandFailed(FailureMessages.emptyTextFieldNameTitle, FailureMessages.emptyTextFieldMessage);
    }

    @FXML
    void onModifyMessage(ActionEvent event) {
        if (!this.modifyMessageTextField.getText().isEmpty())
            this.clientImpl.modifyMessage(this.selectedMessage, this.modifyMessageTextField.getText().trim());
        else
            this.commandFailed(FailureMessages.emptyMessageContentIsGivenTitle,
                    FailureMessages.emptyMessageContentIsGivenMessage);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.clientImpl = GraphicalClientImpl.getUniqueInstanceOfGraphicalClientImpl();
        this.clientImpl.setController(this);
        this.clientUsername.setText(this.clientImpl.getUser().getUsername());
        this.clientImpl.listOfJoinedChannels();
        this.initListViewListeners();
        this.modifyChannelButton.setDisable(true);
        this.deleteMessageButton.setDisable(true);
        this.modifyMessageButton.setDisable(true);
    }

    public void initListViewListeners() {
        this.listOfJoinedChannels.getSelectionModel().selectedItemProperty().addListener((observable, oldValue,
                                                                                          newValue) -> {
            if (newValue != null) {
                String[] parts = newValue.split("-");
                this.selectedChannelName = parts[0].trim();
                this.adminUsername = parts[1].trim();
                this.isPrivateChannel = !parts[2].trim().equalsIgnoreCase("Public");
                if (this.adminUsername.equalsIgnoreCase(this.clientUsername.getText())) {
                    this.channelNameModTextField.setText(this.selectedChannelName);
                    this.modifyIsPrivate.setSelected(this.isPrivateChannel);
                    this.modifyChannelButton.setDisable(false);
                } else {
                    this.channelNameModTextField.setText("");
                    this.modifyIsPrivate.setSelected(false);
                    this.modifyChannelButton.setDisable(true);
                }
                this.clientImpl.getUsersForChannel(this.selectedChannelName);
                Platform.runLater(() -> {
                    List<Message> messages = this.clientImpl.listOfMessagesInChannel(this.selectedChannelName);
                    this.listOfMessages.getItems().setAll(messages);
                });
            }
        });

        this.listOfMessages.getSelectionModel().selectedItemProperty().addListener((observable, oldValue,
                                                                                    newValue) -> {
            if (newValue != null) {
                if (newValue.getUser().getUsername().equalsIgnoreCase(this.clientUsername.getText())) {
                    this.modifyMessageTextField.setText(newValue.getContent());
                    this.modifyMessageButton.setDisable(false);
                    this.deleteMessageButton.setDisable(false);
                    this.selectedMessage = newValue;
                } else {
                    this.modifyMessageTextField.setText("");
                    this.modifyMessageButton.setDisable(true);
                    this.deleteMessageButton.setDisable(true);
                }
            }
        });
        // TODO : models in front to remove.
    }

    public void initListJoinedChannels(List<Channel> list) {
        List<String> channelsName =
                list.stream().map((channel) -> channel.getChannelName() + " - " + channel.getAdmin().getUsername() +
                        " - " + (channel.isPublic() ? "Public" : "Private")).collect(Collectors.toList());
        this.listOfJoinedChannels.getItems().addAll(channelsName);
        this.clientImpl.getAllMessages();
    }

    public void onLogoutSucceeded() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/front/ressources/loginregisterpage.fxml"));
        Parent root = loader.load();
        AuthController controller = loader.getController();
        this.clientImpl.setController(controller);
        controller.scene = this.scene;
        this.scene.setRoot(root);
    }

    public void addChannelToListJoinedChannels(Channel channel) {
        Platform.runLater(() -> {
            this.listOfJoinedChannels.getItems().add(channel.getChannelName() + " - " + channel.getAdmin().getUsername() +
                    " - " + (channel.isPublic() ? "Public" : "Private"));
        });
    }

    public void removeChannelFromListJoinedChannels(String channelName) {
        Platform.runLater(() -> {
            if (channelName.equals(this.selectedChannelName)) {
                this.listOfMessages.getItems().clear();
                this.usersListView.getItems().clear();
            }
            this.listOfJoinedChannels.getItems().remove(channelName);
        });
    }

    public void deleteChannelToListJoinedChannels(String channelName) {
        Platform.runLater(() -> {
            if (channelName.equals(this.selectedChannelName)) {
                this.listOfMessages.getItems().clear();
                this.usersListView.getItems().clear();
            }
            this.listOfJoinedChannels.getItems().remove(channelName);
        });
    }

    public void setJoinedUsersToChannel(List<User> users) {
        Platform.runLater(() -> {
            this.usersListView.getItems().setAll(users);
        });
    }

    public void addUserToJoinedUsersChannel(User user) {
        Platform.runLater(() -> {
            this.usersListView.getItems().add(user);
        });
    }

    public void addMessageToListOfMessages(Message message) {
        if (this.selectedChannelName != null && this.selectedChannelName.equalsIgnoreCase(message.getChannel().getChannelName())) {
            this.messageTextField.clear();
            Platform.runLater(() -> {
                this.listOfMessages.getItems().add(message);
            });
        }
    }

    public void modifyChannelInListJoinedChannels(String oldChannelName, String newChannelName, String isPublic) {
        Platform.runLater(() -> {
            ObservableList<String> temp = this.listOfJoinedChannels.getItems();
            int index = 0;
            String newItem = null;
            for (String listViewItem : temp) {
                if (listViewItem.split("-")[0].trim().equalsIgnoreCase(oldChannelName)) {
                    newItem = listViewItem;
                    break;
                }
                index++;
            }
            if (newItem != null) {
                this.selectedChannelName=newChannelName;
                String[] parts = newItem.split("-");
                this.listOfJoinedChannels.getItems().set(index,
                        newChannelName + " - " + parts[1] + " - " + (isPublic.equalsIgnoreCase("true") ? "Public" :
                                "Private"));
            }
        });
    }

    public void modifyMessageInListOfMessages(int idMessage, String messageContent, String channelName) {
        if (this.selectedChannelName.equalsIgnoreCase(channelName)) {
            ObservableList<Message> temp = this.listOfMessages.getItems();
            int index = 0;
            Message newItem = null;
            for (Message listViewItem : temp) {
                if (listViewItem.getId() == idMessage) {
                    newItem = listViewItem;
                    break;
                }
                index++;
            }
            if (newItem != null) {
                newItem.setContent(messageContent);
                this.listOfMessages.getItems().set(index, newItem);
            }
        }
    }

}
