package Controller;

import java.io.IOException;
import java.net.InetAddress;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import ConnectDB.HandleMain;
import application.Singleton;
import application.UIManager;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;


public class FormMainController {
	private static InetAddress ipAddress;
	private final int PORT_SERVER = 9999;
	private volatile boolean C_Setting = true;
	private volatile boolean C_Check = true;
	
	private HandleMain handlemain = new HandleMain();
	
    @FXML
    private Button btnGroup;
    
    @FXML
    private Button btnChangePassword;
    
    @FXML
    private AnchorPane pnSetting;
    
    @FXML
    private Button btnSetting;

    @FXML
    private Button btnHome;
    
    @FXML
    private Button btnLogout;

    @FXML
    private ImageView btnUser;

    @FXML
    private Button btn_Group;

    @FXML
    private FlowPane flowPane_btn;

    @FXML
    private ImageView imgview_Setting;

    @FXML
    private Text txtNameUser;

    @FXML
    private Text txtRole;
    
    @FXML
    public void initialize() {
	    btn_Group.setManaged(false);
        String username = Singleton.getInstance().getNameuser();
        String role = Singleton.getInstance().getRole();
        LocalTime currentTime = LocalTime.now();
        int hour = currentTime.getHour();
        int minute = currentTime.getMinute();
        
        if(role.equals("GiangVien")) {
            txtRole.setText("Giảng viên:");
        	txtNameUser.setText(username);        	
        } else {
            txtRole.setText("Sinh viên:");
        	txtNameUser.setText(username);  
    	    btnGroup.setManaged(false);
        }
	    if(Singleton.getInstance().getRole().equals("GiangVien")) {
	    	for(String value : handlemain.GetNameGroup_ByGV(Singleton.getInstance().getID_Administrator())) {
		        Button newButton = new Button("New Button");
		        newButton.setPrefHeight(155);
		        newButton.setPrefWidth(155);
		        newButton.setText(value);
		        newButton.setStyle("-fx-background-color: #00a8ff; -fx-font-size: 18px; -fx-text-fill: #fff;");
		        
		        newButton.setOnMouseEntered(event -> {
		            newButton.setStyle("-fx-background-color: #0fbcf9; -fx-font-size: 18px; -fx-text-fill: #fff;");
		        });
		        newButton.setOnMouseExited(event -> {
		            newButton.setStyle("-fx-background-color: #00a8ff; -fx-font-size: 18px; -fx-text-fill: #fff;");
		        });
		        
		        newButton.setOnAction(e -> btnClicked_Group(e));
		        flowPane_btn.getChildren().add(newButton);  
	    	}
	    } else {
	    	for(String value : handlemain.GetNameGroup_BySV(Singleton.getInstance().getID_Administrator().substring(0, 5))) {
		        Button newButton = new Button("New Button");
		        newButton.setPrefHeight(155);
		        newButton.setPrefWidth(155); 
		        newButton.setText(value);
		        newButton.setStyle("-fx-background-color: #00a8ff; -fx-font-size: 18px; -fx-text-fill: #fff;");
		        
		        int lastSpaceIndex = newButton.getText().lastIndexOf(" ");
		        String id = newButton.getText().substring(lastSpaceIndex + 1);
		        
		        LocalTime timestart = handlemain.GetTimeStart(id);
		        if(timestart != null) {
			        int gio = timestart.getHour();
			        int phut = timestart.getMinute();
			        if(((hour-gio)==0 && (minute-phut)<=10) || ((24-hour+gio)==1 && (60-minute+phut)<=10)) {
			        	newButton.setStyle("-fx-background-color: #00a8ff; -fx-font-size: 18px; -fx-text-fill: #fff; -fx-font-weight: bold;");
				        newButton.setOnMouseEntered(event -> {
				            newButton.setStyle("-fx-background-color: #0fbcf9; -fx-font-size: 18px; -fx-text-fill: #fff; -fx-font-weight: bold;");
				        });
				        newButton.setOnMouseExited(event -> {
				            newButton.setStyle("-fx-background-color: #00a8ff; -fx-font-size: 18px; -fx-text-fill: #fff; -fx-font-weight: bold;");
				        });
				        newButton.setOnAction(e -> btnClicked_Group(e));
				        flowPane_btn.getChildren().add(newButton);  
			        } else {
			        	newButton.setStyle("-fx-background-color: #00a8ff; -fx-font-size: 18px; -fx-text-fill: #fff;");
				        newButton.setOnMouseEntered(event -> {
				            newButton.setStyle("-fx-background-color: #0fbcf9; -fx-font-size: 18px; -fx-text-fill: #fff;");
				        });
				        newButton.setOnMouseExited(event -> {
				            newButton.setStyle("-fx-background-color: #00a8ff; -fx-font-size: 18px; -fx-text-fill: #fff;");
				        });
				        newButton.setOnAction(e -> btnClicked_Group(e));
				        flowPane_btn.getChildren().add(newButton);  
				    } 
		        } else {
		        	newButton.setStyle("-fx-background-color: #00a8ff; -fx-font-size: 18px; -fx-text-fill: #fff;");
			        newButton.setOnMouseEntered(event -> {
			            newButton.setStyle("-fx-background-color: #0fbcf9; -fx-font-size: 18px; -fx-text-fill: #fff;");
			        });
			        newButton.setOnMouseExited(event -> {
			            newButton.setStyle("-fx-background-color: #00a8ff; -fx-font-size: 18px; -fx-text-fill: #fff;");
			        });
			        newButton.setOnAction(e -> btnClicked_Group(e));
			        flowPane_btn.getChildren().add(newButton);  
			    } 
	    	}
	    }
    }

