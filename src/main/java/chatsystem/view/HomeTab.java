package chatsystem.view;

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

}