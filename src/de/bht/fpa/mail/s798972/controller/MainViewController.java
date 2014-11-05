package de.bht.fpa.mail.s798972.controller;

import de.bht.fpa.mail.s798972.model.applicationLogic.FileManager;
import de.bht.fpa.mail.s798972.model.applicationLogic.FolderManagerIF;
import de.bht.fpa.mail.s798972.model.data.Component;
import de.bht.fpa.mail.s798972.model.data.Folder;
import java.io.File;
import java.io.IOException;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
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

    private FolderManagerIF folderManager;

    private final Image folderIcon = new Image(getClass().getResourceAsStream("folder_icon.png"));

    private final TreeItem<Component> dummyItem = new TreeItem<>(new Folder(new File("/"), true));

    private final File defaultRoot = new File("/Users");
    private final List<File> rootList = new ArrayList<>();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        configureTree();
        configureMenue();
    }

    public void configureTree() {
        setRootItemTreeView(defaultRoot);
    }

    /**
     *
     * @param rootPath
     */
    public void setRootItemTreeView(File rootPath) {
        folderManager = new FileManager(rootPath);
        TreeItem<Component> rootItem = new TreeItem<>(folderManager.getTopFodler(), new ImageView(folderIcon));
        rootItem.setExpanded(true);
        rootItem.addEventHandler(TreeItem.branchExpandedEvent(), (TreeModificationEvent<Component> e) -> handleTreeItemExpandedEvent(e));
        explorerTreeView.setRoot(rootItem);
        
        if(rootPath != defaultRoot){
            rootList.add(rootPath);
        }

        loadTreeItemContent(rootItem);
    }

    private void loadTreeItemContent(TreeItem<Component> node) {
        Folder folder = (Folder) node.getValue();
        folderManager.loadContent(folder);

        for (Component subFolder : folder.getComponents()) {
            TreeItem<Component> subItem = new TreeItem<>(subFolder, new ImageView(folderIcon));
            if (subFolder.isExpandable()) {
                subItem.getChildren().add(dummyItem);
            }
            node.getChildren().add(subItem);
        }
    }

    private void handleTreeItemExpandedEvent(TreeModificationEvent<Component> event) {
        TreeItem<Component> expandItem = event.getTreeItem();

        System.out.println("-------- load childeren of item: " + expandItem.getValue().getName());
        expandItem.getChildren().removeAll(expandItem.getChildren().sorted());
        loadTreeItemContent(expandItem);
    }

    public void configureMenue() {
        for (Menu m : menuBar.getMenus()) {
            for (MenuItem item : m.getItems()) {
                item.setOnAction((event) -> handleMenuItemEvent(event));
            }
        }
    }

    final String menuIdFileOpen = "fileOpen";
    final String menuIdFileHistory = "fileHistory";

    private void handleMenuItemEvent(Event event) {
        final MenuItem item = (MenuItem) event.getSource();

        switch (item.getId()) {
            case menuIdFileOpen: {
                File file = openFile();
                if (file != null) {
                    System.out.println("Root Item set on: " + file.getAbsolutePath());
                    setRootItemTreeView(file);
                }
                break;
            }
            case menuIdFileHistory: {
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
        try {
            Pane myPane = (Pane) fxmlLoader.load();
            Scene myScene = new Scene(myPane);
            editStage.setScene(myScene);
            editStage.show();
        } catch (IOException ex) {
            Logger.getLogger(MainViewController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public List<File> getRootList(){
        return rootList;
    }
}
