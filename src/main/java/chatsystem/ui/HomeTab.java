package chatsystem.ui;


import chatsystem.MainClass;
import chatsystem.contacts.ContactList;
import chatsystem.observers.MyObserver;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.*;

public class HomeTab extends JFrame {

    //LOGGER
    private static final Logger LOGGER = LogManager.getLogger(HomeTab.class);


    //OBSERVERS
    ArrayList<MyObserver> observers = new ArrayList<>();
    public synchronized void addObserver(MyObserver obs) {
        this.observers.add(obs);
    }



    public HomeTab(){

        //we set up the frame
        JFrame frame = new JFrame("Home");
        frame.setSize(600, 600);
        frame.setLocationRelativeTo(null); //center the JFrame on the screen
        frame.setLayout(new BorderLayout());


        //we don't want to close right away
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        //we add a window listener
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                for (MyObserver obs : observers) {
                    //it notifies todisconnect
                    try {
                        obs.toDisconnect(frame, MainClass.me);
                    } catch (IOException ex) {
                        LOGGER.error("Couldn't warn others that I'm disconnected");
                        throw new RuntimeException(ex);
                    }
                }
            }
        });




        JPanel startChatPanel = new JPanel(new BorderLayout());
        JButton button_startChat = new JButton("Start Chatting");

        button_startChat.addActionListener(e->{
            MainWindow mainWindow = new MainWindow();
            ContactList.getInstance().addObserver(mainWindow);
            System.out.println("MainWindow added as observer"); // Debug print
            mainWindow.setVisible(true);
            dispose();
        });
        startChatPanel.add(button_startChat, BorderLayout.CENTER);
        startChatPanel.setSize(new Dimension(100, 100));





        //Redirection Panel
        JButton closeButton = new JButton("Close");
        closeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                for (MyObserver obs : observers) {
                    obs.toCloseApp(frame);
                }
            }
        });

        JButton previousButton = new JButton("Previous");
        previousButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    Beginning beginning = new Beginning();
                    beginning.addObserver(MainClass.controller);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                frame.dispose();
            }
        });


        JPanel redirectionPanel = new JPanel(new GridLayout(1, 3)); //arranges the components in a grid
        redirectionPanel.add(new JLabel());
        redirectionPanel.add(closeButton);
        redirectionPanel.add(previousButton);
        redirectionPanel.setSize(600, 100);


        frame.add(startChatPanel, BorderLayout.CENTER);
        frame.add(redirectionPanel, BorderLayout.PAGE_END);
        frame.setVisible(true);



    }
}
