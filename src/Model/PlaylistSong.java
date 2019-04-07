package Model;

public class PlaylistSong {
    public static final String TABLE_NAME = "playlistsong";
    public static final String COL_PLAYLISTID = "playlistID";
    public static final String COL_SONGID = "songID";
    public static final String COL_USERNAME = "username";
    public static final String COL_PK = "PK";

    private int playlistID;
    private int songID;
    private String username;
    public static double PK = 0;

    public PlaylistSong(){}

    public int getPlaylistID() {
        return playlistID;
    }

    public void setPlaylistID(int playlistID) {
        this.playlistID = playlistID;
    }

    public int getSongID() {
        return songID;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setSongID(int songID) {
        this.songID = songID;
    }
}
