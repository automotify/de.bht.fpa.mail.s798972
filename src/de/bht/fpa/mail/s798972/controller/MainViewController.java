package de.bht.fpa.mail.s798972.controller;

import de.bht.fpa.mail.s798972.model.applicationLogic.EmailManagerIF;
import de.bht.fpa.mail.s798972.model.applicationLogic.FileManager;
import de.bht.fpa.mail.s798972.model.applicationLogic.FolderManagerIF;
import de.bht.fpa.mail.s798972.model.applicationLogic.XmlEmailManager;
import de.bht.fpa.mail.s798972.model.data.Component;
import de.bht.fpa.mail.s798972.model.data.Folder;
import java.io.File;
import java.io.IOException;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeItem.TreeModificationEvent;
import javafx.scene.control.TreeView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * Controller for main application builded with FXML document FXMLMainView
 * 
 * @author Lukas Abegg, S53647, FPA - Beuth Hochschule
 * @version 1.0
 */
public class MainViewController implements Initializable {

    @FXML
    private TreeView explorerTreeView;

    @FXML
    private MenuBar menuBar;

    private EmailManagerIF emailManager; /* EmailManager for handling email model */

    private FolderManagerIF folderManager; /* FileManager for holding folder model */

    private static Image FOLDERICON; /* Image icon for folders */

    private static Image FOLDERICON_OPEN; /* Image icon for expanded folder */

    private static File DEFAULTROOT; /* default root path */

    private final static String MENU_ID_FILE_OPEN = "fileOpen"; /* constant for fileOpen Action Command */

    private final static String MENU_ID_FILE_HISTORY = "fileHistory"; /* constant for fileHistory Action Command */

    private final List<File> historyList = new ArrayList<>(); /* saves all root folder chosen in history */


    /**
     * Constructor method
     */
    public MainViewController() {
        FOLDERICON = new Image(getClass().getResourceAsStream("folder_icon.png"));
        FOLDERICON_OPEN = new Image(getClass().getResourceAsStream("folder_open_icon.png"));
        DEFAULTROOT = new File("/Users/lukas/git-projects/de.bht.fpa.mail.s798972/emailData/Account");
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

        configureMenue(); /* set menu in application */

    }

    /**
     * Configure root item and TreeItem handlers and instantiate models for
     * folder and email handling
     *
     * @param rootPath path to root item as String
     */
    public void configureTree(File rootPath) {
        folderManager = new FileManager(rootPath); /* manage folder model */

        emailManager = new XmlEmailManager(); /* manage emails functions */

        TreeItem<Component> rootItem = new TreeItem<>(folderManager.getTopFodler(), new ImageView(FOLDERICON));
        rootItem.setExpanded(true);
        /* set event handler for TreeItem expanding and collapse */
        rootItem.addEventHandler(TreeItem.branchExpandedEvent(), (TreeModificationEvent<Component> e) -> handleTreeItemExpandCollapseEvent(e));
        rootItem.addEventHandler(TreeItem.branchCollapsedEvent(), (TreeModificationEvent<Component> e) -> handleTreeItemExpandCollapseEvent(e));

        explorerTreeView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> handleLoadEmailEvent((TreeItem<Component>) newValue));
        explorerTreeView.setRoot(rootItem);

        /* saves root path, if its not the default root path, in list for saving history */
        if (rootPath != DEFAULTROOT) {
            Boolean found = false;
            /* check if path is already in list added */
            for (File item : historyList) {
                if (item.equals(rootPath)) {
                    found = true;
                    break;
                }
            }
            /* add path to list */
            if (!found) {
                historyList.add(rootPath);
            }
        }

