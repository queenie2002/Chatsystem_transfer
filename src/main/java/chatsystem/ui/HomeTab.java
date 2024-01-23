package chatsystem.ui;
import chatsystem.observers.MyObserver;

import java.awt.*;
import java.util.ArrayList;
import javax.swing.*;

public class HomeTab extends JFrame {




    public HomeTab(){
        setTitle("HomeTab");
        setSize(500,300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new FlowLayout());

        JButton button_startChat = new JButton("Start Chatting");
        /*button_startChat.addActionListener(e->{
            MainWindow mainWindow = new MainWindow();
            mainWindow.setVisible(true);
            dispose();
        });*/
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
