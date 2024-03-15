package application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;


public class Running_Client extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("FXML/FormLogin.fxml"));
            Parent root = loader.load();
            
            primaryStage.setScene(new Scene(root));
            primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
