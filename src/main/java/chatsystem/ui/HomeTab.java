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

/*
package chatsystem.ui;

import chatsystem.contacts.*;
import chatsystem.network.TCPClient;
import chatsystem.MainClass;
import chatsystem.observers.MyObserver;

import java.awt.*;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;

public class HomeTab {

    private ArrayList<MyObserver> observers = new ArrayList<>();

    public void addObserver(MyObserver observer) {
        observers.add(observer);
    }



    public HomeTab() {
        //addObserver(() -> {showOnlineContacts();});
        //addObserver(() -> {showAllContacts();});


        // Create and set up the window
        JFrame frame = new JFrame("Home");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Create buttons
        JButton button_startChat = new JButton("Start A New TCPMessage");
        button_startChat.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {


                for (MyObserver observer : observers) {
                    observer.showOnlineContacts();
                }



                showOnlineContacts();
                frame.dispose();
            }
        });

        JButton button_seeHistory = new JButton("See Past Chats");
        button_seeHistory.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                showAllContacts();
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

    private void showContact(ArrayList<User> userList, String titleFrame) {

        JFrame contactFrame = new JFrame(titleFrame);
        contactFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        for (User user : userList) {
            JButton userButton = new JButton(user.getNickname());
            panel.add(userButton);
        }

        contactFrame.add(new JScrollPane(panel), BorderLayout.CENTER);
        contactFrame.pack();
        contactFrame.setLocationRelativeTo(null);
        contactFrame.setVisible(true);
    }

    private void showOnlineContacts2() {
        ArrayList<User> onlineUsers = ContactList.getInstance().getConnectedContactsList();
        showContact(onlineUsers, "Online Contacts");

        //doit lancer tcpsession avec tout le monde mais initialize window aue si on appuie sur  le bouton, connecter le bouton a initialize window
        for (User user : onlineUsers) {
            startTcpSession(user);
        }
    }

    private void showAllContacts() {
        ArrayList<User> allUsers = ContactList.getInstance().getContactList();
        showContact(allUsers, "Chat History");


        //doit ouvrir initializiechatwindow que si on appuie sur le bouton, et lance une tcpsession seulement sii status true
        for (User user : allUsers) {
            if (user.getStatus()) {

                //startTcpSession(user);
            }
        }
    }



    private void startTcpSession(User user) {
        System.out.println("startTCPSession with user : "+user);
        TCPClient tcpClient = new TCPClient();
        try {
            String localUserIP = MainClass.me.getIpAddress().getHostAddress();
            tcpClient.startConnection(user.getIpAddress().getHostAddress(), MainClass.TCP_SERVER_PORT);
            MainClass.initializeChatWindow(localUserIP, tcpClient);
        } catch (IOException ex) {
            ex.printStackTrace(); // Handle connection errors here
        }
    }
}
*/

package chatsystem.ui;
import chatsystem.contacts.*;
import chatsystem.network.TCPClient;
import chatsystem.MainClass;
import chatsystem.observers.MyObserver;

import java.awt.*;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;

public class HomeTab extends JFrame {
    //private ArrayList<MyObserver> observers2 = new ArrayList<>();

    /*public void addObserver2(MyObserver observer2) {
        observers2.add(observer2);
    }*/

    public HomeTab(){
        setTitle("HomeTab");
        setSize(500,300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new FlowLayout());

        JButton button_startChat = new JButton("Start Chatting");
        button_startChat.addActionListener(e->{
            MainWindow mainWindow = new MainWindow();
            mainWindow.setVisible(true);
            dispose();
        });

        add(button_startChat);
    }
}
