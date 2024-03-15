package Controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;

import javafx.scene.control.TextField;
import javafx.stage.Stage;
import ConnectDB.HandleMain;
import application.Singleton;
import application.UIManager;
import javafx.application.Platform;
import javafx.event.ActionEvent;

public class FormChangePasswordController {
	HandleMain handle = new HandleMain();

	@FXML
	private Button btnChange;
	@FXML
	private TextField txtOldPass;
	@FXML
	private TextField txtNewPass;
	@FXML
	private TextField txtRetypePass;

	// Event Listener on Button[#btnChange].onAction
	@FXML
	public void btnClicked_ChangePassword(ActionEvent event) {
		String email = Singleton.getInstance().getEmail();
		if(handle.CheckPassword(email, txtOldPass.getText())) {
			if(txtNewPass.getText().equals(txtRetypePass.getText())) {
				handle.ChangePassword(email, txtNewPass.getText());
				Platform.runLater(() -> {
					Alert alert = new Alert(Alert.AlertType.INFORMATION);
					alert.setTitle("Thông báo");
					alert.setHeaderText(null);
					alert.setContentText("Thay đổi mật khẩu thành công!");
					alert.showAndWait();
				});
			} else {
				Platform.runLater(() -> {
					Alert alert = new Alert(Alert.AlertType.WARNING);
					alert.setTitle("Cảnh báo");
					alert.setHeaderText(null);
					alert.setContentText("Mật khẩu mới không khớp\nVui lòng nhập lại!");
					alert.showAndWait();
				});
				txtNewPass.setText("");
				txtRetypePass.setText("");
			}
		} else {
			Platform.runLater(() -> {
				Alert alert = new Alert(Alert.AlertType.WARNING);
				alert.setTitle("Cảnh báo");
				alert.setHeaderText(null);
				alert.setContentText("Mật khẩu cũ không chính xác!\nVui lòng nhập lại!");
				alert.showAndWait();
			});
			txtOldPass.setText("");
		}
	}
}








