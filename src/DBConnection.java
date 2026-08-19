import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

	// Pointing to out database 
	private static final String URL      = "jdbc:mysql://localhost:3306/hotel_system"; // change "hotel_reservation" to your database name
	private static final String USERNAME = "root";   
	private static final String PASSWORD = "";       // leave empty unless you change your password
	
	public static Connection getConnection() throws SQLException {
	    try {
	        Class.forName("com.mysql.cj.jdbc.Driver");
	        return DriverManager.getConnection(URL, USERNAME, PASSWORD);
	    } catch (ClassNotFoundException e) {
	        throw new SQLException("MySQL JDBC Driver not found. Check your build path!", e);
	        }
	}
	
	  
}
