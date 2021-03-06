package Controller;

import ClientControl.ClientController;
import Model.*;
import View.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.Stage;
import javafx.util.Duration;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import static View.Main.getMainStage;

public class MainController extends Controller implements Initializable {
    @FXML private MediaView mv;
    private MediaPlayer mp, currPlayer, nextPlayer;
    private Media me;
    public static Pane dbPane, mpPane;
    @FXML Slider volumeSlider, progressSlider;
    @FXML Button playPlaylistBtn;
    @FXML BorderPane bPane;
    @FXML ImageView playImgVw, pauseImgVw, repeatImgVw, nextImgVw, prevImgVw, repeatOnceVw, repeatPlaylistImgVw, notifImgVw, uncheckedNotifImgVw, refreshImg; //,repeatAllImgVw;
    @FXML Label artistLbl,nameLbl, songLbl, logoutLbl, dashboardPlaylistLbl;
    @FXML Label playlistNameLbl;
    @FXML ScrollPane sp;
    @FXML AnchorPane ap;
    @FXML VBox dashboardVBox, playlistVBox;
    @FXML Pane dashboardPane, playlistPane;
    @FXML Label mySongsPlaylist, myMostPlayed, artistsLbl, albumsLbl, genresLbl, yearLbl, myPlaylistsLbl;

    @FXML ImageView crtAbmBtn, upldSongBtn, imgvwShuffle, unshuffleImgVw;
    @FXML Label crtAbmLbl, upldSongLbl, sortLbl;

    @FXML TextField mainSearchFld, otherSearchFld;
    @FXML ComboBox<String> dbPaneSortBy;

    Displayer displayer;
    PlaylistBuilder genrePlaylistBuilder, albumPlaylistBuilder, artistPlaylistBuilder, yearPlaylistBuilder;
    Queue<String> songsQueue;
    Stack<MediaPlayer> prevS;
    ArrayList<MediaPlayer> prevList, players;
    int songIndex, tempJ, checkShuffle;
    boolean isArtist = false;
    public static ArrayList<String> notifications = new ArrayList<>();
    Database db;
    AccountService as;
    SongService ss;
    PlaylistService pls;
    PlaylistSongService ps;
    SongLoader sLoader;
    PlayMP3 play;
    static ArrayList<String> songs, songsArtist, songsTitle;
//    static ArrayList<String> songBackup;
    public static int j = 0;
    public static Queue<String> songsCopy;
    public static Queue<String> songsBackup;
    public static Stack<String> songsDump;
    ObservableList<String> sortList = FXCollections.observableArrayList("Date Uploaded", "Year", "Alphabetical", "Artist",
            "Album", "Genre");
    ArrayList<Integer> indexes;


