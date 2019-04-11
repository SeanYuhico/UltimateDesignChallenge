package Controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.DisplacementMap;
import javafx.scene.layout.VBox;

public class QueueWindowController {

    @FXML Button queueButton, historyButton, recentlyAddedButton;
    @FXML VBox queueVBox;
    @FXML Label titleLabel;

    public void initialize() {}

    public void showQueue() {

    }

    public void showHistory() {

    }

    public void showRecentlyAdded() {

    }

    public void queueHover() { queueButton.setEffect(new DisplacementMap()); }
    public void queueHoverOut() { queueButton.setEffect(null); }
    public void historyHover() { historyButton.setEffect(new DisplacementMap());}
    public void historyHoverOut() { historyButton.setEffect(null); }
    public void recentlyAddedHover() { recentlyAddedButton.setEffect(new DisplacementMap()); }
    public void recentlyAddedHoverOut() { recentlyAddedButton.setEffect(null); }
}
