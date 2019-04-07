package View;

import Model.Playlist;

public abstract class PlaylistBuilder {
    protected Playlist playlist;

    public Playlist getPlaylist(){
        return playlist;
    }

    public abstract void buildName(String name);
    public abstract void buildSongCount(int songCount);
    public abstract void buildUsername();
}
