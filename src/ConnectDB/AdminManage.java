package ConnectDB;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class AdminManage {
	public static ConnectSQL connect;
	public static Connection connection;
	public static PreparedStatement stmt;
	public static ResultSet resultSet;
	
	public AdminManage() {}
	
	public AdminManage(ConnectSQL connect) {
		this.connect = connect;
	}
	
	static {
		connection = connect.getConnection();
	}

	public static void InsertUserAndAccount(String id_user, String email, String password, String phone_number, boolean gender, String role, String name_user) {
		try {
			String query_user = "INSERT INTO TB_User(ID_User, Name_user, PhoneNumber_user, Gender) VALUES (?, ?, ?, ?)";
			stmt = connection.prepareStatement(query_user);
			stmt.setString(1, id_user);
			stmt.setString(2, name_user);
			stmt.setString(3, phone_number);
			stmt.setBoolean(4, gender);
			stmt.executeUpdate();	
		} catch(Exception e) {}
		
		try {
			String query_account = "INSERT INTO Account(Email, Password, Role, ID_User) VALUES (?, ?, ?, ?)";
			stmt = connection.prepareStatement(query_account);
			stmt.setString(1, email);
			stmt.setString(2, password);
			stmt.setString(3, role);
			stmt.setString(4, id_user);
			stmt.executeUpdate();
		} catch(Exception e) {}
	}
	
	public static int getQuantityFeedback() {
		int count = 0;
		try {
	        String query = "SELECT COUNT(*) FROM Feedback";
	        stmt = connection.prepareStatement(query);
	        resultSet = stmt.executeQuery();
	        resultSet.next();
	        count = resultSet.getInt(1);
		} catch(Exception e) {}
		return count;
	}
	
	public static List<String> getFeedback() {
		List<String> list = new ArrayList<>();
	    try {
	        String query = "SELECT Email FROM Feedback";
	        stmt = connection.prepareStatement(query);
	        resultSet = stmt.executeQuery();    
	        while (resultSet.next()) {
	            String email = resultSet.getString("Email");
	            list.add(email);
	        }
	    } catch (Exception e) {}
		return list;
	}
	
	public static void DeleteFeedback(String email) {
	    try {
	        String query = "DELETE FROM Feedback WHERE Email = ?";
	        stmt = connection.prepareStatement(query);
			stmt.setString(1, email);
	        stmt.executeUpdate();
	    } catch(Exception e) {}
	}
	
	public static String getID_By_Email(String email) {
		String id = "";
        try {
            String query = "SELECT a.ID_User " +
                           "FROM Account a " +
                           "INNER JOIN Feedback f ON a.Email = f.Email " +
                           "WHERE a.Email = ?";
	        stmt = connection.prepareStatement(query);
			stmt.setString(1, email);
	        resultSet = stmt.executeQuery(); 
            while (resultSet.next()) {
            	id = resultSet.getString("ID_User");
            }
        } catch (Exception e) {}
		return id;
	}
	
	public static void Update_Password(String email, String password) {
	    try {
	        String query = "UPDATE Account SET Password = ? WHERE Email = ?";
	        stmt = connection.prepareStatement(query);
	        stmt.setString(1, password);
	        stmt.setString(2, email);
	        stmt.executeUpdate();
	    } catch(Exception e) {}
	}
}









