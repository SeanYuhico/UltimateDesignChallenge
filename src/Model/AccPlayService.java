package Model;

import ClientControl.ClientController;
import Controller.LoginArtistController;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AccPlayService {
    private Database db;

    public AccPlayService(Database db)
    {
        this.db = db;
    }

    public boolean add(Playlist p){
        // ADD CONTACT
        ClientController.getInstance().accplayAdd(p);

        String query = "INSERT INTO " + AccPlay.TABLE_NAME + " VALUE (?, ?, ?)";
        Connection connection = db.getConnection();

        try{
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, p.getPlaylistID());
            statement.setString(2, LoginArtistController.getLoggedUser());
            statement.setDouble(3, AccPlay.PK);

            boolean added = statement.execute();
            return added;
        }catch (SQLException e){
            e.printStackTrace();
        }
        return false;
    }

    public List<AccPlay> getAll(){
        //GET CONTACTS
        Connection connection = db.getConnection();
        List<AccPlay> accPlays = new ArrayList<>();

        String query = "SELECT * FROM " + AccPlay.TABLE_NAME;

        try {
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet rs = statement.executeQuery();

            while(rs.next()){
                AccPlay ap = new AccPlay();
                ap.setPlaylistID(rs.getInt(AccPlay.COL_PLAYLISTID));
                ap.setUser(rs.getString(AccPlay.COL_USER));
                AccPlay.PK = rs.getDouble(AccPlay.COL_PK);

                accPlays.add(ap);
            }

        } catch (SQLException e){
            e.printStackTrace();
        }
        return accPlays;
    }

    public void unfollow(int id, String user){
        Connection connection = db.getConnection();

        String query = "DELETE FROM " + AccPlay.TABLE_NAME + " WHERE playlistID = " + id + " AND user = '" + user + "'";

        try{
            PreparedStatement statement = connection.prepareStatement(query);
            statement.execute();
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public static void guestLogout(){
        Database db = new Database();
        Connection connection = db.getConnection();
        String query = "DELETE FROM " + AccPlay.TABLE_NAME + " WHERE user = 'Guest'";
        try{
            PreparedStatement statement = connection.prepareStatement(query);
            statement.execute();
        }catch(SQLException ex) {
            ex.printStackTrace();
        }
    }
}
