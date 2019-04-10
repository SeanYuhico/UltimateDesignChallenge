package Controller;

import Model.*;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.DisplacementMap;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.List;

public class ProfileController {
    @FXML Label labelLabel, usernameLabel;
    @FXML Button publicPlaylistsButton, likedSongsButton, followingButton;
    @FXML VBox profileVBox;

    ArrayList<Playlist> publicPlaylists = new ArrayList<>();
    ArrayList<Song> likedSongs = new ArrayList();

    public void initialize () {

        AccountService accountService = new AccountService(new Database());
        SongService songService = new SongService(new Database());
        List<Song> songs = songService.getAll();


        for (int i = 0; i < accountService.getAll().size(); i++)
            if (accountService.getAll().get(i).getUsername().equals(LoginArtistController.getLoggedUser())) {
                if (accountService.getAll().get(i).isArtist())
                    labelLabel.setText("Artist");
                else
                    labelLabel.setText("Listener");

            }
        for (Song s: songs)
            if (s.getUsername().equals(LoginArtistController.getLoggedUser()) && s.isFave())
                likedSongs.add(s);

//        for
        usernameLabel.setText(LoginArtistController.getLoggedUser());


    }

    public void showPublicPlaylists() {

    }

    public void showLikedSongs() {

    }

    public void showFollowing() {

    }

    public void publicPlaylistsHover() {
        publicPlaylistsButton.setEffect(new DisplacementMap());
    }

    public void publicPlaylistsHoverOut() {
        publicPlaylistsButton.setEffect(null);
    }

    public void likedSongsHover() {
        likedSongsButton.setEffect(new DisplacementMap());
    }

    public void likedSongsHoverOut() {
        likedSongsButton.setEffect(null);
    }

    public void followingHover() {
        followingButton.setEffect(new DisplacementMap());
    }

    public void followingHoverOut() {
        followingButton.setEffect(null);
    }
}
