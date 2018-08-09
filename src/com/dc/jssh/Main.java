package com.dc.jssh;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.IOException;

public class Main  extends Application {
    @Override
    public void start(Stage primaryStage) throws IOException {
        GridPane root = new GridPane();
        primaryStage.setTitle("JSSH");
        primaryStage.setScene(new Scene(root, 300, 275));
        SshTextArea sshTextArea = new SshTextArea();
        root.add(sshTextArea.getTextArea(), 0, 0);
        SshInput sshInput = new SshInput();
        root.add(sshInput.getTextField(), 0, 1);
        primaryStage.show();

        Platform.runLater(() -> {
            SshSession session = new SshSession("192.168.1.2", sshTextArea, sshInput);
            try {
                session.connect();
                session.disconnect();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

    }


    public static void main(String[] args) {
        launch(args);
    }
}
