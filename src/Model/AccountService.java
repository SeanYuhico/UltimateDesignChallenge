package Model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AccountService {

    private Database db;

    public AccountService(Database db){
        this.db = db;
    }

    public boolean add(Account a){
        // ADD CONTACT

        String query = "INSERT INTO " + Account.TABLE_NAME + " VALUE (?, ?, ?)";
        Connection connection = db.getConnection();

        try{
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, a.getUsername());
            statement.setString(2, a.getPassword());
            statement.setBoolean(3, a.isArtist());

            boolean added = statement.execute();
            return added;
        }catch (SQLException e){
            e.printStackTrace();
        }
        return false;
    }

    public List<Account> getAll(){
        //GET CONTACTS
        Connection connection = db.getConnection();
        List<Account> accounts = new ArrayList<>();

        String query = "SELECT * FROM " + Account.TABLE_NAME;

        try {
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet rs = statement.executeQuery();

            while(rs.next()){
                Account a = new Account();
                a.setUsername(rs.getString(Account.COL_USERNAME));
                a.setPassword(rs.getString(Account.COL_PASSWORD));
                a.setArtist(rs.getBoolean(Account.COL_ARTIST));
                accounts.add(a);
            }

        } catch (SQLException e){
            e.printStackTrace();
        }

        return accounts;
    }

    public static void guestLogout(){
        Database db = new Database();
        Connection connection = db.getConnection();
        String query = "DELETE FROM " + Account.TABLE_NAME + " WHERE username = 'Guest'";
        try{
            PreparedStatement statement = connection.prepareStatement(query);
            statement.execute();
        }catch(SQLException ex) {
            ex.printStackTrace();
        }
    }

}
