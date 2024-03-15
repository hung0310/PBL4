package ConnectDB;

import java.sql.Statement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ConnectSQL {
	public static Statement stm;
	public static Connection cnn;	
	public static String URLString;
	public static PreparedStatement stmt;
	public static ResultSet resultSet;
	
	static {
		//URLString = "jdbc:sqlserver://192.168.1.56:1433;databaseName=OnlineTeaching;user=sa;password=123456789;encrypt=false";	
		URLString = "jdbc:sqlserver://DRTEN\\MASTERMOS:1433;databaseName=OnlineTeaching;integratedSecurity=true;encrypt=false";
	}
	
    public static Connection getConnection() {
        try {
            cnn = DriverManager.getConnection(URLString);
            return cnn;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
	
	public static Boolean ConnectToSQL() {
		try {
			cnn = DriverManager.getConnection(URLString);
			stm = cnn.createStatement();
			return true;
		} catch (SQLException e) {
			return false;
		}				
	}
}