        loadTreeItemContent(rootItem); /* get all files in root path and add theme in TreeView */

    }

    /**
     * load all files from directory node
     *
     * @param node TreeItem node which is expanded
     */
    private void loadTreeItemContent(TreeItem<Component> node) {
        Folder folder = (Folder) node.getValue();
        node.getChildren().removeAll(node.getChildren()); /* clear TreeItem node before appending new children */

        TreeItem<Component> dummyItem = new TreeItem<>(new Folder(new File("/"), true)); /* is needed to show node expandable before children will be loaded */

        folderManager.loadContent(folder); /* load all children of folder */

        /* add all children of folder as sub items of node */
        for (Component subFolder : folder.getComponents()) {
            TreeItem<Component> subItem = null;
            /* handle file elements */
            if (subFolder instanceof Folder) {
                subItem = new TreeItem<>(subFolder, new ImageView(FOLDERICON));
                /* show node expandable if there are any childrens */
                if (subFolder.isExpandable()) {
                    subItem.getChildren().add(dummyItem);
                }
            }
            node.getChildren().add(subItem); /* add TreeItem to node */

        }

    }

    /**
     * Event handler for expand TreeItem Event loads all children of clicked
     * node
     *     
     * @param event TreeModificationEvent to get clicked node
     */
    private void handleTreeItemExpandCollapseEvent(TreeModificationEvent<Component> event) {
        TreeItem<Component> item = event.getTreeItem(); /* clicked node */

        /* check which event is handled */
        if (event.getEventType().equals(TreeItem.branchExpandedEvent())) { /* node is expanded */

            System.out.println("-------- expand item: " + item.getValue().getName());
            setFolderExpandedIcon(item, true); /* set icon for open folder */

            loadTreeItemContent(item); /* load all children elements */

        } else if (event.getEventType().equals(TreeItem.branchCollapsedEvent())) { /* node is collapsed */

            System.out.println("-------- collapse item: " + item.getValue().getName());
            setFolderExpandedIcon(item, false); /* set icon for closed folder */

        }
    }

    /**
     * initiate menu for application and set handler for menu items
     */
    public void configureMenue() {
        menuBar.getMenus().stream().forEach((m) -> { /* loop through all menus */

            m.getItems().stream().forEach((item) -> { /* loop through all menu items */

                item.setOnAction((event) -> handleMenuItemEvent(event)); /* set handler to all menu items */

            });
        });
    }

    /**
     * Event handler for all menu item events from application menu
     *     
     * @param event Event to get event id and MenuItem object
     */
    private void handleMenuItemEvent(Event event) {
        final MenuItem item = (MenuItem) event.getSource(); /* clicked menu item */

        /* handling events depending on action id */
        switch (item.getId()) {
            /* handle choosing file to load in TreeView event */
            case MENU_ID_FILE_OPEN: {
                File file = openFile(); /* choose file from file system */

                if (file != null) { /* check if file is chosen */

                    System.out.println("Root Item set on: " + file.getAbsolutePath());
                    configureTree(file); /* reload tree for chosen file */

                }
                break;
            }
            /* handle choosing file from history list to reopen in tree view */
            case MENU_ID_FILE_HISTORY: {
                showBaseDirHistoryView(); /* load view for history list */

                break;
            }
            default:
                break; /* if any other event, do nothing */

        }
    }

    /**
     * Choose file from file system with DirectoryChooser object
     *     
     * @return chosen file as File object
     */
    private File openFile() {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("FPA Mailer File Explorer");
        return directoryChooser.showDialog(null); /* launch dialog to select a file */

    }

    /**
     * loads View FXMLBaseDirHistoryView to choose a file from history list
     */
    private void showBaseDirHistoryView() {
        Stage editStage = new Stage(StageStyle.UTILITY); /* view needs a stage to be loaded inside */

        editStage.setTitle("FPA Mail Base Direction History");
        URL location = getClass().getResource("/de/bht/fpa/mail/s798972/view/FXMLBaseDirHistoryView.fxml");

        /* instantiate a new fxml loader with fxml document and controller for history list */
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(location);
        fxmlLoader.setController(new BaseDirHistoryViewController(this));

        /* load view and build view content */
        try {
            Pane myPane = (Pane) fxmlLoader.load();
            Scene myScene = new Scene(myPane);
            editStage.setScene(myScene);
            editStage.show(); /* launch view */

        } catch (IOException ex) {
            System.err.println("Error by loading view FXMLBaseDirHistoryView " + ex.getMessage());
        }
    }

    /**
     * Returns history list for showing base directory history list as example
     *     
     * @return List of File objects with all opened root directories
     */
    public List<File> getHistoryList() {
        return historyList;
    }

    /**
     * Set icon for folder expanded or closed
     */
    private void setFolderExpandedIcon(TreeItem<Component> node, boolean expand) {
        if (expand) {
            node.setGraphic(new ImageView(FOLDERICON_OPEN));
        } else {
            node.setGraphic(new ImageView(FOLDERICON));
        }
    }

    /**
     * Load email for tree item
     *
     * @param item handled TreeItem
     */
    private void handleLoadEmailEvent(TreeItem<Component> item) {
        if (item != null) {
            System.out.println("Selected Text : " + item.getValue());
            Folder folder = (Folder) item.getValue(); /* get Folder object for item */

            folder = emailManager.loadEmails(folder); /* load emails for folder */

            emailManager.printFolderContent(folder); /* print emails in folder */

        }
    }
}
