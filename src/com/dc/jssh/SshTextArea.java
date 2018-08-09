package com.dc.jssh;

import javafx.scene.control.Label;
import javafx.scene.control.TextArea;

import java.io.IOException;
import java.io.OutputStream;

public class SshTextArea extends OutputStream {
    private TextArea textArea;

    public SshTextArea() {
        textArea = new TextArea();
    }

    @Override
    public void write(int b) throws IOException {
        textArea.appendText(String.valueOf((char)b));
        textArea.end();
    }

    public TextArea getTextArea() {
        return textArea;
    }
}
