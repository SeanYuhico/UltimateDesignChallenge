package Model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SongService {

    private Database db;

    public SongService(Database db){
        this.db = db;
    }

    public boolean add(Song s){
        // ADD CONTACT

        String query = "INSERT INTO " + Song.TABLE_NAME + " VALUE (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        Connection connection = db.getConnection();

        try{
            File file = new File(s.getFilename());
            FileInputStream input = new FileInputStream(file);

            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, s.getSongID()); // uhh temporary lang to
            statement.setString(2, s.getTitle());
            statement.setString(3, s.getArtist());
            statement.setString(4, s.getAlbumName());
            statement.setString(5, s.getGenre());
            statement.setString(6, s.getSongName());
            statement.setString(7, s.getYear());
            statement.setInt(8, s.getNumTimesPlayed());
            statement.setString(9, s.getUsername());
            statement.setBinaryStream(10, input);
            statement.setBoolean(11, s.isFave());

            boolean added = statement.execute();
            return added;
        }catch (SQLException e){
            e.printStackTrace();
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        return false;
    }

    public List<Song> getAll(){
        Connection connection = db.getConnection();
        List<Song> songs = new ArrayList<>();

        String query = "SELECT * FROM " + Song.TABLE_NAME;

        try {
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet rs = statement.executeQuery();

            while(rs.next()){
                Song s = new Song();
                s.setSongID(rs.getInt(Song.COL_SONGID));
                s.setTitle(rs.getString(Song.COL_TITLE));
                s.setArtist(rs.getString(Song.COL_ARTIST));
                s.setAlbumName(rs.getString(Song.COL_ALBUMNAME));
                s.setGenre(rs.getString(Song.COL_GENRE));
                s.setSongName(rs.getString(Song.COL_SONGNAME));
                s.setYear(rs.getString(Song.COL_YEAR));
                s.setNumTimesPlayed(rs.getInt(Song.COL_NUMTIMESPLAYED));
                s.setUsername(rs.getString(Song.COL_USERNAME));
                s.setFave(rs.getBoolean(Song.COL_FAVE));

                songs.add(s);
            }

        } catch (SQLException e){
            e.printStackTrace();
        }

        return songs;
    }

    public void incNumTimesPlayed(Song s){
        Connection connection = db.getConnection();
        String query = "UPDATE " + Song.TABLE_NAME + " SET numTimesPlayed = numTimesPlayed + 1 WHERE songID = " + s.getSongID();
        try{
            PreparedStatement statement = connection.prepareStatement(query);
            statement.execute();
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public boolean update(int id, Song s){
        // UPDATE A CONTACT
        Connection connection = db.getConnection();
        String query = "UPDATE " + Song.TABLE_NAME + " SET title = ?, artist = ?, albumName = ?, genre = ?, year = ? " + "WHERE songID = " + id;

        try {
            PreparedStatement statement = connection.prepareStatement(query);

            statement.setString(1,s.getTitle());
            statement.setString(2,s.getArtist());
            statement.setString(3,s.getAlbumName());
            statement.setString(4,s.getGenre());
            statement.setString(5,s.getYear());

            boolean updated = statement.execute();
            return updated;

        } catch (SQLException e){
            e.printStackTrace();
        }
        return false;
    }

    public void delete(Song song)
    {
        Connection connection = db.getConnection();
        try {
            String querySong = "DELETE FROM " + Song.TABLE_NAME + " WHERE songID = " + song.getSongID();
            PreparedStatement statementSong = connection.prepareStatement(querySong);
            statementSong.execute();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void guestLogout()
    {
        Database db = new Database();
        Connection connection = db.getConnection();
        String query = "DELETE FROM " + Song.TABLE_NAME + " WHERE username = 'Guest'";
        try{
            PreparedStatement statement = connection.prepareStatement(query);
            statement.execute();
        }catch(SQLException ex) {
            ex.printStackTrace();
        }
    }

    public static void makeFave(int id, String fave)
    {
        Database db = new Database();
        Connection connection = db.getConnection();
        String query = "UPDATE " + Song.TABLE_NAME + " SET fave = " + fave + " WHERE songID = " + id;
        try{
            PreparedStatement statement = connection.prepareStatement(query);
            statement.execute();
        }catch(SQLException ex) {
            ex.printStackTrace();
        }
    }
}

