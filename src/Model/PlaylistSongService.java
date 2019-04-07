package Model;

import View.LoginWindow;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PlaylistSongService {
    private Database db;

    public PlaylistSongService(Database db){
        this.db = db;
    }

    public boolean add(Playlist p, Song s){
        // ADD CONTACT

        String query = "INSERT INTO " + PlaylistSong.TABLE_NAME + " VALUE (?, ?, ?, ?)";
        Connection connection = db.getConnection();

        try{
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, p.getPlaylistID());
            statement.setInt(2, s.getSongID());
            statement.setString(3, LoginWindow.getLoggedUser());
            statement.setDouble(4, PlaylistSong.PK);

            boolean added = statement.execute();
            return added;
        }catch (SQLException e){
            e.printStackTrace();
        }
        return false;
    }

    public List<PlaylistSong> getAll(){
        //GET CONTACTS
        Connection connection = db.getConnection();
        List<PlaylistSong> playlistSongs = new ArrayList<>();

        String query = "SELECT * FROM " + PlaylistSong.TABLE_NAME;

        try {
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet rs = statement.executeQuery();

            while(rs.next()){
                PlaylistSong ps = new PlaylistSong();
                ps.setPlaylistID(rs.getInt(PlaylistSong.COL_PLAYLISTID));
                ps.setSongID(rs.getInt(PlaylistSong.COL_SONGID));
                ps.setUsername(rs.getString(PlaylistSong.COL_USERNAME));
                PlaylistSong.PK = rs.getDouble(PlaylistSong.COL_PK);

                playlistSongs.add(ps);
            }

        } catch (SQLException e){
            e.printStackTrace();
        }
        return playlistSongs;
    }

    public void removeSongFromPlaylist(Playlist p, Song s)
    {
        Connection connection = db.getConnection();
        String queryPlaylist = "UPDATE " + Playlist.TABLE_NAME + " SET songCount = songCount - 1 WHERE name = '" + p.getName() + "'";
        String query = "DELETE FROM " + PlaylistSong.TABLE_NAME + " WHERE songID = " + s.getSongID() + " AND playlistID = " +
                p.getPlaylistID();

        try{
            PreparedStatement statement = connection.prepareStatement(query);
            PreparedStatement statementPlaylist = connection.prepareStatement(queryPlaylist);
            statement.execute();
            if(p.getSongCount() > 0)
                statementPlaylist.execute();
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public void addSongToPlaylist(Playlist p, Song s)
    {
        Connection connection = db.getConnection();
        PlaylistSong.PK++;
        this.add(p, s);
        String query = "UPDATE " + Playlist.TABLE_NAME + " SET songCount = songCount + 1 WHERE name = '" + p.getName() + "'";
        try{
            PreparedStatement statement = connection.prepareStatement(query);
            statement.execute();
        }catch(SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void deleteSong(Song song) {
        try {
            Connection connection = db.getConnection();
            String queryPlaylistSong = "DELETE FROM " + PlaylistSong.TABLE_NAME + " WHERE songID = " + song.getSongID();
            PreparedStatement statementPlaylistSong = connection.prepareStatement(queryPlaylistSong);
            statementPlaylistSong.execute();
        }
        catch (SQLException e){
            e.printStackTrace();
        }
    }

    public void deletePlaylist(Playlist playlist) {
        try {
            Connection connection = db.getConnection();
            String queryPlaylistSong = "DELETE FROM " + PlaylistSong.TABLE_NAME + " WHERE playlistID = " + playlist.getPlaylistID();
            PreparedStatement statementPlaylistSong = connection.prepareStatement(queryPlaylistSong);
            statementPlaylistSong.execute();
        }
        catch (SQLException e){
            e.printStackTrace();
        }
    }

    public static void guestLogout()
    {
        Database db = new Database();
        Connection connection = db.getConnection();
        String query = "DELETE FROM " + PlaylistSong.TABLE_NAME + " WHERE username = 'Guest'";
        try{
            PreparedStatement statement = connection.prepareStatement(query);
            statement.execute();
        }catch(SQLException ex) {
            ex.printStackTrace();
        }
    }
}
