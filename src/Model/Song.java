package Model;

import java.io.File;
import java.sql.Blob;

public class Song {
    public static final String TABLE_NAME = "song";
    public static final String COL_SONGID = "songID";
    public static final String COL_TITLE = "title";
    public static final String COL_ARTIST = "artist";
    public static final String COL_ALBUMNAME = "albumName";
    public static final String COL_GENRE = "genre";
    public static final String COL_SONGNAME = "songName";
    public static final String COL_YEAR = "year";
    public static final String COL_NUMTIMESPLAYED = "numTimesPlayed";
    public static final String COL_USERNAME = "username";
    public static final String COL_FAVE = "fave";

    private int songID;
    private String title;
    private String artist;
    private String albumName;
    private String genre;
    private String songName;
    private String year;
    private int numTimesPlayed;
    private Song nextOnQueue = null;
    private String username;
    private String filename;
    private boolean fave = false;

    public Song(){}

    public int getSongID() {
        return songID;
    }

    public void setSongID(int songID) {
        this.songID = songID;
    }

    public String getTitle() {
        return title;
    }

    public String getArtist() {
        return artist;
    }

    public String getAlbumName() {
        return this.albumName;
    }

    public String getGenre() {
        return genre;
    }

    public String getYear() {
        return year;
    }

    public int getNumTimesPlayed() {
        return numTimesPlayed;
    }

    public String getUsername() {
        return username;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public void setAlbumName(String albumName) {
        this.albumName = albumName;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getSongName() {
        return songName;
    }

    public void setSongName(String songName) {
        this.songName = songName;
    }

    public void setNextOnQueue(Song nextSong) {nextOnQueue = nextSong;}

    public void setNumTimesPlayed(int numTimesPlayed) {
        this.numTimesPlayed++;
    }

    public void setUsername(String username)
    {
        this.username = username;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public boolean isFave() {
        return fave;
    }

    public void setFave(boolean fave) {
        this.fave = fave;
    }
}

