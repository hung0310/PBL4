package Controller.Thread_Of_Client;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.TargetDataLine;


public class voiceClientThread {
	public  SourceDataLine audio_out;
	public TargetDataLine audio_in;
	private DatagramSocket client_socket;
	private DatagramSocket client_socket_rc;
	private DatagramSocket client_socket_send;
	private DatagramSocket client_socket_send_ano;
	private InetAddress ipAddress;
	private int Port;
	public byte buffer_speaker_cli[] = new byte[20000];
	public byte buffer_speaker_another_cli[] = new byte[20000];
	public byte buffer_send_cli[] = new byte[20000];
	public byte buffer_send_another_cli[] = new byte[20000];
	public  boolean flag_speaker_client = false;
	public  boolean flag_voice_client = false;
	public  boolean flag_to_another_client = false;
	public  boolean flag_speaker_another_client = false;
	public voiceClientThread(String ipAddress) {
	    try {
	    	this.client_socket_send_ano = new DatagramSocket();
	    	this.client_socket_rc = new DatagramSocket(8998);
	    	this.client_socket = new DatagramSocket(8999);
	    	this.client_socket_send = new  DatagramSocket();
			this.ipAddress = InetAddress.getByName(ipAddress);
			this.Port = 8899;
		} catch (Exception e) {}
	}
	public void closeThread_Client() {
	    try {
	    	flag_voice_client = false;
		    flag_speaker_client = false;
	        if (client_socket_send != null && !client_socket_send.isClosed()) {
	            client_socket_send.close();
	        }
	    } catch (Exception e) {
	        e.printStackTrace(); // hoặc ghi log nếu cần thiết
	    }

	    try {
	        if (client_socket != null && !client_socket.isClosed()) {
	            client_socket.close();
	        }
	    } catch (Exception e) {
	        e.printStackTrace(); // hoặc ghi log nếu cần thiết
	    }

	    
	}


	public  void start_mic( ) {
		this.flag_voice_client= true;
	}
	public  void start_speaker() {
		this.flag_speaker_client = true;
	}
	public  void start_speaker_another() {
		this.flag_speaker_another_client = true;
	}
	public  void stop_speaker_another() {
		this.flag_speaker_another_client = false;
	}
	
	public  void stop_mic( ) {
		this.flag_voice_client = false;
	}
	public  void stop_speaker() {
		this.flag_speaker_client = false;
	}
	public void client_voice_send() {
	    Thread client_voice_send = new Thread(() -> {
	        try {
	            // Khởi tạo định dạng âm thanh
	            AudioFormat format = new AudioFormat(44100.0F, 16, 2, true, false);
	            // Tạo thông tin cho dòng đầu vào âm thanh (TargetDataLine)
	            DataLine.Info infor = new DataLine.Info(TargetDataLine.class, format);

	            // Kiểm tra xem hệ thống có hỗ trợ định dạng âm thanh được chỉ định hay không
	            if (!AudioSystem.isLineSupported(infor)) {
	                System.out.println("Không hỗ trợ định dạng âm thanh này");
	                return;
	            }

	            // Tạo và cấu hình dòng đầu vào âm thanh (TargetDataLine)
	            audio_in = (TargetDataLine) AudioSystem.getLine(infor);
	            audio_in.open(format);
	            audio_in.start();
	            System.out.print("Gửi âm thanh đến server");

	            while (flag_voice_client) {
	                // Đọc dữ liệu âm thanh để lưu vào buffer
	                audio_in.read(buffer_send_cli, 0, buffer_send_cli.length);

	                // Đối tượng DatagramPacket để gửi âm thanh
	                DatagramPacket data1 = new DatagramPacket(buffer_send_cli, buffer_send_cli.length, ipAddress, 8899);
	                DatagramPacket data2 = new DatagramPacket(buffer_send_cli, buffer_send_cli.length, ipAddress, 7778);

	                // Gửi đến cổng 1
	                client_socket_send.send(data1);

	                // Gửi đến cổng 2
	                client_socket_send_ano.send(data2);
	            }
	        } catch (LineUnavailableException | UnknownHostException | SocketException e) {
	            e.printStackTrace();
	        } catch (IOException e) {
	            e.printStackTrace();
	        } finally {
	            audio_in.stop();
	            audio_in.close();
	        }
	    });

	    client_voice_send.start();
	}
	

	
public void speaker_client() {
		Thread client_voice_receive = new Thread(() -> {
		    try {
		        // Khởi tạo định dạng âm thanh
		        AudioFormat format = new AudioFormat(44100.0F, 16, 2, true, false);
		        // Tạo và cấu hình dòng đầu ra âm thanh (SourceDataLine)
		        DataLine.Info outInfo = new DataLine.Info(SourceDataLine.class, format);
		        if (!AudioSystem.isLineSupported(outInfo)) {
		            System.out.println("Không hỗ trợ định dạng âm thanh đầu ra này");
		            return;
		        }
		        audio_out = (SourceDataLine) AudioSystem.getLine(outInfo);
		        audio_out.open(format);
		        audio_out.start();
		        System.out.println("Nhận âm thanh từ phía server");
		       
		        // Tạo socket trước khi vào vòng lặp
		        while (flag_speaker_client) {
		            DatagramPacket data = new DatagramPacket(buffer_speaker_cli, buffer_speaker_cli.length);
		            client_socket.receive(data);
		            System.out.println(data.getAddress());
		            buffer_speaker_cli = data.getData();
			        audio_out.write(buffer_speaker_cli, 0, buffer_speaker_cli.length);
			        
			       
		        }
		    } catch (Exception ex) {
		        ex.printStackTrace();
		    } 
		});
		client_voice_receive.start();
	}
	
public void another_speaker_client() {
	Thread client_voice_receive = new Thread(() -> {
	    try {
	        // Khởi tạo định dạng âm thanh
	        AudioFormat format = new AudioFormat(44100.0F, 16, 2, true, false);
	        // Tạo và cấu hình dòng đầu ra âm thanh (SourceDataLine)
	        DataLine.Info outInfo = new DataLine.Info(SourceDataLine.class, format);
	        if (!AudioSystem.isLineSupported(outInfo)) {
	            System.out.println("Không hỗ trợ định dạng âm thanh đầu ra này");
	            return;
	        }
	        audio_out = (SourceDataLine) AudioSystem.getLine(outInfo);
	        audio_out.open(format);
	        audio_out.start();
	        System.out.println("Nhận âm thanh từ phía server");
	      
	        // Tạo socket trước khi vào vòng lặp
	        while (flag_speaker_another_client) {
	        	 DatagramPacket data_another = new DatagramPacket(buffer_speaker_another_cli, buffer_speaker_another_cli.length);
			        client_socket_rc.receive(data_another);
			        System.out.println(data_another.getAddress());
			        buffer_speaker_another_cli = data_another.getData();
			        audio_out.write(buffer_speaker_another_cli, 0, buffer_speaker_another_cli.length);
	        }
	    } catch (Exception ex) {
	        ex.printStackTrace();
	    } 
	});
	client_voice_receive.start();
}
		

	
}