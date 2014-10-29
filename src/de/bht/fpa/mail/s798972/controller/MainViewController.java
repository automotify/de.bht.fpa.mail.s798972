package de.bht.fpa.mail.s798972.controller;

import de.bht.fpa.mail.s798972.model.data.FileElement;
import de.bht.fpa.mail.s798972.model.data.Component;
import de.bht.fpa.mail.s798972.model.data.Folder;
import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;


public class MainViewController implements Initializable {

    @FXML
    TreeView explorerTreeView;

    @FXML
    private Label label;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        configureTree();
    }
    
    @FXML
    private void handleButtonAction(ActionEvent event) {
        //   System.out.println("You clicked me!");
        //   label.setText("Hello World!");
    }

    private final Node folderIcon = new ImageView(
            new Image(getClass().getResourceAsStream("folder_icon.png"))
    );
    private final Node fileIcon = new ImageView(
            new Image(getClass().getResourceAsStream("file_icon.png"))
    );

    public void configureTree() {
        File rootPath = new File("/Users");
        TreeItem<Component> rootItem = new TreeItem<> (new Folder(rootPath, true), folderIcon);
        rootItem.setExpanded(true);       
        explorerTreeView.setRoot(rootItem);        
 
        loadContent(rootPath, rootItem);
    }   

    public void loadContent(final File nodePath, TreeItem node) {
        for (final File path : nodePath.listFiles()) {
            if (path.isDirectory()) {
                boolean isEmpty = path.list().length > 0;
                System.out.println("Folder: "+path.getName()+" / "+isEmpty);
                
                TreeItem<Folder> folder = new TreeItem<> (new Folder(path, true), folderIcon);
                node.getChildren().add(folder);
            } else {
                TreeItem<FileElement> file = new TreeItem<> (new FileElement(path), fileIcon);
                node.getChildren().add(file);
            }
        }
    }
    
    @FXML
    public void handleMouseClick(ActionEvent event) {
        System.out.println(event.toString() + "\n");
        
        TreeItem<Component> node = (TreeItem) event.getSource();
        loadContent(new File(node.getValue().getPath()), node);
    }

}
