package Controller;

//import BuilderPattern.Playlist;
import Model.*;
import View.*;
import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.Stage;
import javafx.util.Duration;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.xml.crypto.Data;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.Map;
import java.util.ResourceBundle;

import static View.Main.getMainStage;

public class MainController implements Initializable, Controller {
    @FXML private MediaView mv;
    private MediaPlayer mp, currPlayer, nextPlayer;
    private Media me;
    public static Pane dbPane, mpPane;
    @FXML Slider volumeSlider, progressSlider;
    @FXML Button playBtn, pauseBtn;
    @FXML BorderPane bPane;
    @FXML ImageView playImgVw, pauseImgVw, repeatImgVw, nextImgVw, prevImgVw, repeatOnceVw; //,repeatAllImgVw;
    @FXML Label artistLbl,nameLbl, songLbl, logoutLbl, dashboardPlaylistLbl;
    @FXML Label playlistNameLbl;
    @FXML ScrollPane sp;
    @FXML AnchorPane ap;
    @FXML VBox dashboardVBox, playlistVBox;
    @FXML Pane dashboardPane, playlistPane;
    @FXML Label mySongsPlaylist, myMostPlayed, artistsLbl, albumsLbl, genresLbl, yearLbl, myPlaylistsLbl;
    Displayer displayer;
    PlaylistBuilder genrePlaylistBuilder, albumPlaylistBuilder, artistPlaylistBuilder, yearPlaylistBuilder;
//    Queue<MediaPlayer> players;
    Stack<MediaPlayer> prevS;
    ArrayList<MediaPlayer> prevList, players;
    int songIndex, j =0;
    Database db;
    SongService ss;
    SongLoader sLoader;
    PlayMP3 play;
    ArrayList<String> songs;


    @Override
    public void initialize (URL location, ResourceBundle resources) {

        String aaronPath = new File("src/10,000 Reasons (Bless the Lord) - Matt Redman.mp3").getAbsolutePath();
//        String jerickPath = new File("C:\\Users\\11717777\\Downloads\\DesignChallenge2\\src\\10,000 Reasons (Bless the Lord) - Matt Redman.mp3").getAbsolutePath();
//        String song2 = new File("/Users/seanyuhico/Documents/SCHOOL/DesignChallenge2/src/ONE IN A MILLION.mp3").getAbsolutePath();
//        String song3 = new File("/Users/seanyuhico/Documents/SCHOOL/DesignChallenge2/src/TT.mp3").getAbsolutePath();

        Database db = new Database();
        SongService ss = new SongService(db);
        displayer = new Displayer();
        genrePlaylistBuilder = new GenrePlaylistBuilder();
        albumPlaylistBuilder = new AlbumPlaylistBuilder();
        artistPlaylistBuilder = new ArtistPlaylistBuilder();
        yearPlaylistBuilder = new YearPlaylistBuilder();

        boolean checker = false;
        for (Song s : ss.getAll()) {
            if (s.getUsername().equals(LoginWindow.getLoggedUser())) {
                checker = true;
            }
        }

        ArrayList<String> files = new ArrayList<>();
        if (checker) {
//            files = initList();
            files.add(aaronPath);
//        files.add(song2);
//        files.add(song3);
            prevList = new ArrayList<>();
            prevS = new Stack<>();
//        players = new LinkedList<>();
            players = new ArrayList<>();
            for (int i = 0; i < files.size(); i++) {
                prevList.add(new MediaPlayer(new Media(new File(files.get(i)).toURI().toString())));
                players.add(new MediaPlayer(new Media(new File(files.get(i)).toURI().toString())));
            }
            songIndex = 0;

//        me = new Media(new File(aaronPath).toURI().toString());
//        mp = new MediaPlayer(me);

            me = new Media(new File(files.get(0)).toURI().toString());
            mp = players.get(0);
            mv = new MediaView();
            mv.setMediaPlayer(mp);
            volumeSlider.setValue(mp.getVolume() * 100);
            DoubleProperty width = mv.fitWidthProperty();
            DoubleProperty height = mv.fitHeightProperty();
            width.bind(Bindings.selectDouble(mv.sceneProperty(), "width"));
            height.bind(Bindings.selectDouble(mv.sceneProperty(), "height"));
        }

//        DoubleProperty width = mv.fitWidthProperty();
//        DoubleProperty height = mv.fitHeightProperty();
//        width.bind(Bindings.selectDouble(mv.sceneProperty(), "width"));
//        height.bind(Bindings.selectDouble(mv.sceneProperty(), "height"));

//        setMPLabels();
//        volumeSlider.setValue(mp.getVolume() * 100);
            pauseImgVw.setVisible(false);
            playImgVw.setVisible(true);
            dashboardPane.setVisible(true);
            playlistPane.setVisible(false);

            dbPane = dashboardPane;
            mpPane = playlistPane;
            showMySongs();
        }


