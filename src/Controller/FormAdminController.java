package Controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.Optional;

import ConnectDB.AdminManage;
import application.UIManager;
import javafx.application.Platform;
import javafx.event.ActionEvent;

import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

public class FormAdminController {
	AdminManage admin_manage = new AdminManage();
	@FXML
	private Button btnAddAccount;
	@FXML
	private Button btnLogout;
	@FXML
	private Button btnFeedback;
	@FXML
	private Label QuantityFeedback;
    @FXML
    private BorderPane borderPaneMain;
    
    public void initialize() throws UnknownHostException {
    	QuantityFeedback.setText(admin_manage.getQuantityFeedback()+"");
    }

	// Event Listener on Button[#btnAddAccount].onAction
	@FXML
	public void btnClickedAccount(ActionEvent event) {
		try {
			Parent root = FXMLLoader.load(UIManager.class.getResource("FXML/FormAddUser.fxml"));
			borderPaneMain.setRight(root);
		} catch (IOException e) {}
	}
	// Event Listener on Button[#btnLogout].onAction
	@FXML
	public void btnLogout(ActionEvent event) {
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
	// Event Listener on Button[#btnFeedback].onAction
	@FXML
	public void btnClickedFeedback(ActionEvent event) {
		borderPaneMain.setRight(null);
		try {
			Parent root = FXMLLoader.load(UIManager.class.getResource("FXML/FormFeedback.fxml"));
			borderPaneMain.setRight(root);
		} catch (IOException e) {}
	}
}