    @FXML
    void btnClickedGroup(ActionEvent event) {
		try {
			UIManager.showForm("FormAddGroup");
		} catch (IOException e) {}
    }

    @FXML
    void btnClickedHome(ActionEvent event) {
        try {
			UIManager.closeForm(getStage());
			UIManager.showForm("FormMain");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Stage getStage() {
        return (Stage) btnHome.getScene().getWindow();
    }
	
	static {
		try {
			ipAddress = InetAddress.getLocalHost();
		} catch(Exception e) {}
	}
    
    @FXML
    void btnClicked_Group(ActionEvent event) {
        Button clickedButton = (Button) event.getSource(); 
        int lastSpaceIndex = clickedButton.getText().lastIndexOf(" ");
    	String idgroup = clickedButton.getText().substring(lastSpaceIndex + 1);
        LocalTime currentTime = LocalTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        String time = currentTime.format(formatter);
        
    	String id_meeting_server = ipAddress.getHostAddress() + " " + PORT_SERVER;
    	String check = handlemain.getID_Meeting(idgroup);

    	//Code sai, mai sua lai
    	Singleton.getInstance().setIPAddress(id_meeting_server.split(" ")[0]);
    	Singleton.getInstance().setPort(id_meeting_server.split(" ")[1]);
    	
        ButtonType buttonTypeYes = new ButtonType("Yes", ButtonBar.ButtonData.YES);
        ButtonType buttonTypeNo = new ButtonType("No", ButtonBar.ButtonData.NO);
        Singleton.getInstance().setID_Group(idgroup);
        if(id_meeting_server.equals(check)) {
            if(Singleton.getInstance().getRole().equals("GiangVien")) {
    	        Alert alert = new Alert(AlertType.WARNING);
    	        alert.setTitle("Thông báo");
    	        alert.setHeaderText(null); // Không có tiêu đề phụ
    	        alert.setContentText("Đang có cuộc họp diễn ra, bạn không thể tạo cuộc họp mới!\n"
    	        		+ "Nếu muốn, bạn hãy kết thúc cuộc họp hiện tại!");
    	        alert.showAndWait();
    	        return;
            } 
        }
        
        if(!C_Check) {
	        Alert alert = new Alert(AlertType.WARNING);
	        alert.setTitle("Thông báo");
	        alert.setHeaderText(null); // Không có tiêu đề phụ
	        alert.setContentText("Bạn đã tham gia vào cuộc họp!\n"
	        		+ "Nếu muốn, hãy rời cuộc họp hiện tại!");
	        alert.showAndWait();
	        return;
        }
        
        if(check != null && Singleton.getInstance().getRole().equals("SinhVien")) {
        	String id_meeting_client = handlemain.getID_Meeting(idgroup);// 192.168.1.112 9999
        	Singleton.getInstance().setIPAddress(id_meeting_client.split(" ")[0]);
        	Singleton.getInstance().setPort(id_meeting_client.split(" ")[1]);    

	        Alert alert = new Alert(AlertType.INFORMATION);
	        alert.setTitle("Thông báo");
	        alert.setHeaderText(null); // Không có tiêu đề phụ
	        alert.setContentText("Bạn có muốn tham gia cuộc họp hay không?");
	        alert.getButtonTypes().setAll(buttonTypeYes, buttonTypeNo);
	        Optional<ButtonType> result = alert.showAndWait();
	        if(result.get() == buttonTypeYes) {
	    		try {
	    			UIManager.showForm("FormMeeting");
	    		} catch (IOException e) {}
	    		C_Check = false;
	        } 
	        return;
        } else if(Singleton.getInstance().getRole().equals("GiangVien")) {
           
	        Alert alert = new Alert(AlertType.INFORMATION);
	        alert.setTitle("Thông báo");
	        alert.setHeaderText(null); // Không có tiêu đề phụ
	        alert.setContentText("Bạn có xác nhận tạo cuộc họp hay không?");
	        alert.getButtonTypes().setAll(buttonTypeYes, buttonTypeNo);
	        Optional<ButtonType> result = alert.showAndWait();
	        if(result.get() == buttonTypeYes) {
	        	clickedButton.setStyle("-fx-background-color: #00a8ff; -fx-font-size: 18px; -fx-text-fill: #fff; -fx-font-weight: bold;");
	        	clickedButton.setOnMouseEntered(mouseEvent -> {
	        		clickedButton.setStyle("-fx-background-color: #0fbcf9; -fx-font-size: 18px; -fx-text-fill: #fff; -fx-font-weight: bold;");
		        });
	        	clickedButton.setOnMouseExited(mouseEvent -> {
	        		clickedButton.setStyle("-fx-background-color: #00a8ff; -fx-font-size: 18px; -fx-text-fill: #fff; -fx-font-weight: bold;");
		        });
	    		try {
	    			
	    			handlemain.CreateMeeting(id_meeting_server, idgroup, time);
	    			UIManager.showForm("FormMeeting");
	    		} catch (IOException e) {}
	        }        	
        } else
			try {
				if(check == null && Singleton.getInstance().getRole().equals("SinhVien")) {
				    Alert alert = new Alert(AlertType.WARNING);
				    alert.setTitle("Thông báo");
				    alert.setHeaderText(null); // Không có tiêu đề phụ
				    alert.setContentText("Hiện tại chưa có cuộc họp nào được tổ chức cả!");
				    alert.showAndWait();
				    return;
				}
			} catch (Exception e) {}
    }
    
    @FXML
    void btnClicked_Logout(ActionEvent event) {
	    Platform.runLater(() -> {
	        ButtonType buttonYes = new ButtonType("Yes", ButtonBar.ButtonData.YES);
	        ButtonType buttonNo = new ButtonType("No", ButtonBar.ButtonData.NO);
	        Alert alert = new Alert(AlertType.INFORMATION);
	        alert.setTitle("Thông báo");
	        alert.setHeaderText(null); // Không có tiêu đề phụ
	        alert.setContentText("Xác nhận đăng xuất?");
	        alert.getButtonTypes().setAll(buttonYes, buttonNo);
	        Optional<ButtonType> result = alert.showAndWait();
	        if (result.get() == buttonYes) {
				Platform.runLater(() -> {
					Alert _alert = new Alert(Alert.AlertType.INFORMATION);
					_alert.setTitle("Thông báo");
					_alert.setHeaderText(null);
					_alert.setContentText("Đăng xuất thành công");
					_alert.showAndWait(); 
					
	                Stage currentStage = (Stage) btnLogout.getScene().getWindow();
	                currentStage.close();
	                try {
						UIManager.showForm("FormLogin");
					} catch (Exception e) {}
				});
	        }
	    });
    }
    
    @FXML
    void btnClickedSetting(ActionEvent event) {
		if(C_Setting) {
			pnSetting.setVisible(true);
			C_Setting = false;
		} else {
			pnSetting.setVisible(false);	
			C_Setting = true;
		}
    }
    
    @FXML
    void btnClicked_ChangePassword(ActionEvent event) {
        try {
			UIManager.showForm("FormChangePassword");
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
}
