package com.chengsoft.tutorial;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.text.Font;
import javafx.stage.Stage;

/**
 * Created by Tim on 2/14/2016.
 */
public class Chapter_4 extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        Label message = new Label("Hello, JavaFX!");
        message.setFont(new Font(80));
        stage.setScene(new Scene(message));
        stage.setTitle("Hello");
        stage.show();
    }
}