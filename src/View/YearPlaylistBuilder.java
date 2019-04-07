package View;

import Model.Playlist;

public class YearPlaylistBuilder extends PlaylistBuilder {
    private Playlist playlist;

    public YearPlaylistBuilder() {
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
        playlist.setUsername(LoginWindow.getLoggedUser());
    }
}
