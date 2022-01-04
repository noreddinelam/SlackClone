package front;

import client.GraphicalClientImpl;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
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
        SlockController controller=loader.getController();
        controller.scene=this.scene;
        this.scene.setRoot(root);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.clientImpl = GraphicalClientImpl.getUniqueInstanceOfGraphicalClientImpl();
    }
}
