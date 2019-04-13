package View;

import Controller.MainController;
import Model.*;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.util.List;

public class UserProfile {
    private static Database db;
    private static PlaylistService ps;
    private static SongService ss;
    private static List<Song> songs;
    private static List<Playlist> playlists;

    private static Label songLabel;
    private static Label playlistLabel;
    private static Label albumLabel;

    public static void initialize(VBox vBox) {
        vBox.getChildren().clear();
        db = new Database();
        ss = new SongService(db);
        ps = new PlaylistService(db);
        playlists = ps.getAll();
        songs = ss.getAll();

        songLabel = new Label("SONGS");
        songLabel.setAlignment(Pos.CENTER);
        songLabel.setPrefSize(822, 51);
        songLabel.setFont(Font.font("Lucida Fax", FontWeight.SEMI_BOLD, 18));
        songLabel.setTextFill(Color.DARKBLUE);

        playlistLabel = new Label("PLAYLISTS");
        playlistLabel.setAlignment(Pos.CENTER);
        playlistLabel.setPrefSize(822, 51);
        playlistLabel.setFont(Font.font("Lucida Fax", FontWeight.SEMI_BOLD, 18));
        playlistLabel.setTextFill(Color.DARKGREEN);

        albumLabel = new Label("ALBUMS");
        albumLabel.setAlignment(Pos.CENTER);
        albumLabel.setPrefSize(822, 51);
        albumLabel.setFont(Font.font("Lucida Fax", FontWeight.SEMI_BOLD, 18));
        albumLabel.setTextFill(Color.CRIMSON);
    }

    public static void display(Label dashboardPlaylistLbl, Account account, VBox dashboardVBox, Pane dashboardPane, Pane playlistPane,
                               MainController controller){
        initialize(dashboardVBox);

        if(account.isArtist()) {
            dashboardVBox.getChildren().add(songLabel);
            for(Song s : songs)
                if(s.getUsername().equals(account.getUsername()))
                    dashboardVBox.getChildren().addAll(new SongHBox(s, dashboardVBox, controller));

            dashboardVBox.getChildren().add(albumLabel);
            for (Playlist p : playlists)
                if (p.getUsername().equals(account.getUsername()) && p.isAlbum() && !p.getName().equals("No Album"))
                    dashboardVBox.getChildren().addAll(new PlaylistHBox(dashboardPlaylistLbl, p, dashboardVBox,
                            dashboardPane, playlistPane, controller));
        }

        dashboardVBox.getChildren().addAll(playlistLabel);
        for(Playlist p : playlists)
            if((p.getUsername().equals(account.getUsername())) && !p.getName().equals("My Songs") && !p.getName().equals("Most Played Songs") &&
                    !p.isAlbum() && p.isPublic())
                dashboardVBox.getChildren().addAll(new PlaylistHBox(dashboardPlaylistLbl, p, dashboardVBox,
                        dashboardPane, playlistPane, controller));

    }
}
