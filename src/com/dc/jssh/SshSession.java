package com.dc.jssh;

import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import net.schmizz.sshj.SSHClient;
import net.schmizz.sshj.common.LoggerFactory;
import net.schmizz.sshj.common.StreamCopier;
import net.schmizz.sshj.connection.channel.direct.Session;
import net.schmizz.sshj.transport.verification.PromiscuousVerifier;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class SshSession {
    private String hostname;
    private SSHClient ssh;
    private OutputStream output;
    private InputStream input;

    public SshSession(String hostname, OutputStream output, InputStream input) {
        this.hostname = hostname;
        this.ssh = new SSHClient();
        this.output = output;
        this.input = input;
    }

    public void connect() throws IOException {
        ssh.loadKnownHosts();
        ssh.addHostKeyVerifier(new PromiscuousVerifier());
        ssh.connect(hostname);
        ssh.authPassword(System.getProperty("user.name").toLowerCase(), "ndoqwhb");
        try (Session session = ssh.startSession()) {
            session.allocateDefaultPTY();
            final Session.Shell shell = session.startShell();

            new StreamCopier(shell.getInputStream(), output, LoggerFactory.DEFAULT)
                    .bufSize(shell.getLocalMaxPacketSize())
                    .spawn("stdout");

            new StreamCopier(shell.getErrorStream(), output, LoggerFactory.DEFAULT)
                    .bufSize(shell.getLocalMaxPacketSize())
                    .spawn("stderr");

            // Now make System.in act as stdin. To exit, hit Ctrl+D (since that results in an EOF on System.in)
            // This is kinda messy because java only allows console input after you hit return
            // But this is just an example... a GUI app could implement a proper PTY
            new StreamCopier(input, shell.getOutputStream(), LoggerFactory.DEFAULT)
                    .bufSize(shell.getRemoteMaxPacketSize())
                    .copy();
        }
    }

    public void disconnect() throws IOException {
        ssh.disconnect();
    }
}
