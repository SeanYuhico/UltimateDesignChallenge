package Model;

import Controller.LoginArtistController;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TimesPlayedService {
    private Database db;

    public TimesPlayedService(Database db){
        this.db = db;
    }

    public boolean add(int id){
        // ADD CONTACT

        String query = "INSERT INTO " + TimesPlayed.TABLE_NAME + " VALUE (?, ?, ?, ?)";
        Connection connection = db.getConnection();

        try{
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, id);
            statement.setString(2, LoginArtistController.getLoggedUser());
            statement.setInt(3, 0);
            statement.setDouble(4, TimesPlayed.PK);

            boolean added = statement.execute();
            return added;
        }catch (SQLException e){
            e.printStackTrace();
        }
        return false;
    }

    public List<TimesPlayed> getAll(){
        //GET CONTACTS
        Connection connection = db.getConnection();
        List<TimesPlayed> tps = new ArrayList<>();

        String query = "SELECT * FROM " + TimesPlayed.TABLE_NAME;

        try {
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet rs = statement.executeQuery();

            while(rs.next()){
                TimesPlayed tp = new TimesPlayed();
                tp.setSongID(rs.getInt(TimesPlayed.COL_SONGID));
                tp.setAccountName(rs.getString(TimesPlayed.COL_ACCOUNTNAME));
                tp.setNumTimesPlayed(rs.getInt(TimesPlayed.COL_NUMTIMESPLAYED));
                TimesPlayed.PK = rs.getDouble(TimesPlayed.COL_PK);

                tps.add(tp);
            }

        } catch (SQLException e){
            e.printStackTrace();
        }
        return tps;
    }

    public void incNumTimesPlayed(int id, String user){
        Connection connection = db.getConnection();
        String query = "UPDATE " + TimesPlayed.TABLE_NAME + " SET numTimesPlayed = numTimesPlayed + 1 WHERE accountName = '" +
                user + "' AND songID = " + id;
        try{
            PreparedStatement statement = connection.prepareStatement(query);
            statement.execute();
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

}
