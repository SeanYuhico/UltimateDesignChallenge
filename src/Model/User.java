package Model;


import java.util.ArrayList;

public class User {
    private static final User singleton = new User();
    private String username;
    private String password;
    private ArrayList<Playlist> playlists;
    private ArrayList<Playlist> publicPlaylists;
    private ArrayList<Song> songs;


    public User() {

    }

    public static User getInstance() { return singleton; }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public ArrayList<Playlist> getPlaylists() {
        return playlists;
    }

    public void setPlaylists(ArrayList<Playlist> playlists) {
        this.playlists = playlists;
    }

    public ArrayList<Playlist> getPublicPlaylists() {
        return publicPlaylists;
    }

    public void setPublicPlaylists(ArrayList<Playlist> publicPlaylists) {
        this.publicPlaylists = publicPlaylists;
    }

    public ArrayList<Song> getSongs() {
        return songs;
    }

    public void setSongs(ArrayList<Song> songs) {
        this.songs = songs;
    }
}
