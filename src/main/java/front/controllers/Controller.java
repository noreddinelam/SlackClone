package front.controllers;

import client.ClientImpl;
import client.GraphicalClientImpl;
import javafx.fxml.Initializable;
import javafx.scene.Scene;

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
}
