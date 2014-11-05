/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.bht.fpa.mail.s798972.controller;

import java.io.File;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.stage.Stage;
import javafx.stage.Window;

public class BaseDirHistoryViewController implements Initializable {

    @FXML
    private Button cancelButton;
    @FXML
    private Button okButton;
    @FXML
    private ListView<File> listView;
    
    private final MainViewController mainController;

    public BaseDirHistoryViewController(MainViewController mainViewC) {
        mainController = mainViewC;
    }

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cancelButton.setOnAction((ActionEvent event) -> handleButtonEvent(event));
        okButton.setOnAction((ActionEvent event) -> handleButtonEvent(event));
        
        fillList();
    }
    
    private void fillList(){
        listView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        listView.getItems().removeAll(listView.getItems());
        
        List<File> listHistory = mainController.getHistoryList();
        if (listHistory.isEmpty()) {
            listView.getItems().add(new File("now base directories in history"));
            okButton.setDisable(true);
        }else{
            listView.getItems().addAll(listHistory);
        }
    }

    private void close(Window w) {
        Stage stage = (Stage) w;
        stage.close();
    }

    private static final String BUTTON_OK = "okButton";
    private static final String BUTTON_CANCEL = "cancelButton";

    private void handleButtonEvent(ActionEvent event) {
        final Button b = (Button) event.getSource();

        switch (b.getId()) {
            case BUTTON_OK: {
                mainController.setRootItemTreeView(listView.getSelectionModel().getSelectedItem());
                close(okButton.getScene().getWindow());
            }
            case BUTTON_CANCEL: {
                close(cancelButton.getScene().getWindow());
            }
        }
    }
}
