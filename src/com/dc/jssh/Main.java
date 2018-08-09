package com.dc.jssh;

import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.IOException;

public class Main  extends Application {
    @Override
    public void start(Stage primaryStage) throws IOException {
        Parent root = new GridPane();
        primaryStage.setTitle("JSSH");
        primaryStage.setScene(new Scene(root, 300, 275));
        primaryStage.show();


        SshSession session = new SshSession("192.168.1.2");
        session.connect();
        session.disconnect();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
