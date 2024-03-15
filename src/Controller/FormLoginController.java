package Controller;

import java.io.IOException;
import ConnectDB.Login;
import application.Singleton;
import application.UIManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class FormLoginController{
    private Login login = new Login();
	

    @FXML
    private Button btnLogin;

    @FXML
    private Button btnLogin_anmt;

    @FXML
    private AnchorPane formAnimation;
    
    @FXML
    private TextField txtShowPass;

    @FXML
    private AnchorPane formLogin;

    @FXML
    private Hyperlink hpForgotPassword;

    @FXML
    private CheckBox cbShowPassword;
    
    @FXML
    private TextField txtEmail;

    @FXML
    private PasswordField txtPassword;
    
    public Stage getStage() {
        return (Stage) btnLogin.getScene().getWindow();
    }

    @FXML
    void btnClickedLogin(ActionEvent event) {
		String Role = login.Login( txtEmail.getText(), txtPassword.getText() );
		Singleton.getInstance().setRole(Role);
		Singleton.getInstance().setNameUser(login.GetNameUser(txtEmail.getText()));
		Singleton.getInstance().setID_Administrator(login.getID_User(txtEmail.getText()));
		if(Role.equals("GiangVien") || Role.equals("SinhVien")) {
			try {
				UIManager.showForm("FormMain");
				UIManager.closeForm(getStage());
				Singleton.getInstance().setEmail(txtEmail.getText());
			} catch (IOException e) {}
		} else if(Role.equals("Admin")) {
			try {
				UIManager.showForm("FormAdmin");
				UIManager.closeForm(getStage());
				Singleton.getInstance().setEmail(txtEmail.getText());
			} catch (IOException e) {} 
		} else {
			Alert alert = new Alert(Alert.AlertType.WARNING);
	        alert.setTitle("WARNING");
	        alert.setHeaderText(null);
	        alert.setContentText("Login Failed! \n Please try again!");
	        alert.showAndWait();
		}
    }
    
    @FXML
    void Clicked_ForgetPassword(ActionEvent event) {
    	try {
			UIManager.showForm("ForgetPassword");
		} catch (IOException e) {}
    }
    
    @FXML
    void Clicked_ShowPassword(ActionEvent event) {
    	if(cbShowPassword.isSelected()) {
    		txtPassword.setVisible(false);
    		txtShowPass.setText(txtPassword.getText());
    		txtShowPass.setVisible(true);
    	} else {
    		txtPassword.setVisible(true);
    		txtShowPass.setVisible(false);
    	}
    }
}
