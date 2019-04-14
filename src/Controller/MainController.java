package Controller;

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

import static View.Main.getMainStage;

public class MainController extends Controller implements Initializable {
    @FXML private MediaView mv;
    private MediaPlayer mp, currPlayer, nextPlayer;
    private Media me;
    public static Pane dbPane, mpPane;
    @FXML Slider volumeSlider, progressSlider;
    @FXML Button playBtn, pauseBtn;
    @FXML BorderPane bPane;
    @FXML ImageView playImgVw, pauseImgVw, repeatImgVw, nextImgVw, prevImgVw, repeatOnceVw, notifImgVw, uncheckedNotifImgVw; //,repeatAllImgVw;
    @FXML Label artistLbl,nameLbl, songLbl, logoutLbl, dashboardPlaylistLbl;
    @FXML Label playlistNameLbl;
    @FXML ScrollPane sp;
    @FXML AnchorPane ap;
    @FXML VBox dashboardVBox, playlistVBox;
    @FXML Pane dashboardPane, playlistPane;
    @FXML Label mySongsPlaylist, myMostPlayed, artistsLbl, albumsLbl, genresLbl, yearLbl, myPlaylistsLbl;

    @FXML ImageView crtAbmBtn, upldSongBtn, imgvwShuffle;
    @FXML Label crtAbmLbl, upldSongLbl;

    @FXML TextField mainSearchFld, otherSearchFld;
    @FXML ComboBox<String> dbPaneSortBy;

    Displayer displayer;
    PlaylistBuilder genrePlaylistBuilder, albumPlaylistBuilder, artistPlaylistBuilder, yearPlaylistBuilder;
    Queue<String> songsQueue;
    Stack<MediaPlayer> prevS;
    ArrayList<MediaPlayer> prevList, players;
    int songIndex, j =0;
    boolean isArtist = false;
    public static ArrayList<String> notifications = new ArrayList<>();
    Database db;
    AccountService as;
    SongService ss;
    PlaylistService pls;
    PlaylistSongService ps;
    SongLoader sLoader;
    PlayMP3 play;
    ArrayList<String> songs;
    ObservableList<String> sortList = FXCollections.observableArrayList("Date Uploaded", "Year", "Alphabetical", "Artist",
            "Album", "Genre");


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
        songsQueue = new LinkedList<>();
        displayer = new Displayer();
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
        dashboardPane.setVisible(true);
        playlistPane.setVisible(false);

        dbPane = dashboardPane;
        mpPane = playlistPane;
        showMySongs();

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

