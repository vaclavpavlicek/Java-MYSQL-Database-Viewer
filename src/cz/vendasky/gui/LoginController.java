package cz.vendasky.gui;

import cz.vendasky.MySQLConnection;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class LoginController implements Initializable {

    @FXML
    private TextField hostnameField;
    @FXML
    private TextField portField;
    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Label messageLabel;
    @FXML
    private Button cancelButton;
    @FXML
    private Button loginButton;

    @FXML
    private void cancelButtonAction() {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void loginButtonAction(ActionEvent e) throws SQLException, ClassNotFoundException, IOException {
        messageLabel.setText("");
        if (hostnameField.getText().isEmpty()) {
            messageLabel.setText("Location field cannot be empty.");
            return;
        }
        if (portField.getText().isEmpty()) {
            messageLabel.setText("Port field cannot be empty.");
            return;
        }
        if (usernameField.getText().isEmpty()) {
            messageLabel.setText("Username field cannot be empty.");
            return;
        }
        if (passwordField.getText().isEmpty()) {
            messageLabel.setText("Password field cannot be empty.");
        }
        MySQLConnection.connect(hostnameField.getText(), portField.getText(), usernameField.getText(), passwordField.getText());
        ((Node) (e.getSource())).getScene().getWindow().hide();

        Parent root = FXMLLoader.load(getClass().getResource("databases.fxml"));

        Stage stage = new Stage();
        Scene scene = new Scene(root);

        stage.setScene(scene);

        stage.setTitle("Choose database");

        stage.show();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
