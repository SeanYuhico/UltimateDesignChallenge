package Model;

import java.sql.*;

public class SongLoader {
    private Database db;
    private String tempFilename;

    public SongLoader(Database db)
    {
        this.db = db;
    }

    public String loadSong(String title)
    {
        System.out.println(title);
        Connection connection = db.getConnection();
        String query = "SELECT file FROM " + Song.TABLE_NAME + " WHERE title = '" + title + "'";

        try{
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet rs = statement.executeQuery();
            if(rs.next()) {
                Blob blob = rs.getBlob("file");
                SongConverter songConverter = new SongConverter();
                if(blob != null) {
                    tempFilename = songConverter.convertFromBlob(blob, title);
                    System.out.println("Song loaded");
                }
            }
        }catch (SQLException e)
        {
            e.printStackTrace();
        }
        return tempFilename;
    }
}
