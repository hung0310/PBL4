package Controller.Thread_Of_Server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.net.UnknownHostException;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.TargetDataLine;

import application.Singleton;

public class voiceserverThread {
	public  SourceDataLine audio_out;
	public TargetDataLine audio_in;
	DatagramSocket server_socket;
	DatagramSocket server_socket_send;
	DatagramSocket server_socket_rc;
	DatagramSocket server_socket_sv;
	public byte buffer_speaker[] = new byte[20000];
	public  boolean flag_speaker = false;
	public  boolean flag_voice = false;
	public byte buffer_speaker_sv[] = new byte[20000];
	public byte buffer_another_speaker_sv[] = new byte[20000];
	public byte buffer_sv[] = new byte[20000];
	public boolean flag_another_speaker = false;
	public voiceserverThread() {
		try {
			this.server_socket_rc = new DatagramSocket(7778);
			this. server_socket_send = new DatagramSocket();
			this.server_socket = new DatagramSocket(8899);
			this.server_socket_sv = new DatagramSocket();
		} catch (SocketException e) {
			e.printStackTrace();
		}

	}
	
	public void closeThread_Server() {
	    try {
	    	flag_voice = false;
		    flag_speaker = false;
	        if (server_socket_sv != null && !server_socket_sv.isClosed()) {
	            server_socket_sv.close();
	        }
	    } catch (Exception e) {
	        e.printStackTrace(); // hoặc ghi log nếu cần thiết
	    }

	    try {
	        if (server_socket != null && !server_socket.isClosed()) {
	            server_socket.close();
	        }
	    } catch (Exception e) {
	        e.printStackTrace(); // hoặc ghi log nếu cần thiết
	    }

	    
	}
	
	


	public  void start_mic( ) {
		this.flag_voice= true;
	}
	public  void start_speaker() {
		this.flag_speaker = true;
	}
	public  void start_anothe_speaker() {
		this.flag_another_speaker = true;
	}
	public  void stop_anothe_speaker() {
		this.flag_another_speaker = false;
	}
	public  void stop_mic( ) {
		this.flag_voice= false;
	}
	public  void stop_speaker() {
		this.flag_speaker = false;
	}

	public void speaker_server() {
		Thread server_receive_voice = new Thread(() -> {
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
		        
		        System.out.println("Nhận âm thanh từ phía client");
		        
		        while (flag_speaker) {
		        	 
		            DatagramPacket data = new DatagramPacket(buffer_speaker_sv, buffer_speaker_sv.length);
		            server_socket.receive(data);
		            buffer_speaker_sv = data.getData();
		            System.out.println(data.getAddress());
		            //audio_out.write(buffer_speaker_sv, 0, buffer_speaker_sv.length);
		        }
		    } catch (Exception ex) {
		        ex.printStackTrace();
		    } 
		});
		server_receive_voice.start();
	}
	public void speaker_another_server() {
	    Thread server_receive_voice = new Thread(() -> {
	        try {
	            while (flag_another_speaker) {
	                DatagramPacket data = new DatagramPacket(buffer_another_speaker_sv, buffer_another_speaker_sv.length);
	                server_socket_rc.receive(data);
	                buffer_another_speaker_sv = data.getData();
	                System.out.println(data.getAddress());
                    
	                for (DatagramPacket value : Singleton.getInstance().getUsers()) {
	                    DatagramPacket voice_server = new DatagramPacket(buffer_another_speaker_sv, buffer_another_speaker_sv.length, value.getAddress(), 8998);
	                    server_socket_sv.send(voice_server);
	                }
	            }
	        } catch (Exception ex) {
	            ex.printStackTrace();
	        }
	    });
	    server_receive_voice.start();
	}
	 public void server_voice_send() {
		 Thread server_voice_send = new Thread(() -> {
		   	 try { 
		   		 // Khởi tạo định dạng âm thanh
			        AudioFormat format = new AudioFormat(44100.0F,16,2,true,false);
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
							System.out.print("Gửi âm thanh đến client");
							
							while(flag_voice) {
								
			     				//đọc dữ liệu âm thanh để lưu vào buffer
							audio_in.read(buffer_sv, 0, buffer_sv.length);
							//đối tượng datagrampacke t đểgửi âm thanh
								 for(DatagramPacket value : Singleton.getInstance().getUsers()) {
									 System.out.println(value.getAddress().toString() + value.getPort());
								     DatagramPacket voice_server = new DatagramPacket(buffer_sv, buffer_sv.length, value.getAddress(), 8999);
								     server_socket_sv.send(voice_server);
								    
								 }
							}
						} catch (LineUnavailableException | UnknownHostException | SocketException e) {} catch (IOException e) {
							e.printStackTrace();
						} 
				         audio_in.stop();
				         audio_in.close();

		    });
		
			  server_voice_send.start();
		 
		
	 }
	
}