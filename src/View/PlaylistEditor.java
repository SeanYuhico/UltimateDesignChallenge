package View;

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

public class PlaylistEditor {
    public static void addNewPlaylist(){
        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        GridPane layout = new GridPane();

        Label title = new Label("NEW PLAYLIST");
        TextField playlistInput = new TextField();
        Button create = new Button("Create Playlist");
        Button cancel = new Button("Cancel");
        playlistInput.setPromptText("Name this playlist");

        layout.setPadding(new Insets(10, 10, 10, 10));
        layout.setVgap(5);
        layout.setHgap(5);

        GridPane.setConstraints(title, 1, 0);
        GridPane.setConstraints(playlistInput, 1, 1);
        GridPane.setConstraints(create, 1, 2);
        GridPane.setConstraints(cancel, 1, 3);

        create.setOnAction(e -> {
            Database db = new Database();
//            AccountService accService = new AccountService(db);
            PlaylistService plService = new PlaylistService(db);
            List<Playlist> playlists = plService.getAll();

            String playlistName = playlistInput.getText();

            Playlist p = new Playlist();
            p.setPlaylistID(plService.getAll().size()+1);
            p.setName(playlistName);
            p.setSongCount(0);
            p.setUsername(LoginWindow.getLoggedUser());

            for(Playlist playlist : playlists)
                if(playlist.getName().equals(playlistName))
                    AlertBox.display("Wrong Input", "Playlist already exists.");

            plService.add(p);
            window.close();
        });

        cancel.setOnAction(e -> window.close());
        layout.getChildren().addAll(title, playlistInput, create, cancel);

        window.setScene(new Scene(layout, 300, 200));
        window.showAndWait();
    }

    public static void editPlaylist(int playlistID)
    {
        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        GridPane layout = new GridPane();

        Database db = new Database();
        PlaylistService ps = new PlaylistService(db);

        Label title = new Label("RENAME PLAYLIST");
        TextField playlistInput = new TextField();
        Button rename = new Button("Rename");
        Button cancel = new Button("Cancel");
        playlistInput.setPromptText("Input new playlist name");

        layout.setPadding(new Insets(10, 10, 10, 10));
        layout.setVgap(5);
        layout.setHgap(5);

        GridPane.setConstraints(title, 1, 0);
        GridPane.setConstraints(playlistInput, 1, 1);
        GridPane.setConstraints(rename, 1, 2);
        GridPane.setConstraints(cancel, 1, 3);

        rename.setOnAction(e -> {
            String playlistName = playlistInput.getText();

            Playlist p = new Playlist();
            p.setName(playlistName);

            ps.update(p, playlistID);
            window.close();
        });

        cancel.setOnAction(e -> window.close());
        layout.getChildren().addAll(title, playlistInput, rename, cancel);

        window.setScene(new Scene(layout, 300, 200));
        window.showAndWait();
    }
}
