package com.dc.jssh;

import java.io.OutputStream;
import javax.swing.*;

public class SshTextArea extends OutputStream {
    private JTextArea textArea;
    private StringBuilder buffer;

    public SshTextArea() {
        textArea = new JTextArea();
        buffer = new StringBuilder(128);
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
