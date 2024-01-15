package chatsystem.ui;

import chatsystem.network.TCPClient;
import chatsystem.network.TCPMessage;
import chatsystem.network.TCPServer;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class ChatWindow {
    private JFrame frame;
    private JTextArea messageArea;
    private String myUserIP; // IP address of the current user

    private TCPClient tcpClient;

    private JTextField messageInputField;
    public ChatWindow(String myUserIP, TCPClient tcpClient) {
        this.myUserIP = myUserIP;
        this.tcpClient=tcpClient;
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


        messageInputField = new JTextField();
        frame.add(messageInputField,BorderLayout.SOUTH);

        JButton sendButton = new JButton("Send");
        sendButton.addActionListener(e->sendMessage());
        frame.add(sendButton,BorderLayout.EAST);

        frame.setVisible(true);
    }

    private void sendMessage(){
        String message = messageInputField.getText();
        if(!message.isEmpty()){
            try{
                tcpClient.sendMessage(message);
                displayMessage(new TCPMessage(message,"date",myUserIP, "IP"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void displayMessage(TCPMessage message) {
        if (message.getFromUserIP().equals(myUserIP)) {
            messageArea.append("Me: " + message.getContent() + "\n");
        } else {
            messageArea.append("From " + message.getFromUserIP() + ": " + message.getContent() + "\n");
        }
    }
}
