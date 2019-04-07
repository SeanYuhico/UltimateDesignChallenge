package View;

import Controller.MainController;
import Model.*;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.image.Image;
import javafx.scene.control.MenuItem;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.media.MediaPlayer;
import java.util.ArrayList;
import java.util.List;

public class SongHBox extends HBox {
    private ImageView playBtn;
    private ImageView deleteBtn;
    private Label titleLbl, artistLbl, albumLbl, genreLbl, yearLbl;
    private boolean songExists;
    private String tempFilename;

    public SongHBox(Song song, VBox dashboardVBox, MainController controller)
    {
        // Properties
        setVisible(true);
        setCacheShape(true);
        setCenterShape(true);
        setScaleShape(true);
        setOpacity(1);
        setAlignment(Pos.TOP_LEFT);
        setPickOnBounds(true);

        // Contents
        playBtn = new ImageView(new Image("File:\\Users\\Legs\\Desktop\\dc2\\src\\Pictures\\play.png"));
        deleteBtn = new ImageView(new Image("File:\\Users\\Legs\\Desktop\\dc2\\src\\Pictures\\x.png"));
        titleLbl = new Label(song.getTitle());
        artistLbl = new Label(song.getArtist());
        albumLbl = new Label(song.getAlbumName());
        genreLbl = new Label(song.getGenre());
        yearLbl = new Label(song.getYear());

        // Layout
        setFillHeight(true);
        setPrefWidth(823);
        setPrefHeight(37);
        setMinWidth(USE_COMPUTED_SIZE);
        setMaxWidth(USE_COMPUTED_SIZE);
        setMinHeight(USE_COMPUTED_SIZE);
        setMaxHeight(USE_COMPUTED_SIZE);
        setSnapToPixel(true);

        playBtn.setFitHeight(38);
        playBtn.setFitWidth(32);
        playBtn.setVisible(true);
        playBtn.setPickOnBounds(true);
        playBtn.setPreserveRatio(true);

        deleteBtn.setFitHeight(24);
        deleteBtn.setFitWidth(24);
        deleteBtn.setVisible(true);
        deleteBtn.setPickOnBounds(true);
        deleteBtn.setPreserveRatio(true);
        setMargin(deleteBtn, new Insets(4, 5, 0, 20));

        titleLbl.setPrefWidth(289);
        titleLbl.setPrefHeight(29);
        setMargin(titleLbl, new Insets(4, 5, 0, 0));
        artistLbl.setPrefWidth(170);
        artistLbl.setPrefHeight(29);
        setMargin(artistLbl, new Insets(4, 5, 0, 0));
        albumLbl.setPrefWidth(170);
        albumLbl.setPrefHeight(29);
        setMargin(albumLbl, new Insets(4, 5, 0, 0));
        genreLbl.setPrefWidth(170);
        genreLbl.setPrefHeight(29);
        setMargin(genreLbl, new Insets(4, 5, 0, 0));
        yearLbl.setPrefWidth(121);
        yearLbl.setPrefHeight(29);
        setMargin(yearLbl, new Insets(4, 5, 0, 0));

        // Functionalities
        Database db = new Database();
        PlaylistSongService pss = new PlaylistSongService(db);
        SongService ss = new SongService(db);
        PlaylistService ps = new PlaylistService(db);
        List<Playlist> playlists = ps.getAll();
        List<PlaylistSong> playlistSongs = pss.getAll();


        final ContextMenu contextMenu = new ContextMenu();
        Menu addToPlaylist= new Menu("Add to Playlist");
        // di pwede mag-add ng songs sa default playlists.
        for(Playlist p : playlists) {
            if ((!p.getName().equals("My Songs") && !p.getName().equals("Most Played Songs") &&
                    p.getUsername().equals(LoginWindow.getLoggedUser()))) {
                MenuItem addHere = new MenuItem(p.getName());
                addHere.setOnAction(e -> {
                    boolean add = true;
                    for (PlaylistSong playlistSong : playlistSongs) {
                        if (playlistSong.getPlaylistID() == p.getPlaylistID() && playlistSong.getSongID() == song.getSongID()) {
                            AlertBox.display("Error", "Nandyan na yung song vuvu");
                            add = false;
                            break;
                        }
                    }
                    if (add)
                        pss.addSongToPlaylist(p, song);
                });
                addToPlaylist.getItems().addAll(addHere);
            }
        }

        ArrayList<Integer> playlistIDsInPS = new ArrayList<>();
        for(PlaylistSong playlistSong : pss.getAll())
            playlistIDsInPS.add(playlistSong.getPlaylistID());

        Menu removeFromPlaylist = new Menu("Remove from Playlist");
        for(Playlist p : playlists){
            if((!p.getName().equals("My Songs") && !p.getName().equals("Most Played Songs")) &&
                    p.getUsername().equals(LoginWindow.getLoggedUser())) {
                MenuItem removeFromHere = new MenuItem(p.getName());
                removeFromHere.setOnAction(e -> {
                    if(!playlistIDsInPS.contains(p.getPlaylistID())) {
                        AlertBox.display("Error", "Playlist has no song in it.");
                    }
                    else
                        pss.removeSongFromPlaylist(p, song);
                });
                removeFromPlaylist.getItems().addAll(removeFromHere);
            }
        }

        MenuItem addToQueue = new MenuItem("Add to Queue");
        MenuItem edit = new MenuItem("Edit");
        MenuItem addToFaves = new MenuItem("Add to Favorites");
        contextMenu.getItems().addAll(addToPlaylist, removeFromPlaylist, addToQueue, edit, addToFaves);

        playBtn.setOnMouseClicked(e -> {
            controller.pause();
            SongLoader loader = new SongLoader(db);
            SongService service = new SongService(db);
            PlayMP3 play = new PlayMP3();
            for(int i = 0; i < service.getAll().size(); i++){
                if(titleLbl.getText().equals(service.getAll().get(i).getTitle())){
                    System.out.println("Hell yes");
                    if(!songExists) {
                        tempFilename = loader.loadSong(titleLbl.getText());
                    }
                    play.setMedia(tempFilename);
                    MediaPlayer mp = play.getMediaPlayer();
                    controller.setMp(mp);
                    controller.setMPLabels(artistLbl.getText(), titleLbl.getText());
                }
            }
            songExists = true;
            controller.play();
        });

        deleteBtn.setOnMouseClicked(e -> {
            Boolean ans = ConfirmBox.display("Delete Song", "Are you sure you want to delete this song?");
            if (ans) {
                for(Playlist p : playlists) {
                    // If dinelete sa default playlist, tanggal yung buong song sa music player.
                    if ((p.getName().equals("My Songs") || p.getName().equals("Most Played Songs") || p.getName().equals("Artists") ||
                            p.getName().equals("Albums") || p.getName().equals("Genres") ||
                            p.getName().equals("Year")) && p.getUsername().equals(LoginWindow.getLoggedUser())) {
                        ss.delete(song);
                    }
                    // If hindi default, tanggal lang yung relationship and decrement the songCount.
                    pss.deleteSong(song);
                    ps.decrement(p);
                }
                dashboardVBox.getChildren().remove(this);
            }
        });

        titleLbl.setOnMouseClicked(e -> {
            if(e.getButton() == MouseButton.SECONDARY){
                contextMenu.show(titleLbl, e.getScreenX(), e.getScreenY());
                edit.setOnAction(ex -> EditSongWindow.display(dashboardVBox, song.getSongID(), controller));
                addToFaves.setOnAction(ev -> SongService.makeFave(song.getSongID(), "true"));
            }
        });

        this.getChildren().addAll(playBtn, deleteBtn, titleLbl, artistLbl, albumLbl, genreLbl, yearLbl);
    }


}
