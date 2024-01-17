package chatsystem.ui;

import chatsystem.network.TCPClient;
import chatsystem.network.TCPMessage;
import chatsystem.network.TCPServer;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ChatWindow implements TCPServer.MessageObserver{
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

    @Override
    public void handleMessage(TCPMessage msg) {
        System.out.println("DANS HANDLEMESSAGE My IP: " + myUserIP + ", From IP: " + msg.getFromUserIP());
        SwingUtilities.invokeLater(() -> displayMessage(msg));
    }

    private void sendMessage(){
        String message = messageInputField.getText();
        if(!message.isEmpty()){
            try{
                // Create a properly formatted string for serialization
                String serializedData = message + "," + getCurrentDate() + "," + myUserIP + "," + "RecipientIP"; // Replace "RecipientIP" as needed
                TCPMessage tcpMessage = TCPMessage.deserialize(serializedData);

                tcpClient.sendMessage(tcpMessage);
                System.out.println("DANS SENDMESSAGE My IP: " + myUserIP + ", From IP: " + tcpMessage.getFromUserIP());
                displayMessage(tcpMessage);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private String getCurrentDate() {
        // Format the date as required. This is just an example.
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return dateFormat.format(new Date());
    }

    public void displayMessage(TCPMessage message) {
        System.out.println("My IP: " + myUserIP + ", From IP: " + message.getFromUserIP());

        if (message.getFromUserIP().equals(myUserIP)) {
            System.out.println("Displaying my message: " + message.getContent());
            messageArea.append("Me: " + message.getContent() + "\n");
        } else {
            System.out.println("Displaying message: " + message.getContent());
            messageArea.append("From " + message.getFromUserIP() + ": " + message.getContent() + "\n");
        }
    }

    public void setVisible(boolean visible) {
        frame.setVisible(visible);
    }

}