    @Override
    public void initialize (URL location, ResourceBundle resources) {
        String aaronPath = new File("src/10,000 Reasons (Bless the Lord) - Matt Redman.mp3").getAbsolutePath();
//        String jerickPath = new File("C:\\Users\\11717777\\Downloads\\DesignChallenge2\\src\\10,000 Reasons (Bless the Lord) - Matt Redman.mp3").getAbsolutePath();
//        String song2 = new File("/Users/seanyuhico/Documents/SCHOOL/DesignChallenge2/src/ONE IN A MILLION.mp3").getAbsolutePath();
//        String song3 = new File("/Users/seanyuhico/Documents/SCHOOL/DesignChallenge2/src/TT.mp3").getAbsolutePath();

        Database db = new Database();
        SongService ss = new SongService(db);
        as = new AccountService(db);
        pls = new PlaylistService(db);
        ps = new PlaylistSongService(db);
        songs = new ArrayList<>();
        songsArtist = new ArrayList<>();
        songsTitle = new ArrayList<>();
        songsCopy = new LinkedList<>();
        songsBackup = new LinkedList<>();
        songsDump = new Stack<>();
        displayer = new Displayer();
        QueueWindowController.recentlyPlayed = new ArrayList<>();
        genrePlaylistBuilder = new GenrePlaylistBuilder();
        albumPlaylistBuilder = new AlbumPlaylistBuilder();
        artistPlaylistBuilder = new ArtistPlaylistBuilder();
        yearPlaylistBuilder = new YearPlaylistBuilder();

//        boolean checker = false;
        for (Song s : ss.getAll()) {
            if (s.getUsername().equals(LoginArtistController.getLoggedUser())) {
//                checker = true;
            }
        }

//        ArrayList<String> files = new ArrayList<>();
//        if (checker) {
//            files = initList();
//            files.add(aaronPath);
//        files.add(song2);
//        files.add(song3);
//            prevList = new ArrayList<>();
//            prevS = new Stack<>();
////        players = new LinkedList<>();
//            players = new ArrayList<>();
//            for (int i = 0; i < files.size(); i++) {
//                prevList.add(new MediaPlayer(new Media(new File(files.get(i)).toURI().toString())));
//                players.add(new MediaPlayer(new Media(new File(files.get(i)).toURI().toString())));
//            }
//            songIndex = 0;

        me = new Media(new File(aaronPath).toURI().toString());
        mp = new MediaPlayer(me);

//            me = new Media(new File(files.get(0)).toURI().toString());
//            mp = players.get(0);
        mv = new MediaView();
        mv.setMediaPlayer(mp);
        volumeSlider.setValue(mp.getVolume() * 100);
//            DoubleProperty width = mv.fitWidthProperty();
//            DoubleProperty height = mv.fitHeightProperty();
//            width.bind(Bindings.selectDouble(mv.sceneProperty(), "width"));
//            height.bind(Bindings.selectDouble(mv.sceneProperty(), "height"));
//        }

//        DoubleProperty width = mv.fitWidthProperty();
//        DoubleProperty height = mv.fitHeightProperty();
//        width.bind(Bindings.selectDouble(mv.sceneProperty(), "width"));
//        height.bind(Bindings.selectDouble(mv.sceneProperty(), "height"));

//        setMPLabels();
        volumeSlider.setValue(mp.getVolume() * 100);
        pauseImgVw.setVisible(false);
        playImgVw.setVisible(true);

        if(LoginArtistController.getLoggedAccount().isArtist()) {
            dashboardPane.setVisible(true);
            playlistPane.setVisible(false);
            showMySongs();
        }
        else{
            dashboardPane.setVisible(false);
            playlistPane.setVisible(true);
            showMyPlaylists();
            mySongsPlaylist.setVisible(false);
            mySongsPlaylist.setDisable(true);
        }

        dbPaneSortBy.setItems(sortList);
        dbPaneSortBy.getSelectionModel().selectFirst();
        dbPaneSortBy.setOnAction(e -> dashboardSort());

        uncheckedNotifImgVw.setVisible(false);

        // someone make this work pls T_T
//        if (!LoginArtistController.getLoggedAccount().isArtist()) {
        for(int i = 0; i < as.getAll().size(); i++){
            if(as.getAll().get(i).getUsername().equals(LoginArtistController.getLoggedUser()) && as.getAll().get(i).isArtist()){
                isArtist = true;
            }
        }


        if(!isArtist){
            crtAbmBtn.setDisable(true);
            crtAbmBtn.setVisible(false);
            crtAbmLbl.setVisible(false);
            upldSongBtn.setDisable(true);
            upldSongBtn.setVisible(false);
            upldSongLbl.setVisible(false);
        }
    }


    public void logout(){
        Boolean ans = ConfirmBox.display("Logout", "Are you sure you want to logout?");
        if(ans) {
            if(LoginArtistController.getLoggedUser().equals("Guest")) {
                // Delete all files of guest.
                AccountService.guestLogout();
                PlaylistService.guestLogout();
                PlaylistSongService.guestLogout();
                TimesPlayedService.guestLogout();
                FollowerService.guestLogout();
                AccPlayService.guestLogout();
            }
//            MusicPlayer.close(getMainStage());
//            mp.stop();

            try{
                Parent root = FXMLLoader.load(getClass().getResource("/View/LoginArtist.fxml"));
                Main.getMainStage().setScene(new Scene(root, 789, 417));
                Main.getMainStage().centerOnScreen();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        mp.stop();
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

    public void initPlay(String filename){
        db = new Database();
        ss = new SongService(db);
        sLoader = new SongLoader(db);
        play = new PlayMP3();
//        songs = new ArrayList<>();
//        songsCopy = new LinkedList<>();
        String title, artist;

//        for (int i = 0; i < ss.getAll().size(); i++) {
//            if (sLoader.loadSong(ss.getAll().get(i).getTitle()).equals(filename)) {
//                title = ss.getAll().get(i).getTitle();
//                artist = ss.getAll().get(i).getArtist();
//                songs.add(sLoader.loadSong(title));
//                songsArtist.add(artist);
//                songsTitle.add(title);
//                songsCopy.add(title);
//            }
//        }
        System.out.println(songs.size());
        play.setMedia(filename);
        System.out.println("dito ka pala ahhhhhhhhh");
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
                songsCopy.remove();
                next();
            }
        });

        QueueWindowController.recentlyPlayed.add(nameLbl.getText());
    }

