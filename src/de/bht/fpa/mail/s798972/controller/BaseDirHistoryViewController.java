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

/**
 * Controller for base directories history list
 *
 * @author Lukas Abegg, S53647, FPA - Beuth Hochschule
 * @version 1.0
 */
public class BaseDirHistoryViewController implements Initializable {

    @FXML
    private Button cancelButton;
    @FXML
    private Button okButton;
    @FXML
    private ListView<File> listView; /* all root folder chosen in history */

    private final MainViewController mainController; /* MainView controller object */

    private static final String BUTTON_OK = "okButton"; /* constant for Action Command Button OK */

    private static final String BUTTON_CANCEL = "cancelButton"; /* constant for Action Command Button Cancel */


    /**
     * Constructor method
     *
     * @param mainViewC MainView controller object
     */
    public BaseDirHistoryViewController(MainViewController mainViewC) {
        mainController = mainViewC;
    }

    /**
     * Initiate list event handler and fill history list
     *
     * @param url application URL
     * @param rb bundle for handling language translations
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cancelButton.setOnAction((ActionEvent event) -> handleButtonEvent(event));
        okButton.setOnAction((ActionEvent event) -> handleButtonEvent(event));

        fillList(); /* fill history list */

    }

    /**
     * Method to fill history list with all chosen root files
     */
    private void fillList() {
        listView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE); /* set select single mode in list */

        listView.getItems().removeAll(listView.getItems()); /* remove all old components first */

        /* ger list from main application and fill it*/
        List<File> listHistory = mainController.getHistoryList();
        if (listHistory.isEmpty()) {
            listView.getItems().add(new File("now base directories in history"));
            okButton.setDisable(true);
        } else {
            listView.getItems().addAll(listHistory);
        }
    }

    /* close action for view instance */
    private void close(Window w) {
        Stage stage = (Stage) w;
        stage.close();
    }

    /**
     * Handle all button events in view
     *
     * @param event ActionEvent to find ID of clicked button
     */
    private void handleButtonEvent(ActionEvent event) {
        final Button b = (Button) event.getSource();

        /* handle different button click events */
        switch (b.getId()) {
            /* file is chosen, set new root in main application tree view and close this view */
            case BUTTON_OK: {
                mainController.configureTree(listView.getSelectionModel().getSelectedItem());
                close(okButton.getScene().getWindow());
            }
            /* no file chosen, close this view */
            case BUTTON_CANCEL: {
                close(cancelButton.getScene().getWindow());
            }
            default:
                break; /* else, do nothing */

        }
    }
}
