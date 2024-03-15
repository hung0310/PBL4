package ConnectDB;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class Login {
	public static ConnectSQL connect;
	public static Connection connection;
	public static PreparedStatement stmt;
	public static ResultSet resultSet;
	
	public Login() {}
	
	public Login(ConnectSQL connect) {
		this.connect = connect;
	}
	
	static {
		connection = connect.getConnection();
	}
	
	public static String Login(String email, String password) {
		String Role = "";
		if(connect.ConnectToSQL()) {
			try {
				String query = "SELECT * FROM Account WHERE Email = ? AND Password = ?";
				stmt = connection.prepareStatement(query);
				stmt.setString(1, email);
				stmt.setString(2, password);
				
				resultSet = stmt.executeQuery();
				
				if (resultSet.next()) {
					Role = resultSet.getString("Role");
					System.out.println(Role);
				}
			} catch(Exception e) {}	
		}
		return Role;
	}
	public static String GetNameUser(String txtEmail) {
	    String nameuser = "";
	    try {
	        String id;
	        String query_id = "SELECT ID_User FROM Account WHERE Email = ?";
	        stmt = connection.prepareStatement(query_id);
	        stmt.setString(1, txtEmail);
	        resultSet = stmt.executeQuery();
	        if (resultSet.next()) {
	            id = resultSet.getString("ID_User");
	            
	            String query_name = "SELECT Name_user FROM TB_User WHERE ID_User = ?";
	            stmt = connection.prepareStatement(query_name); // Sửa từ query_id thành query_name
	            stmt.setString(1, id);
	            resultSet = stmt.executeQuery();
	            if (resultSet.next()) {
	                nameuser = resultSet.getString("Name_user");
	            }
	        }
	    } catch(Exception e) {
	        e.printStackTrace();
	    }
	    return nameuser;
	}
	public static String getID_User(String txtEmail) {
        String id = "";
	    try {
	        String query_id = "SELECT ID_User FROM Account WHERE Email = ?";
	        stmt = connection.prepareStatement(query_id);
	        stmt.setString(1, txtEmail);
	        resultSet = stmt.executeQuery();
	        if (resultSet.next()) {
	            id = resultSet.getString("ID_User");
	        }
	    } catch(Exception e) {
	        e.printStackTrace();
	    }
	    return id;
	}
	
	public void ForgetPassword(String email) {
		try {
			String query = "INSERT INTO Feedback(Email) VALUES (?)";
			stmt = connection.prepareStatement(query);
			stmt.setString(1, email);
			stmt.executeUpdate();	
		} catch(Exception e) {}
	}
}
