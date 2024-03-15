package Controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;

import javafx.scene.control.TextField;
import javafx.stage.Stage;
import ConnectDB.Login;
import application.UIManager;
import javafx.event.ActionEvent;

public class ForgetPasswordController {
	Login login = new Login();
	@FXML
	private TextField txtEmail;
	@FXML
	private Button btnForgetPass;
	
    public Stage getStage() {
        return (Stage) btnForgetPass.getScene().getWindow();
    }

	// Event Listener on Button[#btnForgetPass].onAction
	@FXML
	public void btnClicked_ForgetPass(ActionEvent event) {
		if(txtEmail.getText()!="") {
			Alert alert = new Alert(Alert.AlertType.INFORMATION);
	        alert.setTitle("Thông báo");
	        alert.setHeaderText(null);
	        alert.setContentText("Mật khẩu mới sẽ được chúng tôi cung cấp qua tài khoản đăng nhập của bạn.\nVui lòng chờ xử lý!");
	        alert.showAndWait(); 
	        login.ForgetPassword(txtEmail.getText());
	        try {
	        	UIManager.closeForm(getStage());
	        } catch(Exception e) {}			
		}
	}
}
