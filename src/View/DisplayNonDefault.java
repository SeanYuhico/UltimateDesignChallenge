package View;

import Controller.MainController;
import Model.*;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.media.MediaPlayer;

import java.util.List;

public class DisplayNonDefault {
    private static Database db;
    private static PlaylistService ps;
    private static PlaylistSongService playlistSongService;
    private static SongService ss;
    private static List<Playlist> playlists;
    private static List<PlaylistSong> playlistSongs;
    private static List<Song> songs;
    private static MainController controller;

    public static void initialize(VBox vbox) {
        vbox.getChildren().clear();
        db = new Database();
        ss = new SongService(db);
        ps = new PlaylistService(db);
        playlistSongService = new PlaylistSongService(db);
        playlists = ps.getAll();
        playlistSongs = playlistSongService.getAll();
        songs = ss.getAll();
    }

    public static void displayPlaylistSongs(String playlistName, VBox dashboardVBox, MainController controller)
    {
        initialize(dashboardVBox);

        for(Playlist playlist : playlists)
            if(playlist.getName().equals(playlistName) && playlist.getUsername().equals(LoginWindow.getLoggedUser())) {
                int playlistID = playlist.getPlaylistID();
                for (PlaylistSong playlistSong : playlistSongs)
                    if(playlistSong.getPlaylistID() == playlistID){
                        int songID = playlistSong.getSongID();
                        for(Song song : songs)
                            if(song.getSongID() == songID) {
                                dashboardVBox.getChildren().add(new SongHBox(song, dashboardVBox, controller));
                            }
                    }
            }
    }

    public static void displayAllPlaylists(VBox playlistVBox, VBox dashboardVBox, Pane dashboardPane, Pane playlistPane, MediaPlayer mp, Label dashboardPlaylistLbl, Label playlistNameLbl) {
        initialize(playlistVBox);

        for (Playlist p : playlists)
            if((!p.getName().equals("My Songs") && !p.getName().equals("Most Played Songs") && !p.getName().equals("Artists") &&
                    !p.getName().equals("Albums") && !p.getName().equals("Genres") && !p.getName().equals("Year")) &&
                    p.getUsername().equals(LoginWindow.getLoggedUser())) {
                playlistVBox.getChildren().add(new PlaylistHBox(dashboardPlaylistLbl, p, playlistVBox, dashboardVBox, dashboardPane, playlistPane,
                        controller));
            }
    }


    public static void displayByMostPlayed (VBox dashboardVBox, MainController controller) {

        initialize(dashboardVBox);

        int largestNum = 0;

        for(Song song: songs)
            if(song.getUsername().equals(LoginWindow.getLoggedUser()))
                if (song.getNumTimesPlayed() > largestNum)
                    largestNum = song.getNumTimesPlayed();

        for (int i=largestNum; i>=0; i--)
            for(Song song: songs)
                if(song.getUsername().equals(LoginWindow.getLoggedUser()))
                    if (song.getNumTimesPlayed() == i)
                        dashboardVBox.getChildren().add(new SongHBox(song, dashboardVBox, controller));

    }

    public static void displaySongs (String playlistName, VBox dashboardVBox, MainController controller){
        dashboardVBox.getChildren().clear();

        boolean isArtist = false;
        boolean isAlbum = false;
        boolean isGenre = false;
        boolean isYear = false;

        for (Song song : ss.getAll()) {
            if (song.getArtist().equals(playlistName)) {
                isArtist = true;
                break;
            } else if (song.getAlbumName().equals(playlistName)) {
                isAlbum = true;
                break;
            } else if (song.getGenre().equals(playlistName)) {
                isGenre = true;
                break;
            } else if (song.getYear().equals(playlistName)) {
                isYear = true;
                break;
            }
        }
        for(Song kanta : ss.getAll())
            if(isArtist) {
                if (kanta.getArtist().equals(playlistName))
                    dashboardVBox.getChildren().add(new SongHBox(kanta, dashboardVBox, controller));
            }
            else if(isAlbum){
                if (kanta.getAlbumName().equals(playlistName))
                    dashboardVBox.getChildren().add(new SongHBox(kanta, dashboardVBox, controller));
            }
            else if(isGenre){
                if (kanta.getGenre().equals(playlistName))
                    dashboardVBox.getChildren().add(new SongHBox(kanta, dashboardVBox, controller));
            }
            else if(isYear){
                if (kanta.getYear().equals(playlistName))
                    dashboardVBox.getChildren().add(new SongHBox(kanta, dashboardVBox, controller));
            }
    }


}
