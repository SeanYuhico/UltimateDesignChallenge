package View;

import Controller.LoginArtistController;
import Model.Playlist;
public class ArtistPlaylistBuilder extends PlaylistBuilder {
    private Playlist playlist;

    public ArtistPlaylistBuilder() {
        this.playlist = new Playlist();
    }

    public Playlist getPlaylist() {
        return playlist;
    }

    @Override
    public void buildName(String name) {
        playlist.setName(name);
    }

    @Override
    public void buildSongCount(int songCount) {
        playlist.setSongCount(songCount);
    }

    @Override
    public void buildUsername(){
        playlist.setUsername(LoginArtistController.getLoggedUser());
    }
}
