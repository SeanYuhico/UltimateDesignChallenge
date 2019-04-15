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
    ArrayList<Follower> followers = new ArrayList<>();
    ArrayList<Account> accounts = new ArrayList<>();

    public void initialize () {

        AccountService accountService = new AccountService(new Database());
        List<Account> accounts = accountService.getAll();
        SongService songService = new SongService(new Database());
        List<Song> songs = songService.getAll();
        FollowerService followerService = new FollowerService(new Database());
        List<Follower> followers = followerService.getAll();
        PlaylistService playlistService = new PlaylistService(new Database());
        List<Playlist> playlists = playlistService.getAll();


//        for (int i = 0; i < accountService.getAll().size(); i++) {
//            if (accountService.getAll().get(i).getUsername().equals(LoginArtistController.getLoggedUser())) {
//                if (accountService.getAll().get(i).isArtist())
//                    labelLabel.setText("Artist");
//                else
//                    labelLabel.setText("Listener");
//
//            }
//
//        }

        for (Account a: accounts) {
            if (a.getUsername().equals(LoginArtistController.getLoggedUser())) {
                if (a.isArtist())
                    labelLabel.setText("Artist");
                else
                    labelLabel.setText("Listener");
            }
            this.accounts.add(a);
        }


        for (Playlist p: playlists)
            if (p.getUsername().equals(LoginArtistController.getLoggedUser()) && p.isPublic())
                publicPlaylists.add(p);

        for (Song s: songs)
            if (s.getUsername().equals(LoginArtistController.getLoggedUser()) && s.isFave())
                likedSongs.add(s);

        for (Follower f: followers)
            if (f.getFollower().equals(LoginArtistController.getLoggedUser()))
                this.followers.add(f);

        usernameLabel.setText(LoginArtistController.getLoggedUser());

        showPublicPlaylists();
    }

    public void showPublicPlaylists() {
        profileVBox.getChildren().clear();

        for (Playlist playlist: publicPlaylists)
            profileVBox.getChildren().add(new Label(playlist.getName()));
    }

    public void showLikedSongs() {
        profileVBox.getChildren().clear();

        for (Song song: likedSongs)
            profileVBox.getChildren().add(new Label(song.getTitle())); // incomplete pa to
    }

    public void showFollowing() {
        profileVBox.getChildren().clear();

        profileVBox.getChildren().add(new Label("ARTISTS"));

        for (Follower follower: followers)
            for (Account account: accounts)
                if (follower.getFollowing().equals(account.getUsername()) && account.isArtist())
                    profileVBox.getChildren().add(new Label(follower.getFollowing()));

        profileVBox.getChildren().add(new Label("LISTENERS"));

        for (Follower follower: followers)
            for (Account account: accounts)
                if (follower.getFollowing().equals(account.getUsername()) && !account.isArtist())
                    profileVBox.getChildren().add(new Label(follower.getFollowing()));
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
