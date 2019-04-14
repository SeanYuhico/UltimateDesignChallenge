package View;

import Controller.LoginArtistController;
import Model.*;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.io.File;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.List;

public class UploadSongWindow implements Window{
    public static void display(){
        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        GridPane layout = new GridPane();
        PlaylistService ps = new PlaylistService(new Database());

        Label windowTitle = new Label("UPLOAD NEW SONG");
        Label titleLabel = new Label("Title: ");
//        Label albumLabel = new Label("Album: ");
        Label genreLabel = new Label("Genre: ");
        Label yearLabel = new Label("Year: ");
        TextField titleInput = new TextField();
//        ComboBox<String> albumInput = new ComboBox<>();
        ComboBox<String> genreInput = new ComboBox<>();
        TextField yearInput = new TextField();
        TextField fileSelected = new TextField();

        TextField cheatTextField = new TextField();

        fileSelected.setEditable(false);
        Button selectFile = new Button("Choose Song");
        Button uploadBtn = new Button("Upload");
//        albumInput.setPromptText("Select Album");
//        for(Playlist p : ps.getAll())
//            if(p.isAlbum())
//                albumInput.getItems().add(p.getName());
        genreInput.setPromptText("Select Genre");
        genreInput.getItems().addAll("KPOP", "OPM", "Rock", "Pop", "Ballad", "RNB");
        genreInput.getSelectionModel().selectFirst();

        layout.setPadding(new Insets(10, 10, 10, 10));
        layout.setVgap(5);
        layout.setHgap(5);

        GridPane.setConstraints(windowTitle, 1, 0);
        GridPane.setConstraints(titleLabel, 0, 1);
        GridPane.setConstraints(titleInput, 1, 1);
//        GridPane.setConstraints(albumLabel, 0, 2);
//        GridPane.setConstraints(albumInput, 1, 2);
        GridPane.setConstraints(genreLabel, 0, 3);
        GridPane.setConstraints(genreInput, 1, 3);
        GridPane.setConstraints(yearLabel, 0, 4);
        GridPane.setConstraints(yearInput, 1, 4);
        GridPane.setConstraints(selectFile, 0, 5);
        GridPane.setConstraints(fileSelected, 1, 5);
        GridPane.setConstraints(uploadBtn, 1, 6);

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
            PlaylistSongService playlistSongService = new PlaylistSongService(db);
            TimesPlayedService tps = new TimesPlayedService(db);

            int checker = 1;
            String title = titleInput.getText();
            String genre = genreInput.getSelectionModel().getSelectedItem();
            String year = yearInput.getText();
            String songName = fileSelected.getText();
            String filename = cheatTextField.getText(); // para sa blob file

            if(title.isEmpty() || filename.isEmpty())
                checker = 0;
            if(checker == 0)
                AlertBox.display("Input Error", "Please input at least title and file");
            else {
                if (year.isEmpty())
                    year = "Unknown Year";

                Song s = new Song();
                s.setSongID(service.getAll().size()+1);
                s.setTitle(title);
                s.setArtist(LoginArtistController.getLoggedUser());
//                s.setAlbumName(albumInput.getValue());
                s.setAlbumName("No Album");
                s.setGenre(genre);
                s.setSongName(songName);
                s.setYear(year);
                s.setUsername(LoginArtistController.getLoggedUser());
                s.setFilename(filename);
                s.setDateUploaded(new Timestamp(System.currentTimeMillis()));

                int add = 1;
                for(Song song : service.getAll())
                    if(song.getUsername().equals(LoginArtistController.getLoggedUser()) && song.getSongName().equals(s.getSongName()))
                        add = 0;
                if(add == 1)
                    service.add(s);
                else
                    AlertBox.display("Error in Uploading Song", "Huy inupload mo na tong song na to.");

                // automatically adds the song to the default playlists of the user.
                for(Playlist playlist : ps.getAll())
                    if((playlist.getName().equals("My Songs") || playlist.getName().equals("Most Played Songs") ||
                            playlist.getName().equals("No Album")) && playlist.getUsername().equals(LoginArtistController.getLoggedUser()))
                    {
                        PlaylistSong.PK++;
                        playlistSongService.add(playlist, s);
                        ps.upload(playlist);
                    }

                titleInput.setText("");
                yearInput.setText("");
                fileSelected.setText("");
                tps.add(s.getSongID());
            }
        });
        layout.getChildren().addAll(windowTitle, titleLabel, titleInput, /*albumLabel, albumInput,*/
                genreLabel, genreInput, yearLabel, yearInput, selectFile, fileSelected, uploadBtn);

        window.setScene(new Scene(layout, 300, 300));
        window.setTitle("UPLOAD NEW SONG");
        window.showAndWait();
    }
}