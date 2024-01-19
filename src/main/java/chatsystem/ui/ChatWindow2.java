package chatsystem.ui;

import chatsystem.MainClass;
import chatsystem.network.TCPClient;
import chatsystem.network.TCPMessage;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;


public class ChatWindow2 extends JPanel{
    private JTextArea messageArea;
    private JTextField messageInputField;
    private TCPClient tcpClient;

    public ChatWindow2(String userIP){
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
            tcpClient.startConnection(userIP, MainClass.TCP_SERVER_PORT);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void displayReceivedMessage(TCPMessage message) {
        SwingUtilities.invokeLater(() -> {
            String senderIP = message.getFromUserIP();
            String displayText = senderIP.equals(MainClass.me.getIpAddress().getHostAddress())
                    ? "Me: "
                    : senderIP + ": ";  // Use sender's IP address
            displayText += message.getContent();
            messageArea.append(displayText + "\n");
            messageArea.setCaretPosition(messageArea.getDocument().getLength());
        });
    }
    private void sendMessage() {
        String messageContent = messageInputField.getText().trim();
        if (!messageContent.isEmpty()) {
            try {
                // Current date and time
                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String currentDate = dateFormat.format(new Date());

                String fromUserIP = MainClass.me.getIpAddress().getHostAddress();

                String toUserIP = "192.168.1.1"; //in the meantime i put a random fake address

                    TCPMessage message = new TCPMessage(messageContent, currentDate, fromUserIP, toUserIP);
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
