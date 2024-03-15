package Controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import java.net.InetAddress;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Optional;

import ConnectDB.HandleMain;
import Controller.Thread_Of_Client.Manage_ProcessingClient;
import Controller.Thread_Of_Client.ThreadLoad_Of_Client;
import Controller.Thread_Of_Client.ThreadShare_Of_Client;
import Controller.Thread_Of_Client.voiceClientThread;
import Controller.Thread_Of_Server.Manage_Connect;
import Controller.Thread_Of_Server.ThreadLoad_Of_Server;
import Controller.Thread_Of_Server.ThreadShare_Of_Server;
import Controller.Thread_Of_Server.ThreadShare_ToClients;
import Controller.Thread_Of_Server.voiceserverThread;
import application.Singleton;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.scene.control.TextArea;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.scene.image.ImageView;

public class FormMeetingController {
	private ThreadShare_ToClients threadShare_to_client;
	private HandleMain handlemain = new HandleMain();
	public DatagramSocket serverSocket;
	public DatagramSocket clientSocket;
	private Manage_Connect connect;
	public Manage_ProcessingClient processing;
	private ThreadShare_Of_Server threadShare_Server;
	private ThreadLoad_Of_Server threadLoad_Task_Server;
	private ThreadShare_Of_Client threadShare_Client;
	private ThreadLoad_Of_Client threadLoad_Task_Client;
	public DatagramSocket socketmessage;
	public DatagramSocket socketmessage_sv;
	private static boolean C_Chat = true;
	volatile boolean tempEndShare = true;
	public byte[] incoming = new byte[1024];

	public DatagramPacket packet;

	static int count = 0;
	static boolean loadImage = true;

	private volatile boolean tempGV = true;
	private volatile boolean tempSV = true;
	private volatile boolean tempEnd = true;
	private volatile boolean tempLoad = true;
	private volatile boolean check_leave = true;
	private volatile boolean isMicrophoneStatus = false;
	private volatile boolean isScreenStatus = false;

	voiceserverThread thread_sv;
	voiceClientThread thread_cli;
	@FXML
	private StackPane stackpane;
	@FXML
	private AnchorPane pnChatBox;
	@FXML
	private AnchorPane pnHandle;
	@FXML
	public  TextArea messageArea;
	@FXML
	public  TextArea messageArea1;
	@FXML
	private Button btnSend;
	@FXML
	private ImageView imgViewMeeting;
	@FXML
	private ImageView img_mic;
	@FXML
	private ImageView img_screen;
	@FXML
	private ImageView img_block_mic;
	@FXML
	private ImageView img_block_screen;
	@FXML
	private Button btnChat;
	@FXML
	private Button btnShare;
	@FXML
	private Button btnMicro;
	@FXML
	private Button btnLeave;
	@FXML
	private Button stopMic1;

	@FXML
	private Button BlockScreen_Sv;

	@FXML
	private Button Block_mic_sv;

	@FXML
	private AnchorPane button_micAndScreen;

	@FXML
	private Button StopSpeaker;
	@FXML
	private Button btnSpeaker;

	@FXML
	public void initialize() throws UnknownHostException {

		if (Singleton.getInstance().getRole().equals("GiangVien")) {
			connect = new Manage_Connect();
			connect.start();
			serverSocket = connect.getServerSocket();

			//threadShare_Server = new ThreadShare_Of_Server(imgViewMeeting);
			threadLoad_Task_Server = new ThreadLoad_Of_Server(imgViewMeeting);
			CheckShare_GV.start();
			Check_SV_Leave.start();
			chatBox_server_receive();
			thread_sv = new voiceserverThread();
			
		} else {
			processing = new Manage_ProcessingClient();
			processing.start();
			clientSocket = processing.getClientSocket();
			threadLoad_Task_Client = new ThreadLoad_Of_Client(imgViewMeeting);
			threadShare_Client = new ThreadShare_Of_Client(clientSocket, imgViewMeeting, processing.getIP());
			LoadImage_SV.start();
			CheckShare_SV.start();
			CheckEndMeeting.start();
			chatBox_client_receive();
			button_micAndScreen.setVisible(false);
			thread_cli = new voiceClientThread(processing.getIP());
			ReceiveStatusScreen.start();
			ReceiveStatusMic.start();
		}
	}

