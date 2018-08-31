package com.dc.jssh;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.io.PrintStream;
import java.util.Properties;
import javax.swing.*;

public class Main {
    private final JButton sendButton = new JButton("Send CMD");
    private JTextField sendField;
    private PrintStream print;
    private PipedInputStream pip;
    private PipedOutputStream pop;

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                new Main();
            } catch (Exception e) {
                System.out.print("failed to run");
                e.printStackTrace();
            }
        });
    }

    private Main() throws JSchException, IOException {
        buildGUI();
        connectSSH();
        actionListeners();
    }

    private void buildGUI() throws IOException {

        JFrame mainFrame = new JFrame();
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setBounds(100, 100, 900, 900);
        mainFrame.getContentPane().setLayout(null);
        JPanel panel = new JPanel();
        panel.setBounds(10, 11, 864, 699);
        mainFrame.getContentPane().add(panel);
        mainFrame.setVisible(true);
        panel.setLayout(null);
        sendField = new JTextField();
        sendField.setText("Enter Command Then Click Send");
        sendField.setBounds(565, 261, 299, 169);
        panel.add(sendField);
        sendField.setColumns(10);
        panel.add(sendField);
        panel.add(sendButton);
        sendButton.setBounds(565, 441, 81, 23);
        JTextArea ta = new JTextArea(50, 50);

        // TODO: https://stackoverflow.com/questions/44104355/jsch-interactive-ssh-console-in-a-swing-gui
//        SshTextArea taos = new SshTextArea(ta, 60);


        pip = new PipedInputStream(40);
//            this.channel.setInputStream(pip);
//
        pop = new PipedOutputStream(pip);
        print = new PrintStream(pop);
        //this.channel.setOutputStream(System.out);
        //System.out.println(send);

        JScrollPane scroll = new JScrollPane(ta);
        panel.add(scroll);
        scroll.setBounds(132, 5, 423, 906);
        scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        scroll.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);

    }

    public void connectSSH() throws JSchException {
        JSch jsch = new JSch();
        jsch.setKnownHosts(System.getProperty("user.home") + "/.ssh/known_hosts");
        jsch.addIdentity(System.getProperty("user.home") + "/.ssh/id_rsa");

        String host = "se-dev-01.dqs.traveljigsaw.com";
        String user = "corbettd";

        Session session = jsch.getSession(user, host, 22);

        Properties config = new Properties();
        config.put("StrictHostKeyChecking", "no");
        session.setConfig(config);
        session.connect();

        Channel channel = session.openChannel("shell");
        channel.setInputStream(pip);
        channel.setOutputStream(System.out);

        channel.connect();
    }

    private void actionListeners() {
        SshListener listener = new SshListener();
        sendField.addKeyListener(listener);
        sendButton.addActionListener(listener);
    }

    class SshListener implements ActionListener, KeyListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            sendText();
        }

        @Override
        public void keyReleased(KeyEvent e) {
            if (e.getKeyChar() == '\n') {
                sendText();
            }
        }

        private void sendText() {
            print.println(sendField.getText());
            sendField.setText("");
        }

        @Override
        public void keyTyped(KeyEvent e) {
        }

        @Override
        public void keyPressed(KeyEvent e) {
        }
    }
}
