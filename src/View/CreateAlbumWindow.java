package View;

import Controller.LoginArtistController;
import Model.Database;
import Model.Playlist;
import Model.PlaylistService;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.List;

public class CreateAlbumWindow {
    public static void addNewAlbum(){
        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        GridPane layout = new GridPane();

        Label title = new Label("NEW ALBUM");
        TextField albumInput = new TextField();
        Button create = new Button("Create Album");
        Button cancel = new Button("Cancel");
        albumInput.setPromptText("Name this album");

        layout.setPadding(new Insets(10, 10, 10, 10));
        layout.setVgap(5);
        layout.setHgap(5);

        GridPane.setConstraints(title, 1, 0);
        GridPane.setConstraints(albumInput, 1, 1);
        GridPane.setConstraints(create, 1, 2);
        GridPane.setConstraints(cancel, 1, 3);

        create.setOnAction(e -> {
            Database db = new Database();
//            AccountService accService = new AccountService(db);
            PlaylistService plService = new PlaylistService(db);
            List<Playlist> playlists = plService.getAll();

            String playlistName = albumInput.getText();

            Playlist p = new Playlist();
            p.setPlaylistID(plService.getAll().size()+1);
            p.setName(playlistName);
            p.setSongCount(0);
            p.setUsername(LoginArtistController.getLoggedUser());
            p.setAlbum(true);

            for(Playlist playlist : playlists)
                if(playlist.getName().equals(playlistName) && playlist.isAlbum())
                    AlertBox.display("Wrong Input", "Album already exists.");

            plService.add(p);
            window.close();
        });

        cancel.setOnAction(e -> window.close());
        layout.getChildren().addAll(title, albumInput, create, cancel);

        window.setScene(new Scene(layout, 300, 200));
        window.showAndWait();
    }
}
