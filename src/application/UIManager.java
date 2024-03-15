package application;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class UIManager {
	static boolean flag = false;
    public static void showForm(String formName) throws IOException {
        Stage stage = new Stage();
        Parent root = FXMLLoader.load(UIManager.class.getResource("FXML/" + formName + ".fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public static void closeForm(Stage stage) {
        stage.close();
    }
    public static boolean closeFormMeeting(Stage stage) {
    	stage.setOnCloseRequest(event-> {
    		flag = true;
    		System.out.println("Close form");
    	});
    	return flag;
    }
}
