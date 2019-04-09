package View;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;


public class Main extends Application {
    public static Stage mainStage;
    @Override
    public void start(Stage primaryStage) throws IOException {
        mainStage = primaryStage;
        Parent root = FXMLLoader.load(getClass().getResource("/View/LoginArtist.FXML"));
        mainStage.setScene(new Scene(root, 599, 417));
    }

    public static void main(String[] args) {
        launch(args);
    }

    public static Stage getMainStage() {
        return mainStage;
    }
}