package View;

import Model.*;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.io.File;
import java.util.List;

public class UploadSongWindow implements Window{
    public static void display(){
        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        GridPane layout = new GridPane();

        Label windowTitle = new Label("UPLOAD NEW SONG");
        Label titleLabel = new Label("Title: ");
        Label artistLabel = new Label("Artist: ");
        Label albumLabel = new Label("Album: ");
        Label genreLabel = new Label("Genre: ");
        Label yearLabel = new Label("Year: ");
        TextField titleInput = new TextField();
        TextField artistInput = new TextField();
        TextField albumInput = new TextField();
        ComboBox genreInput = new ComboBox<>();
        TextField yearInput = new TextField();
        TextField fileSelected = new TextField();

        TextField cheatTextField = new TextField();

        fileSelected.setEditable(false);
        Button selectFile = new Button("Choose Song");
        Button uploadBtn = new Button("Upload");
        genreInput.setPromptText("Select Genre");
        genreInput.getItems().addAll("KPOP", "OPM", "Rock", "Pop", "Ballad", "RNB");
        genreInput.getSelectionModel().selectFirst();

        layout.setPadding(new Insets(10, 10, 10, 10));
        layout.setVgap(5);
        layout.setHgap(5);

        GridPane.setConstraints(windowTitle, 1, 0);
        GridPane.setConstraints(titleLabel, 0, 1);
        GridPane.setConstraints(titleInput, 1, 1);
        GridPane.setConstraints(artistLabel, 0, 2);
        GridPane.setConstraints(artistInput, 1, 2);
        GridPane.setConstraints(albumLabel, 0, 3);
        GridPane.setConstraints(albumInput, 1, 3);
        GridPane.setConstraints(genreLabel, 0, 4);
        GridPane.setConstraints(genreInput, 1, 4);
        GridPane.setConstraints(yearLabel, 0, 5);
        GridPane.setConstraints(yearInput, 1, 5);
        GridPane.setConstraints(selectFile, 0, 6);
        GridPane.setConstraints(fileSelected, 1, 6);
        GridPane.setConstraints(uploadBtn, 1, 7);

        selectFile.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Upload Song");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Audio Files", "*.wav", "*.mp3", "*.aac"));
            File songToUpload = fileChooser.showOpenDialog(null);
            fileSelected.setText(songToUpload.getName());
            cheatTextField.setText(songToUpload.getAbsolutePath());
        });

        /** KULANG PA NG PLAYTIME. **/
        uploadBtn.setOnAction(e -> {
            Database db = new Database();
            SongService service = new SongService(db);
            PlaylistService plservice = new PlaylistService(db);
            List<Playlist> playlists = plservice.getAll();
            PlaylistSongService playlistSongService = new PlaylistSongService(db);

            int checker = 1;
            String title = titleInput.getText();
            String artist = artistInput.getText();
            String album = albumInput.getText();
            String genre = genreInput.getSelectionModel().getSelectedItem().toString();
            String year = yearInput.getText();
            String songName = fileSelected.getText();
            String filename = cheatTextField.getText(); // para sa blob file

            if(title.isEmpty() || filename.isEmpty())
                checker = 0;
            if(checker == 0)
                AlertBox.display("Input Error", "Please input at least title and file");
            else {
                if (artist.isEmpty())
                    artist = "Unknown Artist";
                if (album.isEmpty())
                    album = "Unknown Album";
                if (year.isEmpty())
                    year = "Unknown Year";

                Song s = new Song();
                s.setSongID(service.getAll().size()+1);
                s.setTitle(title);
                s.setArtist(artist);
                s.setAlbumName(album);
                s.setGenre(genre);
                s.setSongName(songName);
                s.setYear(year);
                s.setUsername(LoginWindow.getLoggedUser());
                s.setFilename(filename);

                int add = 1;
                for(Song song : service.getAll())
                    if(song.getSongName().equals(s.getSongName()))
                        add = 0;
                if(add == 1)
                    service.add(s);
                else
                    AlertBox.display("Error in Uploading Song", "Bobo ka ba? Inupload mo na tong song na to eh.");

                PlaylistService ps = new PlaylistService(db);
                // automatically adds the song to the default playlists of the user.
                for(Playlist playlist : playlists)
                    if((playlist.getName().equals("My Songs") || playlist.getName().equals("Most Played Songs"))
                            && playlist.getUsername().equals(LoginWindow.getLoggedUser())) {
                        PlaylistSong.PK++;
                        playlistSongService.add(playlist, s);
                        ps.upload(playlist);
                    }

                titleInput.setText("");
                artistInput.setText("");
                albumInput.setText("");
                yearInput.setText("");
                fileSelected.setText("");
            }
        });
        layout.getChildren().addAll(windowTitle, titleLabel, titleInput, artistLabel, artistInput, albumLabel, albumInput,
                genreLabel, genreInput, yearLabel, yearInput, selectFile, fileSelected, uploadBtn);

        window.setScene(new Scene(layout, 300, 300));
        window.setTitle("UPLOAD NEW SONG");
        window.showAndWait();
    }
}