package View;

import Controller.MainController;
import Model.*;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

public class PlaylistHBox extends HBox {
    private ImageView playBtn;
    private ImageView deleteBtn;
    private Label titleLbl, countLbl;

    public PlaylistHBox(Label dashboardPlaylistLbl, Playlist p, VBox playlistVBox, VBox dashboardVBox, Pane dashboardPane, Pane playlistPane,
                        MainController controller)
    {
        // Properties
        setVisible(true);
        setCacheShape(true);
        setCenterShape(true);
        setScaleShape(true);
        setOpacity(1);
        setAlignment(Pos.TOP_LEFT);
        setPickOnBounds(true);

        // Content
        playBtn = new ImageView(new Image("File:\\Users\\Legs\\Desktop\\dc2\\src\\Pictures\\play.png"));
        deleteBtn = new ImageView(new Image("File:\\Users\\Legs\\Desktop\\dc2\\src\\Pictures\\x.png"));
        titleLbl = new Label(p.getName());
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

        playBtn.setFitHeight(38);
        playBtn.setFitWidth(32);
        playBtn.setPickOnBounds(true);
        playBtn.setPreserveRatio(true);

        deleteBtn.setFitHeight(24);
        deleteBtn.setFitWidth(24);
        deleteBtn.setPickOnBounds(true);
        deleteBtn.setPreserveRatio(true);
        setMargin(deleteBtn, new Insets(4, 5, 0, 20));

        titleLbl.setPrefWidth(289);
        titleLbl.setPrefHeight(29);
        setMargin(titleLbl, new Insets(4, 5, 0, 0));
        countLbl.setPrefWidth(289);
        countLbl.setPrefHeight(29);
        setMargin(countLbl, new Insets(4, 5, 0, 0));

        // Functionalities
        Database db = new Database();
        PlaylistSongService pss = new PlaylistSongService(db);
        PlaylistService ps = new PlaylistService(db);

        final ContextMenu contextMenu = new ContextMenu();
        MenuItem rename = new MenuItem("Rename");
        MenuItem addToFaves = new MenuItem("Add to Favorites");
        if(p.isFave())
            addToFaves.setText("Remove from Favorites");
        else
            addToFaves.setText("Add to Favorites");

        rename.setOnAction(e -> PlaylistEditor.editPlaylist(p.getPlaylistID()));
        contextMenu.getItems().addAll(rename, addToFaves);

        deleteBtn.setOnMouseClicked(e -> {
            Boolean ans = ConfirmBox.display("Delete Playlist", "Are you sure you want to delete this playlist?");
            if(ans) {
                playlistVBox.getChildren().remove(this);
                pss.deletePlaylist(p);
                ps.delete(p);
            }
        });

        titleLbl.setOnMouseClicked(e -> {
            if(e.getButton() == MouseButton.SECONDARY){
                contextMenu.show(titleLbl, e.getScreenX(), e.getScreenY());
                rename.setOnAction(ex -> PlaylistEditor.editPlaylist(p.getPlaylistID()));
                addToFaves.setOnAction(ev -> {
                    if(addToFaves.getText().equals("Add to Favorites")) {
                        PlaylistService.makeFave(p.getPlaylistID(), "true");
                        addToFaves.setText("Remove from Favorites");
                    }
                    else if(addToFaves.getText().equals("Remove from Favorites")){
                        PlaylistService.makeFave(p.getPlaylistID(), "false");
                        addToFaves.setText("Add to Favorites");
                    }
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

        this.getChildren().addAll(playBtn, deleteBtn, titleLbl, countLbl);
    }

    public PlaylistHBox(Label dashboardPlaylistLbl, Playlist p, VBox dashboardVBox, Pane dashboardPane, Pane playlistPane,
                        MainController controller)
    {
        // Properties
        setVisible(true);
        setCacheShape(true);
        setCenterShape(true);
        setScaleShape(true);
        setOpacity(1);
        setAlignment(Pos.TOP_LEFT);
        setPickOnBounds(true);

        // Content
        playBtn = new ImageView(new Image("File:\\Users\\aaron\\Desktop\\DesignChallenge2\\DesignChallenge2\\src\\Pictures\\play.png"));
        titleLbl = new Label(p.getName());
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

        playBtn.setFitHeight(38);
        playBtn.setFitWidth(32);
        playBtn.setPickOnBounds(true);
        playBtn.setPreserveRatio(true);

        titleLbl.setPrefWidth(289);
        titleLbl.setPrefHeight(29);
        setMargin(titleLbl, new Insets(4, 5, 0, 0));
        countLbl.setPrefWidth(289);
        countLbl.setPrefHeight(29);
        setMargin(countLbl, new Insets(4, 5, 0, 0));

        // Functionalities
        titleLbl.setOnMouseClicked(e -> {
            if (e.getClickCount() == 2) {
                dashboardPane.setVisible(true);
                playlistPane.setVisible(false);
                dashboardPlaylistLbl.setText(p.getName());
                DisplayNonDefault.displaySongs(p.getName(), dashboardVBox, controller);
            }
        });

        this.getChildren().addAll(titleLbl, countLbl);
    }
}
