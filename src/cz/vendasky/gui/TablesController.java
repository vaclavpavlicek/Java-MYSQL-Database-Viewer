package cz.vendasky.gui;

import cz.vendasky.Model;
import cz.vendasky.MySQLConnection;
import javafx.event.ActionEvent;
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

public class TablesController implements Initializable {

    @FXML
    private ListView tablesListView;

    @FXML
    public void goBack(ActionEvent actionEvent) throws IOException {
        Model model = Model.getInstace();
        model.setSelectedDatabase("");
        ((Node) (actionEvent.getSource())).getScene().getWindow().hide();

        Parent root = FXMLLoader.load(getClass().getResource("databases.fxml"));

        Stage stage = new Stage();
        Scene scene = new Scene(root);

        stage.setScene(scene);

        stage.setTitle("Choose database");

        stage.show();
    }

    @FXML
    public void itemClick(MouseEvent mouseEvent) throws IOException {
        Model model = Model.getInstace();
        model.setSelectedTable(tablesListView.getSelectionModel().getSelectedItem().toString());
        ((Node) (mouseEvent.getSource())).getScene().getWindow().hide();

        System.out.println(tablesListView.getSelectionModel().getSelectedItem().toString());

        Parent root = FXMLLoader.load(getClass().getResource("rows.fxml"));

        Stage stage = new Stage();
        Scene scene = new Scene(root);

        stage.setScene(scene);

        stage.setTitle("Table detail");

        stage.show();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            MySQLConnection instance = MySQLConnection.getInstance();
            Connection connection = instance.getConnection();
            Statement statement = connection.createStatement();
            Model model = Model.getInstace();
            ResultSet rs = statement.executeQuery("SELECT TABLE_NAME FROM INFORMATION_SCHEMA.TABLES WHERE table_schema = '"
                    + model.getSelectedDatabase() + "'");
            List<String> tables = new ArrayList<String>();
            while (rs.next()) {
                tables.add(rs.getString("table_name"));
            }
            System.out.println(tables);
            this.tablesListView.getItems().addAll(tables);
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

}
