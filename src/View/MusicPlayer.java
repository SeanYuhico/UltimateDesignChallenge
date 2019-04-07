package View;

import Model.*;
import View.ConfirmBox;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Paths;

public class MusicPlayer {
    public static void display(Stage primaryStage) {
        try {
            URL urlAaron = new URL("File:\\Users\\aaron\\Desktop\\DesignChallenge2\\DesignChallenge2\\src\\View\\Dashboard.fxml");
            URL urlLegs = new URL("File:\\Users\\Legs\\Desktop\\dc2\\src\\View\\Dashboard.fxml");
            String path = "/Users/seanyuhico/Documents/SCHOOL/DesignChallenge2/src/View/Dashboard.fxml";
            FXMLLoader loader = new FXMLLoader(urlLegs);
            FXMLLoader loaderSean = new FXMLLoader(Paths.get(path).toUri().toURL());
            Parent root = loader.load();
            primaryStage.setTitle("REAL Music Player");
            primaryStage.setScene(new Scene(root, 1155, 700));
            primaryStage.setResizable(false);
            primaryStage.centerOnScreen();
            primaryStage.show();

            primaryStage.setOnCloseRequest(ex ->
            {
                ex.consume();
                Boolean answer = ConfirmBox.display("Exit", "Are you sure you want to exit?");
                if (answer) {
                    PlaylistService.guestLogout();
                    SongService.guestLogout();
                    PlaylistSongService.guestLogout();
                    primaryStage.close();
//                    Converter converter = new Converter();
//                    try {
//                        FileUtils.deleteDirectory(converter.getDirectory());
//                    }
//                    catch (IOException e){
//                        e.printStackTrace();
//                    }
//                }
                }
            });
        } catch (MalformedURLException ex) {
            ex.printStackTrace();
        } catch(IOException ex) {
            ex.printStackTrace();
        }
    }

    public static void close(Stage primaryStage)
    {
        primaryStage.close();
    }
}
