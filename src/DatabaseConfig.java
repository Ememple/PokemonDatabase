import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DatabaseConfig {

    private static DatabaseConfig instance;
    private Connection connection;
    private final Properties configProps = new Properties();

    private DatabaseConfig() {
        try (FileInputStream fis = new FileInputStream("res/config.properties");
             InputStreamReader reader = new InputStreamReader(fis, StandardCharsets.UTF_8)) {
            configProps.load(reader);
        }
        catch (IOException e) {
            System.err.println("Couldn't find config.properties: " + e.getMessage());
        }
    }

    public static synchronized DatabaseConfig getInstance() {
        if (instance == null) {
            instance = new DatabaseConfig();
        }
        return instance;
    }

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