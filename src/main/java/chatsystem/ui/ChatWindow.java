package chatsystem.ui;

import chatsystem.network.TCPMessage;
import javax.swing.*;
import java.awt.*;

public class ChatWindow {
    private JFrame frame;
    private JTextArea messageArea;
    private String myUserIP; // IP address of the current user

    public ChatWindow(String myUserIP) {
        this.myUserIP = myUserIP;
        createAndShowGUI();
    }

    private void createAndShowGUI() {
        frame = new JFrame("Chat System");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 400);

        messageArea = new JTextArea();
        messageArea.setEditable(false);
        messageArea.setLineWrap(true);
        messageArea.setWrapStyleWord(true);

        JScrollPane scrollPane = new JScrollPane(messageArea);
        frame.add(scrollPane, BorderLayout.CENTER);

        frame.setVisible(true);
    }

    public void displayMessage(TCPMessage message) {
        if (message.getFromUserIP().equals(myUserIP)) {
            messageArea.append("Me: " + message.getContent() + "\n");
        } else {
            messageArea.append("From " + message.getFromUserIP() + ": " + message.getContent() + "\n");
        }
    }
}
