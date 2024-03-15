package Controller.Thread_Of_Server;

import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.zip.CRC32;

import javax.imageio.ImageIO;

import Controller.Image_Compression;
import application.Singleton;
import javafx.scene.image.ImageView;

public class ThreadShare_ToClients extends Thread{
	
	private DatagramSocket serverSocket;
	private ImageView imgView;
	private static int port;
	private volatile boolean isRunning = true;
	private Image_Compression imageCompression = new Image_Compression(); 
	
	public ThreadShare_ToClients(DatagramSocket serverSocket, ImageView imgView) {
		this.serverSocket = serverSocket;
		this.imgView = imgView;
		this.port = 7777;
	}
	
	@Override
	public void run() {
		while(isRunning) {
			try {
				Robot robot = new Robot();
				BufferedImage screenshots = robot.createScreenCapture(new Rectangle(Toolkit.getDefaultToolkit().getScreenSize()));
				
				File outputFile = new File("screenshot.jpg");
				String FileName = outputFile.getAbsolutePath();
				ImageIO.write(screenshots, "jpg", outputFile);
                //Thread.sleep(33);
				
		        File file = imageCompression.Compression(FileName);
		        FileInputStream fis = new FileInputStream(file);
		        byte[] fileData = new byte[(int) file.length()];
		        fis.read(fileData);
		        fis.close();
		
		        int chunkSize = 1024; // Kích thước mỗi phần dữ liệu
		        int numChunks = (int) Math.ceil((double) fileData.length / chunkSize); // Số lượng gói cần gửi
		        
		        //String header = numChunks + "";
		        String header = numChunks + " " + getCRC_Of_Image(file.getAbsolutePath());
		        //System.out.println(getCRC_Of_Image(file.getAbsolutePath()));
		        byte[] headerData = header.getBytes();
		        
		        for(DatagramPacket value : Singleton.getInstance().getUsers()) {
			        DatagramPacket _headerPacket = new DatagramPacket(headerData, headerData.length, value.getAddress(), port);
			        serverSocket.send(_headerPacket);
			        
			        for (int i = 0; i < numChunks; i++) {
			            int start = i * chunkSize;
			            int end = Math.min(fileData.length, (i + 1) * chunkSize);
			            byte[] sendData = new byte[end - start];
			            System.arraycopy(fileData, start, sendData, 0, end - start);
			
			            DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, value.getAddress(), port);
			            serverSocket.send(sendPacket);
			        }			        		
		        }
			} catch(Exception ex) {
				ex.printStackTrace();
			}
		}
	}
	
	public void startShare_Server() {
		this.isRunning = true;
	}
	
	public void stopShare_Server() {
		this.isRunning = false;
	}
	
	public static long getCRC_Of_Image(String pathImage) {
		try(FileInputStream fis = new FileInputStream(pathImage)) {
			byte[] data = new byte[8192];
			int byteRead;
			CRC32 crc = new CRC32();
			while((byteRead = fis.read(data)) != -1) {
				crc.update(data, 0, byteRead);
			}
			return crc.getValue();
		} catch(Exception e) {
			return -1;
		}
	}
}
