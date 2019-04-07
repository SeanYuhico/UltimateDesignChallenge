package View;

import Controller.MainController;
import Model.*;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.List;



public class DisplayGenres implements  PlaylistInterface {

    @Override
    public void group(Label playlistNameLbl, VBox playlistVBox, VBox dashboardVBox, Pane dashboardPane, Pane playlistPane, MainController controller) {

        initialize(playlistVBox);

        ArrayList<Playlist> genrePlaylists = new ArrayList<>();

        for(String genre : getAllGenres()) {
            GenrePlaylistBuilder gpb = new GenrePlaylistBuilder();
            int howManySongs = 0;
            gpb.buildName(genre);
            for(Song s : ss.getAll())
                if(s.getUsername().equals(LoginWindow.getLoggedUser()) && s.getGenre().equals(genre))
                    howManySongs++;
            gpb.buildSongCount(howManySongs);
            gpb.buildUsername();
            genrePlaylists.add(gpb.getPlaylist());
        }

        for(Playlist playlist : genrePlaylists)
            playlistVBox.getChildren().add(new PlaylistHBox(playlistNameLbl, playlist, dashboardVBox, dashboardPane, playlistPane, controller));
    }

    public List<String> getAllGenres() {
        List<String> genres = new ArrayList<>();
        for (Song s : ss.getAll())
            if (s.getUsername().equals(LoginWindow.getLoggedUser()))
                if (!(genres.contains(s.getGenre())))
                    genres.add(s.getGenre());
        return genres;
    }

    @Override
    public void initialize(VBox vbox) {
        vbox.getChildren().clear();
    }
}
