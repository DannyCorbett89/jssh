package com.dc.jssh;

import com.jcraft.jsch.Channel;
import java.awt.*;
import javax.swing.*;

public class SshWindow extends JFrame {
    private SshTextArea output;
    private SshInput input;

    public SshWindow(Channel channel) {
        super("SSH");
        setLocationRelativeTo(null);
        setSize(400, 400);
        setLayout(new FlowLayout());

        output = new SshTextArea();
        channel.setOutputStream(output);
        add(output.getTextArea());

        input = new SshInput();
        channel.setInputStream(input);
        add(input.getTextField());

        setVisible(true);
    }
}
