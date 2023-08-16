import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DbConnectionFactory {

    private static final String dbUrl = "jdbc:postgresql://localhost:5432/hr";
    private static final String userName = "postgres";
    private static final String userPassword = "password";

    private DbConnectionFactory() {
    }

    public static Connection createConnection() throws SQLException {
        System.setProperty("jdbc.drivers", "org.postgresql.Driver");
        Connection connection = DriverManager.getConnection(dbUrl, userName, userPassword);
        System.out.println("Connection to DB set successfully!");
        return connection;
    }
}
