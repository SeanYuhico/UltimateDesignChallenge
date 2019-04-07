package Model;

import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;

public class PlayMP3 {

    private Media media;
    private static MediaPlayer mp;

    public void playSong() {
        try {
            mp.play();
            mp.setRate(1);
        } catch (Exception e) {
            e.getMessage();
            e.printStackTrace();
            System.out.println(e);
        }
    }

    public void stopSong(){
        mp.stop();
    }
    public void setMedia(String filename){
        media = new Media(filename);
        mp = new MediaPlayer(media);
    }
    public MediaPlayer getMediaPlayer(){
        return mp;
    }
}