	@FXML
	void btnClicked_speaker(ActionEvent event) {
		try {
			if (Singleton.getInstance().getRole().equals("GiangVien")) {
				thread_sv.start_anothe_speaker();
				thread_sv.speaker_another_server();
				thread_sv.start_speaker();
				thread_sv.speaker_server();
				StopSpeaker.setVisible(true);
				btnSpeaker.setVisible(false);

			} else {
				thread_cli.start_speaker_another();
				thread_cli.another_speaker_client();
				thread_cli.start_speaker();
				thread_cli.speaker_client();
				StopSpeaker.setVisible(true);
				btnSpeaker.setVisible(false);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// Event Listener on Button[#btnSend].onAction
	@FXML
	public void btnClicked_Send(ActionEvent event) {
		if (Singleton.getInstance().getRole().equals("GiangVien")) {
			chatBox_server_send();

		} else {
			chatBox_client_send();
		}
	}

	// Event Listener on Button[#btnChat].onAction
	@FXML
	public void btnClicked_Chat(ActionEvent event) {
		pnChatBox.toFront();
		pnHandle.toBack();
		
		if(C_Chat) {
			if (Singleton.getInstance().getRole().equals("GiangVien")) {
				 ArrayList<String> messages = handlemain.get_Messages(Singleton.getInstance().getID_Group());
			
	             displayMessages(messages);
			}else {
				 ArrayList<String> messages = handlemain.get_Messages(Singleton.getInstance().getID_Group());
	             displayMessages(messages);
			}
			pnChatBox.setVisible(true);
			C_Chat = false;
			
		} else {
			pnChatBox.setVisible(false);	
			C_Chat = true;
		}
		System.out.println(C_Chat);
	}

	// Method to display messages in the messageArea
	private void displayMessages(ArrayList<String> messages) {
		messageArea.clear();

		for (String message : messages) {
			messageArea.appendText(message + "\n");
		}
	}
	// Event Listener on Button[#btnShare].onAction

	@FXML
	void btnClicked_StopSpeaker(ActionEvent event) {
		if (Singleton.getInstance().getRole().equals("GiangVien")) {
			thread_sv.stop_anothe_speaker();
			thread_sv.stop_speaker();
			StopSpeaker.setVisible(false);
			btnSpeaker.setVisible(true);
		} else {
			thread_cli.stop_speaker();
			thread_cli.stop_speaker_another();
			StopSpeaker.setVisible(false);
			btnSpeaker.setVisible(true);
		}
	}

	@FXML
	void btnStop_Micro(ActionEvent event) {
		if (Singleton.getInstance().getRole().equals("GiangVien")) {
			thread_sv.stop_mic();
			btnMicro.setVisible(true);
			stopMic1.setVisible(false);
		} else {
		
			thread_cli.stop_mic();
			btnMicro.setVisible(true);
			stopMic1.setVisible(false);
		}
	}

	// Loi tai viec chay luong Thread
	@FXML
	public void btnClicked_Share(ActionEvent event) {
		++count;
		System.out.println(count);
		if (count % 2 != 0) {
			if (Singleton.getInstance().getRole().equals("GiangVien")) {
				ButtonType buttonTypeYes = new ButtonType("Yes", ButtonBar.ButtonData.YES);
				ButtonType buttonTypeNo = new ButtonType("No", ButtonBar.ButtonData.NO);
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("Thông báo");
				alert.setHeaderText(null); // Không có tiêu đề phụ
				alert.setContentText("Trình ghi hình sẽ bắt đầu sau khi bạn xác nhận");
				alert.getButtonTypes().setAll(buttonTypeYes, buttonTypeNo);
				Optional<ButtonType> result = alert.showAndWait();
				if (result.get() == buttonTypeYes) {
					System.out.println(count);
					System.out.println("Tiến trình ghi hình đang chạy...");
//					if(!threadShare_Server.isAlive() && threadShare_Server == null)
					threadShare_Server = new ThreadShare_Of_Server(imgViewMeeting);
					threadShare_Server.setDaemon(true);
					threadShare_Server.start();
				} else
					--count;
			}
			if (Singleton.getInstance().getRole().equals("SinhVien")) {
				processing.SendRequestShare();
			}
		} else if (count % 2 == 0) {
			ButtonType buttonTypeYes = new ButtonType("Yes", ButtonBar.ButtonData.YES);
			ButtonType buttonTypeNo = new ButtonType("No", ButtonBar.ButtonData.NO);
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Thông báo");
			alert.setHeaderText(null); // Không có tiêu đề phụ
			alert.setContentText("Bạn có muốn dừng việc ghi hình lại không?");
			alert.getButtonTypes().setAll(buttonTypeYes, buttonTypeNo);
			Optional<ButtonType> result = alert.showAndWait();
			System.out.println("Tiến trình ghi hình đã kết thúc");
			if (result.get() == buttonTypeYes) {
				if(Singleton.getInstance().getRole().equals("GiangVien")) {
					System.out.println(count);
					threadShare_Server.stopShare_Server();
				} else {
					threadShare_Client.stopShare_Client();
					
					ThreadLoad_Of_Client threadLoad_Task_Client = new ThreadLoad_Of_Client(imgViewMeeting);
					Thread threadLoad_Client = new Thread(threadLoad_Task_Client);
					threadLoad_Client.setDaemon(true);
					threadLoad_Client.start();
					
					tempSV = true;
					processing.setCheckShare(false);
					System.out.println("Check: " + processing.getCheckShare() + "<>" + tempSV);
				}
			} else {
				--count;
				System.out.println(count);
			}
		}
	}
	
//	Thread Check_EndShare_GV= new Thread(() -> {
//		while(tempEndShare) {
//			if(connect.getControl()) {
//				
//			}
//		}
//	});

	Thread LoadImage_SV = new Thread(() -> {
		while (tempLoad) {
			if (processing.getCheckConnect()) {
				Thread threadLoad_Client = new Thread(threadLoad_Task_Client);
				threadLoad_Client.setDaemon(true);
				threadLoad_Client.start();
				tempLoad = false;
			}
		}
	});

	Thread CheckShare_GV = new Thread(() -> {
		while (tempGV) {
			if (connect.getControl()) {
				StopShare();
				tempGV = false;
			}
		}
	});

	Thread CheckShare_SV = new Thread(() -> {
		while (tempSV) {
			if (processing.getCheckShare()) {
				threadLoad_Task_Client.stopLoad();
				threadShare_Client.startShare_Client();
				threadShare_Client.setDaemon(true);
				threadShare_Client.start();
				tempSV = false;
			}
		}
	});

	Thread CheckEndMeeting = new Thread(() -> {
		while (tempEnd) {
			if (processing.getCheckEndMeeting()) {
				Platform.runLater(() -> {
					Alert alert = new Alert(Alert.AlertType.INFORMATION);
					alert.setTitle("Thông báo");
					alert.setHeaderText(null);
					alert.setContentText("Giảng viên đã rời khỏi cuộc họp");
					alert.showAndWait();
				});
				tempEnd = false;
				try {
					Thread.sleep(3000);
					Platform.runLater(() -> {
						Stage currentStage = (Stage) btnLeave.getScene().getWindow();
						currentStage.close();
					});
				} catch (InterruptedException e) {}
			}
		}
	});

	public void StopShare() {

		Platform.runLater(() -> {
			ButtonType buttonYes = new ButtonType("Yes", ButtonBar.ButtonData.YES);
			ButtonType buttonNo = new ButtonType("No", ButtonBar.ButtonData.NO);
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Thông báo");
			alert.setHeaderText(null); // Không có tiêu đề phụ
			alert.setContentText("Có một người dùng muốn chia sẻ màn hình\nBạn có muốn chấp nhận không?");
			alert.getButtonTypes().setAll(buttonYes, buttonNo);
			Optional<ButtonType> result = alert.showAndWait();
			if (result.get() == buttonYes) {
				connect.sendAcceptShare();
				DatagramSocket tempSocket = threadShare_Server.getSocket();
				threadShare_Server.stopShare_Server();

				Thread threadLoad_Server = new Thread(threadLoad_Task_Server);
				threadLoad_Server.setPriority(Thread.MAX_PRIORITY);
				threadLoad_Server.setDaemon(true);
				threadLoad_Server.start();

				threadShare_to_client = new ThreadShare_ToClients(tempSocket, imgViewMeeting);
				threadShare_to_client.setDaemon(true);
				threadShare_to_client.start();
			}
		});
	}
	@FXML
	public void btnClicked_Micro(ActionEvent event) {
		try {
			if (Singleton.getInstance().getRole().equals("GiangVien")) {
				thread_sv.start_mic();
				thread_sv.server_voice_send();
				stopMic1.setVisible(true);
				btnMicro.setVisible(false);
			} else {
				
				thread_cli.start_mic();
				thread_cli.client_voice_send();
				stopMic1.setVisible(true);
				btnMicro.setVisible(false);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@FXML
	public void btnClicked_Leave(ActionEvent event) {
		try {
			if (Singleton.getInstance().getRole().equals("GiangVien")) {
				String id_meeting = connect.get_IP_Port_Server();
				System.out.println("id_meeting: " + id_meeting);
				connect.sendEndMeeting();
				handlemain.deleteMessage(Singleton.getInstance().getID_Group());
				Stage currentStage = (Stage) btnLeave.getScene().getWindow();
				currentStage.close();
				handlemain.DeleteMeeting(id_meeting);
				
				connect.stopProcessing();
				if (threadShare_Server != null && threadShare_Server.isAlive())
					threadShare_Server.stopShare_Server();
//				if (threadLoad_Server != null && threadLoad_Server.isAlive()) {
//					threadLoad_Server.interrupt();
//					threadLoad_Task_Server.closeThread_Load_Of_Server();
//				}
//				if (threadShare_to_client != null && threadShare_to_client.isAlive())
//					threadShare_to_client.stopShare_Server();
				tempGV = false;
				check_leave = false;
				if (thread_sv != null)
					thread_sv.closeThread_Server();
//				if (receiverThread != null && receiverThread.isAlive())
//					receiverThread.interrupt();

				Singleton.getInstance().RemoveUser_EndMeeting();
			} else {
				Stage currentStage = (Stage) btnLeave.getScene().getWindow();
				currentStage.close();
				processing.SendNotificationLeave();
				processing.stopProcessing();
				if (threadShare_Client != null && threadShare_Client.isAlive())
					threadShare_Client.stopShare_Client();
//				if (threadLoad_Client != null && threadLoad_Client.isAlive())
//					threadLoad_Client.interrupt();
				if (thread_cli != null)
					thread_cli.closeThread_Client();
//				if (clientThread != null && clientThread.isAlive())
//					clientThread.interrupt();

				tempLoad = false;
				tempSV = false;
				tempEnd = false;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	Thread Check_SV_Leave = new Thread(() -> {
		while (check_leave) {
			if (connect.Get_Flag_Check_leave()) {
				String message_leave = connect.GetMessageLeave_Of_Client();
				Platform.runLater(() -> {
					Alert alert = new Alert(Alert.AlertType.INFORMATION);
					alert.setTitle("Thông báo");
					alert.setHeaderText(null);
					alert.setContentText(message_leave);
					alert.showAndWait();
				});
				connect.Set_Flag_Check_leave(false);
			}
		}
	});

	public void chatBox_client_receive() {
		Thread clientThread = new Thread(() -> {
			DatagramSocket socketmessage = null;

			try {
				// Tạo DatagramSocket bên ngoài vòng lặp
				socketmessage = new DatagramSocket(9000);

				while (true) {
					byte[] receiveData = new byte[1024];
					DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);

					// Sử dụng socket hiện tại để nhận gói tin
					socketmessage.receive(receivePacket);

					String message = new String(receivePacket.getData(), 0, receivePacket.getLength());
					System.out.println("Client nhận: " + message);
					handlemain.SaveMessage(Singleton.getInstance().getID_Administrator(),
							Singleton.getInstance().getID_Group(), message);
					// Cập nhật tin nhắn trên màn hình (TextArea)
					messageArea.appendText(message + "\n");
				}
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				// Đảm bảo đóng socket khi không cần thiết
				if (socketmessage != null && !socketmessage.isClosed()) {
					socketmessage.close();
				}
			}
		});

		clientThread.start();
	}

	public void chatBox_client_send() {
		try {
			String message = messageArea1.getText();
			String name = Singleton.getInstance().getNameuser();
			String messageToSend = name + " : " + message + "\n";
			messageArea.appendText(messageToSend);

			// Gửi tin nhắn đến server
			byte[] byteMessage = messageToSend.getBytes();
			String ipaddress = processing.getIP();

			InetAddress address_sv = null;

			socketmessage = new DatagramSocket();

			address_sv = InetAddress.getByName(ipaddress);

			DatagramPacket sendPacket = new DatagramPacket(byteMessage, byteMessage.length, address_sv, 3344);

			socketmessage.send(sendPacket);
		} catch (IOException e) {
			// Xử lý lỗi gửi gói tin
			e.printStackTrace();
		}

		// Xóa nội dung trong TextField sau khi gửi
		messageArea1.clear();
	}

	public void chatBox_server_send() {
		try {
			String message = messageArea1.getText();
			String name = Singleton.getInstance().getNameuser();
			String messageToSend = name + " : " + message + "\n";
			messageArea.appendText(messageToSend);
			// Gửi tin nhắn đến từng client
			DatagramSocket socketmessage_sv = new DatagramSocket();
			byte[] byteMessage = messageToSend.getBytes();
			for (DatagramPacket value : Singleton.getInstance().getUsers()) {
				DatagramPacket sendPacket = new DatagramPacket(byteMessage, byteMessage.length, value.getAddress(),
						9000);
				socketmessage_sv.send(sendPacket);
				// Xóa nội dung trong TextField sau khi gửi
				messageArea1.clear();
			}

		} catch (Exception e) {

		}

	}

	public void chatBox_server_receive() {
		// Bắt đầu luồng nhận tin nhắn từ client
		Thread receiverThread = new Thread(() -> {
			try {
				// Tạo DatagramSocket bên ngoài vòng lặp
				DatagramSocket socketmessage_sv = new DatagramSocket(3344);

				while (true) {
					DatagramPacket packet = new DatagramPacket(incoming, incoming.length);

					// Nhận gói tin từ socket hiện tại
					socketmessage_sv.receive(packet);

					// Thêm người dùng vào danh sách
					Singleton.getInstance().setUsers(packet);

					String message = new String(packet.getData(), 0, packet.getLength());
					handlemain.SaveMessage(Singleton.getInstance().getID_Administrator(),
							Singleton.getInstance().getID_Group(), message);
					System.out.println("Server received: " + message);
					// Hiển thị tin nhắn lên TextArea
					messageArea.appendText(message + "\n");
				}
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				// Đảm bảo rằng socket sẽ được đóng khi không còn cần thiết
				if (socketmessage_sv != null && !socketmessage_sv.isClosed()) {
					socketmessage_sv.close();
				}
			}
		});
		receiverThread.start();
	}

	private void sendMicrophoneStatusToStudents() {
		try {
			for (DatagramPacket value : Singleton.getInstance().getUsers()) {
				String statusMessage = isMicrophoneStatus ? "#blockMic" : "#unblockMic";
				byte[] sendData = statusMessage.getBytes();
				DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, value.getAddress(), value.getPort());
				serverSocket.send(sendPacket);
			}
		} catch (IOException e) {}
	}

	private void sendScreenStatusToStudents() {

		try {
			for (DatagramPacket value : Singleton.getInstance().getUsers()) {
				String statusMessage = isScreenStatus ? "#blockScreen" : "#unblockScreen";
				byte[] sendData = statusMessage.getBytes();
				DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, value.getAddress(), value.getPort());
				serverSocket.send(sendPacket);
			}
		} catch (IOException e) {}
	}

	Thread ReceiveStatusMic = new Thread(() -> {
		try {
			while (true) {
				if (processing.getMessageBlock()!=null && processing.getMessageBlock().equals("#blockMic")) {
					btnMicro.setDisable(true);
					img_mic.setVisible(false);
					img_block_mic.setVisible(true);
					//System.out.print(processing.getMessageBlock());
				} else if (processing.getMessageBlock()!=null && processing.getMessageBlock().equals("#unblockMic")) {
					btnMicro.setDisable(false);
					img_mic.setVisible(true);
					img_block_mic.setVisible(false);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	});

	Thread ReceiveStatusScreen = new Thread(() -> {
		try {

			while (true) {
				if (processing.getMessageBlock()!=null && processing.getMessageBlock().equals("#blockScreen")) {
					btnShare.setDisable(true);
					img_screen.setVisible(false);
					img_block_screen.setVisible(true);
				} else if (processing.getMessageBlock()!=null && processing.getMessageBlock().equals("#unblockScreen")) {
					btnShare.setDisable(false);
					img_screen.setVisible(true);
					img_block_screen.setVisible(false);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	});

	@FXML
	void btnBlockMic_sv(ActionEvent event) {
		if (Singleton.getInstance().getRole().equals("GiangVien")) {
			isMicrophoneStatus = !isMicrophoneStatus;
			sendMicrophoneStatusToStudents();
		}
	}

	@FXML
	void btnBlockScreen_sv(ActionEvent event) {
		if (Singleton.getInstance().getRole().equals("GiangVien")) {
			isScreenStatus = !isScreenStatus;
			sendScreenStatusToStudents();
		}
	}
}