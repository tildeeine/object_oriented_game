package dodge.fxui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class DodgeApp extends Application{

	@Override
	public void start(final Stage primaryStage) throws Exception {
		primaryStage.setTitle("Dodge");
		primaryStage.setScene(new Scene(FXMLLoader.load(DodgeApp.class.getResource("Dodge.fxml"))));
		primaryStage.show();
	}
	
	public static void main(String[] args) {
		DodgeApp.launch(args);
	}

}
