package View;

import Model.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.ArrayList;

public class Register {

    public static void display()
    {
        Stage window = new Stage();

        window.initModality(Modality.APPLICATION_MODAL);
        GridPane layout = new GridPane();

        Label title = new Label("REGISTRATION");
        Label nameLabel = new Label("Username: ");
        Label passLabel = new Label("Password: ");
        Label confirmPassLabel = new Label("Confirm Password: ");
        Label registerAs = new Label("Register as: ");

        TextField nameInput = new TextField();
        PasswordField passInput = new PasswordField();
        PasswordField conPassInput = new PasswordField();
        ChoiceBox<String> reg = new ChoiceBox<>();
        reg.getItems().addAll("Artist", "Listener");
        reg.getSelectionModel().selectFirst();

        Button registerButton = new Button("Register");
        nameInput.setPromptText("Username");
        passInput.setPromptText("Password");
        conPassInput.setPromptText("Confirm Password");

        layout.setPadding(new Insets(10, 10, 10, 10));
        layout.setVgap(5);
        layout.setHgap(5);

        GridPane.setConstraints(title, 1, 0);
        GridPane.setConstraints(nameLabel, 0, 1);
        GridPane.setConstraints(nameInput, 1, 1);
        GridPane.setConstraints(passLabel, 0, 2);
        GridPane.setConstraints(passInput, 1, 2);
        GridPane.setConstraints(confirmPassLabel, 0, 3);
        GridPane.setConstraints(conPassInput, 1, 3);
        GridPane.setConstraints(registerAs, 0, 4);
        GridPane.setConstraints(reg, 1, 4);
        GridPane.setConstraints(registerButton, 1, 5);

        registerButton.setOnAction(e -> {
            if(conPassInput.getText().equals(passInput.getText())) {
                Database db = new Database();
                AccountService accService = new AccountService(db);
                String username = nameInput.getText();
                String password = passInput.getText();
                Account a = new Account();
                a.setUsername(username);
                a.setPassword(password);
                if(reg.getValue().equals("Listener"))
                    a.setArtist(false);
                else
                    a.setArtist(true);
                PlaylistService plService = new PlaylistService(db);

                // MOST PLAYED SONGS
                Playlist mps = new Playlist();
                mps.setPlaylistID(plService.getAll().size()+1);
                mps.setName("Most Played Songs");
                mps.setSongCount(0);
                mps.setUsername(username);

                // MY SONGS
                Playlist ms = new Playlist();
                ms.setPlaylistID(plService.getAll().size()+2);
                ms.setName("My Songs");
                ms.setSongCount(0);
                ms.setUsername(username);

                // NO ALBUM PLAYLIST
                Playlist na = new Playlist();
                na.setPlaylistID(plService.getAll().size()+3);
                na.setName("No Album");
                na.setSongCount(0);
                na.setUsername(username);
                na.setAlbum(true);

                int exists = 0;

                for (int i = 0; i < accService.getAll().size(); i++) {
                    if (username.equals(accService.getAll().get(i).getUsername()))
                        exists++;
                }

                if (exists == 0 && !username.equals("") && !password.equals("")) {
                    AlertBox.display("Testing", "Registration Successful!");
                    accService.add(a);
                    plService.add(mps);
                    if(a.isArtist())
                        plService.add(ms);
                    plService.add(na);
                    window.close();
                }
                else if(username.equals("") || password.equals(""))
                    AlertBox.display("Testing", "Blank Fields Detected!");
                else
                    AlertBox.display("Testing", "Username already exists!");
            }
            else {
                AlertBox.display("Testing", "Passwords do not match!");
                passInput.setText("");
                conPassInput.setText("");
            }
        });

        layout.getChildren().addAll(title, nameLabel, nameInput, passLabel, passInput, confirmPassLabel, conPassInput,
                registerAs, reg, registerButton);

        Scene regScene = new Scene(layout, 300,200);
        window.setScene(regScene);
        window.showAndWait();
    }
}