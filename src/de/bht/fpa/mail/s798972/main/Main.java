package de.bht.fpa.mail.s798972.main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Main class to start Application
 *
 * @author Lukas Abegg, S53647, FPA - Beuth Hochschule
 * @version 1.0
 */
public class Main extends Application {

    /**
     * Initiate and load the application in stage window
     *
     * @param stage the application window object
     * @throws Exception all exceptions thrown in stage object
     */
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/de/bht/fpa/mail/s798972/view/FXMLMainView.fxml"));

        Scene scene = new Scene(root, 900, 600);

        stage.setScene(scene);
        stage.setTitle("FPA Mailer");
        stage.show();
    }

    /**
     * Starter method
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}
