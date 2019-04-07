package View;

import Model.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.List;

public class FavePlaylistWindow implements Window{
    private static Database db;
    private static PlaylistService ps;
    private static PlaylistSongService playlistSongService;
    private static SongService ss;
    private static List<Playlist> playlists;
    private static List<PlaylistSong> playlistSongs;
    private static List<Song> songs;
    private static TreeView<String> treeView;

    public static void display() {

        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        ContextMenu contextMenu = new ContextMenu();
        MenuItem unfave = new MenuItem("Unfave");
        contextMenu.getItems().add(unfave);

        window.setTitle("Favorite Playlists");


        db = new Database();
        ps = new PlaylistService(db);
        playlistSongService = new PlaylistSongService(db);
        ss = new SongService(db);
        playlists = ps.getAll();
        playlistSongs = playlistSongService.getAll();
        songs = ss.getAll();

        TreeItem root = new TreeItem<>();
        root.setExpanded(true);

        treeView = new TreeView<>(root);
        treeView.setShowRoot(false);

        for(Playlist playlist : playlists)
            if(playlist.isFave() && playlist.getUsername().equals(LoginWindow.getLoggedUser())) {
                int playlistID = playlist.getPlaylistID();
                TreeItem<String> temp = makeBranch(playlist.getName(), root, window);
                for (PlaylistSong playlistSong: playlistSongs)
                    if(playlistSong.getPlaylistID() == playlistID){
                        int songID = playlistSong.getSongID();
                        for(Song song : songs) {
                            if (song.getSongID() == songID) {
                                System.out.println(playlist.getName());
                                makeBranch(song.getTitle(), temp, window);
                            }
                            System.out.println("playlist " +playlist.getName());
                        }
                    }
            }

        //Layout
        Pane layout = new Pane();
        layout.getChildren().add(treeView);
        Scene scene = new Scene(layout, 250, 250);
        window.setScene(scene);
        window.show();

    }


    public static TreeItem makeBranch(String title, TreeItem<String> parent, Stage window) {
        TreeItem<String> item = new TreeItem<>(title);
        item.setExpanded(true);
        parent.getChildren().add(item);

        return item;
    }
}
