package front.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class AuthController extends Controller {

    @FXML
    private PasswordField password;

    @FXML
    private TextField username;

    @FXML
    void onLogin(ActionEvent event) {
        this.clientImpl.login(username.getText(),password.getText());
    }

    @FXML
    void onRegister(ActionEvent event) {
        this.clientImpl.register(username.getText(),password.getText());
    }

    public void authSucceeded() throws Exception{
        this.scene.setRoot(new FXMLLoader(getClass().getResource("/front/ressources/vue.fxml")).load());
    }

}
