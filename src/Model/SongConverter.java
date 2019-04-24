package Model;


import java.io.*;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SongConverter extends ConverterTemplate {

    private File directory;
    private String tempFilename;
    private ArrayList<String> results;
    private int index;

    public boolean checkFile(String title){
        results = new ArrayList<>();

        File[] files = new File("/Users/seanyuhico/Documents/SCHOOL/UltimateDesignChallenge/src/Music").listFiles();

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
                directory = new File("/Users/seanyuhico/Documents/SCHOOL/UltimateDesignChallenge/src/Music");
                String newTitle = title.replaceAll("\\s", "");
                File tempMp3 = File.createTempFile(newTitle, ".mp3", directory);
                InputStream is = blob.getBinaryStream();
                FileOutputStream fos = new FileOutputStream(tempMp3);
                int b = 0;
                while ((b = is.read()) != -1) {
                    fos.write(b);
                }
                fos.close();
                System.out.println("Byte array to mp3 conversion: successful");
                tempFilename = tempMp3.getName();
            } catch (IOException e) {
                e.getMessage();
                e.printStackTrace();
                System.out.println(e);
            } catch (SQLException e) {
                e.getMessage();
                e.printStackTrace();
                System.out.println(e);
            }
            tempFilename = "file:/Users/seanyuhico/Documents/SCHOOL/UltimateDesignChallenge/src/Music/" + tempFilename;
//            return tempFilename;
        }
        else {
            tempFilename = "file:/Users/seanyuhico/Documents/SCHOOL/UltimateDesignChallenge/src/Music/" + results.get(index);
        }
        return tempFilename;
    }


//    public boolean checkImg(String title){
//        results = new ArrayList<>();
//
//
//        File[] files = new File("C:\\Users\\aaron\\Desktop\\UltimateDesignChallenge\\src\\AlbumArt\\").listFiles();
//
//        for (File file : files) {
//            if (file.isFile()) {
//                results.add(file.getName());
//            }
//        }
//
//        for(int i=0; i<results.size(); i++){
//            if(results.get(i).contains(title.replaceAll("\\s", ""))){
//                System.out.println("TRUE");
//                index = i;
//                return true;
//            }
//        }
//        System.out.println("FALSE");
//        return false;
//    }
//
//    public String convertToImg(Blob blob, String title){
//        if (!checkImg(title)) {
//            try {
//                directory = new File("C:\\Users\\aaron\\Desktop\\UltimateDesignChallenge\\src\\AlbumArt");
//                String newTitle = title.replaceAll("\\s", "");
//                File tempImg = File.createTempFile(newTitle, ".png", directory);
//                InputStream is = blob.getBinaryStream();
//                FileOutputStream fos = new FileOutputStream(tempImg);
//                int b = 0;
//                while ((b = is.read()) != -1) {
//                    fos.write(b);
//                }
//                fos.close();
//                System.out.println("Byte array to mp3 conversion: successful");
//                tempFilename = tempImg.getName();
//            } catch (IOException e) {
//                e.getMessage();
//                e.printStackTrace();
//                System.out.println(e);
//            } catch (SQLException e) {
//                e.getMessage();
//                e.printStackTrace();
//                System.out.println(e);
//            }
//            tempFilename = "file:\\Users\\aaron\\Desktop\\UltimateDesignChallenge\\src\\AlbumArt\\" + tempFilename;
////            return tempFilename;
//        }
//        else {
//            tempFilename = "file:\\Users\\aaron\\Desktop\\UltimateDesignChallenge\\src\\AlbumArt\\" + results.get(index);
//        }
//        return tempFilename;
//    }

    public File getDirectory() {
        return directory;
    }
}
