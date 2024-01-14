package chatsystem.view;

import chatsystem.network.TCPMessage;

import javax.swing.*;
import java.awt.*;

public class ChatWindow {

    private JFrame frame;
    private JTextArea messageArea;

    public ChatWindow() {
        // Create and set up the window
        frame = new JFrame("Chat Window");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 300);

        // Create a text area to display messages
        messageArea = new JTextArea();
        messageArea.setEditable(false); // Make it read-only
        messageArea.setLineWrap(true);
        messageArea.setWrapStyleWord(true);

        // Add a scroll pane to the text area
        JScrollPane scrollPane = new JScrollPane(messageArea);

        // Add components to the frame
        frame.add(scrollPane, BorderLayout.CENTER);

        // Display the window
        frame.setVisible(true);
    }

    public void displayNewMessage(TCPMessage message) {
        // Append the new message to the text area
        // You might want to format the message (e.g., show sender, timestamp)
        messageArea.append("From " + message.getFromUserIP() + ": " + message.getContent() + "\n");
    }

    // Additional methods if needed...
}
