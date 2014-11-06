package de.bht.fpa.mail.s798972.controller;

import de.bht.fpa.mail.s798972.model.applicationLogic.EmailManagerIF;
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

public class MainViewController implements Initializable {

    @FXML
    private TreeView explorerTreeView;

    @FXML
    private MenuBar menuBar;

    private EmailManagerIF folderManager;

    private static Image FOLDERICON;
    private static Image FOLDERICON_OPEN;
    private static File DEFAULTROOT;

    private final List<File> historyList = new ArrayList<>();

    public MainViewController() {
        FOLDERICON = new Image(getClass().getResourceAsStream("folder_icon.png"));
        FOLDERICON_OPEN = new Image(getClass().getResourceAsStream("folder_open_icon.png"));
        DEFAULTROOT = new File("/Users");
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        configureTree();
        configureMenue();
    }

    public void configureTree() {
        setRootItemTreeView(DEFAULTROOT);
    }

    /**
     *
     * @param rootPath
     */
    public void setRootItemTreeView(File rootPath) {
        folderManager = new XmlEmailManager(rootPath);
        TreeItem<Component> rootItem = new TreeItem<>(folderManager.getTopFodler(), new ImageView(FOLDERICON));
        rootItem.setExpanded(true);
        rootItem.addEventHandler(TreeItem.branchExpandedEvent(), (TreeModificationEvent<Component> e) -> handleTreeItemExpandCollapseEvent(e));
        rootItem.addEventHandler(TreeItem.branchCollapsedEvent(), (TreeModificationEvent<Component> e) -> handleTreeItemExpandCollapseEvent(e));

        explorerTreeView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> handleLoadEmailEvent((TreeItem<Component>) newValue));
        explorerTreeView.setRoot(rootItem);

        if (rootPath != DEFAULTROOT) {
            Boolean found = false;
            for (File item : historyList) {
                if (item.equals(rootPath)) {
                    found = true;
                    break;
                }
            }

            if (!found) {
                historyList.add(rootPath);
            }
        }

        loadTreeItemContent(rootItem);
    }

    private void loadTreeItemContent(TreeItem<Component> node) {
        Folder folder = (Folder) node.getValue();
        node.getChildren().removeAll(node.getChildren().sorted());
        TreeItem<Component> dummyItem = new TreeItem<>(new Folder(new File("/"), true));

        folderManager.loadContent(folder);

        for (Component subFolder : folder.getComponents()) {
            TreeItem<Component> subItem = null;

            if (subFolder instanceof Folder) {
                subItem = new TreeItem<>(subFolder, new ImageView(FOLDERICON));
                if (subFolder.isExpandable()) {
                    subItem.getChildren().add(dummyItem);
                }
            }
            node.getChildren().add(subItem);
        }

    }

    private void handleTreeItemExpandCollapseEvent(TreeModificationEvent<Component> event) {
        TreeItem<Component> expandItem = event.getTreeItem();

        System.out.println("-------- load childeren of item: " + expandItem.getValue().getName());
        if (event.getEventType().equals(TreeItem.branchExpandedEvent())) {
            setFolderExpandedIcon(expandItem, true);
            loadTreeItemContent(expandItem);
        } else if (event.getEventType().equals(TreeItem.branchCollapsedEvent())) {
            setFolderExpandedIcon(expandItem, false);
        }
    }

    public void configureMenue() {
        menuBar.getMenus().stream().forEach((m) -> {
            m.getItems().stream().forEach((item) -> {
                item.setOnAction((event) -> handleMenuItemEvent(event));
            });
        });
    }

    private final static String MENU_ID_FILE_OPEN = "fileOpen";
    private final static String MENU_ID_FILE_HISTORY = "fileHistory";

    private void handleMenuItemEvent(Event event) {
        final MenuItem item = (MenuItem) event.getSource();

        switch (item.getId()) {
            case MENU_ID_FILE_OPEN: {
                File file = openFile();
                if (file != null) {
                    System.out.println("Root Item set on: " + file.getAbsolutePath());
                    setRootItemTreeView(file);
                }
                break;
            }
            case MENU_ID_FILE_HISTORY: {
                showBaseDirHistoryView();
                break;
            }
        }
    }

    private File openFile() {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("FPA Mailer File Explorer");
        return directoryChooser.showDialog(null);
    }

    private void showBaseDirHistoryView() {
        Stage editStage = new Stage(StageStyle.UTILITY);
        editStage.setTitle("FPA Mail Base Direction History");
        URL location = getClass().getResource("/de/bht/fpa/mail/s798972/view/FXMLBaseDirHistoryView.fxml");

        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(location);
        fxmlLoader.setController(new BaseDirHistoryViewController(this));
        try {
            Pane myPane = (Pane) fxmlLoader.load();
            Scene myScene = new Scene(myPane);
            editStage.setScene(myScene);
            editStage.show();
        } catch (IOException ex) {
            System.err.println("Error by loading view FXMLBaseDirHistoryView " + ex.getMessage());
        }
    }

    public List<File> getHistoryList() {
        return historyList;
    }

    private void setFolderExpandedIcon(TreeItem<Component> node, boolean expand) {
        if (expand) {
            node.setGraphic(new ImageView(FOLDERICON_OPEN));
        } else {
            node.setGraphic(new ImageView(FOLDERICON));
        }
    }

    private void handleLoadEmailEvent(TreeItem<Component> item) {
        System.out.println("Selected Text : " + item.getValue());
        folderManager.printFolderContent((Folder) item.getValue());
    }
}
