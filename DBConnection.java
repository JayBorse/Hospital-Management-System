import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection("jdbc:mysql://localhost:3306/hospital_db", "root", "12345678");
        } catch (Exception e) {
            throw new SQLException("Error in database connection: " + e.getMessage());
        }
    }
}