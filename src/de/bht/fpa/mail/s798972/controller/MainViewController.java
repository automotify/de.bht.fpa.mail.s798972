package de.bht.fpa.mail.s798972.controller;

import de.bht.fpa.mail.s798972.model.data.Component;
import de.bht.fpa.mail.s798972.model.data.FileElement;
import de.bht.fpa.mail.s798972.model.data.Folder;
import java.io.File;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeItem.TreeModificationEvent;
import javafx.scene.control.TreeView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * Controller for main application builded with FXML document FXMLMainView
 * 
* @author Lukas Abegg, S53647, FPA - Beuth Hochschule
 * @version 1.0
 */
public class MainViewController implements Initializable {

    @FXML
    private TreeView explorerTreeView;

    private static Image FOLDERICON; 
    private static Image FILEICON;
    private static File DEFAULTROOT;    /* default root path */

    /**
     * 
     */
    public MainViewController() {
        FOLDERICON = new Image(getClass().getResourceAsStream("folder_icon.png"));
        FILEICON = new Image(getClass().getResourceAsStream("file_icon.png"));
        DEFAULTROOT = new File("/Users");
    }

    /**
     * Call initiation methods and set default parameters for main view
     *     
     * @param url application url
     * @param rb bundle for handling language translations
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        configureTree();
    }

    public void configureTree() {
        setRootItemTreeView(DEFAULTROOT);
    }

    /**
     *
     * @param rootPath
     */
    public void setRootItemTreeView(File rootPath) {
        TreeItem<Component> rootItem = new TreeItem<>(new Folder(rootPath, true), new ImageView(FOLDERICON));
        rootItem.setExpanded(true);
        rootItem.addEventHandler(TreeItem.branchExpandedEvent(), (TreeModificationEvent<Component> e) -> handleTreeItemExpandedEvent(e));
        explorerTreeView.setRoot(rootItem);

        loadTreeItemContent(rootItem);
    }

    private void loadTreeItemContent(TreeItem<Component> node) {
        node.getChildren().removeAll(node.getChildren().sorted());
        TreeItem<Component> dummyItem = new TreeItem<>(new Folder(new File("/"), true));
        TreeItem<Component> subItem;

        for (File f : new File(node.getValue().getPath()).listFiles()) {
            if (f.getName().startsWith(".")) {
                continue; // keine versteckten Files anzeigen
            }
            if (f.isDirectory()) {
                subItem = new TreeItem<>(new Folder(f, hasSubFiles(f)), new ImageView(FOLDERICON));
                if (hasSubFiles(f)) {
                    subItem.getChildren().add(dummyItem);
                }
            } else {
                subItem = new TreeItem<>(new FileElement(f), new ImageView(FILEICON));
            }
            node.getChildren().add(subItem);
        }
    }

    private Boolean hasSubFiles(File path) {
        try {
            return path.list().length > 0;
        } catch (Exception e) {
            System.out.println(path.getName());
            System.out.println("Error by reading length of: " + e.getMessage());
        }
        return false;
    }

    private void handleTreeItemExpandedEvent(TreeModificationEvent<Component> event) {
        TreeItem<Component> expandItem = event.getTreeItem();

        System.out.println("-------- load childeren of item: " + expandItem.getValue().getName());
        loadTreeItemContent(expandItem);
    }
}