    public void logout(){
        Boolean ans = ConfirmBox.display("Logout", "Are you sure you want to logout?");
        if(ans) {
            if(LoginWindow.getLoggedUser().equals("Guest")) {
                // Delete all files of guest.
                PlaylistService.guestLogout();
                SongService.guestLogout();
                PlaylistService.guestLogout();
            }
            MusicPlayer.close(getMainStage());
            LoginWindow.display(getMainStage());
            mp.stop();
        }
    }

    public void changeProgress()
    {
        progressSlider.valueProperty().addListener(e ->{
            if (mp.getTotalDuration() != null)
                mp.seek(mp.getTotalDuration().multiply(progressSlider.getValue() / 100.0));
        });
    }
    public void changeVolume()
    {
        volumeSlider.valueProperty().addListener(e -> mp.setVolume(volumeSlider.getValue() / 100));
    }

    public void setMPLabels(String artist, String name)
    {
        artistLbl.setText(artist);
        nameLbl.setText(name);
    }
    public void play()
    {
        mp.play();
        mp.setRate(1);
        playImgVw.setVisible(false);
        pauseImgVw.setVisible(true);
//        mp.currentTimeProperty().addListener((Observable)-> {
//            if(progressSlider.isValueChanging()){
//                mp.seek(Duration.seconds(progressSlider.getValue()*totalTime/100));
//            }
//            if(progressSlider.isPressed()){
//                mp.seek(Duration.seconds(progressSlider.getValue()*totalTime/100));
//            }
//            progressSlider.setValue(((mp.getCurrentTime().toSeconds()*100)/totalTime));
//        });
//        mp.setCycleCount(mp.INDEFINITE);

        mp.setOnEndOfMedia(new Runnable() {
            @Override
            public void run() {
                next();
            }
        });
    }

    public void playAll() {
        db = new Database();
        ss = new SongService(db);
        sLoader = new SongLoader(db);
        play = new PlayMP3();
        songs = new ArrayList<>();

        for (int i = 0; i < ss.getAll().size(); i++) {
            songs.add(sLoader.loadSong(ss.getAll().get(i).getTitle()));
        }
        play.setMedia(songs.get(j));
        MediaPlayer mp = play.getMediaPlayer();
        setMp(mp);
        setMPLabels(ss.getAll().get(j).getArtist(), ss.getAll().get(j).getTitle());
        mp.play();
        mp.setRate(1);
        playImgVw.setVisible(false);
        pauseImgVw.setVisible(true);

        mp.setOnEndOfMedia(new Runnable() {
            @Override
            public void run() {
                next();
            }
        });
    }


        public void pause ()
        {
            mp.pause();
            playImgVw.setVisible(true);
            pauseImgVw.setVisible(false);
        }

        public void next ()
        {
            j++;
            if (j < ss.getAll().size()) {
                play.setMedia(songs.get(j));
                MediaPlayer mp = play.getMediaPlayer();
                setMp(mp);
                setMPLabels(ss.getAll().get(j).getArtist(), ss.getAll().get(j).getTitle());
                play();
            }
//        players.element().dispose();
//        prevS.push(players.element());
//        players.get(songIndex).stop();
//        songIndex++;
//        players.remove(songIndex);
//        if(songIndex>=players.size()) {
//            players = resetQueue(players, prevList);
//            mp = players.element();
//            mp = players.get(songIndex);
//            mv.setMediaPlayer(mp);
//            mp.seek(mp.getStartTime());
//            pause();
//        }
//        else {
            /*mp = players.element()*//*get((players.indexOf(currPlayer) +1) *//*% players.size()*//*)*/
            ;
//            mp=players.element();
//            mp=players.get(songIndex);
//            mv.setMediaPlayer(mp);
//            mp.seek(mp.getStartTime());
//            play();
//        }
        }


    public void prev()
    {
        j--;
        if (j < ss.getAll().size()  && j >= 0) {
            play.setMedia(songs.get(j));
            MediaPlayer mp = play.getMediaPlayer();
            setMp(mp);
            setMPLabels(ss.getAll().get(j).getArtist(), ss.getAll().get(j).getTitle());
            play();
        }
//        }
//        if (mp.getCurrentTime().toSeconds() <= 3)
//        {
//            players.get(songIndex).stop();
//            songIndex--;
//            mp = players.get(songIndex);
//            mv.setMediaPlayer(mp);
//            mp.seek(mp.getStartTime());
//            play();
//        }
//
//        else
//        {
//            mp.seek(mp.getStartTime());
//            mp.setRate(1);
//            mp.play();
//        }
    }

    public void addNewPlaylist()
    {
        PlaylistEditor.addNewPlaylist();
        update();
    }

    public void repeatSong()
    {
         mp.setOnEndOfMedia(new Runnable() {
             @Override
             public void run() {
                 mp.seek(Duration.ZERO);
                 mp.play();
             }
         });

        repeatImgVw.setVisible(false);
        repeatOnceVw.setVisible(true);
    }

    public void stopRepeat(){
        mp.setOnEndOfMedia(new Runnable() {
            @Override
            public void run() {

            }
        });
        repeatOnceVw.setVisible(false);
        repeatImgVw.setVisible(true);
    }

