package Model;

public class TimesPlayed {
    public static final String TABLE_NAME = "TimesPlayed";
    public static final String COL_SONGID = "songID";
    public static final String COL_ACCOUNTNAME = "accountName";
    public static final String COL_NUMTIMESPLAYED = "numTimesPlayed";
    public static final String COL_PK = "PK";

    private int songID;
    private String accountName;
    private int numTimesPlayed;
    public static double PK = 0;

    public int getSongID() {
        return songID;
    }

    public void setSongID(int songID) {
        this.songID = songID;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public int getNumTimesPlayed() {
        return numTimesPlayed;
    }

    public void setNumTimesPlayed(int numTimesPlayed) {
        this.numTimesPlayed = numTimesPlayed;
    }
}
