package View;

import Controller.LoginArtistController;
import Controller.MainController;
import Model.*;
import com.mysql.cj.log.Log;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;

import java.io.File;

public class PlaylistHBox extends HBox {
    private ImageView playBtn;
    private ImageView deleteBtn;
    private ImageView albumCover;
    private Label titleLbl, artistLbl, countLbl;

    public PlaylistHBox(Label dashboardPlaylistLbl, Playlist p, VBox playlistVBox, VBox dashboardVBox, Pane dashboardPane, Pane playlistPane,
                        MainController controller)
    {
        Database db = new Database();
        PlaylistSongService pss = new PlaylistSongService(db);
        PlaylistService ps = new PlaylistService(db);

        // Properties
        setVisible(true);
        setCacheShape(true);
        setCenterShape(true);
        setScaleShape(true);
        setOpacity(1);
        setAlignment(Pos.TOP_LEFT);
        setPickOnBounds(true);

        // Content
//        albumCover = new ImageView();
        playBtn = new ImageView(new Image("/Pictures/play.png"));
        deleteBtn = new ImageView(new Image("/Pictures/x.png"));
        titleLbl = new Label(p.getName());
        countLbl = new Label("Number of Songs : " + p.getSongCount());

        if((p.getName().equals("No Album") && p.isAlbum()) || (!p.getUsername().equals(LoginArtistController.getLoggedUser()))) {
            deleteBtn.setDisable(true);
            deleteBtn.setVisible(false);
        }

        // Layout
        setFillHeight(true);
        setPrefWidth(823);
        setPrefHeight(37);
        setMinWidth(USE_COMPUTED_SIZE);
        setMaxWidth(USE_COMPUTED_SIZE);
        setMinHeight(USE_COMPUTED_SIZE);
        setMaxHeight(USE_COMPUTED_SIZE);
        setSnapToPixel(true);

//        albumCover.setFitHeight(38);
//        albumCover.setFitWidth(32);
//        albumCover.setPickOnBounds(true);
//        albumCover.setPreserveRatio(true);
//        setMargin(albumCover, new Insets(4,5,0,10));

        playBtn.setFitHeight(38);
        playBtn.setFitWidth(32);
        playBtn.setPickOnBounds(true);
        playBtn.setPreserveRatio(true);
        setMargin(playBtn, new Insets(4,5,0,10));

        deleteBtn.setFitHeight(24);
        deleteBtn.setFitWidth(24);
        deleteBtn.setPickOnBounds(true);
        deleteBtn.setPreserveRatio(true);
        setMargin(deleteBtn, new Insets(4, 5, 0, 20));

        titleLbl.setPrefWidth(289);
        titleLbl.setPrefHeight(29);
        setMargin(titleLbl, new Insets(4, 5, 0, 5));
        countLbl.setPrefWidth(289);
        countLbl.setPrefHeight(29);
        setMargin(countLbl, new Insets(4, 5, 0, 0));

        // Functionalities
        ImageLoader img = new ImageLoader(db);

        albumCover = new ImageView();
        if(p.isAlbum() && !p.getName().equals("No Album"))
            albumCover.setImage(new Image(img.loadImage(p.getName())));

        albumCover.setFitHeight(38);
        albumCover.setFitWidth(32);
        albumCover.setPickOnBounds(true);
        albumCover.setPreserveRatio(true);
        setMargin(albumCover, new Insets(4,5,0,10));

        final ContextMenu contextMenu = new ContextMenu();
        MenuItem rename = new MenuItem("Rename");
        MenuItem addToPublic = new MenuItem("Add to Public");
        if(p.isPublic())
            addToPublic.setText("Remove from Public");
        else
            addToPublic.setText("Add to Public");
        MenuItem addCover = new MenuItem("Add Album Cover");

        rename.setOnAction(e -> PlaylistEditor.editPlaylist(p.getPlaylistID()));

        if(!p.getName().equals("No Album") && p.getUsername().equals(LoginArtistController.getLoggedUser()))
            contextMenu.getItems().addAll(rename, addToPublic);

        if(p.isAlbum() && !p.getName().equals("No Album") && p.getUsername().equals(LoginArtistController.getLoggedUser()))
            contextMenu.getItems().add(addCover);


        deleteBtn.setOnMouseClicked(e -> {
            Boolean ans = ConfirmBox.display("Delete", "Are you sure you want to delete?");
            if(ans) {
                if(p.isAlbum() && p.getSongCount() > 0)
                    AlertBox.display("Error", "Please remove all songs in album first.");
                else {
                    playlistVBox.getChildren().remove(this);
                    pss.deletePlaylist(p);
                    ps.delete(p);
                }
            }
        });

        titleLbl.setOnMouseClicked(e -> {
            if(e.getButton() == MouseButton.SECONDARY){
                contextMenu.show(titleLbl, e.getScreenX(), e.getScreenY());
                rename.setOnAction(ex -> PlaylistEditor.editPlaylist(p.getPlaylistID()));
                addToPublic.setOnAction(ev -> {
                    if(addToPublic.getText().equals("Add to Public")) {
                        ps.makePublic(p.getPlaylistID(), "true");
                        addToPublic.setText("Remove from Public");
                    }
                    else if(addToPublic.getText().equals("Remove from Public")){
                        ps.makePublic(p.getPlaylistID(), "false");
                        addToPublic.setText("Add to Public");
                    }
                });
                addCover.setOnAction(ev -> {
                    FileChooser fileChooser = new FileChooser();
                    fileChooser.setTitle("Upload Album Cover");
                    fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.jpg", "*.png"));
                    File coverToUpload = fileChooser.showOpenDialog(null);
                    ps.uploadAlbumCover(p, coverToUpload.getAbsolutePath());
                });
            }
            else if(e.getButton().equals(MouseButton.PRIMARY)) {
                if (e.getClickCount() == 2) {
                    dashboardPane.setVisible(true);
                    playlistPane.setVisible(false);
                    dashboardPlaylistLbl.setText(p.getName());
                    DisplayNonDefault.displayPlaylistSongs(p.getName(), dashboardVBox, controller);
                }
            }
        });
        if(p.isAlbum() && !p.getName().equals("No Album"))
            this.getChildren().add(albumCover);

        this.getChildren().addAll(playBtn, deleteBtn, titleLbl, countLbl);
    }

