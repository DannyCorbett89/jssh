package com.dc.jssh;

import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import javax.swing.*;

public class SshTextArea extends PipedOutputStream {
    private JTextArea textArea;
    private StringBuilder buffer;
    private PipedInputStream input;

    public SshTextArea() {
        textArea = new JTextArea();
        buffer = new StringBuilder(128);
    }

    public SshTextArea(JTextArea textArea, PipedInputStream input, int capacity) throws IOException {
        super(input);
        this.textArea = textArea;
        buffer = new StringBuilder(capacity);
    }

    @Override
    public void write(int b) {
        char c = (char) b;

        if (c == '\u0007') {
            return;
        }
        String value = Character.toString(c);
        buffer.append(value);
        if (value.equals("\n") || b == 27) {
            textArea.append(buffer.toString());
            buffer.delete(0, buffer.length());
        }
    }

    public JTextArea getTextArea() {
        return textArea;
    }
}
