package Controller;

import Model.Account;
import Model.AccountService;
import Model.Database;
import View.*;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.effect.*;



public class LoginArtistController {

    @FXML TextField usernameTextField;
    @FXML PasswordField passwordField;
    @FXML Button signInButton, logListenerButton;

    private Database db;
    private AccountService accountService;
    private static String loggedUser;

    public void signIn(){
        db = new Database();
        accountService = new AccountService(db);

        if (usernameTextField.getText().equals(""))
            AlertBox.display("Error", "Username field cannot be left blank");
        if (passwordField.getText().equals(""))
            AlertBox.display("Error", "Password field cannot be left blank");
        else {
            String username = usernameTextField.getText();
            String password = passwordField.getText();
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
                MusicPlayer.display(Main.getMainStage());
            } else if (uExists > 0 && pExists == 0)
                AlertBox.display("Testing", "Incorrect password!");
            else
                AlertBox.display("Testing", "Username does not exist!");
            System.out.println("DB Boys!");
        }
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

    public void openRegWindow(){
        Register.display();
    }

    public static String getLoggedUser() {
        return loggedUser;
    }
}
