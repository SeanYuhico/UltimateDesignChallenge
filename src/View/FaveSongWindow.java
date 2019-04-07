package View;

import Model.Database;
import Model.Song;
import Model.SongService;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.List;

public class FaveSongWindow implements Window{
    public static void display()
    {
        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        GridPane layout = new GridPane();
        Database db = new Database();
        SongService ss = new SongService(db);
        List<Song> songs = ss.getAll();

        window.setTitle("Favorite Songs");

        layout.setPadding(new Insets(10, 10, 10, 10));
        layout.setVgap(5);
        layout.setHgap(5);

        ContextMenu contextMenu = new ContextMenu();
        MenuItem unfave = new MenuItem("Unfave");
        contextMenu.getItems().add(unfave);

        int i = 1;
        for(Song s : songs)
            if(s.isFave() && s.getUsername().equals(LoginWindow.getLoggedUser())) {
                Label faveSong = new Label(s.getTitle());
                faveSong.setOnMouseClicked(e -> {
                    if (e.getButton() == MouseButton.SECONDARY) {
                        contextMenu.show(faveSong, e.getScreenX(), e.getScreenY());
                        unfave.setOnAction(ev -> {
                            SongService.makeFave(s.getSongID(), "false");
                            window.close();
                            FaveSongWindow.display(); // recursive para pag nagset unfave sya dapat magupdate yung window
                        });
                    }
                });
                GridPane.setConstraints(faveSong, 1, i);
                i++;
                layout.getChildren().addAll(faveSong);
            }

        window.setScene(new Scene(layout, 300, 200));
        window.show();

    }
}
