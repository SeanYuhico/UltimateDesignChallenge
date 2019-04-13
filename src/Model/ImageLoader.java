package Model;

import java.sql.*;

public class ImageLoader {
    private Database db;
    private String tempFilename;

    public ImageLoader(Database db)
    {
        this.db = db;
    }

    public String loadImage(String title)
    {
        System.out.println(title);
        Connection connection = db.getConnection();
        String query = "SELECT cover FROM " + Playlist.TABLE_NAME + " WHERE name = '" + title + "'";

        try{
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet rs = statement.executeQuery();
            if(rs.next()) {
                Blob blob = rs.getBlob("cover");
                Converter converter = new Converter();
                if(blob != null) {
                    tempFilename = converter.convertToImg(blob, title);
                    System.out.println("Image loaded");
                }
            }
        }catch (SQLException e)
        {
            e.printStackTrace();
        }
        return tempFilename;
    }
}
