package com.dc.jssh;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.UIKeyboardInteractive;
import com.jcraft.jsch.UserInfo;
import javax.swing.*;

public class Main {
    public static void main(String[] arg) throws Exception {
        JSch jsch = new JSch();

        jsch.setKnownHosts(System.getProperty("user.home") + "/.ssh/known_hosts");
        jsch.addIdentity(System.getProperty("user.home") + "/.ssh/id_rsa");

        String host;
        if (arg.length > 0) {
            host = arg[0];
        } else {
            host = "corbettd@se-61.wbe-01.traveljigsaw.com";
        }
        String user = host.substring(0, host.indexOf('@'));
        host = host.substring(host.indexOf('@') + 1);

        Session session = jsch.getSession(user, host, 22);

        UserInfo ui = new MyUserInfo() {
            @Override
            public void showMessage(String message) {
                JOptionPane.showMessageDialog(null, message);
            }

            @Override
            public boolean promptYesNo(String message) {
                Object[] options = {"yes", "no"};
                int foo = JOptionPane.showOptionDialog(null,
                        message,
                        "Warning",
                        JOptionPane.DEFAULT_OPTION,
                        JOptionPane.WARNING_MESSAGE,
                        null, options, options[0]);
                return foo == 0;
            }
        };

        session.setUserInfo(ui);

        session.connect(30000);   // making a connection with timeout.

        Channel channel = session.openChannel("shell");

        new SshWindow(channel);
        channel.connect(3 * 1000);
    }

    public static abstract class MyUserInfo
            implements UserInfo, UIKeyboardInteractive {
        @Override
        public String getPassword() {
            return null;
        }

        @Override
        public boolean promptYesNo(String str) {
            return false;
        }

        @Override
        public String getPassphrase() {
            return null;
        }

        @Override
        public boolean promptPassphrase(String message) {
            return false;
        }

        @Override
        public boolean promptPassword(String message) {
            return false;
        }

        @Override
        public void showMessage(String message) {
        }

        @Override
        public String[] promptKeyboardInteractive(String destination,
                                                  String name,
                                                  String instruction,
                                                  String[] prompt,
                                                  boolean[] echo) {
            return null;
        }
    }
}
