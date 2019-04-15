package Model;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.ArrayList;

public class ImageConverter extends ConverterTemplate {
    private File directory;
    private String tempFilename;
    private ArrayList<String> results;
    private int index;

    public boolean checkFile(String title){
        results = new ArrayList<>();


        File[] files = new File("C:\\Users\\aaron\\Desktop\\UltimateDesignChallenge\\src\\AlbumArt\\").listFiles();

        for (File file : files) {
            if (file.isFile()) {
                results.add(file.getName());
            }
        }

        for(int i=0; i<results.size(); i++){
            if(results.get(i).contains(title.replaceAll("\\s", ""))){
                System.out.println("TRUE");
                index = i;
                return true;
            }
        }
        System.out.println("FALSE");
        return false;
    }

    public String convertFromBlob(Blob blob, String title){
        if (!checkFile(title)) {
            try {
                directory = new File("C:\\Users\\aaron\\Desktop\\UltimateDesignChallenge\\src\\AlbumArt");
                String newTitle = title.replaceAll("\\s", "");
                File tempImg = File.createTempFile(newTitle, ".png", directory);
                InputStream is = blob.getBinaryStream();
                FileOutputStream fos = new FileOutputStream(tempImg);
                int b = 0;
                while ((b = is.read()) != -1) {
                    fos.write(b);
                }
                fos.close();
                System.out.println("Byte array to mp3 conversion: successful");
                tempFilename = tempImg.getName();
            } catch (IOException e) {
                e.getMessage();
                e.printStackTrace();
                System.out.println(e);
            } catch (SQLException e) {
                e.getMessage();
                e.printStackTrace();
                System.out.println(e);
            }
            tempFilename = "file:\\Users\\aaron\\Desktop\\UltimateDesignChallenge\\src\\AlbumArt\\" + tempFilename;
//            return tempFilename;
        }
        else {
            tempFilename = "file:\\Users\\aaron\\Desktop\\UltimateDesignChallenge\\src\\AlbumArt\\" + results.get(index);
        }
        return tempFilename;
    }
}
