package cz.vendasky.gui;

import cz.vendasky.Model;
import cz.vendasky.MySQLConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;
import javafx.util.Callback;

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

    static class TableCell extends ListCell<String> {
        HBox hbox = new HBox();
        Label label = new Label("");
        Pane pane = new Pane();
        Button button = new Button("*");
        String lastItem;
        Model model;

        public TableCell(Model model) {
            super();
            hbox.getChildren().addAll(label, pane, button);
            HBox.setHgrow(pane, Priority.ALWAYS);
            this.model = model;
            button.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    try {
                        model.setSelectedTable(lastItem);
                        MySQLConnection instance = MySQLConnection.getInstance();
                        Connection connection = instance.getConnection();
                        Statement statement = connection.createStatement();
                        System.out.println("DESCRIBE " + model.getSelectedDatabase() + "." + model.getSelectedTable());
                        ResultSet rs = statement.executeQuery("DESCRIBE " + model.getSelectedDatabase() + "." + model.getSelectedTable());
                        while (rs.next()) {
                            System.out.println(rs.getString("field") + " " + rs.getString("type") + " " + rs.getString("null") + " " + rs.getString("key") + " " + rs.getString("default") + " " + rs.getString("extra"));
                        }

                    } catch (SQLException e) {
                        e.printStackTrace();
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                    System.out.println(lastItem + " : " + event);
                }
            });
        }

        @Override
        protected void updateItem(String item, boolean empty) {
            super.updateItem(item, empty);
            setText(null);  // No text in label of super class
            if (empty) {
                lastItem = null;
                setGraphic(null);
            } else {
                lastItem = item;
                label.setText(item!=null ? item : "<null>");
                setGraphic(hbox);
            }
        }
    }

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
            ObservableList<String> list = FXCollections.observableArrayList(tables);
            tablesListView.getItems().addAll(list);
            tablesListView.setCellFactory(new Callback<ListView<String>, ListCell<String>>() {
                @Override
                public ListCell<String> call(ListView<String> param) {
                    return new TableCell(model);
                }
            });
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

}
