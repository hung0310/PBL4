package application;

import java.net.DatagramPacket;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javafx.scene.control.Button;

public class Singleton {
    private static Singleton instance;
	private static ArrayList<DatagramPacket> users = new ArrayList<>();
	private static ArrayList<DatagramPacket> mic = new ArrayList<>();
	private String email;
    private String nameuser;
    private String role;
    private boolean flag = true;
    private String ID_Administrator;
    private String ipAddress;
    private String Port;
    private String Id_group;
    private Singleton() {}

    public static Singleton getInstance() {
        if (instance == null) {
            instance = new Singleton();
        }
        return instance;
    }

    public String getNameuser() {
        return this.nameuser;
    }

    public void setNameUser(String nameuser) {
        this.nameuser = nameuser;
    }
    public String getID_Group() {
    	return this.Id_group;
    }
    
    public void setID_Group(String Id_group) {
    	this.Id_group = Id_group;
    }

    public String getRole() {
        return this.role;
    }

    public void setRole(String role) {
        this.role = role;
    }
    
    public String getID_Administrator() {
    	return this.ID_Administrator;
    }
    
    public void setID_Administrator(String ID_Administrator) {
    	this.ID_Administrator = ID_Administrator;
    }
    
    public List<DatagramPacket> getUser_mic() {
    	return this.mic;
    }
    
    public void setUser_mic(DatagramPacket packet) {
    	this.mic.add(packet);
    }
    
    public List<DatagramPacket> getUsers() {
    	return this.users;
    }
    
    public void setUsers(DatagramPacket packet) {
    	this.users.add(packet);
    }
    
    public void RemoveUser_EndMeeting() {
    	users.clear();
    }
    
    public String getIPAddress() {
    	return this.ipAddress;
    }
    
    public void setIPAddress(String ip) {
    	this.ipAddress = ip;
    }
    
    public String getPort() {
    	return this.Port;
    }
    
    public void setPort(String port) {
    	this.Port = port;
    }
    
    public boolean getFlag() {
    	return flag;
    }
    public void setFlag(boolean flag) {
    	this.flag = flag;
    }
    
    public void setEmail(String email) {
    	this.email = email;
    }
    public String getEmail() {
    	return this.email;
    }
    
    public void Remove_IP_User(String ip, String port) {
        Iterator<DatagramPacket> iterator = getUsers().iterator();
        while (iterator.hasNext()) {
            DatagramPacket value = iterator.next();
            String IP = value.getAddress().toString();
            String PORT = value.getPort() + "";
            System.out.println(IP + "><" + PORT);
            if (IP.equals(ip) && PORT.equals(port)) {
                iterator.remove(); 
            }
        }
    }
}