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
    ArrayList<Song> likedSongs = new ArrayList<>();
    ArrayList<String> followings = new ArrayList<>();
    ProfileController controller = this;

    public void initialize () {

        AccountService accountService = new AccountService(new Database());
        SongService songService = new SongService(new Database());
        List<Song> songs = songService.getAll();
        FollowerService followerService = new FollowerService(new Database());
        List<Follower> followers = followerService.getAll();


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

        for (Follower f: followers)
            if (f.getFollower().equals(LoginArtistController.getLoggedUser()))
                followings.add(f.getFollowing());

        usernameLabel.setText(LoginArtistController.getLoggedUser());

        showPublicPlaylists();
    }

    public void showPublicPlaylists() {
        profileVBox.getChildren().clear();


    }

    public void showLikedSongs() {
        profileVBox.getChildren().clear();

        for (Song song: likedSongs)
            profileVBox.getChildren().add(new Label(song.getTitle())); // incomplete pa to
    }

    public void showFollowing() {
        profileVBox.getChildren().clear();

        for (String following: followings)
            profileVBox.getChildren().add(new Label(following));
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
