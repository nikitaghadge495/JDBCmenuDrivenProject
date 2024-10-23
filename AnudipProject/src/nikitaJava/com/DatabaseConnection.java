package nikitaJava.com;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/DiagnosticBookingDB";
    private static final String USER = "root"; // replace with your MySQL username
    private static final String PASSWORD = "Nikita@29"; // replace with your MySQL password

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
