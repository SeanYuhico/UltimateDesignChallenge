package Model;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.SQLException;

public class Converter {

    private File directory;
    private String tempFilename;

    public String convertToMP3(Blob blob){
        try {
            directory = new File("C:\\Users\\Legs\\Desktop\\dc2\\src\\Music");
            File tempMp3 = File.createTempFile("temporary", ".mp3",
                    directory);
            InputStream is = blob.getBinaryStream();
            FileOutputStream fos = new FileOutputStream(tempMp3);
            int b = 0;
            while((b = is.read()) != -1){
                fos.write(b);
            }
            fos.close();
            System.out.println("Byte array to mp3 conversion: successful");
            tempFilename = tempMp3.getName();
        }
        catch (IOException e){
            e.getMessage ();
            e.printStackTrace();
            System.out.println(e);
        }
        catch (SQLException e){
            e.getMessage ();
            e.printStackTrace();
            System.out.println(e);
        }
        tempFilename = "file:/C:/Users/Legs/Desktop/dc2/src/Music/" + tempFilename;
        return tempFilename;
    }

    public File getDirectory() {
        return directory;
    }
}
