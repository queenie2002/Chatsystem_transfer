/*package chatsystem.view;

import chatsystem.network.UDPSender;
import chatsystem.network.UDPReceiver;

import java.awt.*;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class HomeTab {

    public HomeTab(UDPReceiver r, UDPSender s) {

        // Create and set up the window
        JFrame frame = new JFrame("Home");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


        //Create buttons
        JButton button_startChat = new JButton("Start A New TCPMessage");
        button_startChat.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //------------------------------------------------------SEND SOMEWHERE ELSE
                frame.dispose();
            }
        });

        JButton button_seeHistory = new JButton("See History");
        button_seeHistory.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //-------------------------------------------------------SEND SOMEWHERE ELSE
                frame.dispose();
            }
        });


        JPanel p = new JPanel(new GridLayout(1, 2));
        p.add(button_startChat);
        p.add(button_seeHistory);
        frame.add(p);
        frame.setLocationRelativeTo(null); //center the JFrame on the screen
        frame.setSize(600, 100);

        // Display the window.
        frame.setVisible(true);
    }

}*/

package chatsystem.ui;

import chatsystem.contacts.ContactList;
import chatsystem.contacts.User;
import chatsystem.network.UDPSender;
import chatsystem.network.UDPReceiver;
import chatsystem.network.TCPClient;
import chatsystem.MainClass;

import java.awt.*;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;

public class HomeTab {

    private UDPReceiver udpReceiver;
    private UDPSender udpSender;

    public HomeTab(UDPReceiver r, UDPSender s) {
        this.udpReceiver = r;
        this.udpSender = s;

        // Create and set up the window
        JFrame frame = new JFrame("Home");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Create buttons
        JButton button_startChat = new JButton("Start A New TCPMessage");
        button_startChat.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showOnlineContacts();
                frame.dispose();
            }
        });

        JButton button_seeHistory = new JButton("See History");
        button_seeHistory.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Code to see history
                frame.dispose();
            }
        });

        JPanel p = new JPanel(new GridLayout(1, 2));
        p.add(button_startChat);
        p.add(button_seeHistory);
        frame.add(p);
        frame.setLocationRelativeTo(null); // Center the JFrame on the screen
        frame.setSize(600, 100);

        // Display the window.
        frame.setVisible(true);
    }

    private void showOnlineContacts() {
        ArrayList<User> onlineUsers = ContactList.getInstance().getConnectedContactsList();

        JFrame contactFrame = new JFrame("Online Contacts");
        contactFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        for (User user : onlineUsers) {
            JButton userButton = new JButton(user.getNickname());
            userButton.addActionListener(e -> startTcpSession(user));
            panel.add(userButton);
        }

        contactFrame.add(new JScrollPane(panel), BorderLayout.CENTER);
        contactFrame.pack();
        contactFrame.setLocationRelativeTo(null); // Center the JFrame on the screen
        contactFrame.setVisible(true);
    }

    private void startTcpSession(User user) {
        System.out.println("startTCPSession with user : "+user);
        TCPClient tcpClient = new TCPClient();
        try {
            String localUserIP = MainClass.me.getIpAddress().getHostAddress();
            tcpClient.startConnection(user.getIpAddress().getHostAddress(), MainClass.TCP_SERVER_PORT);
            ChatWindow cw = new ChatWindow(localUserIP, tcpClient);
            // Open a chat window or handle the session as needed
        } catch (IOException ex) {
            ex.printStackTrace(); // Handle connection errors here
        }
    }
}
