package Model;


import java.io.*;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Converter {

    private File directory;
    private String tempFilename;
    private ArrayList<String> results;
    private int index;

    public String convertToMP3(Blob blob, String title){
        if (!checkMP3(title)) {
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

    public boolean checkMP3(String title){
        results = new ArrayList<>();


        File[] files = new File("/Users/seanyuhico/Documents/SCHOOL/UltimateDesignChallenge/src/Music/").listFiles();

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

    /*public static void splitFile(File f) throws IOException {
        int partCounter = 1;//I like to name parts from 001, 002, 003, ...
        //you can change it to 0 if you want 000, 001, ...

        int sizeOfFiles = 1024 * 1024;// 1MB
        byte[] buffer = new byte[sizeOfFiles];

        String fileName = f.getName();

        //try-with-resources to ensure closing stream
        try (FileInputStream fis = new FileInputStream(f);
             BufferedInputStream bis = new BufferedInputStream(fis)) {

            int bytesAmount = 0;
            while ((bytesAmount = bis.read(buffer)) > 0) {
                //write each chunk of data into separate file with different number in name
                String filePartName = String.format("%s.%03d", fileName, partCounter++);
                File newFile = new File(f.getParent(), filePartName);
                try (FileOutputStream out = new FileOutputStream(newFile)) {
                    out.write(buffer, 0, bytesAmount);
                }
            }
        }
    }*/

    public File getDirectory() {
        return directory;
    }
}
