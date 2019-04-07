package View;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class ProfileWindow implements Window{

    public static void display() {
        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("My Profile");
        GridPane layout = new GridPane();

        Label faveSongs = new Label("Favorite Songs");
        Label favePlaylists = new Label("Favorite Playlists");
        Label username = new Label(LoginWindow.getLoggedUser());
        ImageView profile = new ImageView(new Image("File:\\Users\\Legs\\Desktop\\dc2\\src\\Pictures\\profile.png"));

        layout.setPadding(new Insets(10, 10, 10, 10));
        layout.setVgap(10);
        layout.setHgap(10);

        GridPane.setConstraints(username, 1, 1);
        GridPane.setConstraints(profile, 2,1);
        GridPane.setConstraints(faveSongs, 1, 2);
        GridPane.setConstraints(favePlaylists, 1, 3);

        faveSongs.setOnMouseClicked(e -> {

            window.close();
            FaveSongWindow.display();
        });

        favePlaylists.setOnMouseClicked(e -> {
            FavePlaylistWindow.display();
            window.close();
        });

        layout.getChildren().addAll(username, profile, faveSongs, favePlaylists);
        window.setScene(new Scene(layout, 300, 200));
        window.showAndWait();
    }
}