    public PlaylistHBox(Label dashboardPlaylistLbl, Playlist p, VBox dashboardVBox, Pane dashboardPane, Pane playlistPane,
                        MainController controller)
    {
        AccPlayService aps = new AccPlayService(new Database());
        AccountService as = new AccountService(new Database());
        boolean isArtist = false;
        // Properties
        setVisible(true);
        setCacheShape(true);
        setCenterShape(true);
        setScaleShape(true);
        setOpacity(1);
        setAlignment(Pos.TOP_LEFT);
        setPickOnBounds(true);

        // Content
        albumCover = new ImageView();
        playBtn = new ImageView(new Image("/Pictures/play.png"));
        titleLbl = new Label(p.getName());
        artistLbl = new Label(p.getUsername());
        countLbl = new Label("Number of Songs : " + p.getSongCount());

        // Layout
        setFillHeight(true);
        setPrefWidth(823);
        setPrefHeight(37);
        setMinWidth(USE_COMPUTED_SIZE);
        setMaxWidth(USE_COMPUTED_SIZE);
        setMinHeight(USE_COMPUTED_SIZE);
        setMaxHeight(USE_COMPUTED_SIZE);
        setSnapToPixel(true);

        albumCover.setFitHeight(38);
        albumCover.setFitWidth(32);
        albumCover.setPickOnBounds(true);
        albumCover.setPreserveRatio(true);
        setMargin(albumCover, new Insets(4,5,0,10));

        playBtn.setFitHeight(38);
        playBtn.setFitWidth(32);
        playBtn.setPickOnBounds(true);
        playBtn.setPreserveRatio(true);
        setMargin(playBtn, new Insets(4,5,0,10));

        titleLbl.setPrefWidth(289);
        titleLbl.setPrefHeight(29);
        setMargin(titleLbl, new Insets(4, 5, 0, 10));

        artistLbl.setPrefSize(289,29);
        setMargin(artistLbl, new Insets(4,5,0,10));

        countLbl.setPrefWidth(289);
        countLbl.setPrefHeight(29);
        setMargin(countLbl, new Insets(4, 5, 0, 0));

        // Functionalities
        final ContextMenu contextMenu = new ContextMenu();


        MenuItem addToQueue = new MenuItem("Add to Queue");
        contextMenu.getItems().add(addToQueue);

        MenuItem follow = new MenuItem("Follow Playlist");
        for(AccPlay ap : aps.getAll())
            if(ap.getPlaylistID() == p.getPlaylistID() && ap.getUser().equals(LoginArtistController.getLoggedUser())){
                follow.setText("Unfollow Playlist");
                break;
            }
            else
                follow.setText("Follow Playlist");

        for(int i = 0; i < as.getAll().size(); i++){
            if(as.getAll().get(i).getUsername().equals(LoginArtistController.getLoggedUser()) && as.getAll().get(i).isArtist()){
                isArtist = true;
            }
        }

        if(!isArtist && !p.getUsername().equals(LoginArtistController.getLoggedUser()))
            contextMenu.getItems().add(follow);

        titleLbl.setOnMouseClicked(e -> {
            if (e.getClickCount() == 2) {
                dashboardPane.setVisible(true);
                playlistPane.setVisible(false);
                dashboardPlaylistLbl.setText(p.getName());
                DisplayNonDefault.displaySongs(p.getName(), dashboardVBox, controller);
            }
            else if(e.getButton() == MouseButton.SECONDARY){
                contextMenu.show(titleLbl, e.getScreenX(), e.getScreenY());
                follow.setOnAction(ev -> {
                    if(follow.getText().equals("Follow Playlist")){
                        aps.add(p, LoginArtistController.getLoggedUser());
                        follow.setText("Unfollow Playlist");
                    }
                    else{
                        aps.unfollow(p.getPlaylistID(), LoginArtistController.getLoggedUser());
                        follow.setText("Follow Playlist");
                    }
                });
                addToQueue.setOnAction(ex ->
                        System.out.println("Playlist queued!"));
            }
        });

        if(p.isAlbum() && !p.getName().equals("No Album"))
            this.getChildren().add(albumCover);

        this.getChildren().addAll(playBtn, titleLbl, artistLbl, countLbl);
    }
}
