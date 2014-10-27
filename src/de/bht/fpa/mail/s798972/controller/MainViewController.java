package de.bht.fpa.mail.s798972.controller;

import de.bht.fpa.mail.s798972.model.data.FileElement;
import de.bht.fpa.mail.s798972.model.data.Folder;
import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;

/**
 *
 * @author Lukas Abegg
 */
public class MainViewController implements Initializable {

    public TreeView tree;
    public TreeItem node;

    @FXML
    private Label label;

    @FXML
    private void handleButtonAction(ActionEvent event) {
        //   System.out.println("You clicked me!");
        //   label.setText("Hello World!");
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        configureTree();
    }

    public void configureTree() {
        node = new TreeItem(new Folder(new File("~"), true));
        //loadFilesForNode(node.
    }

    public void loadFilesForNode(final File nodePath, TreeItem node) {
        for (final File path : nodePath.listFiles()) {
            if (path.isDirectory()) {
                final File[] files = path.listFiles();
                node.getChildren().add(new Folder(path, files.length > 0));
            } else if (path.isFile()) {
                node.getChildren().add(new FileElement(path));
            }
        }
    }

}
