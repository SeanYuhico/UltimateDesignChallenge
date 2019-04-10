package View;

import Controller.LoginArtistController;
import Controller.MainController;
import Model.*;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.util.ArrayList;
import java.util.List;

public class AccountHBox extends HBox {
    private ImageView dp;
    private Label usernameLbl;
    private boolean isFollowing = true;
    // optional. display num of followers.
    private Label followersLabel;
    private int numFollowers;

    public AccountHBox(Account account, Label dashboardPlaylistLbl, VBox dashboardVBox, Pane dashboardPane, Pane playlistPane,
                       MainController controller)
    {
        // Properties
        setVisible(true);
        setCacheShape(true);
        setCenterShape(true);
        setScaleShape(true);
        setOpacity(1);
        setAlignment(Pos.TOP_LEFT);
        setPickOnBounds(true);

        // Content
        dp = new ImageView(new Image("/Pictures/profilePic.png"));
        usernameLbl = new Label(account.getUsername());

        // Optional Contents.
//        followersLabel = new Label("Number of Followers: ");
//        numFollowers = account.getFollowers();

        // Layout
        setFillHeight(true);
        setPrefWidth(823);
        setPrefHeight(37);
        setMinWidth(USE_COMPUTED_SIZE);
        setMaxWidth(USE_COMPUTED_SIZE);
        setMinHeight(USE_COMPUTED_SIZE);
        setMaxHeight(USE_COMPUTED_SIZE);
        setSnapToPixel(true);

        dp.setFitHeight(38);
        dp.setFitWidth(32);
        dp.setVisible(true);
        dp.setPickOnBounds(true);
        dp.setPreserveRatio(true);
        setMargin(dp, new Insets(4, 5, 0, 10));

        usernameLbl.setPrefSize(289, 29);
        usernameLbl.setFont(Font.font("Lucida Fax", FontWeight.SEMI_BOLD, 18));
        setMargin(usernameLbl, new Insets(4, 5, 0, 10));

        // Functionalities
        Database db = new Database();
        FollowerService fs = new FollowerService(db);



        final ContextMenu contextMenu = new ContextMenu();
        MenuItem visitProfile = new MenuItem("Visit Profile");
        MenuItem follow = new MenuItem("Follow");
        for(int i = 0; i < fs.getAll().size(); i++)
            if((fs.getAll().get(i).getFollower().equals(LoginArtistController.getLoggedUser())) &&
                    (fs.getAll().get(i).getFollowing().equals(usernameLbl.getText()))) {
                follow.setText("Unfollow");
            }
            else
                follow.setText("Follow");



        contextMenu.getItems().addAll(visitProfile, follow);

        usernameLbl.setOnMouseClicked(e -> {
            if(e.getButton() == MouseButton.SECONDARY){
                contextMenu.show(usernameLbl, e.getScreenX(), e.getScreenY());
                visitProfile.setOnAction(ex -> ArtistProfile.display(dashboardPlaylistLbl, usernameLbl.getText(), dashboardVBox,
                        dashboardPane, playlistPane, controller));
                follow.setOnAction(ex -> {
                    if(follow.getText().equals("Follow"))
                        fs.add(LoginArtistController.getLoggedUser(), usernameLbl.getText());
                    else if(follow.getText().equals("Unfollow"))
                        fs.unfollow(LoginArtistController.getLoggedUser(), usernameLbl.getText());
                });
            }
        });

        this.getChildren().addAll(dp, usernameLbl);
    }
}
