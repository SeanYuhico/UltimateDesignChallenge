package View;

import Controller.MainController;
import Model.*;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.List;

public class DisplayArtists implements  PlaylistInterface {

    @Override
    public void group(Label playlistNameLbl, VBox playlistVBox, VBox dashboardVBox, Pane dashboardPane, Pane playlistPane, MainController controller) {

        initialize(playlistVBox);

        ArrayList<Playlist> artistPlaylists = new ArrayList<>();
        for(String artist : getAllArtists()) {
            int howManySongs = 0;
            ArtistPlaylistBuilder artPB = new ArtistPlaylistBuilder();
            artPB.buildName(artist);
            for(Song s : ss.getAll())
                if(s.getUsername().equals(LoginWindow.getLoggedUser()) && s.getArtist().equals(artist))
                    howManySongs++;
            artPB.buildSongCount(howManySongs);
            artPB.buildUsername();
            artistPlaylists.add(artPB.getPlaylist());
        }

        for(Playlist playlist : artistPlaylists)
            playlistVBox.getChildren().add(new PlaylistHBox(playlistNameLbl, playlist, dashboardVBox, dashboardPane, playlistPane, controller));
    }

    @Override
    public void initialize(VBox currentVBox) {
        currentVBox.getChildren().clear();
    }

    public List<String> getAllArtists() {
        List<String> artists = new ArrayList<>();
        for (Song s : ss.getAll())
            if (s.getUsername().equals(LoginWindow.getLoggedUser()))
                if (!(artists.contains(s.getArtist())))
                    artists.add(s.getArtist());
        return artists;
    }
}
