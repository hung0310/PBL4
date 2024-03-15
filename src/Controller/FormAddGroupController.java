package Controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.net.URL;
import java.util.ResourceBundle;
import ConnectDB.CreateGroup;
import application.Singleton;
import javafx.event.ActionEvent;
import javafx.scene.control.ComboBox;

public class FormAddGroupController implements Initializable {
	private CreateGroup creategroup = new CreateGroup();
	private static String lsh;
	@FXML
	private TextField txtNameGroup;
	@FXML
	private ComboBox<String> cbbKhoa;
	@FXML
	private ComboBox<String> cbbLSH;
    @FXML
    private TextField txtKeyGroup;
	@FXML
	private Button btnCreate;
	@FXML
	private Button btnCancel;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
	    if(creategroup.GetKhoa() != null) {
	    	for(String value : creategroup.GetKhoa()) {
	    	    cbbKhoa.getItems().addAll(value);
	    	}
	    }	
	    cbbKhoa.focusedProperty().addListener((observable, oldValue, newValue) -> {
	        if (!newValue) {
	           cbbLSH.getItems().clear();
	    	   for(String value : creategroup.GetLSH(cbbKhoa.getValue())) {
	    		   cbbLSH.getItems().addAll(value);
	    	   }  
            }
        });
	}
	
	@FXML
	public void btnClicked_Create(ActionEvent event) {
		String khoa = cbbKhoa.getSelectionModel().getSelectedItem().toString();
		String lsh = cbbLSH.getSelectionModel().getSelectedItem().toString();
		String id_group = txtKeyGroup.getText() + khoa + lsh;
		String id = khoa + lsh;
		String ID_Administrator = Singleton.getInstance().getID_Administrator();
		
		creategroup.CreateGroup(id_group, txtNameGroup.getText(), lsh, id, ID_Administrator);
		
		Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Thông báo");
        alert.setHeaderText(null);
        alert.setContentText("Bạn đã tạo nhóm thành công!");
        alert.showAndWait();  
		try {
			Stage stage = (Stage) btnCreate.getScene().getWindow(); 
			stage.close(); 
		} catch(Exception e) {}
	}

	@FXML
	public void btnClicked_Cancel(ActionEvent event) {
		try {
			Stage stage = (Stage) btnCancel.getScene().getWindow(); 
			stage.close(); 
		} catch(Exception e) {}
	}
}
