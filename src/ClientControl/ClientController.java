package ClientControl;

import Model.*;

import java.util.List;

public class ClientController{

    private static ClientController instance;

    private Client client;

    private ClientController(){
        client = new Client("Guest1", "192.168.11.125", "/Users/seanyuhico/Documents/SCHOOL/UltimateDesignChallenge/src/Music/");
    }

    public static synchronized ClientController getInstance(){
        if(instance == null){
            instance = new ClientController();
        }
        return  instance;
    }


    private void send(String text){
        client.getConnection().sendToServer(text);
    }

    public void setUsername(String username){
        client.setUser(username);
    }

    public void setDirectory(String directory){
        client.setDirectory(directory);
    }

    public void getMusic(){
        send("getmusic");
    }

    public void syncAll(){
        send("syncAll");
    }

    public void uploadMusic(String filepath){
        send("uploadmusic;" + filepath);
    }

    public void accountAdd(Account account){
        String str = "account;add;" + account.getUsername() + ";" + account.getPassword() + ";" + account.isArtist();
        send(str);
    }

    public List<Account> accountGetAll(){
        send("account;getAll");
        return client.getConnection().getAccounts();
    }

    public void followerAdd(String s1, String s2){
        String str = "follower;add;" + s1 + ";" + s2;
        send(str);
    }

    public List<Follower> followerGetAll(){
        send("follower;getAll");
        return client.getConnection().getFollowers();
    }

    public void followerUnfollow(String s1, String s2){
        String str = "follower;unfollow;" + s1 + ";" + s2;
        send(str);
    }

    public void playlistAdd(Playlist playlist){
        String str = "playlist;add;" + playlist.getPlaylistID() + ";" + playlist.getName() + ";" + playlist.getSongCount() + ";" + playlist.getUsername() + ";" + playlist.isPublic() + ";" + playlist.isAlbum();
        send(str);
    }

    public List<Playlist> playlistGetAll(){
        send("playlist;getAll");
        return client.getConnection().getPlaylists();
    }

    public void playlistUpload(Playlist playlist){
        String str = "playlist;upload;" + playlist.getName();
        send(str);
    }

    public void playlistUpdate(Playlist playlist, int ID){
        String str = "playlist;update;" + playlist.getName() + ";" + ID;
        send(str);
    }

    public void playlistDecrement(Playlist playlist){
        String str = "playlist;decrement;" + playlist.getName();
        send(str);
    }

    public void playlistDelete(Playlist playlist){
        String str = "playlist;delete;" + playlist.getPlaylistID();
        send(str);
    }

    public void playlistMakePublic(int ID, String fav){
        String str = "playlist;makePublic;" + ID + ";" + fav;
        send(str);
    }

    public void playlistUploadAmbumCover(Playlist playlist, String filename){
        String str = "playlist;uploadAlbumCover;" + playlist.getPlaylistID() + ";" + filename;
        send(str);
    }

    public void playlistSongAdd(Playlist playlist, Song song){
        String str = "playlistsong;add;" + playlist.getPlaylistID() + ";" + song.getSongID();
        send(str);
    }

    public List<PlaylistSong> playlistSongGetAll(){
        send("playlistsong;getAll");
        return client.getConnection().getPlaylistSongs();
    }

    public void playlistSongRemoveSongfromPlaylist(Playlist playlist, Song song){
        String str = "playlistsong;removeSongFromPlaylist;" + playlist.getName() + ";" + playlist.getPlaylistID() + ";" + song.getSongID();
        send(str);
    }

    public void playListSongAddSongToPlaylist(Playlist playlist, Song song){
        String str = "playlistsong;addSongToPlaylist;" + playlist.getName() + ";" + playlist.getPlaylistID() + ";" + song.getSongID();
        send(str);
    }

    public void playlistSongDeleteSong(Song song){
        String str = "playlistsong;deleteSong;" + song.getSongID();
        send(str);
    }

    public void playlistSongDeletePlaylist(Playlist playlist){
        String str = "playlistsong;deletePlaylist;" + playlist.getPlaylistID();
        send(str);
    }

    public void songAdd(Song song){
        String str = "song;add;" + song.getSongID() + ";" + song.getTitle() + ";" + song.getArtist() + ";" + song.getAlbumName() + ";" + song.getGenre() + ";" + song.getSongName() + ";" + song.getYear() + ";" + song.getUsername() + ";" + song.getFilename() + ";" + song.isFave() + ";" + song.getDateUploaded();
        send(str);
    }

    public List<Song> songGetAll(){
        send("song;getAll");
        return client.getConnection().getSongs();
    }

    public List<Song> songGetAll2(String s){
        send("song;getAll2;" + s);
        return client.getConnection().getSongs();
    }

    public void songUpdate(Song song, int ID){
        String str = "song;update;" + ID + ";" + song.getTitle() + ";" + song.getArtist() + ";" + song.getAlbumName() + ";" + song.getGenre() + ";" + song.getYear();
        send(str);
    }

    public void songDelete(Song song){
        String str = "song;delete;" + song.getSongID();
        send(str);
    }

    public void songMakeFave(int ID, String fav){
        String str = "song;makeFave;" + ID + ";" + fav;
        send(str);
    }

    public void songChangeAlbum(Playlist playlist, int ID){
        String str = "song;changeAlbum;" + playlist.getName() + ";" + ID;
        send(str);
    }

    public void accplayAdd(Playlist playlist){
        String str = "accplay;add;" + playlist.getPlaylistID();
        send(str);
    }

    public List<AccPlay> accplayGetAll(){
        send("accplay;getAll");
        return client.getConnection().getAccountPlaylists();
    }

    public void accplayUnfollow(int ID){
        String str = "accplay;unfollow;" + ID;
        send(str);
    }

    public void timesplayedAdd(int ID){
        String str = "timesplayed;add;" + ID;
        send(str);
    }

    public List<TimesPlayed> timesplayedGetAll(){
        send("timesplayed;getAll");
        return client.getConnection().getTimesPlayed();
    }

    public void incNumTimesPlayed(int ID){
        String str = "timesplayed;incNumTimesPlayed;" + ID;
        send(str);
    }

    public String imageload(String string){
        send("imageloader;" + string);
        return client.getConnection().getTemploadImage();
    }

    public String songload(String string){
        send("songloader;" + string);
        return client.getConnection().getTemploadSong();
    }

    public void guestLogout(){
        //handles all 3 guest logouts
        send("playlist;guestLogout");
        send("playlistsong;guestLogout");
        send("song;guestLogout");
    }

    public Client getClient(){
        return client;
    }
}
