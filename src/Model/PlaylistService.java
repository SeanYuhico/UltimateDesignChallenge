package Model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PlaylistService {
    private Database db;

    public PlaylistService(Database db){
        this.db = db;
    }

    public boolean add(Playlist p){
        // ADD CONTACT

        String query = "INSERT INTO " + Playlist.TABLE_NAME + " VALUE (?, ?, ?, ?, ?)";
        Connection connection = db.getConnection();

        try{
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, p.getPlaylistID());
            statement.setString(2, p.getName());
            statement.setInt(3, p.getSongCount());
            statement.setString(4, p.getUsername());
            statement.setBoolean(5, p.isFave());

            boolean added = statement.execute();
            return added;
        }catch (SQLException e){
            e.printStackTrace();
        }
        return false;
    }

    public List<Playlist> getAll(){
        //GET CONTACTS
        Connection connection = db.getConnection();
        List<Playlist> playlists = new ArrayList<>();

        String query = "SELECT * FROM " + Playlist.TABLE_NAME;

        try {
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet rs = statement.executeQuery();

            while(rs.next()){
                Playlist p = new Playlist();
                p.setPlaylistID(rs.getInt(Playlist.COL_PLAYLISTID));
                p.setName(rs.getString(Playlist.COL_NAME));
                p.setSongCount(rs.getInt(Playlist.COL_SONGCOUNT));
                p.setUsername(rs.getString(Playlist.COL_USERNAME));
                p.setFave(rs.getBoolean(Playlist.COL_FAVE));

                playlists.add(p);
            }

        } catch (SQLException e){
            e.printStackTrace();
        }

        return playlists;
    }

    public void upload(Playlist playlist){
        Connection connection = db.getConnection();
        String query = "UPDATE " + Playlist.TABLE_NAME + " SET songCount = songCount + 1 WHERE name = '" + playlist.getName() + "'";
        try{
            PreparedStatement statement = connection.prepareStatement(query);
            statement.execute();
        }catch(SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void update(Playlist playlist, int id)
    {
        Connection connection = db.getConnection();
        String update = "UPDATE " + Playlist.TABLE_NAME + " SET name = '" + playlist.getName() + "' WHERE playlistID = " + id;
        try {
            PreparedStatement statementPlaylist = connection.prepareStatement(update);
            statementPlaylist.execute();
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public void decrement(Playlist p)
    {
        Connection connection = db.getConnection();
        String decrement = "UPDATE " + Playlist.TABLE_NAME + " SET songCount = songCount - 1 WHERE name = '" + p.getName() + "'";
        try {
            PreparedStatement statementPlaylist = connection.prepareStatement(decrement);
            statementPlaylist.execute();
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public void delete(Playlist p)
    {
        Connection connection = db.getConnection();
        try {
            String querySong = "DELETE FROM " + Playlist.TABLE_NAME + " WHERE playlistID = " + p.getPlaylistID();
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
        String query = "DELETE FROM " + Playlist.TABLE_NAME + " WHERE username = 'Guest'";
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
        String query = "UPDATE " + Playlist.TABLE_NAME + " SET fave = " + fave + " WHERE playlistID = " + id;
        try{
            PreparedStatement statement = connection.prepareStatement(query);
            statement.execute();
        }catch(SQLException ex) {
            ex.printStackTrace();
        }
    }
}
