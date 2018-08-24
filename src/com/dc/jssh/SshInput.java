package com.dc.jssh;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.io.InputStream;
import javax.swing.*;

public class SshInput extends InputStream {
    private JTextField textField;
    byte[] contents = new byte[0];
    int pointer = 0;

    public SshInput() {
        textField = new JTextField(20);
        textField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                if (e.getKeyChar() == '\n') {
                    contents = textField.getText().concat("\n").getBytes();
                    pointer = 0;
                    textField.setText("");
                }
                super.keyReleased(e);
            }
        });
    }

    @Override
    public int read() throws IOException {
        if (pointer >= contents.length) return 0;
        return contents[pointer++];
    }

    public JTextField getTextField() {
        return textField;
    }
}
