package com.chengsoft.tutorial;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ResourceBundle;

/**
 * Created by Tim on 2/14/2016.
 */
public class Chapter_4 extends Application implements Initializable {
    @FXML private Label labelFileA;
    @FXML private Label labelFileB;
    @FXML private TextField textFieldFileA;
    @FXML private TextField textFieldFileB;
    @FXML private Button buttonOk;
    @FXML private Button buttonCancel;

//    public static void main(String[] args) {launch(args);}

    @Override
    public void start(Stage stage) throws Exception {
        // Hello World
//        Label message = new Label("Hello, JavaFX!");
//        message.setFont(new Font(80));
//        stage.setScene(new Scene(message));
        stage.setTitle("Hello");

        Path path = Paths.get("src/test/resources/chapter_4.fxml");
        Parent root = FXMLLoader.load(path.toUri().toURL());
        stage.setScene(new Scene(root));
        stage.show();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
//        textFieldFileA.setOnKeyTyped(event -> System.out.println(event.getText()));
        buttonOk.setOnAction(event -> textFieldFileA.setText("test"));
    }
}