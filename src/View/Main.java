package View;

import javafx.application.Application;
import javafx.stage.Stage;


public class Main extends Application {
    public static Stage mainStage;
    @Override
    public void start(Stage primaryStage) throws Exception{
        mainStage = primaryStage;
        LoginWindow.display(mainStage);
    }

    public static void main(String[] args) {
        launch(args);
    }

    public static Stage getMainStage() {
        return mainStage;
    }
}