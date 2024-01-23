package chatsystem.ui;


import chatsystem.MainClass;
import chatsystem.contacts.ContactList;
import chatsystem.observers.MyObserver;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.awt.*;
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
        JFrame frame = new JFrame("User Registration");
        setTitle("HomeTab");
        setSize(500,300);
        //We don't want to close right away
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                for (MyObserver obs : observers) {
                    try {
                        obs.toDisconnect(frame, MainClass.me);
                    } catch (IOException ex) {
                        LOGGER.error("Couldn't warn others that I'm disconnected");
                        throw new RuntimeException(ex);
                    }
                }
            }
        });        setLayout(new FlowLayout());

        JButton button_startChat = new JButton("Start Chatting");

        button_startChat.addActionListener(e->{
            MainWindow mainWindow = new MainWindow();
            ContactList.getInstance().addObserver(mainWindow);
            System.out.println("MainWindow added as observer"); // Debug print
            mainWindow.setVisible(true);
            dispose();
        });

        add(button_startChat);
    }
}
