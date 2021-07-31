package appointmentscheduler;

import javafx.application.Application;
import javafx.stage.Stage;

/**
 * This is the main class of application which extends Application to simulate
 * the JavaFx application behavior
 *
 * @author
 */
public class Main extends Application {

    /**
     * On application start initialize and display SignIn frame
     *
     * @param primaryStage initial stage instance of JavaFx Application
     */
    @Override
    public void start(Stage primaryStage) {

        SignIn l = new SignIn();
        l.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}
