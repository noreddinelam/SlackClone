package front.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class AuthController {

    @FXML
    private PasswordField password;

    @FXML
    private TextField username;

    @FXML
    void onLogin(ActionEvent event) {
        System.out.println("helllllllllllo");
    }

}
