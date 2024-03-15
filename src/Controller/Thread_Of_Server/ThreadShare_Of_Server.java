package Controller.Thread_Of_Server;

import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.zip.CRC32;
import javax.imageio.ImageIO;
import Controller.Image_Compression;
import application.Singleton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class ThreadShare_Of_Server extends Thread{
	
	private DatagramSocket serverSocket;
	private static int port;
	private ImageView imgView;
	private volatile boolean isRunning = true;
	private Image_Compression imageCompression = new Image_Compression();
	
	public ThreadShare_Of_Server(ImageView imgView) {
		try {
			this.serverSocket = new DatagramSocket(8888);
			this.imgView = imgView;
			this.port = 7777;
		} catch (SocketException e) {}
	}
	
	@Override
	public void run() {
		while(isRunning) {
			try {
				Robot robot = new Robot();
				BufferedImage screenshots = robot.createScreenCapture(new Rectangle(Toolkit.getDefaultToolkit().getScreenSize()));
				
				File outputFile = new File("screenshot.jpg");
				String FileName = outputFile.getAbsolutePath();
//				System.out.println("Da chup anh thanh cong");
				ImageIO.write(screenshots, "jpg", outputFile);
                Image image = new Image("file:" + outputFile.getAbsolutePath());
                updateImageView(image);
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
		        	//System.out.println(value.getAddress()+ "<:>" + value.getPort());
			        DatagramPacket headerPacket = new DatagramPacket(headerData, headerData.length, value.getAddress(), port);
			        serverSocket.send(headerPacket);
			        for (int i = 0; i < numChunks; i++) {
			            int start = i * chunkSize;
			            int end = Math.min(fileData.length, (i + 1) * chunkSize);
			            byte[] sendData = new byte[end - start];
			            System.arraycopy(fileData, start, sendData, 0, end - start);
			
			            DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, value.getAddress(), port);
			            serverSocket.send(sendPacket);
			        }
					Thread.sleep(33);		        	
		        }
			} catch(Exception ex) {
				ex.getStackTrace();
			}		
		}
	}
	
	public void startShare_Server() {
		this.isRunning = true;
	}
	
	public void stopShare_Server() {
		this.isRunning = false;
	}
	
	public void closeThread_Share_Of_Server() {
		if(serverSocket != null)
			this.serverSocket.close();
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
	
    public void updateImageView(Image image) {
        imgView.setImage(image);
    }
    
    public DatagramSocket getSocket() {
    	return this.serverSocket;
    }
}
