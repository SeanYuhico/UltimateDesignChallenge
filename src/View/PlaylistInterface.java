package View;

import Controller.MainController;
import Model.*;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.util.List;

public interface PlaylistInterface {
    Database db = new Database();
    SongService ss = new SongService(db);
    PlaylistService ps = new PlaylistService(db);
    PlaylistSongService playlistSongService = new PlaylistSongService(db);
    List<Playlist> playlists = ps.getAll();
    List<PlaylistSong> playlistSongs = playlistSongService.getAll();
    List<Song> songs = ss.getAll();

     void group(Label playlistNameLbl, VBox playlistVBox, VBox dashboardVBox, Pane dashboardPane, Pane playlistPane, MainController controller);
     void initialize(VBox vbox);
}