    public void playAll() {
        db = new Database();
        ss = new SongService(db);
        sLoader = new SongLoader(db);
        play = new PlayMP3();
//        songs = new ArrayList<>();
//        songsCopy = new LinkedList<>();
        j = 0;

        for (int i = 0; i < ss.getAll(dashboardPlaylistLbl.getText()).size(); i++) {
            songs.add(sLoader.loadSong(ss.getAll(dashboardPlaylistLbl.getText()).get(i).getTitle()));
            songsTitle.add(ss.getAll(dashboardPlaylistLbl.getText()).get(i).getTitle());
            songsArtist.add(ss.getAll(dashboardPlaylistLbl.getText()).get(i).getArtist());
            songsCopy.add(ss.getAll(dashboardPlaylistLbl.getText()).get(i).getTitle());
        }
        play.setMedia(songs.get(j));
        /*MediaPlayer*/ mp = play.getMediaPlayer();
//        setMp(mp);
        setMPLabels(ss.getAll(dashboardPlaylistLbl.getText()).get(j).getArtist(), ss.getAll(dashboardPlaylistLbl.getText()).get(j).getTitle());
        mp.play();
        mp.setRate(1);
        playImgVw.setVisible(false);
        pauseImgVw.setVisible(true);

        mp.setOnEndOfMedia(new Runnable() {
            @Override
            public void run() {
                songsCopy.remove();
                next();
            }
        });

        QueueWindowController.recentlyPlayed.add(nameLbl.getText());
    }
    public void playAll(String playlist) {
        db = new Database();
        ss = new SongService(db);
        sLoader = new SongLoader(db);
        play = new PlayMP3();
//        songs = new ArrayList<>();
//        songsCopy = new LinkedList<>();
        j = 0;

        for (int i = 0; i < ss.getAll(playlist).size(); i++) {
            songs.add(sLoader.loadSong(ss.getAll(playlist).get(i).getTitle()));
            songsTitle.add(ss.getAll(playlist).get(i).getTitle());
            songsArtist.add(ss.getAll(playlist).get(i).getArtist());
            songsCopy.add(ss.getAll(playlist).get(i).getTitle());
        }
        play.setMedia(songs.get(j));
        /*MediaPlayer*/ mp = play.getMediaPlayer();
//        setMp(mp);
        setMPLabels(ss.getAll(playlist).get(j).getArtist(), ss.getAll(playlist).get(j).getTitle());
        mp.play();
        mp.setRate(1);
        playImgVw.setVisible(false);
        pauseImgVw.setVisible(true);

        mp.setOnEndOfMedia(new Runnable() {
            @Override
            public void run() {
                songsCopy.remove();
                next();
            }
        });

        QueueWindowController.recentlyPlayed.add(nameLbl.getText());
    }


    public void pause ()
    {
        mp.pause();
        playImgVw.setVisible(true);
        pauseImgVw.setVisible(false);
    }

    /**
     We need a list of songs talaga that is useable for both next and previous.
     queueing perse pero di siya 100% done yet.
     clicking the next button works but clicking previous doesn't yet
     */

    public void queue(String qFile, String artistLbl){
        db = new Database();
        ss = new SongService(db);
        sLoader = new SongLoader(db);
        play = new PlayMP3();
//        songsQueue = new LinkedList<>();
        songs.add(sLoader.loadSong(qFile));
        songsTitle.add(qFile);
        songsArtist.add(artistLbl);
        songsCopy.add(qFile);
        /*for(int k = 0; k < ss.getAll().size(); k++) {
            if(ss.getAll().get(k).getTitle().equals(qFile))
                play.setMedia(songs.get(j));
            MediaPlayer mp = play.getMediaPlayer();
//            setMp(mp);
            setMPLabels(ss.getAll().get(j).getArtist(), ss.getAll().get(j).getTitle());
            play();
        }*/
    }

