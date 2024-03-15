package ConnectDB;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Time;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class HandleMain {
	public static ConnectSQL connect;
	public static Connection connection;
	public static PreparedStatement stmt;
	public static ResultSet resultSet;
	
	public HandleMain() {}
	
	public HandleMain(ConnectSQL connect) {
		this.connect = connect;
	}
	
	static {
		connection = connect.getConnection();
	}
	public static List<String> GetNameGroup_BySV(String ID_Administrator) {
		List<String> list = new ArrayList<>();
		try {
			String query = "SELECT ID_Group, Name_group FROM TB_Group WHERE RIGHT(ID_Group, 5) = ?";
			stmt = connection.prepareStatement(query);
			stmt.setString(1, ID_Administrator);
			resultSet = stmt.executeQuery();	
			while(resultSet.next()) {
	            String idGroup = resultSet.getString("ID_Group");
	            String nameGroup = resultSet.getString("Name_group");
	            String value = nameGroup + " " + idGroup;
	            list.add(value);
			}
		} catch(Exception e) {}
		return list;
	}
	public static List<String> GetNameGroup_ByGV(String ID_Administrator) {
	    List<String> list = new ArrayList<>();
	    try {
	        String query = "SELECT ID_Group, Name_group FROM TB_Group WHERE ID_Administrator = ?";
	        stmt = connection.prepareStatement(query);
	        stmt.setString(1, ID_Administrator);
	        resultSet = stmt.executeQuery();    
	        while (resultSet.next()) {
	            String idGroup = resultSet.getString("ID_Group");
	            String nameGroup = resultSet.getString("Name_group");
	            String value = nameGroup + " " + idGroup;
	            list.add(value);
	        }
	    } catch (Exception e) {}
	    return list;
	}
	public static LocalTime GetTimeStart(String idgroup) {
	    LocalTime timeStart = null;
	    try {
	        String query = "SELECT TimeStart FROM Meeting WHERE ID_Group = ?";
	        stmt = connection.prepareStatement(query);
	        stmt.setString(1, idgroup);
	        resultSet = stmt.executeQuery();
	        if (resultSet.next()) {
	            Time sqlTime = resultSet.getTime("TimeStart");
	            if (sqlTime != null) {
	                timeStart = sqlTime.toLocalTime();
	            }
	        }
	    } catch (Exception e) {}
	    return timeStart;
	}
	public void SaveMessage(String ID_User,String ID_Group, String message) {
		try {
			String sql = "INSERT INTO TB_Message(ID_User,ID_Group,Content_Message) Values(?,?,?)";
			stmt = connection.prepareStatement(sql);
			stmt.setString(1,ID_User);
			stmt.setString(2,ID_Group);
			stmt.setString(3,message);
			stmt.executeUpdate();		
		}catch (Exception e) {
			System.out.println(e.getStackTrace());
		}
	}
	public static void CreateMeeting(String id_meeting, String id_group, String time) {
		try {
			String query = "INSERT INTO Meeting(ID_Meeting, ID_Group, TimeStart) VALUES (?, ?, ?)";
			stmt = connection.prepareStatement(query);
			stmt.setString(1, id_meeting);
			stmt.setString(2, id_group);
			stmt.setString(3, time);
			stmt.executeUpdate();		
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static String getID_Meeting(String id_group) {
		String id = null;
		try {
	        String query = "SELECT * FROM Meeting WHERE ID_Group = ?";
	        stmt = connection.prepareStatement(query);
	        stmt.setString(1, id_group);
	        resultSet = stmt.executeQuery();
			if (resultSet.next()) {
				id = resultSet.getString("ID_Meeting");
			}
		} catch(Exception e) {}
		return id;
	}
	
	public static void DeleteMeeting(String id) {
	    try {
	        String query_id = "DELETE FROM Meeting WHERE ID_Meeting = ?";
	        stmt = connection.prepareStatement(query_id);
	        stmt.setString(1, id);
	        stmt.executeUpdate();
	    } catch(Exception e) {
	        e.printStackTrace();
	    }
	}
	
	public static boolean CheckPassword(String email, String password) {
		try {
			String query = "SELECT * FROM Account WHERE Email = ? AND Password = ?";
	        stmt = connection.prepareStatement(query);
	        stmt.setString(1, email);
	        stmt.setString(2, password);
	        resultSet = stmt.executeQuery();
	        if(resultSet.next()) 
	        	return true;
		} catch(Exception e) {}
		return false;
	}
	
	public static void ChangePassword(String email, String newPass) {
	    try {
	        String query = "UPDATE Account SET Password = ? WHERE Email = ?";
	        stmt = connection.prepareStatement(query);
	        stmt.setString(1, newPass);
	        stmt.setString(2, email);
	        stmt.executeUpdate();
	    } catch(Exception e) {
	        e.printStackTrace();
	    }
	}
	public static ArrayList<String> get_Messages(String id_group) {
	    ArrayList<String> messages = new ArrayList<String>();
	    try {
	        String sqlQuery = "SELECT Content_Message FROM TB_Message " +
	                          "JOIN TB_Group ON TB_Message.ID_Group = TB_Group.ID_Group " +
	                          "WHERE TB_Group.ID_Group = ?";
	        stmt = connection.prepareStatement(sqlQuery);
	        stmt.setString(1, id_group);
	        resultSet = stmt.executeQuery();

	        while (resultSet.next()) {
	            String messageContent = resultSet.getString("Content_Message");
	            messages.add(messageContent);
	        }
	    } catch (Exception e) {
	        e.printStackTrace(); 
	    } 

	    return messages;
	}
	public static void deleteMessage(String id_group) {
		try {
	        String query = "DELETE FROM TB_Message WHERE ID_Group = ?";
	        stmt = connection.prepareStatement(query);
	        stmt.setString(1, id_group);
	        stmt.executeUpdate();
		} catch(Exception e) {}
	}
}