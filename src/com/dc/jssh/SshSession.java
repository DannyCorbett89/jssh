package com.dc.jssh;

import net.schmizz.sshj.SSHClient;
import net.schmizz.sshj.common.LoggerFactory;
import net.schmizz.sshj.common.StreamCopier;
import net.schmizz.sshj.connection.channel.direct.Session;
import net.schmizz.sshj.transport.verification.PromiscuousVerifier;

import java.io.IOException;

public class SshSession {
    private String hostname;
    private SSHClient ssh;

    public SshSession(String hostname) {
        this.hostname = hostname;
        this.ssh = new SSHClient();
    }

    public void connect() throws IOException {
        ssh.loadKnownHosts();
        ssh.addHostKeyVerifier(new PromiscuousVerifier());
        ssh.connect(hostname);
        ssh.authPassword(System.getProperty("user.name").toLowerCase(), "ndoqwhb");
        try (Session session = ssh.startSession()) {
            session.allocateDefaultPTY();
            final Session.Shell shell = session.startShell();

            new StreamCopier(shell.getInputStream(), System.out, LoggerFactory.DEFAULT)
                    .bufSize(shell.getLocalMaxPacketSize())
                    .spawn("stdout");

            new StreamCopier(shell.getErrorStream(), System.err, LoggerFactory.DEFAULT)
                    .bufSize(shell.getLocalMaxPacketSize())
                    .spawn("stderr");

            // Now make System.in act as stdin. To exit, hit Ctrl+D (since that results in an EOF on System.in)
            // This is kinda messy because java only allows console input after you hit return
            // But this is just an example... a GUI app could implement a proper PTY
            new StreamCopier(System.in, shell.getOutputStream(), LoggerFactory.DEFAULT)
                    .bufSize(shell.getRemoteMaxPacketSize())
                    .copy();
        }
    }

    public void disconnect() throws IOException {
        ssh.disconnect();
    }
}
