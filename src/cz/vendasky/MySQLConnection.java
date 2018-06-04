package cz.vendasky;

import java.sql.Connection;

import java.sql.DriverManager;
import java.sql.SQLException;

public class MySQLConnection implements AutoCloseable {

    private static MySQLConnection instance;
    private static String connectionStringTemplate = "jdbc:mysql://%s:%s?user=%s&password=%s";

    private MySQLConnection(String connectionString) throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.jdbc.Driver");
        connection = DriverManager.getConnection(connectionString);
        if (this.connection.isClosed()) {
            this.connection.isValid(10);
        }
    }

    public static void connect(String host, String port, String username, String password) throws SQLException, ClassNotFoundException {
        if (instance == null) {
            instance = new MySQLConnection(String.format(connectionStringTemplate, host, port, username, password));
        }
    }

    public static MySQLConnection getInstance() throws SQLException, ClassNotFoundException {
        if (instance == null) {
            instance = new MySQLConnection(connectionStringTemplate);
        }
        return instance;
    }

    private final Connection connection;

    public Connection getConnection() {
        return connection;
    }

    @Override
    public void close() throws Exception {
        if (connection.isClosed()) {
            connection.close();
        }
    }
}
