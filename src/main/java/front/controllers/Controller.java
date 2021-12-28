package front.controllers;

import client.ClientImpl;
import client.GraphicalClientImpl;
import javafx.fxml.Initializable;

import java.net.URL;
import java.nio.channels.AsynchronousSocketChannel;
import java.util.ResourceBundle;

public abstract class Controller implements Initializable {
    protected ClientImpl clientImpl;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.clientImpl = GraphicalClientImpl.getUniqueInstanceOfGraphicalClientImpl();
    }

    public void setModelData(AsynchronousSocketChannel client,String ipAddress){
        this.clientImpl.setAsynchronousSocketChannel(client);
        this.clientImpl.setIpAddress(ipAddress);
        this.clientImpl.initThreadReader();
        this.clientImpl.initListOfFunctions();
    }
}
