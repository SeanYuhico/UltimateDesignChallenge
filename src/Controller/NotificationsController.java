package Controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class NotificationsController {

    @FXML VBox notifVBox;

    public void initialize() {
        for (String notif: MainController.notifications)
            notifVBox.getChildren().add(new Label(notif));
    }
}
