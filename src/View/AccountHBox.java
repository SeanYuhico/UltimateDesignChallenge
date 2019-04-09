package View;

import Model.Account;
import Model.Database;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class AccountHBox extends HBox {
    private ImageView dp;
    private Label usernameLbl;

    // optional. display num of followers.
    private Label followersLabel;
    private int numFollowers;

    public AccountHBox(Account account)
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
//        Database db = new Database();


        this.getChildren().addAll(dp, usernameLbl);
    }
}
