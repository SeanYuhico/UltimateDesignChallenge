package View;

import Controller.MainController;
import Model.*;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.List;

public class DisplayYears implements PlaylistInterface {

    @Override
    public void group(Label playlistNameLbl, VBox playlistVBox, VBox dashboardVBox, Pane dashboardPane, Pane playlistPane, MainController controller) {

        initialize(playlistVBox);

        ArrayList<Playlist> yearPlaylists = new ArrayList<>();
        for(String year : getAllYears()) {
            YearPlaylistBuilder ypb = new YearPlaylistBuilder();

            int howManySongs = 0;
            ypb.buildName(year);
            for(Song s : ss.getAll())
                if(s.getUsername().equals(LoginWindow.getLoggedUser()) && s.getYear().equals(year))
                    howManySongs++;
            ypb.buildSongCount(howManySongs);
            ypb.buildUsername();
            yearPlaylists.add(ypb.getPlaylist());
        }

        for(Playlist playlist : yearPlaylists)
            playlistVBox.getChildren().add(new PlaylistHBox(playlistNameLbl, playlist, dashboardVBox, dashboardPane, playlistPane, controller));
    }

    public void initialize(VBox vbox) {
        vbox.getChildren().clear();
    }

    public List<String> getAllYears() {
        List<String> years = new ArrayList<>();
        for (Song s : ss.getAll())
            if (s.getUsername().equals(LoginWindow.getLoggedUser()))
                if (!(years.contains(s.getYear())))
                    years.add(s.getYear());
        return years;
    }
}
