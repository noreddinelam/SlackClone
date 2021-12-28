package client;

import front.controllers.AuthController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import shared.Properties;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.AsynchronousSocketChannel;
import java.util.concurrent.ExecutionException;

public class GUIClient extends Application {
    private final static InetSocketAddress serverIpAddress = new InetSocketAddress("localhost", Properties.PORT);
    private final static Logger logger = LoggerFactory.getLogger(Client.class);
    private static String clientIpAddress = "";
    private static AsynchronousSocketChannel client;

    public static void main(String[] args) throws IOException, ExecutionException, InterruptedException {
        AsynchronousSocketChannel socket = AsynchronousSocketChannel.open();
        socket.connect(serverIpAddress).get();
        String[] ipParts = socket.getLocalAddress().toString().split(":");
        clientIpAddress = ipParts[ipParts.length - 1];
        client = socket;
        logger.info("Connexion is done !");
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/front/ressources/loginregisterpage.fxml"));
        Parent root = (Parent) loader.load();
        AuthController authController = loader.getController();
        authController.setModelData(client, clientIpAddress);
        primaryStage.setTitle("Slock");
        primaryStage.setScene(new Scene(root));
        //primaryStage.initStyle(StageStyle.UNDECORATED);
        primaryStage.show();
    }
}
