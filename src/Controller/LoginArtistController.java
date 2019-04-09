package Controller;

import View.AlertBox;
import View.LoginWindow;
import View.Main;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.effect.*;



public class LoginArtistController {

    @FXML TextField usernameTextField;
    @FXML PasswordField passwordField;
    @FXML Button signInButton, logListenerButton;

    public void signIn(){

        if (usernameTextField.getText().equals(""))
            AlertBox.display("Error", "Username field cannot be left blank");
        if (passwordField.getText().equals(""))
            AlertBox.display("Error", "Password field cannot be left blank");
        else
            System.out.println("DB Boys!");

    }

    public void logAsListener() {
        LoginWindow.display(Main.getMainStage());
    }

    public void signInHover() {
        signInButton.setEffect(new DropShadow());
    }

    public void signInHoverOut() {
        signInButton.setEffect(null);
    }

    public void logListenerHover() {
        logListenerButton.setEffect(new DisplacementMap());
    }

    public void logListenerHoverOut() {
        logListenerButton.setEffect(null);
    }

}
