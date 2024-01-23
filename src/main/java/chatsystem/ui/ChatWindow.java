package chatsystem.ui;

import chatsystem.MainClass;
import chatsystem.contacts.User;
import chatsystem.database.DatabaseMethods;
import chatsystem.network.TCPClient;
import chatsystem.network.TCPMessage;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.InetAddress;
import java.time.Instant;
import java.util.ArrayList;


public class ChatWindow extends JPanel{
    private JTextArea messageArea;
    private JTextField messageInputField;
    private TCPClient tcpClient;
    private User user;

    public ChatWindow(User user){
        this.user=user;

        displayMessageHistory();


        setLayout(new BorderLayout());

        messageArea = new JTextArea(10,30);
        messageArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(messageArea);
        add(scrollPane,BorderLayout.CENTER);

        messageInputField = new JTextField();
        add(messageInputField,BorderLayout.SOUTH);

        JButton sendButton = new JButton("Send");
        sendButton.addActionListener(e->sendMessage());
        add(sendButton, BorderLayout.EAST);

        this.tcpClient = new TCPClient();
        try{
            tcpClient.startConnection(user.getIpAddress().getHostAddress(), MainClass.TCP_SERVER_PORT);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void displayReceivedMessage(TCPMessage message) {
        SwingUtilities.invokeLater(() -> {
            String senderIP = message.getFromUserIP();
            String displayText;
            if (senderIP.equals(MainClass.me.getIpAddress().getHostAddress())) {
                displayText = "Me: ";
            } else {
                displayText = this.user.getNickname() + ": " ;
            }
            displayText += message.getContent();
            messageArea.append(displayText + "\n");
            messageArea.setCaretPosition(messageArea.getDocument().getLength());
        });
    }

    private void sendMessage() {
        String messageContent = messageInputField.getText().trim();
        if (!messageContent.isEmpty()) {
            try {

                Instant now = Instant.now();

                String fromUserIP = MainClass.me.getIpAddress().getHostAddress();

                String toUserIP = user.getIpAddress().getHostAddress();

                TCPMessage message = new TCPMessage(messageContent, now, fromUserIP, toUserIP);
                String serializedMessage = tcpClient.serializeMessage(message);
                tcpClient.sendMessage(message);
                // Add the sent message to the database
                DatabaseMethods.addMessage(message);
                displayReceivedMessage(message);
                messageInputField.setText("");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void displayMessageHistory() {
        try {
            InetAddress myIP = MainClass.me.getIpAddress();
            InetAddress userIP = user.getIpAddress();

            ArrayList<TCPMessage> messageHistory = DatabaseMethods.getMessagesList(userIP);
            for (TCPMessage message : messageHistory) {
                displayReceivedMessage(message);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
