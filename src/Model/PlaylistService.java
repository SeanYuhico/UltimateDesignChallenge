package Model;

import ClientControl.Client;
import ClientControl.ClientController;
import Controller.LoginArtistController;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
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
        ClientController.getInstance().playlistAdd(p);

        String query = "INSERT INTO " + Playlist.TABLE_NAME + " VALUE (?, ?, ?, ?, ?, ?, ?)";
        Connection connection = db.getConnection();

        try{
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, p.getPlaylistID());
            statement.setString(2, p.getName());
            statement.setInt(3, p.getSongCount());
            statement.setString(4, p.getUsername());
            statement.setBoolean(5, p.isPublic());
            statement.setBoolean(6, p.isAlbum());
            if(p.isAlbum() && !p.getName().equals("No Album"))
                statement.setBinaryStream(7, new FileInputStream(new File("src/Pictures/vinyl.png")));
            else
                statement.setBinaryStream(7, null);

            boolean added = statement.execute();
            return added;
        }catch (SQLException e){
            e.printStackTrace();
        }catch (FileNotFoundException e){
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
                p.setPublic(rs.getBoolean(Playlist.COL_PUBLIC));
                p.setAlbum(rs.getBoolean(Playlist.COL_ISALBUM));

                playlists.add(p);
            }

        } catch (SQLException e){
            e.printStackTrace();
        }

        return playlists;
    }

    public void upload(Playlist playlist){
        Connection connection = db.getConnection();
        String query = "UPDATE " + Playlist.TABLE_NAME + " SET songCount = songCount + 1 WHERE name = '" + playlist.getName() + "' AND " +
                "username = '" + LoginArtistController.getLoggedUser() + "'";
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
        String decrement = "UPDATE " + Playlist.TABLE_NAME + " SET songCount = songCount - 1 WHERE name = '" + p.getName() + "' AND " +
                "username = '" + LoginArtistController.getLoggedUser() + "'";
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

    public void makePublic(int id, String isPublic)
    {
        Database db = new Database();
        Connection connection = db.getConnection();
        String query = "UPDATE " + Playlist.TABLE_NAME + " SET public = " + isPublic + " WHERE playlistID = " + id;
        try{
            PreparedStatement statement = connection.prepareStatement(query);
            statement.execute();
        }catch(SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void uploadAlbumCover(Playlist p, String filename)
    {
        Connection connection = new Database().getConnection();
        String query = "UPDATE " + Playlist.TABLE_NAME + " SET cover = ? WHERE playlistID = " + p.getPlaylistID();

        try {
            File file = new File(filename);
            FileInputStream input = new FileInputStream(file);

            PreparedStatement statement = connection.prepareStatement(query);
            statement.setBinaryStream(1, input);

            System.out.println("nagexecute shet");
            statement.execute();
        }catch (SQLException e){
            e.printStackTrace();
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
    }
}
