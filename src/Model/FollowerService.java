package Model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class FollowerService {
    private Database db;

    public FollowerService(Database db){
        this.db = db;
    }

    public boolean add(String follower, String following){
        // ADD CONTACT

        String query = "INSERT INTO " + Follower.TABLE_NAME + " VALUE (?, ?, ?)";
        Connection connection = db.getConnection();

        try{
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, follower);
            statement.setString(2, following);
            statement.setDouble(3, Follower.PK);

            boolean added = statement.execute();
            return added;
        }catch (SQLException e){
            e.printStackTrace();
        }
        return false;
    }
    
    public List<Follower> getAll(){
        Connection connection = db.getConnection();
        List<Follower> followList = new ArrayList<>();
        
        String query = "SELECT * FROM " + Follower.TABLE_NAME;

        try {
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet rs = statement.executeQuery();

            while(rs.next()){
                Follower f = new Follower();
                f.setFollower(rs.getString(Follower.COL_FOLLOWER));
                f.setFollowing(rs.getString(Follower.COL_FOLLOWING));
                Follower.PK = rs.getDouble(Follower.COL_PK);

                followList.add(f);
            }

        } catch (SQLException e){
            e.printStackTrace();
        }
        
        return followList;
    }

    public void unfollow(String follower, String unfollowing){
        Connection connection = db.getConnection();

        String query = "DELETE FROM " + Follower.TABLE_NAME + " WHERE Follower = '" + follower + "' AND Following = '" + unfollowing + "'";

        try{
            PreparedStatement statement = connection.prepareStatement(query);
            statement.execute();
        }catch (SQLException e){
            e.printStackTrace();
        }
    }
}
