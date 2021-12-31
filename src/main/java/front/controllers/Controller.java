package front.controllers;

import client.ClientImpl;
import client.GraphicalClientImpl;
import javafx.application.Platform;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;

import java.net.URL;
import java.nio.channels.AsynchronousSocketChannel;
import java.util.ResourceBundle;

public abstract class Controller implements Initializable {
    protected ClientImpl clientImpl;
    protected Scene scene;

    public void setData(AsynchronousSocketChannel client, String ipAddress, Scene scene){
        this.clientImpl.setAsynchronousSocketChannel(client);
        this.clientImpl.setIpAddress(ipAddress);
        this.clientImpl.initThreadReader();
        this.clientImpl.initListOfFunctions();
        this.clientImpl.setController(this);
        this.scene = scene;
    }

    public void commandFailed(String title, String failureMessage) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Authentication Failed");
            alert.setContentText(failureMessage);
            alert.showAndWait();
        });
    }

}
