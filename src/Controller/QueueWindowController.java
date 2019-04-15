package Controller;
import Model.*;

import View.SongHBox;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.effect.DisplacementMap;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.List;

public class QueueWindowController {

    @FXML Button queueButton, historyButton, recentlyAddedButton;
    @FXML VBox queueVBox;
    @FXML Label titleLabel;

    public static ArrayList<String> recentlyPlayed = new ArrayList<>();
    public static ArrayList<String> recentlyAdded = new ArrayList<>();

    public void initialize() {


        AccountService accountService = new AccountService(new Database());
        List<Account> accounts = accountService.getAll();
        SongService songService = new SongService(new Database());
        List<Song> songs = songService.getAll();
        PlaylistService playlistService = new PlaylistService(new Database());
        List<Playlist> playlists = playlistService.getAll();

//            if (LoginArtistController.getLoggedAccount().isArtist()) {
//                    recentlyAddedButton.setVisible(false);
//                    recentlyAddedButton.setDisable(true);
//            }
        }


    public void showQueue() {
        queueVBox.getChildren().clear();
        for(String songTitleDash : MainController.songsCopy){
            queueVBox.getChildren().add(new Label(songTitleDash));
        }

    }

    public void showHistory() {
        queueVBox.getChildren().clear();


        for (String songTitleDash: recentlyPlayed) {

            queueVBox.getChildren().add(new Label(songTitleDash));
        }

    }

    public void showRecentlyAdded() {
        queueVBox.getChildren().clear();

        for (String songTitle: recentlyAdded){
            System.out.println(songTitle);
            queueVBox.getChildren().add(new Label(songTitle));}

    }

    public void queueHover() { queueButton.setEffect(/*new DisplacementMap()*/ new DropShadow()); }
    public void queueHoverOut() { queueButton.setEffect(null); }
    public void historyHover() { historyButton.setEffect(new DropShadow());}
    public void historyHoverOut() { historyButton.setEffect(null); }
    public void recentlyAddedHover() { recentlyAddedButton.setEffect(new DropShadow()); }
    public void recentlyAddedHoverOut() { recentlyAddedButton.setEffect(null); }
}
