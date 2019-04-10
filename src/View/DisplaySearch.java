package View;

import Controller.MainController;
import Model.*;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.util.List;

public class DisplaySearch {
    private static Database db;
    private static PlaylistService ps;
    private static SongService ss;
    private static AccountService as;
    private static List<Song> songs;
    private static List<Playlist> playlists;
    private static List<Account> accounts;

    private static Label songLabel;
    private static Label playlistLabel;
    private static Label artistLabel;


    public static void initialize(VBox vBox){
        vBox.getChildren().clear();
        db = new Database();
        ss = new SongService(db);
        ps = new PlaylistService(db);
        as = new AccountService(db);
        playlists = ps.getAll();
        songs = ss.getAll();
        accounts = as.getAll();

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

        artistLabel = new Label("ARTISTS");
        artistLabel.setAlignment(Pos.CENTER);
        artistLabel.setPrefSize(822, 51);
        artistLabel.setFont(Font.font("Lucida Fax", FontWeight.SEMI_BOLD, 18));
        artistLabel.setTextFill(Color.DARKRED);
    }

    public static void mainDisplay(VBox dashboardVBox, MainController controller){
        initialize(dashboardVBox);
        String searchKey = controller.getMainSearchFld().getText().toLowerCase();

        dashboardVBox.getChildren().add(songLabel);
        for(Song s : songs)
            if(s.getTitle().toLowerCase().contains(searchKey))
                dashboardVBox.getChildren().addAll(new SongHBox(s, dashboardVBox, controller));

        dashboardVBox.getChildren().add(playlistLabel);
        for(Playlist p : playlists)
            if((p.getName().toLowerCase().contains(searchKey)) && !p.getName().equals("My Songs") && !p.getName().equals("Most Played Songs"))
                dashboardVBox.getChildren().addAll(new PlaylistHBox(p));

        dashboardVBox.getChildren().add(artistLabel);
        for(Account a : accounts)
            if(a.getUsername().toLowerCase().contains(searchKey))
                dashboardVBox.getChildren().addAll(new AccountHBox(a));
    }

    public static void otherDisplay(VBox dashboardVBox, MainController controller){
        initialize(dashboardVBox);
        String searchKey = controller.getOtherSearchFld().getText().toLowerCase();

        dashboardVBox.getChildren().add(songLabel);
        for(Song s : songs)
            if(s.getTitle().toLowerCase().contains(searchKey))
                dashboardVBox.getChildren().addAll(new SongHBox(s, dashboardVBox, controller));

        dashboardVBox.getChildren().add(playlistLabel);
        for(Playlist p : playlists)
            if((p.getName().toLowerCase().contains(searchKey)) && !p.getName().equals("My Songs") && !p.getName().equals("Most Played Songs"))
                dashboardVBox.getChildren().addAll(new PlaylistHBox(p));

        dashboardVBox.getChildren().add(artistLabel);
        for(Account a : accounts)
            if(a.getUsername().toLowerCase().contains(searchKey))
                dashboardVBox.getChildren().addAll(new AccountHBox(a));
    }
}