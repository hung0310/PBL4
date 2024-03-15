package Controller.Thread_Of_Server;
import java.io.IOException;
//SERVER
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import application.Singleton;

public class Manage_Connect extends Thread{
	private DatagramSocket serverSocket;
	private DatagramPacket tempPacket;
	private InetAddress ipAddress;
	private final int PORT_SERVER = 9999;
	private	volatile boolean controlShare = false;
	private volatile boolean runningLoad = false;
	private volatile boolean isRunning = true;
	private volatile boolean flag_leave = false;
	private String message_leave;
	private String message;
	private String name_user;

	private static byte[] incoming = new byte[1024];
	
	public Manage_Connect() {
		try {
			ipAddress = InetAddress.getLocalHost();
	        serverSocket = new DatagramSocket(PORT_SERVER);
		} catch(Exception e) {}
	}
	
    public void stopProcessing() {
    	serverSocket.close();
    	if(serverSocket.isClosed()) {
    		System.out.println("Da dong roi");
    	}
        isRunning = false;
    }
	
	@Override
	public void run() {
		System.out.println("Server starting to port: " + PORT_SERVER);
		byte[] byteRequestConnect = ("#AcceptedRequestConnect!").getBytes();
		while(isRunning) {
			DatagramPacket packet = new DatagramPacket(incoming, incoming.length);
			try {
				serverSocket.receive(packet);
				message = new String(packet.getData(), 0, packet.getLength());
				
				if(message.equals("#RequestConnect")) {
					System.out.println("Server received: " + message);
					Singleton.getInstance().setUsers(packet);

					System.out.println("Dia chi cua Client:" + packet.getAddress() + " ** " + packet.getPort());
					
					DatagramPacket forward = new DatagramPacket(byteRequestConnect, byteRequestConnect.length, packet.getAddress(), packet.getPort());
					try {
						serverSocket.send(forward);
					} catch(Exception e) {}		
				} else if(message.contains(" đã rời khỏi cuộc họp")) {
					message_leave = message;
					this.flag_leave = true;
					Singleton.getInstance().Remove_IP_User(packet.getAddress().toString(), packet.getPort() + "");
					System.out.println("Dia chi cua Client da roi cuoc hop:" + packet.getAddress() + " ** " + packet.getPort());
				} else if(message.split(" ")[0].equals("#RequestShare")) {
					System.out.println("Server received: " + message);
					System.out.println("Dia chi Client yeu cau share man hinh: " + packet.getAddress() + " ** " + packet.getPort());
					String[] msg = message.split(" ");
					
            		StringBuilder fullNameBuilder = new StringBuilder();
            		for (int i = 1; i < msg.length; i++) {
            		    fullNameBuilder.append(msg[i]).append(" ");
            		}
            		name_user = fullNameBuilder.toString().trim();
            		
					controlShare = true;
					tempPacket = packet;
				}
			} catch(Exception e) {}	
		}
	}					
	public DatagramSocket getServerSocket() {
		return this.serverSocket;
	}
	
	public String get_IP_Port_Server() {
		return this.ipAddress.getHostAddress() + " " + this.PORT_SERVER;
	}

	public void setControl(boolean control) {
		this.controlShare = control;
	}
	public boolean getControl() {
		return this.controlShare;
	}
	
	public boolean getRunningLoad() {
		return this.runningLoad;
	}
	public void setRunningLoad(boolean load) {
		this.runningLoad = load;
	}
	public String get_ip() {
		return ipAddress.getHostAddress();
	}
	public int get_port() {
		return PORT_SERVER;
	}
	public String get_name_user() {
		return this.name_user;
	}
	public void sendAcceptShare() {
		try {
	    	byte[] byteRequestShare = ("#AcceptedRequestShare!").getBytes();	
			DatagramPacket forward = new DatagramPacket(byteRequestShare, byteRequestShare.length, tempPacket.getAddress(), tempPacket.getPort());
			serverSocket.send(forward);
			String ip = tempPacket.getAddress().toString();
			String port = String.valueOf(tempPacket.getPort());
			Singleton.getInstance().setIPAddress(ip);
			Singleton.getInstance().setPort(port);
			System.out.println("Day la noi check dia chi thu 2: " + ip + " " + port);
			System.out.println("Đã gửi thông điệp chấp nhận chia sẻ màn hình");
		} catch(Exception e) {}
	}
	
	public void sendEndMeeting() {
		try {
	    	byte[] byteEndMeeting = ("#EndMeeting!").getBytes();
        	for(DatagramPacket value : Singleton.getInstance().getUsers()) { 
    			DatagramPacket forward = new DatagramPacket(byteEndMeeting, byteEndMeeting.length, value.getAddress(), value.getPort());
    			serverSocket.send(forward);
    			System.out.println("Đã gửi thông điệp kết thúc cuộc họp");
        	}
		} catch(Exception e) {}
	}
	
	public String GetMessageLeave_Of_Client() {
		return message_leave;
	}
	public boolean Get_Flag_Check_leave() {
		return this.flag_leave;
	}
	public void Set_Flag_Check_leave(boolean flag) {
		this.flag_leave = flag;
	}
	
	
}














