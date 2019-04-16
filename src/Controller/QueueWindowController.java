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
import java.util.Comparator;
import java.util.List;

public class QueueWindowController {

    @FXML Button queueButton, historyButton, recentlyAddedButton;
    @FXML VBox queueVBox;
    @FXML Label titleLabel;

    public static ArrayList<String> recentlyPlayed = new ArrayList<>();
    public static ArrayList<String> recentlyAdded = new ArrayList<>();

    public void initialize() {

//        recentlyPlayed = new ArrayList<>();
//        recentlyAdded = new ArrayList<>();

        AccountService accountService = new AccountService(new Database());
        List<Account> accounts = accountService.getAll();
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
        SongService songService = new SongService(new Database());
        ArrayList<Song> sortedSongs = new ArrayList<>();

       if(LoginArtistController.getLoggedAccount().isArtist()) {
            for (Song s : songService.getAll()) {
                for (String songTitle : recentlyAdded) {
                    if (songTitle.equals(s.getTitle()) && LoginArtistController.getLoggedUser().equals(s.getUsername())) {
                        sortedSongs.add(s);
                    }
                }
            }

            queueVBox.getChildren().clear();
            sortedSongs.sort(Comparator.comparing(Song::getDateUploaded));

            for (Song s : sortedSongs)
                queueVBox.getChildren().add(new Label(s.getTitle()));
        }
        else
        {
            for (String songTitle: recentlyAdded){
                System.out.println(songTitle);
                queueVBox.getChildren().add(new Label(songTitle));
            }
        }




    }

    public void queueHover() { queueButton.setEffect(/*new DisplacementMap()*/ new DropShadow()); }
    public void queueHoverOut() { queueButton.setEffect(null); }
    public void historyHover() { historyButton.setEffect(new DropShadow());}
    public void historyHoverOut() { historyButton.setEffect(null); }
    public void recentlyAddedHover() { recentlyAddedButton.setEffect(new DropShadow()); }
    public void recentlyAddedHoverOut() { recentlyAddedButton.setEffect(null); }
}
