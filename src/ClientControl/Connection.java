package ClientControl;

import Model.*;
import Observer.NotificationObserver;

import javax.management.Notification;
import java.io.*;
import java.net.Socket;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class Connection extends Thread {

    private Client client;
    private Socket s;
    private DataInputStream din;
    private DataOutputStream dout;
    private boolean isRunning = true;
    private ArrayList<Account> accounts;
    private ArrayList<Follower> followers;
    private ArrayList<Playlist> playlists;
    private ArrayList<PlaylistSong> playlistsongs;
    private ArrayList<Song> songs;
    private ArrayList<AccPlay> accountplaylists;
    private ArrayList<TimesPlayed> timesPlayed;
    private String temploadSong;
    private String temploadImage;
    private Database db;
    private AccountService as;
    private FollowerService fs;
    private PlaylistService ps;
    private PlaylistSongService pss;
    private SongService ss;
    private AccPlayService aps;
    private TimesPlayedService tps;
    private ImageLoader im;
    private SongLoader sl;
    private NotificationObserver no;


    public Connection(Socket socket, Client client){
        super(client.getUser());
        this.client = client;
        s = socket;
        accounts = new ArrayList<>();
        followers = new ArrayList<>();
        playlists = new ArrayList<>();
        playlistsongs = new ArrayList<>();
        songs = new ArrayList<>();
        accountplaylists = new ArrayList<>();
        timesPlayed = new ArrayList<>();
        db = new Database();
        as = new AccountService(db);
        fs = new FollowerService(db);
        ps = new PlaylistService(db);
        pss = new PlaylistSongService(db);
        ss = new SongService(db);
        aps = new AccPlayService(db);
        tps = new TimesPlayedService(db);
        im = new ImageLoader(db);
        sl = new SongLoader(db);
        no = new NotificationObserver();
    }

    public void sendToServer(String text){
        String data = client.getUser() + ";" + text;
        try {
            din = new DataInputStream(s.getInputStream());
            dout = new DataOutputStream(s.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            dout.writeUTF(data);
            dout.flush();
        } catch (IOException e) {
            e.printStackTrace();
            close();
            isRunning = false;
        }
    }

    public void run(){
        try {
            din = new DataInputStream(s.getInputStream());
            dout = new DataOutputStream(s.getOutputStream());
            while(isRunning){
                try {
                    while(!(din.available() == 0)){
                        try {
                            Thread.sleep(1);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                            isRunning = false;
                        }
                    }
                    String received = din.readUTF();
                    String[] data = received.split(";", 3);
                    if(data[0].equals(client.getUser())){
                        switch(data[1]){
                            case "getmusic":
                                System.out.println("receiving music");
                                try {
                                    BufferedInputStream bis = new BufferedInputStream(s.getInputStream());
                                    DataInputStream dis = new DataInputStream(bis);

                                    int filesCount = dis.readInt();
                                    File[] files = new File[filesCount];

                                    for (int i = 0; i < filesCount; i++) {
                                        long fileLength = dis.readLong();
                                        String fileName = dis.readUTF();

                                        files[i] = new File(client.getDirectory() + fileName);
                                        //files[i] = new File("/Users/Santi/Desktop/Current/MergedPlayer/src/Music/" + fileName);

                                        FileOutputStream fos = new FileOutputStream(files[i]);
                                        BufferedOutputStream bos = new BufferedOutputStream(fos);

                                        for (int j = 0; j < fileLength; j++) bos.write(bis.read());

                                        bos.flush();
                                        bos.close();


                                        try {
                                            Thread.sleep(20);
                                        } catch (InterruptedException e) {
                                            e.printStackTrace();
                                        }
                                    }

                                    //dis.close();
                                }catch (IOException e){
                                    e.printStackTrace();
                                }
                                break;

                            case "confirmupload":
                                String hmm = client.getUser() + ";" + "confirmupload";
                                try {
                                    dout.writeUTF(hmm);
                                    dout.flush();


                                } catch (IOException e) {
                                    e.printStackTrace();
                                }






                                try {

                                    String filepath = data[2];
                                    File file = new File(filepath);


                                    OutputStream os = s.getOutputStream();

                                    BufferedOutputStream bos = new BufferedOutputStream(os);
                                    DataOutputStream dos = new DataOutputStream(bos);



                                    long length = file.length();
                                    dos.writeLong(length);

                                    String name = file.getName();
                                    dos.writeUTF(name);

                                    FileInputStream fis = new FileInputStream(file);
                                    BufferedInputStream bis = new BufferedInputStream(fis);

                                    int theByte = 0;
                                    while ((theByte = bis.read()) != -1) bos.write(theByte);


                                    bis.close();

                                    try {
                                        Thread.sleep(2);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }

                                    dos.flush();
                                    //dos.close();

                                }catch (IOException e){
                                    e.printStackTrace();
                                }
                                break;

                            case "accounts":
                                System.out.println(received);
                                String[] accountdata = data[2].split("\\|");
                                Account tempAccount = new Account();
                                tempAccount.setUsername(accountdata[0]);
                                tempAccount.setPassword(accountdata[1]);
                                tempAccount.setArtist(Boolean.parseBoolean(accountdata[2]));
                                accounts.add(tempAccount);
                                as.add(tempAccount);
                                /*for(int i = 0; i< accounts.size(); i++){
                                    System.out.println(accounts.get(i).getUsername());
                                }*/
                                break;

                            case "followers":
                                System.out.println(received);
                                String[] followerdata = data[2].split("\\|");
                                Follower tempFollower = new Follower();
                                tempFollower.setFollower(followerdata[0]);
                                tempFollower.setFollowing(followerdata[1]);
                                followers.add(tempFollower);
                                fs.add(tempFollower.getFollower(), tempFollower.getFollowing());
                                break;

                            case "playlists":
                                System.out.println(received);
                                String[] playlistdata = data[2].split("\\|");
                                Playlist tempPlaylist = new Playlist();
                                tempPlaylist.setPlaylistID(Integer.parseInt(playlistdata[0]));
                                tempPlaylist.setName(playlistdata[1]);
                                tempPlaylist.setSongCount(Integer.parseInt(playlistdata[2]));
                                tempPlaylist.setUsername(playlistdata[3]);
                                tempPlaylist.setPublic(Boolean.parseBoolean(playlistdata[4]));
                                tempPlaylist.setAlbum(Boolean.parseBoolean(playlistdata[5]));
                                playlists.add(tempPlaylist);
                                ps.add(tempPlaylist);
                                break;

                            case "playlistsongs":
                                System.out.println(received);
                                String[] playlistsongdata = data[2].split("\\|");
                                PlaylistSong tempPlaylistSong = new PlaylistSong();
                                tempPlaylistSong.setPlaylistID(Integer.parseInt(playlistsongdata[0]));
                                tempPlaylistSong.setSongID(Integer.parseInt(playlistsongdata[1]));
                                tempPlaylistSong.setUsername(playlistsongdata[2]);
                                playlistsongs.add(tempPlaylistSong);
                                Playlist tempPlaylistSongPlaylist = new Playlist();
                                Song tempPlaylistSongSong = new Song();
                                tempPlaylistSongPlaylist.setPlaylistID(Integer.parseInt(playlistsongdata[0]));
                                tempPlaylistSongSong.setSongID(Integer.parseInt(playlistsongdata[1]));
                                pss.add(tempPlaylistSongPlaylist, tempPlaylistSongSong, playlistsongdata[2]);
                                //add to db playlist, song, user
                                break;

                            case "songs":
                                System.out.println(received);
                                String[] songdata = data[2].split("\\|");
                                Song tempSong = new Song();
                                tempSong.setSongID(Integer.parseInt(songdata[0]));
                                tempSong.setTitle(songdata[1]);
                                tempSong.setArtist(songdata[2]);
                                tempSong.setAlbumName(songdata[3]);
                                tempSong.setGenre(songdata[4]);
                                tempSong.setSongName(songdata[5]);
                                tempSong.setYear(songdata[6]);
                                tempSong.setUsername(songdata[7]);
                                //tempSong.setFilename(songdata[8]);
                                tempSong.setFilename(client.getDirectory() + songdata[8]);

                                tempSong.setFave(Boolean.parseBoolean(songdata[9]));
                                tempSong.setDateUploaded(Timestamp.valueOf(songdata[10]));
                                songs.add(tempSong);
                                //songbuilder to add to db
                                ss.add(tempSong);
                                break;

                            case "accountplaylists":
                                System.out.println(received);
                                String[] accountplaylistdata = data[2].split("\\|");
                                AccPlay tempAccPlay = new AccPlay();
                                tempAccPlay.setPlaylistID(Integer.parseInt(accountplaylistdata[0]));
                                tempAccPlay.setUser(accountplaylistdata[1]);
                                accountplaylists.add(tempAccPlay);
                                Playlist tempAccountPlaylist = new Playlist();
                                tempAccountPlaylist.setPlaylistID(Integer.parseInt(accountplaylistdata[0]));
                                aps.add(tempAccountPlaylist);
                                //add to account ps playlist, user
                                break;

                            case "timesplayed":
                                System.out.println(received);
                                String[] timesplayeddata = data[2].split("\\|");
                                TimesPlayed tempTimesPlayed = new TimesPlayed();
                                tempTimesPlayed.setSongID(Integer.parseInt(timesplayeddata[0]));
                                tempTimesPlayed.setAccountName(data[1]);
                                tempTimesPlayed.setNumTimesPlayed(Integer.parseInt(data[2]));
                                timesPlayed.add(tempTimesPlayed);
                                tps.add(tempTimesPlayed.getSongID());
                                break;

                            case "resetaccounts":
                                accounts.clear();
                                break;
                            case "resetfollowers":
                                followers.clear();
                                break;
                            case "resetplaylists":
                                playlists.clear();
                                break;
                            case "resetplaylistsongs":
                                playlistsongs.clear();
                                break;
                            case "resetsongs":
                                songs.clear();
                                break;
                            case "resetaccountplaylists":
                                songs.clear();
                                break;
                            case "resettimesplayed":
                                timesPlayed.clear();
                                break;
                            case "loadsong":
                                temploadSong = data[2];
                                break;
                            case "loadimage":
                                temploadImage = data[2];
                                break;
                            default:
                                System.out.println(data[1]);
                                break;
                        }
                    }
                    if (!data[0].equals(client.getUser())){
                        //System.out.println(data[0]);
                        //System.out.println(data[3]);
                        //System.out.println("gotmsg");
                        switch(data[1]){
                            case "notifupdate":
                                //no.update(data[0]+" has uploaded a new song");
                                //System.out.println("message from " + data[0]);

                                Follower tFollower = new Follower();
                                List<Follower> follow = fs.getAll();
                                for (int i=0; i< follow.size(); i++) {
                                    if (client.getUser() == tFollower.getFollower() && data[0]==tFollower.getFollowing()) {
                                        no.update(data[0]+" has uploaded a new song");
                                    }
                                }

                                break;

                            case "notifplayupdate":
                                //no.update(data[0]+" has uploaded a new song");
                                //System.out.println("message from " + data[0]);


                                Follower tsFollower = new Follower();
                                List<Follower> fsollow = fs.getAll();
                                for (int i=0; i< fsollow.size(); i++) {
                                    if (client.getUser() == tsFollower.getFollower() && data[0]==tsFollower.getFollowing()) {
                                        no.update(data[0]+" has made a playlist public");
                                    }
                                }

                                break;

                        }
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                    close();
                    isRunning = false;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            close();
        }
    }

    public void close(){
        try {
            din.close();
            dout.close();
            //s.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<Account> getAccounts(){
        return accounts;
    }

    public List<Follower> getFollowers(){
        return followers;
    }

    public List<Playlist> getPlaylists(){
        return playlists;
    }

    public List<PlaylistSong> getPlaylistSongs(){
        return playlistsongs;
    }

    public List<Song> getSongs(){
        return songs;
    }

    public List<AccPlay> getAccountPlaylists(){
        return accountplaylists;
    }

    public List<TimesPlayed> getTimesPlayed(){return timesPlayed;}

    public String getTemploadSong(){
        return temploadSong;
    }

    public String getTemploadImage(){
        return temploadImage;
    }
}
