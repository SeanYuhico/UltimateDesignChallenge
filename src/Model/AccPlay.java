package Model;

public class AccPlay {
    public static final String TABLE_NAME = "accplay";
    public static final String COL_PLAYLISTID = "playlistID";
    public static final String COL_USER = "user";
    public static final String COL_PK = "PK";

    private int playlistID;
    private String user;
    public static double PK = 0;

    public int getPlaylistID() {
        return playlistID;
    }

    public void setPlaylistID(int playlistID) {
        this.playlistID = playlistID;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }
}
