package View;

import Controller.MainController;
import Model.*;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.util.List;

public class EditSongWindow implements Window{
    public static void display(VBox dashboardVBox, int songToEdit, MainController controller){
        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        GridPane layout = new GridPane();
        Database db = new Database();
        PlaylistSongService pss = new PlaylistSongService(db);
        List<PlaylistSong> playlistSongs = pss.getAll();
        SongService service = new SongService(db);
        PlaylistService ps = new PlaylistService(db);
        List<Playlist> playlists = ps.getAll();
        String exTitle = "";
        String exArtist = "";
        String exAlbum = "";
        String exGenre = "";
        String exYear = "";

        for(int i = 0; i < service.getAll().size(); i++){
            if(i == songToEdit){
                exTitle = service.getAll().get(i).getTitle();
                exArtist = service.getAll().get(i).getArtist();
                exAlbum = service.getAll().get(i).getAlbumName();
                exGenre = service.getAll().get(i).getGenre();
                exYear = service.getAll().get(i).getYear();
            }
        }

        Label windowTitle = new Label("EDIT SONG DETAILS");
        Label titleLabel = new Label("Title: ");
        Label artistLabel = new Label("Artist: ");
        Label albumLabel = new Label("Album: ");
        Label genreLabel = new Label("Genre: ");
        Label yearLabel = new Label("Year: ");
        TextField titleInput = new TextField(exTitle);
        TextField artistInput = new TextField(exArtist);
        TextField albumInput = new TextField(exAlbum);
        TextField genreInput = new TextField(exGenre);
        TextField yearInput = new TextField(exYear);

        Button editBtn = new Button("Edit");

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
        GridPane.setConstraints(editBtn, 1, 6);

        /** DI PA TO TAPOS. KULANG PA NG PLAYTIME. **/
        editBtn.setOnAction(e -> {
            int titleCheck = 1;
            String title = titleInput.getText();
            String artist = artistInput.getText();
            String album = albumInput.getText();
            String genre = genreInput.getText();
            String year = yearInput.getText();

            if(title.isEmpty())
                titleCheck = 0;
            if(titleCheck == 0)
                AlertBox.display("Input Error", "Please input title for song");
            else {
                if (artist.isEmpty())
                    artist = "Unknown Artist";
                if (album.isEmpty())
                    album = "Unknown Album";
                if (genre.isEmpty())
                    genre = "Unknown Genre";
                if (year.isEmpty())
                    year = "Unknown Year";

                Song s = new Song();
                s.setTitle(title);
                s.setArtist(artist);
                s.setAlbumName(album);
                s.setGenre(genre);
                s.setYear(year);
                s.setUsername(LoginWindow.getLoggedUser());

                service.update(songToEdit, s);
                window.close();

                for(Playlist p : playlists)
                    for(PlaylistSong playlistSong : playlistSongs)
                        if(p.getPlaylistID() == playlistSong.getPlaylistID() && s.getSongID() == playlistSong.getSongID())
                            DisplayNonDefault.displayPlaylistSongs(p.getName(), dashboardVBox, controller);
            }
        });
        layout.getChildren().addAll(windowTitle, titleLabel, titleInput, artistLabel, artistInput, albumLabel, albumInput,
                genreLabel, genreInput, yearLabel, yearInput, editBtn);

        window.setScene(new Scene(layout, 300, 300));
        window.setTitle("EDIT SONG DETAILS");
        window.showAndWait();
    }
}
