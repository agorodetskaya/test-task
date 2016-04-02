package refactor_adressbook.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Factory for connections to database defined in properties. Used DriverManager to get connection with given credentials.
 */
@Component
public class ConnectionFactory {
    private String url;
    private String login;
    private String password;


    @Autowired
    public ConnectionFactory(ResourcesUtil resourcesUtil) {
        try {
            Class.forName("org.h2.Driver");
            Properties properties = resourcesUtil.loadPropertiesFromResources("connection.properties");
            url = properties.getProperty("url");
            login = properties.getProperty("login");
            password = properties.getProperty("password");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("DB Driver not found");
        } catch (IOException e) {
            throw new RuntimeException("Unable to load properties");
        }
    }

    /**
     * Get a database connection
     *
     * @return a connection to the URL
     * @throws SQLException if a database access error occurs
     */
    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, login, password);
    }
}
