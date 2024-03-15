package Controller.Thread_Of_Client;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import application.Singleton;

public class Manage_ProcessingClient extends Thread{
	private static DatagramSocket clientSocket;
	private static InetAddress ipAddress;
	private static int Port;
	private static volatile boolean isRunning = true;
	private static byte[] incoming = new byte[1024];
	private volatile boolean checkShare = false;
	private volatile boolean checkConnect = false;
	private volatile boolean checkEndMeeting = false;
	private static String message_block;
	private volatile boolean check = true;
	private ExecutorService executor = Executors.newSingleThreadExecutor();

	static {
		try {
			ipAddress = InetAddress.getByName(Singleton.getInstance().getIPAddress());
		} catch(Exception e) {}
	}
	
	static {
		Port = Integer.parseInt(Singleton.getInstance().getPort());
	}
	
    public Manage_ProcessingClient() {
        initializeSocket();
    }

    private void initializeSocket() {
        try {
            clientSocket = new DatagramSocket();
            if(check) {
    	        byte[] message = "#RequestConnect".getBytes();
    	        DatagramPacket sendMessgae = new DatagramPacket(message, message.length, ipAddress, Port);
    	        clientSocket.send(sendMessgae);
    	        check =false;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void startProcessing() {
        isRunning = true;
        if (clientSocket == null || clientSocket.isClosed()) {
            initializeSocket();
        }
        Thread newThread = new Thread(this);
        newThread.start();
    }

    public void stopProcessing() {
        isRunning = false;
    }
	
	@Override
	public void run() {
	    try {
            executor.submit(() -> {
                while (isRunning) {
                    DatagramPacket packet = new DatagramPacket(incoming, incoming.length);
                    try {
                        clientSocket.receive(packet);
                        String message_received = new String(packet.getData(), 0, packet.getLength());
                        // Xử lý gói tin
                        handlePacket(message_received);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}
	
	public void handlePacket(String message_received) {
        if (message_received.equals("#AcceptedRequestConnect!")) {
            System.out.println("Server send: " + message_received);
            checkConnect = true;
        } else if (message_received.equals("#AcceptedRequestShare!")) {
            System.out.println("Server send: " + message_received);       	
            checkShare = true;
        } else if(message_received.equals("#EndMeeting!")) {
        	System.out.println("Server send: " + message_received); 
        	checkEndMeeting = true;
        }  else {
        	System.out.println("Server send: " + message_received); 
        	message_block = message_received;
        } 
	}
	
	public void SendRequestShare() {
        try {
        	String name_user = Singleton.getInstance().getNameuser();
    		byte[] message = ("#RequestShare " + name_user).getBytes();
            DatagramPacket sendMessgae = new DatagramPacket(message, message.length, ipAddress, Port);
			clientSocket.send(sendMessgae);
		} catch (IOException e) {} 		
	}
	
	public void SendNotificationLeave() {
        try {
        	String name_user = Singleton.getInstance().getNameuser();
    		byte[] message = (name_user + " đã rời khỏi cuộc họp").getBytes();
            DatagramPacket sendMessgae = new DatagramPacket(message, message.length, ipAddress, Port);
			clientSocket.send(sendMessgae);
		} catch (IOException e) {} 	
	}
	
	public DatagramSocket getClientSocket() {
		return this.clientSocket;
	}
	
	public String getIP() {
		return this.ipAddress.getHostAddress();
	}
	
	public int getPort() {
		return this.Port;
	}
	
	public synchronized void set_isRunning(boolean flag) {
		this.isRunning = flag;
	}
	public synchronized boolean get_isRunning() {
		return this.isRunning;
	}
	
	public synchronized boolean getCheckShare() {
		return this.checkShare;
	}
	public synchronized void setCheckShare(boolean check) {
		this.checkShare = check;
	}
	
	public synchronized boolean getCheckConnect() {
		return this.checkConnect;
	}
	public synchronized void setCheckConnect(boolean check) {
		this.checkConnect = check;
	}
	
	public boolean getCheckEndMeeting() {
		return this.checkEndMeeting;
	}
	public synchronized String getMessageBlock() {
		return this.message_block;
	}
}