    public void queuePlaylist(String playlist){
        db = new Database();
        ps = new PlaylistSongService(db);
        pls = new PlaylistService(db);
        ss = new SongService(db);
        sLoader = new SongLoader(db);
        play = new PlayMP3();
//        songs = new ArrayList<>();
//        songsCopy = new LinkedList<>();
        ArrayList<Integer> iDs = new ArrayList<>();
        int id = 0;
        System.out.println("sleep");
        for(int j = 0; j < pls.getAll().size(); j++){
            if(pls.getAll().get(j).getName().equals(playlist)){
                id = pls.getAll().get(j).getPlaylistID();
                System.out.println("PLEASE");
            }
        }

        for (int i = 0; i < ps.getAll().size(); i++) {
            if(ps.getAll().get(i).getPlaylistID() == id){
                iDs.add(i);
            }
        }

        for (int i = 0; i < ss.getAll(playlist).size(); i++) {
            songs.add(sLoader.loadSong(ss.getAll(playlist).get(i).getTitle()));
            songsTitle.add(ss.getAll(playlist).get(i).getTitle());
            songsArtist.add(ss.getAll(playlist).get(i).getArtist());
            songsCopy.add(ss.getAll(dashboardPlaylistLbl.getText()).get(i).getTitle());
            System.out.println("UGH");
        }
    }

