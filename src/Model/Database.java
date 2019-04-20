package Model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.util.TimeZone;

public class Database {
    private final static String DRIVER_NAME = "com.mysql.jdbc.Driver";
    private final static String URL = "jdbc:mysql://localhost:3306/";
    private final static String USERNAME = "root";
    private final static String PASSWORD = "boiii";
    private final static String DATABASE = "music_player";

    public Database(){
        getConnection();
    }

    public Connection getConnection(){
        try{
            Class.forName(DRIVER_NAME);
            Connection connection = DriverManager.getConnection(
                    URL +
                            DATABASE + "?autoReconnect=true&useSSL=false&useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=" + TimeZone.getDefault().getID(),
                    USERNAME,
                    PASSWORD);
            System.out.println("[MYSQL] Connection Successful!");
            return connection;
        } catch(SQLException e){
            System.out.println("[MYSQL] Did not able to connect :(");
            e.printStackTrace();
            return null;
        } catch(ClassNotFoundException e){
            System.out.println("[MYSQL] Did not able to connect");
            e.printStackTrace();
            return null;
        }
    }
}
