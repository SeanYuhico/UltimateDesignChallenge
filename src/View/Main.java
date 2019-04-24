package View;

import ClientControl.ClientController;
import Model.Database;
import Model.PlaylistSongService;
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
        mainStage.setTitle("Welcome to REAL Music Player");
        mainStage.setResizable(false);
        mainStage.centerOnScreen();
        Parent root = FXMLLoader.load(getClass().getResource("/View/LoginArtist.FXML"));
        mainStage.setScene(new Scene(root, 789, 417));
        mainStage.show();

        ClientController.getInstance().getMusic();

        Database db = new Database();
        PlaylistSongService pss = new PlaylistSongService(db);
        pss.removeAll();

        ClientController.getInstance().syncAll();
    }

    public static void main(String[] args) {
        launch(args);
    }

    public static Stage getMainStage() {
        return mainStage;
    }
}