import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Singleton class for managing MySQL database connection
 */
public class DatabaseConfig {

    private static DatabaseConfig instance;
    private Connection connection;
    private final Properties configProps = new Properties();

    /**
     * Loads the database config from properties file
     */
    private DatabaseConfig() {
        try (FileInputStream fis = new FileInputStream("res/config.properties");
             InputStreamReader reader = new InputStreamReader(fis, StandardCharsets.UTF_8)) {
            configProps.load(reader);
        }
        catch (IOException e) {
            System.err.println("Couldn't find config.properties: " + e.getMessage());
        }
    }

    /**
     * Returns the single instance of connection
     * @return Instance of connection
     */
    public static synchronized DatabaseConfig getInstance() {
        if (instance == null) {
            instance = new DatabaseConfig();
        }
        return instance;
    }

    /**
     * Establishes and returns a connection to the MySQL database using config
     * @return Link to connection
     * @throws SQLException If a database access error occurs
     */
    public Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            String url = "jdbc:mysql://" + configProps.getProperty("host") +":"+ configProps.getProperty("port")+ "/" + configProps.getProperty("dbname");
            connection = DriverManager.getConnection(
                    url,
                    configProps.getProperty("user"),
                    configProps.getProperty("password")
            );
        }
        return connection;
    }
}