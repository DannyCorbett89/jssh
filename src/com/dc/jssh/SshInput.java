package com.dc.jssh;

import javafx.scene.control.TextField;

import java.io.IOException;
import java.io.InputStream;

public class SshInput extends InputStream {
    private TextField textField;

    public SshInput() {
        textField = new TextField();
    }

    @Override
    public int read() throws IOException {
        return 0;
    }

    public TextField getTextField() {
        return textField;
    }
}