    public void initPlay(String filename){
        db = new Database();
        ss = new SongService(db);
        sLoader = new SongLoader(db);
        play = new PlayMP3();
        songs = new ArrayList<>();

        for (int i = 0; i < ss.getAll().size(); i++) {
            if (sLoader.loadSong(ss.getAll().get(i).getTitle()).equals(filename)) {
                songs.add(sLoader.loadSong(ss.getAll().get(i).getTitle()));
            }
        }
        play.setMedia(filename);
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

        for (int i = 0; i < ss.getAll(dashboardPlaylistLbl.getText()).size(); i++) {
            songs.add(sLoader.loadSong(ss.getAll(dashboardPlaylistLbl.getText()).get(i).getTitle()));
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

    /**
     We need a list of songs talaga that is useable for both next and previous.
     queueing perse pero di siya 100% done yet.
     clicking the next button works but clicking previous doesn't yet
     */

    public void queue(String qFile){
        db = new Database();
        ss = new SongService(db);
        sLoader = new SongLoader(db);
        play = new PlayMP3();
//        songsQueue = new LinkedList<>();
        songs.add(sLoader.loadSong(qFile));

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
        songs = new ArrayList<>();
        int id = 0;
        System.out.println("sleep");
        for(int j = 0; j < pls.getAll().size(); j++){
            if(pls.getAll().get(j).getName().equals(playlist)){
                id = pls.getAll().get(j).getPlaylistID();
            }
        }
        System.out.println("PLEASE");
        for (int i = 0; i < ss.getAll().size(); i++) {
            if(ps.getAll().get(i).getPlaylistID() == id){
                songs.add(sLoader.loadSong(ss.getAll().get(i).getTitle()));
            }
        }
        System.out.println("UGH");
    }

    public void next ()
    {j++;
        if(songsQueue != null && songsQueue.peek() != null){
            play.stopSong();
            play.setMedia(songsQueue.remove());
            /*MediaPlayer*/ mp = play.getMediaPlayer();
//                setMp(mp);
            setMPLabels(ss.getAll(dashboardPlaylistLbl.getText()).get(j).getArtist(), ss.getAll(dashboardPlaylistLbl.getText()).get(j).getTitle());
            play();
        }

        else if (j < ss.getAll(dashboardPlaylistLbl.getText()).size()) {
            play.stopSong();
            play.setMedia(songs.get(j));
            /*MediaPlayer*/ mp = play.getMediaPlayer();
//                setMp(mp);
            setMPLabels(ss.getAll(dashboardPlaylistLbl.getText()).get(j).getArtist(), ss.getAll(dashboardPlaylistLbl.getText()).get(j).getTitle());
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
        if (j < ss.getAll(dashboardPlaylistLbl.getText()).size()  && j >= 0) {
            play.stopSong();
            play.setMedia(songs.get(j));
            /*MediaPlayer*/ mp = play.getMediaPlayer();
//            setMp(mp);
            setMPLabels(ss.getAll(dashboardPlaylistLbl.getText()).get(j).getArtist(), ss.getAll(dashboardPlaylistLbl.getText()).get(j).getTitle());
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
        int checker = 0;
        PlaylistService ps = new PlaylistService(new Database());
        for(Playlist p : ps.getAll())
            if(p.isAlbum())
                checker = 1;
        if(checker == 1) {
            UploadSongWindow.display();
            dashboardVBox.getChildren().clear();
            update();
        }
        else
            AlertBox.display("Error", "Gawa ka muna ng album pls lang.");
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
        DisplayNonDefault.displayAllPlaylists(playlistVBox, dashboardVBox, dashboardPane, playlistPane, mp, dashboardPlaylistLbl);
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
        DisplayNonDefault.displayAllAlbums(playlistVBox, dashboardVBox, dashboardPane, playlistPane, mp, dashboardPlaylistLbl);
//        displayer.setPlaylistBuilder(albumPlaylistBuilder);
//        displayer.constructAlbumPlaylist(dashboardPlaylistLbl, playlistVBox, dashboardVBox, dashboardPane, playlistPane, this);
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
        ArrayList<Integer> indexes = getRandom(0, songs.size()-1);;
        ArrayList<String> shuffled = new ArrayList<>();
        for(int i=0; i<songs.size()-1; i++){
            shuffled.add(songs.get(indexes.get(i))); // changes the queue based on the list of random numbers generated
        }
        songs = shuffled;
    }

    public static final Random gen = new Random();
    public static ArrayList<Integer> getRandom(int n, int maxRange) {
        ArrayList<Integer> result = new ArrayList<>();
        ArrayList<Integer> used = new ArrayList<>();

        for (int i = 0; i < maxRange; i++) {

            int newRandom;
            do {
                newRandom = gen.nextInt(maxRange+1);
            } while (used.contains(newRandom));
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
            dashboardPane.setVisible(true);
            playlistPane.setVisible(false);
            dashboardPlaylistLbl.setText("Search");
            dashboardVBox.getChildren().clear();
            DisplaySearch.initialize(dashboardVBox);
            DisplaySearch.display(dashboardPlaylistLbl, dashboardVBox, dashboardPane, playlistPane, this);
        }
    }

    public void otherSearch(){
        if(!otherSearchFld.getText().equals("")) {
            dashboardPane.setVisible(true);
            playlistPane.setVisible(false);
            dashboardPlaylistLbl.setText("Search");
            dashboardVBox.getChildren().clear();
            DisplaySearch.initialize(dashboardVBox);
            DisplaySearch.display(dashboardPlaylistLbl, dashboardVBox, dashboardPane, playlistPane, this);
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

    public Label getDashboardPlaylistLbl() {
        return dashboardPlaylistLbl;
    }

    public MediaPlayer getMp() {
        return mp;
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
