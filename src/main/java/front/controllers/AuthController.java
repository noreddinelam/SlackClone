package front.controllers;

import client.GraphicalClientImpl;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class AuthController extends Controller {

    @FXML
    private PasswordField password;

    @FXML
    private TextField username;

    @FXML
    void onLogin(ActionEvent event) {
        this.clientImpl.login(username.getText(), password.getText());
    }

    @FXML
    void onRegister(ActionEvent event) {
        this.clientImpl.register(username.getText(), password.getText());
    }

    public void authSucceeded() throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/front/ressources/vue.fxml"));
        Parent root = loader.load();
        this.scene.setRoot(root);
    }

    public void authFailed(String failureMessage) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Authentication Failed");
            alert.setContentText(failureMessage);
            alert.showAndWait();
        });
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.clientImpl = GraphicalClientImpl.getUniqueInstanceOfGraphicalClientImpl();
    }
}
