package View;

import Controller.MainController;
import Model.*;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.List;

public class DisplayAlbumNames implements PlaylistInterface {

    @Override
    public void group(Label playlistNameLbl, VBox playlistVBox, VBox dashboardVBox, Pane dashboardPane, Pane playlistPane, MainController controller) {
        initialize(playlistVBox);

        ArrayList<Playlist> albumPlaylists = new ArrayList<>();
        for(String album : getAllAlbums()) {
            AlbumPlaylistBuilder albPB = new AlbumPlaylistBuilder();
            int howManySongs = 0;
            albPB.buildName(album);
            for(Song s : ss.getAll())
                if(s.getUsername().equals(LoginWindow.getLoggedUser()) && s.getAlbumName().equals(album))
                    howManySongs++;
            albPB.buildSongCount(howManySongs);
            albPB.buildUsername();
            albumPlaylists.add(albPB.getPlaylist());
        }

        for(Playlist playlist : albumPlaylists)
            playlistVBox.getChildren().add(new PlaylistHBox(playlistNameLbl, playlist, dashboardVBox, dashboardPane, playlistPane, controller));
    }

    public List<String> getAllAlbums() {
        List<String> albums = new ArrayList<>();
        for (Song s : ss.getAll())
            if (s.getUsername().equals(LoginWindow.getLoggedUser()))
                if (!(albums.contains(s.getAlbumName())))
                    albums.add(s.getAlbumName());
        return albums;
    }

    public void initialize(VBox vbox) {
        vbox.getChildren().clear();
    }

}
