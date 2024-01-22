package chatsystem.ui;

import chatsystem.MainClass;
import chatsystem.contacts.User;
import chatsystem.network.TCPClient;
import chatsystem.network.TCPMessage;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;


public class ChatWindow2 extends JPanel{
    private JTextArea messageArea;
    private JTextField messageInputField;
    private TCPClient tcpClient;
    private User user;

    public ChatWindow2(User user){
        this.user=user;
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
                displayText = this.user.getNickname() + " (" + senderIP + "): "; // Use user's nickname
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

                String toUserIP = user.getIpAddress().getHostAddress(); //"192.168.1.1"; //in the meantime i put a random fake address

                    TCPMessage message = new TCPMessage(messageContent, now, fromUserIP, toUserIP);
                String serializedMessage = tcpClient.serializeMessage(message);
                tcpClient.sendMessage(message);
                displayReceivedMessage(message);
                messageInputField.setText("");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

}
