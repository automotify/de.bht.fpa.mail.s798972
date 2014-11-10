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

    private static Image FOLDERICON; /* Image icon for folders */

    private static Image FILEICON; /* Image icon for files */

    private static File DEFAULTROOT; /* default root path */


    /**
     * Constructor method
     */
    public MainViewController() {
        FOLDERICON = new Image(getClass().getResourceAsStream("folder_icon.png"));
        FILEICON = new Image(getClass().getResourceAsStream("file_icon.png"));
        DEFAULTROOT = new File("/Users");
    }

    /**
     * Initiate TreeView with default root path
     *
     * @param url application URL
     * @param rb bundle for handling language translations
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        configureTree(DEFAULTROOT); /* set root item in TreeView */

    }

    /**
     * Configure root item and TreeItem handlers
     *
     * @param rootPath path to root item as String
     */
    public void configureTree(File rootPath) {
        TreeItem<Component> rootItem = new TreeItem<>(new Folder(rootPath, true), new ImageView(FOLDERICON));
        rootItem.setExpanded(true);
        /* set event handler for TreeItem expanding */
        rootItem.addEventHandler(TreeItem.branchExpandedEvent(), (TreeModificationEvent<Component> e) -> handleTreeItemExpandedEvent(e));
        explorerTreeView.setRoot(rootItem);

        loadTreeItemContent(rootItem); /* get all files in root path and add theme in TreeView */

    }

    /**
     * load all files from directory node
     *
     * @param node TreeItem node which is expanded
     */
    private void loadTreeItemContent(TreeItem<Component> node) {
        node.getChildren().removeAll(node.getChildren()); /* clear TreeItem node before appending new children */

        final TreeItem<Component> dummyItem = new TreeItem<>(new Folder(new File("/"), true)); /* is needed to show node expandable before children will be loaded */

        TreeItem<Component> subItem;
        final File nodeFile = new File(node.getValue().getPath());

        /* get all files in directory and instantiate theme in TreeItem node */
        for (File f : nodeFile.listFiles()) {
            if (f.getName().startsWith(".")) {
                continue; /* do not show any hidded files from file system */

            }
            /* handle directories */
            if (f.isDirectory()) {
                subItem = new TreeItem<>(new Folder(f, hasSubFiles(f)), new ImageView(FOLDERICON));
                /* show node expandable if there are any childrens */
                if (hasSubFiles(f)) {
                    subItem.getChildren().add(dummyItem);
                }
                /* handle files */
            } else {
                subItem = new TreeItem<>(new FileElement(f), new ImageView(FILEICON));
            }
            node.getChildren().add(subItem); /* add TreeItem to node */

        }
    }

    /**
     * Check if path has any children
     *
     * @param path File of node, which has to be checked
     * @return Boolean, directory has any children
     */
    private Boolean hasSubFiles(File path) {
        return path.listFiles() != null;
    }

    /**
     * Event handler for expand TreeItem Event loads all children of clicked
     * node
     *
     * @param event TreeModificationEvent to get clicked node
     */
    private void handleTreeItemExpandedEvent(TreeModificationEvent<Component> event) {
        TreeItem<Component> expandItem = event.getTreeItem(); /* clicked node */

        System.out.println("-------- load childeren of item: " + expandItem.getValue().getName());
        loadTreeItemContent(expandItem); /* load all children elements */

    }
}
