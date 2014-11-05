/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.bht.fpa.mail.s798972.controller;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.stage.Stage;
import javafx.stage.Window;

/**
 * FXML Controller class
 *
 * @author lukas
 */
public class BaseDirHistoryViewController implements Initializable {

    @FXML
    private Button cancelButton;
    private Button okButton;
    private ListView<File> list;

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
        
        list.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        list.getItems().setAll(mainController.getRootList());
    }

    private void close(Window w) {
        Stage stage = (Stage) okButton.getScene().getWindow();
    // do what you have to do
    stage.close();
    }

    final String btnOK = "okButton";
    final String btnCancel = "cancelButton";

    private void handleButtonEvent(ActionEvent event) {
        System.out.print("sasdf");
        Button b = (Button) event.getSource();
        System.out.println(b.getId());


        switch (b.getId()) {
            case btnOK: {
                list.getSelectionModel().getSelectedItem();
                mainController.setRootItemTreeView(
                        new File("/..."));
                Platform.exit();
            }
            case btnCancel: {
                close(cancelButton.getScene().getWindow());
            }
        }
    }
}
