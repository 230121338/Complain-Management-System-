package tut.ac.za.complaint.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Central JDBC connection factory. Connection settings can be overridden with
 * the environment variables DB_URL, DB_USER and DB_PASSWORD so the same build
 * can run against different MySQL/MariaDB instances without code changes.
 */
public class DBConnection {

    private static final String DEFAULT_URL =
            "jdbc:mysql://localhost:3306/complaintdb?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
    private static final String DEFAULT_USER = "complaint";
    private static final String DEFAULT_PASSWORD = "complaint123";

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new ExceptionInInitializerError("MySQL JDBC driver not found: " + e.getMessage());
        }
    }

    private DBConnection() {
    }

    public static Connection getConnection() throws SQLException {
        String url = envOrDefault("DB_URL", DEFAULT_URL);
        String user = envOrDefault("DB_USER", DEFAULT_USER);
        String password = envOrDefault("DB_PASSWORD", DEFAULT_PASSWORD);
        return DriverManager.getConnection(url, user, password);
    }

    private static String envOrDefault(String name, String fallback) {
        String value = System.getenv(name);
        return (value == null || value.isBlank()) ? fallback : value;
    }
}
