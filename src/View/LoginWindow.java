package View;

import Model.*;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class LoginWindow {
    private static String loggedUser;

    public static void display(Stage primaryStage) {
        Database db;
        AccountService accountService;
        Scene start;

        db = new Database();
        accountService = new AccountService(db);

        primaryStage.setTitle("Testing");

        GridPane layout = new GridPane();
        HBox mainButtons = new HBox(5);

        Label title = new Label("Music Player");
        Label nameLabel = new Label("Username: ");
        Label passLabel = new Label("Password: ");
        TextField nameInput = new TextField();
        PasswordField passInput = new PasswordField();
        Button loginButton = new Button("Log In");
        Button registerButton = new Button("Register");
        Button guestButton = new Button("Guest");
        nameInput.setPromptText("Username");
        passInput.setPromptText("Password");

        mainButtons.getChildren().addAll(loginButton, registerButton, guestButton);

        layout.setPadding(new Insets(10, 10, 10, 10));
        layout.setVgap(5);
        layout.setHgap(5);
        layout.setAlignment(Pos.CENTER);
        //layout.setGridLinesVisible(true);

        GridPane.setConstraints(title, 1, 0);
        GridPane.setConstraints(nameLabel, 0, 1);
        GridPane.setConstraints(nameInput, 1, 1);
        GridPane.setConstraints(passLabel, 0, 2);
        GridPane.setConstraints(passInput, 1, 2);
        GridPane.setConstraints(mainButtons, 1, 3);
        //GridPane.setConstraints(loginButton, 0, 3);
        //GridPane.setConstraints(registerButton, 1, 3);


        loginButton.setOnAction(e -> {
            String username = nameInput.getText();
            String password = passInput.getText();
            Account a = new Account();
            a.setUsername(username);
            a.setPassword(password);
            int uExists = 0, pExists = 0;

            for (int i = 0; i < accountService.getAll().size(); i++) {
                if (username.equals(accountService.getAll().get(i).getUsername()))
                    uExists++;
                if (password.equals(accountService.getAll().get(i).getPassword()))
                    pExists++;
            }

            if (uExists > 0 && pExists > 0) {
                AlertBox.display("Testing", "Login Successful!");
                loggedUser = username;
                MusicPlayer.display(primaryStage);
            } else if (uExists > 0 && pExists == 0)
                AlertBox.display("Testing", "Incorrect password!");
            else
                AlertBox.display("Testing", "Username does not exist!");

        });

        registerButton.setOnAction(e -> Register.display());

        guestButton.setOnAction(e -> {
            Account a = new Account();
            a.setUsername("Guest");
            PlaylistService plService = new PlaylistService(db);
            loggedUser = "Guest";

            // MOST PLAYED SONGS
            Playlist mps = new Playlist();
            mps.setPlaylistID(plService.getAll().size()+1);
            mps.setName("Most Played Songs");
            mps.setSongCount(0);
            mps.setUsername("Guest");

            // MY SONGS
            Playlist ms = new Playlist();
            ms.setPlaylistID(plService.getAll().size()+2);
            ms.setName("My Songs");
            ms.setSongCount(0);
            ms.setUsername("Guest");

            // ARTISTS
            Playlist art = new Playlist();
            art.setPlaylistID(plService.getAll().size()+3);
            art.setName("Artists");
            art.setSongCount(0);
            art.setUsername("Guest");

            // ALBUMS
            Playlist alb = new Playlist();
            alb.setPlaylistID(plService.getAll().size()+4);
            alb.setName("Albums");
            alb.setSongCount(0);
            alb.setUsername("Guest");

            // GENREs
            Playlist g = new Playlist();
            g.setPlaylistID(plService.getAll().size()+5);
            g.setName("Genres");
            g.setSongCount(0);
            g.setUsername("Guest");

            // YEAR
            Playlist y = new Playlist();
            y.setPlaylistID(plService.getAll().size()+6);
            y.setName("Year");
            y.setSongCount(0);
            y.setUsername("Guest");

            plService.add(mps);
            plService.add(ms);
            plService.add(art);
            plService.add(alb);
            plService.add(g);
            plService.add(y);

            AlertBox.display("Testing", "Guest Login Successful!");
            MusicPlayer.display(primaryStage);
        });

        layout.getChildren().addAll(title, nameLabel, nameInput, passLabel, passInput, mainButtons);

        start = new Scene(layout, 500,400);
        primaryStage.setScene(start);
        primaryStage.show();
    }

    public static String getLoggedUser()
    {
        return loggedUser;
    }
}
