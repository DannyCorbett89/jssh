package com.dc.jssh;

import java.io.IOException;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class Main  extends Application {
    @Override
    public void start(Stage primaryStage) throws IOException {
        GridPane root = new GridPane();
        primaryStage.setTitle("JSSH");
        primaryStage.setScene(new Scene(root, 300, 275));
        try (SshTextArea sshTextArea = new SshTextArea();
             SshInput sshInput = new SshInput()) {
            root.add(sshTextArea.getTextArea(), 0, 0);
            root.add(sshInput.getTextField(), 0, 1);
            primaryStage.show();

            Platform.runLater(() -> {
                SshSession session = new SshSession("se-35.wbe-01.traveljigsaw.com", sshTextArea, sshInput);
                try {
                    session.connect();
                    session.disconnect();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }

    }


    public static void main(String[] args) {
        launch(args);
    }
}
