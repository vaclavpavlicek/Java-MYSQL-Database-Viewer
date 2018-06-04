package cz.vendasky.gui;

import cz.vendasky.Model;
import cz.vendasky.MySQLConnection;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class DatabasesController implements Initializable {

    @FXML
    private ListView databasesListView;

    @FXML
    public void itemClick(MouseEvent mouseEvent) throws IOException {
        Model model = Model.getInstace();
        model.setSelectedDatabase(databasesListView.getSelectionModel().getSelectedItem().toString());
        ((Node) (mouseEvent.getSource())).getScene().getWindow().hide();

        Parent root = FXMLLoader.load(getClass().getResource("tables.fxml"));

        Stage stage = new Stage();
        Scene scene = new Scene(root);

        stage.setScene(scene);

        stage.setTitle("Choose table");

        stage.show();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            MySQLConnection instance = MySQLConnection.getInstance();
            Connection connection = instance.getConnection();
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("SHOW databases");
            List<String> databases = new ArrayList<String>();
            while (rs.next()) {
                databases.add(rs.getString("database"));
            }
            databasesListView.getItems().addAll(databases);
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

}
