package Controller;

import View.LoginWindow;
import View.Main;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;

import javax.swing.*;
import java.awt.*;

public class LoginArtistController {

    @FXML TextField usernameTextField;
    @FXML PasswordField passwordField;
    @FXML Button signInButton, logListenerButton;

    public void signIn(){

        if (usernameTextField.getText() == null || passwordField.getText() == null) {
            JOptionPane.showMessageDialog(null, "One or more text fields are left blank.",
                    "Error!", JOptionPane.ERROR_MESSAGE);
        }
        else
            System.out.println("DB Boys!");

    }

    public void logAsListener() {
        LoginWindow.display(Main.getMainStage());
    }

}