    public void next ()
    {
        String artist = "", title = "";
//        songsCopy = new LinkedList<>();
        if(j != songs.size()) {
            j++;
        }

        if(j == songs.size()){
            System.out.println("Max songs");
            if(repeatPlaylistImgVw.isVisible()) {
                repeatPlaylist();
            }
            else{
                pauseImgVw.setVisible(false);
                playImgVw.setVisible(true);
            }
        }
        else if(songsQueue != null && songsQueue.peek() != null){ // ewan ko pero i'm scared to take this out
            play.stopSong();
            play.setMedia(songsQueue.remove());
            /*MediaPlayer*/ mp = play.getMediaPlayer();
//                setMp(mp);
            setMPLabels(ss.getAll(dashboardPlaylistLbl.getText()).get(j).getArtist(), ss.getAll(dashboardPlaylistLbl.getText()).get(j).getTitle());
            play();
        }
        else if (indexes != null && j < ss.getAll(dashboardPlaylistLbl.getText()).size() && checkShuffle==0){ //next after calling unshuffle
            play.stopSong();
            play.setMedia(songs.get(j));
//            if(songsCopy.peek()!=null
            if(!songsCopy.isEmpty())
                songsDump.push(songsCopy.remove());
            /*MediaPlayer*/ mp = play.getMediaPlayer();
//                setMp(mp);
//            setMPLabels(ss.getAll(dashboardPlaylistLbl.getText()).get(j).getArtist(), ss.getAll(dashboardPlaylistLbl.getText()).get(j).getTitle());
            setMPLabels(songsArtist.get(j), songsTitle.get(j));
//            QueueWindowController.recentlyPlayed.add(nameLbl.getText());
            play();
        }
        else if (indexes != null && j < songs.size() && checkShuffle==1){
            play.stopSong();
            play.setMedia(songs.get(j));
//            if(songsCopy.peek()!=null)
            if(!songsCopy.isEmpty())
                songsDump.push(songsCopy.remove());
            /*MediaPlayer*/ mp = play.getMediaPlayer();
//                setMp(mp);
            setMPLabels(ss.getAll(dashboardPlaylistLbl.getText()).get(indexes.get(j)).getArtist(), ss.getAll(dashboardPlaylistLbl.getText()).get(indexes.get(j)).getTitle());
//            setMPLabels(songsArtist.get(j), songsTitle.get(j));
//            QueueWindowController.recentlyPlayed.add(nameLbl.getText());
            play();

        }

        else if (j < ss.getAll(dashboardPlaylistLbl.getText()).size()) {
            play.stopSong();
            play.setMedia(songs.get(j));
//            if(songsCopy.peek()!=null)
            if(!songsCopy.isEmpty())
                songsDump.push(songsCopy.remove());
            /*MediaPlayer*/ mp = play.getMediaPlayer();
//                setMp(mp);
//            for(int i=0; i<ss.getAll().size(); i++){
//                String testTitle = ss.getAll().get(i).getTitle().replaceAll("\\s","");
//                String testArtist = ss.getAll().get(i).getArtist();
//                if(songs.get(j).contains(testTitle)){
//                    artist = testArtist;
//                    title = testTitle;
//                }
//            }
            setMPLabels(songsArtist.get(j), songsTitle.get(j));
//            QueueWindowController.recentlyPlayed.add(nameLbl.getText());
            System.out.println(j + " : " + ss.getAll(dashboardPlaylistLbl.getText()).get(j).getTitle());
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
        if(j != 0) {
            j--;
        }

        /*if (indexes != null && j < songs.size()){
            play.stopSong();
            play.setMedia(songs.get(j));
            *//*MediaPlayer*//* mp = play.getMediaPlayer();

            if(j == 0) {
                setMPLabels(songsArtist.get(j), songsTitle.get(j));
            }
            else{

                setMPLabels(songsArtist.get(j), songsTitle.get(j));
            }
            play();
        }
        else*/ if (indexes != null && j < ss.getAll(dashboardPlaylistLbl.getText()).size() && checkShuffle==0){ //next after calling unshuffle
            if (!songsCopy.isEmpty()) {
    //                songsCopy.remove();
                Queue<String> queueTemp = new LinkedList<>();
                if (songsDump.peek() != null)
                    queueTemp.add(songsDump.pop());
                queueTemp.addAll(songsCopy);
                songsCopy = queueTemp;
            }
            play.stopSong();
            play.setMedia(songs.get(j));
//            if(songsCopy.peek()!=null
            if(!songsCopy.isEmpty())
                songsDump.push(songsCopy.remove());
            /*MediaPlayer*/ mp = play.getMediaPlayer();
//                setMp(mp);
//            setMPLabels(ss.getAll(dashboardPlaylistLbl.getText()).get(j).getArtist(), ss.getAll(dashboardPlaylistLbl.getText()).get(j).getTitle());
            setMPLabels(songsArtist.get(j), songsTitle.get(j));
//            QueueWindowController.recentlyPlayed.add(nameLbl.getText());
            play();
        }
        else if (indexes != null && j < songs.size() && checkShuffle==1){
            if (!songsCopy.isEmpty()) {
    //                songsCopy.remove();
                Queue<String> queueTemp = new LinkedList<>();
                if (songsDump.peek() != null)
                    queueTemp.add(songsDump.pop());
                queueTemp.addAll(songsCopy);
                songsCopy = queueTemp;
            }
            play.stopSong();
            play.setMedia(songs.get(j));
//            if(songsCopy.peek()!=null)
            if(!songsCopy.isEmpty())
                songsDump.push(songsCopy.remove());
            /*MediaPlayer*/ mp = play.getMediaPlayer();
//                setMp(mp);
            setMPLabels(songsArtist.get(indexes.get(j)), songsTitle.get(indexes.get(j)));
//            setMPLabels(songsArtist.get(j), songsTitle.get(j));
//            QueueWindowController.recentlyPlayed.add(nameLbl.getText());
            play();

        }
        else if (j < ss.getAll(dashboardPlaylistLbl.getText()).size()  && j >= 0) {

            if (!songsCopy.isEmpty()) {
//                songsCopy.remove();
                Queue<String> queueTemp = new LinkedList<>();
                if (songsDump.peek() != null)
                    queueTemp.add(songsDump.pop());
                queueTemp.addAll(songsCopy);
                songsCopy = queueTemp;
            }

            play.stopSong();
            play.setMedia(songs.get(j));
            /*MediaPlayer*/ mp = play.getMediaPlayer();

            setMPLabels(songsArtist.get(j), songsTitle.get(j));
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

        uncheckedNotifImgVw.setVisible(true);
        notifications.add(LoginArtistController.getLoggedUser() + " added a new playlist.");
    }

    public void createAlbum()
    {
        CreateAlbumWindow.addNewAlbum();
        update();

        uncheckedNotifImgVw.setVisible(true);
        notifications.add(LoginArtistController.getLoggedUser() + " release a new album.");
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
        System.out.println("Repeat Song");
        repeatPlaylistImgVw.setVisible(false);
        repeatOnceVw.setVisible(true);
    }

    public void repeatPlaylist() {
        if(j==songs.size()) {
            mp.setOnEndOfMedia(new Runnable() {
                @Override
                public void run() {
                    playAll();
                }
            });
        }
        if(j==songs.size() /*|| mp.getCurrentTime()==mp.getTotalDuration()*/) {
            System.out.println("Repeat All");
            playAll();
        }
        repeatImgVw.setVisible(false);
        repeatPlaylistImgVw.setVisible(true);
    }

    public void stopRepeat(){
        mp.setOnEndOfMedia(new Runnable() {
            @Override
            public void run() {
                System.out.println("Repeat Disabled");
            }
        });
        System.out.println("Repeat Disabled");
        repeatOnceVw.setVisible(false);
        repeatImgVw.setVisible(true);
    }
    public void uploadSong()
    {
        UploadSongWindow.display();
        dashboardVBox.getChildren().clear();
        update();
        QueueWindowController.recentlyAdded.add(UploadSongWindow.songTitle);
        System.out.println("ditoooo");
//        int checker = 0;
//        PlaylistService ps = new PlaylistService(new Database());
//        for(Playlist p : ps.getAll())
//            if(p.isAlbum())
//                checker = 1;
//        if(checker == 1) {
//            UploadSongWindow.display();
//            dashboardVBox.getChildren().clear();
//            update();
//        }
//        else
//            AlertBox.display("Error", "Gawa ka muna ng album pls lang.");
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
        DisplayNonDefault.displayAllPlaylists(playlistVBox, dashboardVBox, dashboardPane, playlistPane, mp, dashboardPlaylistLbl, this);
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
        playPlaylistBtn.setVisible(true);
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
        DisplayNonDefault.displayAllAlbums(playlistVBox, dashboardVBox, dashboardPane, playlistPane, mp, dashboardPlaylistLbl, this);
//        displayer.setPlaylistBuilder(albumPlaylistBuilder);
//        displayer.constructAlbumPlaylist(dashboardPlaylistLbl, playlistVBox, dashboardVBox, dashboardPane, playlistPane, this);
        dashboardPane.setVisible(false);
        playlistPane.setVisible(true);
    }

    public void showByYears() {
        playPlaylistBtn.setVisible(true);
        playlistNameLbl.setText("Years");
        playlistVBox.getChildren().clear();
        displayer.setPlaylistBuilder(yearPlaylistBuilder);
        displayer.constructYearPlaylist(dashboardPlaylistLbl, playlistVBox, dashboardVBox, dashboardPane, playlistPane, this);
        dashboardPane.setVisible(false);
        playlistPane.setVisible(true);
    }

    public void showByGenres() {
        playPlaylistBtn.setVisible(true);
        playlistNameLbl.setText("Genres");
        playlistVBox.getChildren().clear();
        displayer.setPlaylistBuilder(genrePlaylistBuilder);
        displayer.constructGenrePlaylist(dashboardPlaylistLbl, playlistVBox, dashboardVBox, dashboardPane, playlistPane, this);
        dashboardPane.setVisible(false);
        playlistPane.setVisible(true);
    }

    public void showByMostPlayed() {
        playPlaylistBtn.setVisible(true);
        dashboardPlaylistLbl.setText("My Most Played Songs");
        dashboardVBox.getChildren().clear();
        DisplayNonDefault.displayByMostPlayed(dashboardVBox, this);
        dashboardPane.setVisible(true);
        playlistPane.setVisible(false);
    }

    public void showProfile() throws IOException{
        //ProfileWindow.display();
        Stage stage = new Stage();
        stage.setTitle("My profile");

        Parent root = FXMLLoader.load(getClass().getResource("/View/Profile.fxml"));
        stage.setScene(new Scene(root, 600, 400));
        stage.showAndWait();

    }

    public ArrayList<String> initList(){
        ArrayList<String> results = new ArrayList<>();
        File[] files = new File("\\Users\\aaron\\Desktop\\UltimateDesignChallenge\\src\\Music\\").listFiles();
        for(File file : files){
            if(file.isFile()){
                results.add(file.getName());
            }
        }
        return results;
    }

    public void shuffle() {
        tempJ=j;
        checkShuffle=1;
//        j=0;
        int low=0;
        for (int k=0; k<songs.size(); k++){
            if(songs.get(k).contains(songsCopy.element().replaceAll("\\s", "")))
                low=k;
        }
        System.out.println(songs.size());
        indexes = new ArrayList<>();
        indexes = getRandom(low, songsCopy.size()-1);
        ArrayList<String> shuffled = new ArrayList<>();
//        Queue<String> songsBackup = new LinkedList<>();
        songsBackup.clear();
        songsBackup.addAll(songs);
//        for(int k=0;k<low;k++) {
//            songsBackup.remove();
//        }
//        shuffled.add(songs.get(low));
        songsCopy.clear();
//        songsCopy.add(songsTitle.get(low));
        for(int i=0; i<indexes.size(); i++){
            shuffled.add(songs.get(indexes.get(i))); // changes the queue based on the list of random numbers generated
            songsCopy.add(songsTitle.get(indexes.get(i)));
        }
        for(int i=0; i<j; i++){
            songsCopy.remove();
        }
        songs.addAll(low, shuffled);
        LinkedHashSet<String> hashSet = new LinkedHashSet<>(songs);
        ArrayList<String> setCopy = new ArrayList<>(hashSet);
        songs = setCopy;
//        songsBackup.peek();
        System.out.println("Shuffled!");

        if (unshuffleImgVw.isVisible()) {
            unshuffleImgVw.setVisible(false);
            imgvwShuffle.setVisible(true);
        }
        else {
            unshuffleImgVw.setVisible(true);
            imgvwShuffle.setVisible(false);
        }

    }

    public void refresh(){
        ClientController.getInstance().getMusic();
        ClientController.getInstance().syncAll();
    }

    public void unshuffle(){
        checkShuffle=0;
//        j=tempJ;
        String currSong = songs.get(j);
        System.out.println(songsBackup.size());
        ArrayList<String> unshuffled = new ArrayList<>();
        ArrayList<String> temp = new ArrayList<>();
//        unshuffled.add(songsBackup.element());
        songsCopy.clear();
//        songsCopy.add(songsTitle.get(j));
        int k = indexes.size() + 1;
        Collections.sort(indexes);
        for(int i=0; i<songs.size(); i++){ // possibly modify j from here
            unshuffled.add(songsBackup.element()); // changes the queue based on the list of random numbers generated
            if(unshuffled.get(i).contains(currSong.replaceAll("\\s",""))) {
                j = i;
                System.out.println(j);
            }
            songsCopy.add(songsBackup.remove());
        }
        /*for(String filepath : songsCopy){
            for(String songT : songsTitle){
                if(filepath.contains(songT)){
                    filepath=songT;
                    songsCopy.add(filepath);
                }
            }
        }*/
        for(int i=0; i<k-1; i++){
//            for(int g=0; g<k-1; g++){
                if(songsCopy.element().contains(songsTitle.get(i).replaceAll("\\s",""))){
                    temp.add(songsTitle.get(indexes.get(i)));
                    songsCopy.remove();
                }
//            }
        }
        songsCopy.clear();
        songsCopy.addAll(temp);
        LinkedHashSet<String> hashSet = new LinkedHashSet<>(songsCopy);
        songsCopy.clear();
        ArrayList<String> setCopy = new ArrayList<>(hashSet);
        songsCopy.addAll(setCopy);

        for(int i=0; i<j; i++){
            songsCopy.remove();
        }
//        songs.addAll(songsBackup);
        songs = unshuffled;
//        songsCopy = songsBackup;
        songsBackup.clear();
        System.out.println("Unshuffled!");
        if (imgvwShuffle.isVisible()) {
            unshuffleImgVw.setVisible(true);
            imgvwShuffle.setVisible(false);
        }
        else {
            unshuffleImgVw.setVisible(false);
            imgvwShuffle.setVisible(true);
        }
    }

    public static final Random gen = new Random();
    public static ArrayList<Integer> getRandom(int low, int maxRange) {
        ArrayList<Integer> result = new ArrayList<>();
        ArrayList<Integer> used = new ArrayList<>();
        int extra = low + 1;
        for(int i=0;i<=low;i++) {
            used.add(i);
            result.add(i);
        }
//        result.add(low);
        for (int i = 0; i < maxRange; i++) {

            int newRandom;
            do {
                newRandom = gen.nextInt(maxRange+extra);
            } while (used.contains(newRandom) || newRandom <= low);
            result.add(newRandom);
            used.add(newRandom);
        }
        return result;
    }

    public void update() {
        if (dashboardPane.isVisible()) {
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
            else if (dashboardPlaylistLbl.getText().equals("Years"))
                showByYears();
        }
        else if (playlistPane.isVisible() ) {
            if (playlistNameLbl.getText().equals("My Playlists"))
                showMyPlaylists();
        }
    }

    public void mainSearch(){
        if(!mainSearchFld.getText().equals("")) {
            dashboardVBox.getChildren().clear();
            DisplaySearch.initialize(dashboardVBox);
            DisplaySearch.display(dashboardPlaylistLbl, dashboardVBox, dashboardPane, playlistPane, this);
            dashboardPlaylistLbl.setText("Search");
            dashboardPane.setVisible(true);
            playlistPane.setVisible(false);
            playPlaylistBtn.setVisible(false);
        }
    }

    public void otherSearch(){
        if(!otherSearchFld.getText().equals("")) {
            dashboardVBox.getChildren().clear();
            DisplaySearch.initialize(dashboardVBox);
            DisplaySearch.display(dashboardPlaylistLbl, dashboardVBox, dashboardPane, playlistPane, this);
            dashboardPlaylistLbl.setText("Search");
            playPlaylistBtn.setVisible(false);
            dashboardPane.setVisible(true);
            playlistPane.setVisible(false);
        }
    }

    public void dashboardSort(){
        ArrayList<Song> sortedSongs = new ArrayList<>();
        for(Node currentNode : dashboardVBox.getChildren())
            if(currentNode instanceof SongHBox)
                sortedSongs.add(((SongHBox) currentNode).getSong());

        dashboardVBox.getChildren().clear();

        if(dbPaneSortBy.getValue().equals("Alphabetical")) {
            sortedSongs.sort(Comparator.comparing(Song::getTitle));
            for (Song s : sortedSongs)
                dashboardVBox.getChildren().add(new SongHBox(s, dashboardVBox, this));
        }
        else if(dbPaneSortBy.getValue().equals("Year")){
            sortedSongs.sort(Comparator.comparing(Song::getYear));
            for(Song s : sortedSongs)
                dashboardVBox.getChildren().add(new SongHBox(s, dashboardVBox, this));
        }
        else if(dbPaneSortBy.getValue().equals("Date Uploaded")){
            sortedSongs.sort(Comparator.comparing(Song::getDateUploaded));
            for(Song s : sortedSongs)
                dashboardVBox.getChildren().add(new SongHBox(s, dashboardVBox, this));
        }
        else if(dbPaneSortBy.getValue().equals("Artist")){
            sortedSongs.sort(Comparator.comparing(Song::getArtist));
            for(Song s : sortedSongs)
                dashboardVBox.getChildren().add(new SongHBox(s, dashboardVBox, this));
        }
        else if(dbPaneSortBy.getValue().equals("Album")){
            sortedSongs.sort(Comparator.comparing(Song::getAlbumName));
            for(Song s : sortedSongs)
                dashboardVBox.getChildren().add(new SongHBox(s, dashboardVBox, this));
        }
        else if(dbPaneSortBy.getValue().equals("Genre")){
            sortedSongs.sort(Comparator.comparing(Song::getGenre));
            for(Song s : sortedSongs)
                dashboardVBox.getChildren().add(new SongHBox(s, dashboardVBox, this));
        }
    }

    public void showNotifications() throws IOException{
        uncheckedNotifImgVw.setVisible(false);

        Stage stage = new Stage();
        stage.setTitle("Notifications");
        stage.setResizable(false);
        stage.centerOnScreen();
        Parent root = FXMLLoader.load(getClass().getResource("/View/Notifications.fxml"));
        stage.setScene(new Scene(root, 600, 400));
        stage.showAndWait();
    }

    public void showQueue() throws IOException{
        Stage stage = new Stage();
        stage.setTitle("Activities");
        stage.setResizable(false);
        stage.centerOnScreen();
        Parent root = FXMLLoader.load(getClass().getResource("/View/QueueWindow.fxml"));
        stage.setScene(new Scene(root, 600, 400));
        stage.showAndWait();
    }

    public ComboBox<String> getDbPaneSortBy() {
        return dbPaneSortBy;
    }

    public Label getSortLbl() {
        return sortLbl;
    }

    public Label getDashboardPlaylistLbl() {
        return dashboardPlaylistLbl;
    }

    public MediaPlayer getMp() {
        return mp;
    }

    public void resetJ(){
        j = 0;
    }

    public void setMp(MediaPlayer mp) {
        this.mp = mp;
    }

    public TextField getMainSearchFld(){
        return this.mainSearchFld;
    }

    public TextField getOtherSearchFld(){
        return this.otherSearchFld;
    }
}
