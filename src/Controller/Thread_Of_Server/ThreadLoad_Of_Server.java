package Controller.Thread_Of_Server;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.Arrays;
import java.util.zip.CRC32;

import javafx.concurrent.Task;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class ThreadLoad_Of_Server extends Task<Void>{
	private DatagramSocket serverSocket;
	private ImageView imgViewMeeting;
	
	public ThreadLoad_Of_Server(ImageView imgView) {
        try {
        	serverSocket = new DatagramSocket(5555);
        } catch (IOException e) {
            e.printStackTrace();
        }
		this.imgViewMeeting = imgView;
	}
	
    public void closeThread_Load_Of_Server() {
        serverSocket.close();
        cancel(); 
    }
	
    @Override
    protected Void call() {
        while (!isCancelled()) {
            try {
                byte[] receiveData = new byte[1024];
                DatagramPacket headerPacket = new DatagramPacket(receiveData, receiveData.length);
                serverSocket.receive(headerPacket);
                
                String msg = new String(headerPacket.getData(), 0, headerPacket.getLength());
                
	            String[] message = msg.split(" ");
	            if(message[0].matches("\\d+") && message[1].matches("\\d+")) {
	                int numChunks = Integer.parseInt(message[0]);
	                long checkCRC = Long.parseLong(message[1]);
		                try (FileOutputStream fos = new FileOutputStream("received_image.jpg")) {
		                    boolean success = true;
		                    for (int i = 0; i < numChunks && success; i++) {
		                        DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
		                        serverSocket.receive(receivePacket);
		                        byte[] dataReceived = Arrays.copyOfRange(receivePacket.getData(), 0, receivePacket.getLength());
		                        try {
		                            fos.write(dataReceived);
		                        } catch (IOException e) {
		                            success = false;
		                        }
		                    }
		                } catch (IOException e) {
		                    e.printStackTrace();
		                }
		                File imagePath = new File("received_image.jpg");
	                	if(getCRC_Of_Image(imagePath.getAbsolutePath()) == checkCRC) {
			                Image image = new Image("file:" + imagePath.getAbsolutePath());
			                updateImageView(image);
			                Thread.sleep(33);	                		
	                	}
	                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
		return null;
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
	
    private void updateImageView(Image image) {
        imgViewMeeting.setImage(image);
    }
}
