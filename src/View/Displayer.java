package View;

import Controller.MainController;
import Model.*;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

public class Displayer {
    // Database-related
    private DisplayAlbumNames displayAlbumNames;
    private DisplayArtists displayArtists;
    private DisplayGenres displayGenres;
    private DisplayYears displayYears;

    // Builder Pattern
    private PlaylistBuilder playlistBuilder;

    public Playlist getPlaylist(){
        return playlistBuilder.getPlaylist();
    }

    public void setPlaylistBuilder(PlaylistBuilder playlistBuilder) {
        this.playlistBuilder = playlistBuilder;
    }

    public Displayer(){
        displayAlbumNames = new DisplayAlbumNames();
        displayArtists = new DisplayArtists();
        displayGenres = new DisplayGenres();
        displayYears = new DisplayYears();
    }


    public void constructGenrePlaylist(Label playlistNameLbl, VBox playlistVBox, VBox dashboardVBox, Pane dashboardPane, Pane playlistPane,
                                       MainController controller){
        displayGenres.group(playlistNameLbl, playlistVBox, dashboardVBox, dashboardPane, playlistPane, controller);
    }

    public void constructArtistPlaylist(Label playlistNameLbl, VBox playlistVBox, VBox dashboardVBox, Pane dashboardPane, Pane playlistPane,
                                        MainController controller){
        displayArtists.group(playlistNameLbl, playlistVBox, dashboardVBox, dashboardPane, playlistPane, controller);
    }

    public void constructAlbumPlaylist(Label playlistNameLbl, VBox playlistVBox, VBox dashboardVBox, Pane dashboardPane, Pane playlistPane,
                                       MainController controller){
        displayAlbumNames.group(playlistNameLbl, playlistVBox, dashboardVBox, dashboardPane, playlistPane, controller);
    }

    public void constructYearPlaylist(Label playlistNameLbl, VBox playlistVBox, VBox dashboardVBox, Pane dashboardPane, Pane playlistPane,
                                      MainController controller){
        displayYears.group(playlistNameLbl, playlistVBox, dashboardVBox, dashboardPane, playlistPane, controller);
    }



}