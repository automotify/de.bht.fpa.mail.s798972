
package de.bht.fpa.mail.s798972.main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author Lukas Abegg
 */

public class Main extends Application {
    
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/de/bht/fpa/mail/s798972/view/FXMLMainView.fxml"));
        
        Scene scene = new Scene(root, 900, 600);
        
        stage.setScene(scene);
        stage.setTitle("FPA Mailer");
        stage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