    public void repeatPlaylist() {


        repeatImgVw.setVisible(true);
    }
    public void uploadSong()
    {
        UploadSongWindow.display();
        dashboardVBox.getChildren().clear();
        update();
    }

    public void forward() {

        mp.seek(Duration.seconds(mp.getCurrentTime().toSeconds()+10));
        progressSlider.setValue(mp.getCurrentTime().toSeconds());
    }

    public void backward() {
        mp.seek(Duration.seconds(mp.getCurrentTime().toSeconds()-10));
        progressSlider.setValue(mp.getCurrentTime().toSeconds());
    }

    public void showMyPlaylists() {
        dashboardPane.setVisible(false);
        playlistPane.setVisible(true);
        playlistNameLbl.setText("My Playlists");
        playlistVBox.getChildren().clear();
        DisplayNonDefault.displayAllPlaylists(playlistVBox, dashboardVBox, dashboardPane, playlistPane, mp, dashboardPlaylistLbl, playlistNameLbl);
    }

    public ArrayList<MediaPlayer> resetQueue(ArrayList<MediaPlayer> q, ArrayList<MediaPlayer> mPL){
        int i;
        for (i=0;i<mPL.size();i++){
            q.add(mPL.get(i));
        }
        songIndex=0;
        return q;
    }

    public void showMySongs()
    {
        dashboardPane.setVisible(true);
        playlistPane.setVisible(false);
        dashboardPlaylistLbl.setText("My Songs");
        dashboardVBox.getChildren().clear();
        DisplayNonDefault.displayPlaylistSongs(dashboardPlaylistLbl.getText(), dashboardVBox, this);
        System.out.println(dashboardVBox.getChildren());
    }

    public void showByArtists() {
        playlistNameLbl.setText("Artists");
        playlistVBox.getChildren().clear();
        displayer.setPlaylistBuilder(artistPlaylistBuilder);
        displayer.constructArtistPlaylist(dashboardPlaylistLbl, playlistVBox, dashboardVBox, dashboardPane, playlistPane, this);
        dashboardPane.setVisible(false);
        playlistPane.setVisible(true);
    }

    public void showByAlbumNames() {
        playlistNameLbl.setText("Albums");
        playlistVBox.getChildren().clear();
        displayer.setPlaylistBuilder(albumPlaylistBuilder);
        displayer.constructAlbumPlaylist(dashboardPlaylistLbl, playlistVBox, dashboardVBox, dashboardPane, playlistPane, this);
        dashboardPane.setVisible(false);
        playlistPane.setVisible(true);
    }

    public void showByYears() {
        playlistNameLbl.setText("Years");
        playlistVBox.getChildren().clear();
        displayer.setPlaylistBuilder(yearPlaylistBuilder);
        displayer.constructYearPlaylist(dashboardPlaylistLbl, playlistVBox, dashboardVBox, dashboardPane, playlistPane, this);
        dashboardPane.setVisible(false);
        playlistPane.setVisible(true);
    }

    public void showByGenres() {
        playlistNameLbl.setText("Genres");
        playlistVBox.getChildren().clear();
        displayer.setPlaylistBuilder(genrePlaylistBuilder);
        displayer.constructGenrePlaylist(dashboardPlaylistLbl, playlistVBox, dashboardVBox, dashboardPane, playlistPane, this);
        dashboardPane.setVisible(false);
        playlistPane.setVisible(true);
    }

    public void showByMostPlayed() {
        dashboardPlaylistLbl.setText("My Most Played Songs");
        dashboardVBox.getChildren().clear();
        DisplayNonDefault.displayByMostPlayed(dashboardVBox, this);
        dashboardPane.setVisible(true);
        playlistPane.setVisible(false);
    }

    public void showProfile() {
        ProfileWindow.display();
    }

    public ArrayList<String> initList(){
        ArrayList<String> results = new ArrayList<>();
        File[] files = new File("\\Users\\aaron\\Desktop\\DesignChallenge2\\DesignChallenge2\\src\\Music\\").listFiles();
        for(File file : files){
            if(file.isFile()){
                results.add(file.getName());
            }
        }
        return results;
    }

    public void shuffle() {}

    public void update() {
        if (dashboardVBox.isVisible()) {
            if (dashboardPlaylistLbl.getText().equals("My Songs"))
                showMySongs();
            else if (dashboardPlaylistLbl.getText().equals("My Most Played Songs"))
                showByMostPlayed();
            else if (dashboardPlaylistLbl.getText().equals("Genres"))
                showByGenres();
            else if (dashboardPlaylistLbl.getText().equals("Album Names"))
                showByAlbumNames();
            else if (dashboardPlaylistLbl.getText().equals("Artists"))
                showByArtists();
            else if (dashboardPlaylistLbl.equals("Years"))
                showByYears();
        }
        else if (playlistVBox.isVisible() ) {
            if (playlistNameLbl.getText().equals("My Playlists"))
                showMyPlaylists();
        }
    }

    public MediaPlayer getMp() {
        return mp;
    }

    public void setMp(MediaPlayer mp) {
        this.mp = mp;
    }
}
