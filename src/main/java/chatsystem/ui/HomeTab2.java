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

public class HomeTab2 extends JFrame {
    private ArrayList<MyObserver> observers2 = new ArrayList<>();

    public void addObserver2(MyObserver observer2) {
        observers2.add(observer2);
    }

    public HomeTab2(){
        setTitle("HomeTab");
        setSize(300,200);
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
