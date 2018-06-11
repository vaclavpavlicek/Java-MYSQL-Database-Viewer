package cz.vendasky.gui;

import cz.vendasky.Model;
import cz.vendasky.MySQLConnection;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
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

public class TableDescribeController implements Initializable {

    @FXML
    private TableView describeTableView;

    @FXML
    public void goBack(ActionEvent actionEvent) throws IOException {
        Model model = Model.getInstace();
        model.setSelectedTable("");
        ((Node) (actionEvent.getSource())).getScene().getWindow().hide();

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
            loadRowsToTable(statement);
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void loadRowsToTable(Statement statement) throws SQLException {
        Model model = Model.getInstace();
        List<String> columns = new ArrayList<>();
        columns.add("field");
        columns.add("type");
        columns.add("null");
        columns.add("key");
        columns.add("default");
        columns.add("extra");
        for (int i = 0; i < columns.size(); i++) {
            TableColumn tableColumn = new TableColumn<List<String>, String>(columns.get(i));
            int finalI = i;
            tableColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<List<String>, String>, ObservableValue<String>>() {
                @Override
                public ObservableValue<String> call(TableColumn.CellDataFeatures<List<String>, String> data) {
                    return new ReadOnlyStringWrapper(data.getValue().get(finalI));
                }
            });
            describeTableView.getColumns().add(tableColumn);
        }

        final ObservableList<List<String>> data = FXCollections.observableArrayList();
        ResultSet rs = statement.executeQuery("DESCRIBE " + model.getSelectedDatabase() + "." + model.getSelectedTable());

        while (rs.next()) {
            List<String> cells = new ArrayList<String>();
            columns.forEach(column -> {
                try {
                    cells.add(rs.getString(column));
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            });
            data.add(cells);
        }
        describeTableView.getItems().addAll(data);
    }

}

