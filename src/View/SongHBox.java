package View;

import Controller.LoginArtistController;
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
        playBtn = new ImageView(new Image("/Pictures/play.png"));
        deleteBtn = new ImageView(new Image("/Pictures/x.png"));
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
        Menu addToAlbum = new Menu("Add to Album");

        // di pwede mag-add ng songs sa default playlists.
        for(Playlist p : playlists) {
            if (!p.getName().equals("My Songs") && !p.getName().equals("Most Played Songs") &&
                    p.getUsername().equals(LoginArtistController.getLoggedUser()) && !p.isAlbum()) {
                MenuItem addHere = new MenuItem(p.getName());
                addHere.setOnAction(e -> {
                    boolean add = true;
                    for (PlaylistSong playlistSong : playlistSongs) {
                        if (playlistSong.getPlaylistID() == p.getPlaylistID() && playlistSong.getSongID() == song.getSongID()) {
                            AlertBox.display("Error", "Song already in playlist lah");
                            add = false;
                            break;
                        }
                    }
                    if (add)
                        pss.addSongToPlaylist(p, song);
                });
                addToPlaylist.getItems().add(addHere);
            }
            else if(!p.getName().equals("No Album") && p.getUsername().equals(LoginArtistController.getLoggedUser()) && p.isAlbum()) {
                MenuItem addAlbum = new MenuItem(p.getName());
                addAlbum.setOnAction(e -> {
                    boolean add = true;
                    for (PlaylistSong playlistSong : playlistSongs) {
                        if (playlistSong.getPlaylistID() == p.getPlaylistID() && playlistSong.getSongID() == song.getSongID()) {
                            AlertBox.display("Error", "Song already in album lah");
                            add = false;
                            break;
                        }
                    }
                    if (add) {
                        pss.addSongToPlaylist(p, song);
                        ss.changeAlbum(p, song.getSongID());
                        controller.update();
                        for(Playlist pl : playlists)
                            if(pl.getName().equals("No Album") && pl.getUsername().equals(LoginArtistController.getLoggedUser()))
                                for(PlaylistSong pSong : pss.getAll())
                                    if(pl.getPlaylistID() == pSong.getPlaylistID())
                                        pss.removeSongFromPlaylist(pl, song);
                    }
                });
                addToAlbum.getItems().add(addAlbum);
            }
        }

        ArrayList<Integer> playlistIDsInPS = new ArrayList<>();
        for(PlaylistSong playlistSong : pss.getAll())
            playlistIDsInPS.add(playlistSong.getPlaylistID());

        Menu removeFromPlaylist = new Menu("Remove from Playlist");
        Menu removeFromAlbum = new Menu("Remove from Album");
        for(Playlist p : playlists){
            if(!p.getName().equals("My Songs") && !p.getName().equals("Most Played Songs") &&
                    p.getUsername().equals(LoginArtistController.getLoggedUser()) && !p.isAlbum()) {
                MenuItem removeFromHere = new MenuItem(p.getName());
                removeFromHere.setOnAction(e -> {
                    if(!playlistIDsInPS.contains(p.getPlaylistID())) {
                        AlertBox.display("Error", "Song not in playlist.");
                    }
                    else
                        pss.removeSongFromPlaylist(p, song);
                });
                removeFromPlaylist.getItems().add(removeFromHere);
            }
            else if(!p.getName().equals("No Album") && p.getUsername().equals(LoginArtistController.getLoggedUser()) && p.isAlbum()){
                MenuItem remove = new MenuItem(p.getName());
                remove.setOnAction(e -> {
                    if(!playlistIDsInPS.contains(p.getPlaylistID())) {
                        AlertBox.display("Error", "Song not in album.");
                    }
                    else {
                        pss.removeSongFromPlaylist(p, song);
                        int checker = 1;
                        for(PlaylistSong pSong : pss.getAll())
                            if(song.getSongID() == pSong.getSongID())
                                for(Playlist pl : ps.getAll())
                                    if(pl.isAlbum() && !pl.getName().equals("No Album") && (pSong.getPlaylistID() == pl.getPlaylistID()))
                                        checker = 0;
                        if(checker == 1) {
                            for(Playlist pl : ps.getAll())
                                if(pl.isAlbum() && pl.getName().equals("No Album") && pl.getUsername().equals(LoginArtistController.getLoggedUser())) {
                                    pss.addSongToPlaylist(pl, song);
                                    ss.changeAlbum(pl, song.getSongID());
                                }
                        }
                    }
                    controller.update();
                });
                removeFromAlbum.getItems().add(remove);
            }
        }

        MenuItem addToQueue = new MenuItem("Add to Queue");
        MenuItem edit = new MenuItem("Edit");
        MenuItem addToFaves = new MenuItem("Add to Favorites");
        if(song.isFave())
            addToFaves.setText("Add to Favorites");
        else
            addToFaves.setText("Remove from Favorites");

        contextMenu.getItems().addAll(addToPlaylist, removeFromPlaylist, addToAlbum, removeFromAlbum, addToQueue, edit, addToFaves);

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
                            p.getName().equals("Year")) && p.getUsername().equals(LoginArtistController.getLoggedUser())) {
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
                addToFaves.setOnAction(ev -> {
                    if(addToFaves.getText().equals("Remove from Favorites")) {
                        SongService.makeFave(song.getSongID(), "false");
                        addToFaves.setText("Add to Favorites");
                    }
                    else{
                        SongService.makeFave(song.getSongID(),"true");
                        addToFaves.setText("Remove from Favorites");
                    }
                });
            }
        });

        this.getChildren().addAll(playBtn, deleteBtn, titleLbl, artistLbl, albumLbl, genreLbl, yearLbl);
    }


}
