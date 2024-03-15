package ConnectDB;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CreateGroup {
	public static ConnectSQL connect;
	public static Connection connection;
	public static PreparedStatement stmt;
	public static ResultSet resultSet;
	
	public CreateGroup() {}
	
	public CreateGroup(ConnectSQL connect) {
		this.connect = connect;
	}
	
	static {
		connection = connect.getConnection();
	}
	
	public static List<String> GetKhoa() {
		List<String> list = new ArrayList<>();
		try {
			String query = "SELECT DISTINCT LEFT(ID_User, 2) FROM TB_User WHERE LEFT(ID_User, 2) != 'GV' AND ID_User != 'Admin'";
			stmt = connection.prepareStatement(query);
			
			resultSet = stmt.executeQuery();	
			while(resultSet.next()) {
				String value = resultSet.getString(1);
				list.add(value);
			}
		} catch(Exception e) {}
		return list;
	}
	public static List<String> GetLSH(String khoa) {
		Set<String> uniqueValues = new HashSet<>();
		List<String> list = new ArrayList<>();
		try {
			String query = "SELECT * FROM TB_User WHERE LEFT(ID_User, 2) != 'GV' AND LEFT(ID_User, 2) = '" + khoa + "'";
			
			stmt = connection.prepareStatement(query);
			
			resultSet = stmt.executeQuery();	
			while(resultSet.next()) {
				String value = resultSet.getString(1);
		        String firstString = value.substring(0, value.length() - 9);
		        String result = firstString.substring(2);
                if (uniqueValues.add(result)) {
                    list.add(result);
                }
			}
		} catch(Exception e) {}
		return list;
	}
	
	public static List<String> GetIDUser(String id_user) {
		List<String> list = new ArrayList<>();
		try {
			String query = "SELECT * FROM TB_User WHERE LEFT(ID_User, 5) = ? AND LEFT(ID_User, 2) != 'GV'";
			stmt = connection.prepareStatement(query);
			stmt.setString(1, id_user);
			
			resultSet = stmt.executeQuery();	
			while(resultSet.next()) {
				String value = resultSet.getString("ID_User");
				list.add(value);
			}
		} catch(Exception e) {}
		return list;
	}
	
	public static int Get_Number_of_participants(String lsh) {
		int number = 0;
		try {
			String query = "SELECT COUNT(ID_User) FROM TB_User WHERE SUBSTRING(ID_User, 3, 3) = ?";
			stmt = connection.prepareStatement(query);
			stmt.setString(1, lsh);
			
			resultSet = stmt.executeQuery();
			
			if (resultSet.next()) {
				number = resultSet.getInt(1);
			}
		} catch(Exception e) {}	
		return number;
	}
	public void CreateGroup(String id_group, String name_group, String lsh, String id, String id_administrator) {
		int number = Get_Number_of_participants(lsh);
		try {
			String query1 = "INSERT INTO TB_Group(ID_Group, Name_group, Number_of_participants, ID_Administrator) VALUES (?, ?, ?, ?)";
			stmt = connection.prepareStatement(query1);
			stmt.setString(1, id_group);
			stmt.setString(2, name_group);
			stmt.setInt(3, number);
			stmt.setString(4, id_administrator);
			
			stmt.executeUpdate();	
			
		    if(GetIDUser(id) != null) {
		    	List<String> temp = new ArrayList<>(GetIDUser(id));
		    	for(String value : temp) {
					String query2 = "INSERT INTO Management_Group(ID_User, ID_Group) VALUES (?, ?)";
					stmt = connection.prepareStatement(query2);
					stmt.setString(1, value);
					stmt.setString(2, id_group);	
					stmt.executeUpdate();
		    	}
		    }	
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
}
