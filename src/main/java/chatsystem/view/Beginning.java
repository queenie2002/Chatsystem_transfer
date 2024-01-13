package chatsystem.view;

import chatsystem.controller.UDPSendMessage;
import chatsystem.controller.UDPReceiveMessage;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;


public class Beginning extends JFrame {

    //constructor: sets up the basic properties of the window like the title
    public Beginning(UDPReceiveMessage r, UDPSendMessage s) throws IOException {

        JFrame frame = new JFrame("Welcome To The ChatSystem");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JButton registerButton = new JButton("Register");
        JButton loginButton = new JButton("Login");



        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                try {
                    Register register = new Register(r, s);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }

                frame.dispose();
            }
        });






        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Login login = new Login(r, s);
                frame.dispose();
            }
        });





        // Create layout
        JPanel panel = new JPanel(new GridLayout(1, 2)); //arranges the components in a grid
        panel.add(registerButton);
        panel.add(loginButton);
        panel.setBorder(new EmptyBorder(200, 100, 200, 100));











        JButton closeButton = new JButton("Close");
        closeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    UDPSendMessage.toDisconnect();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                frame.dispose();
            }
        });

        // Create layout
        JPanel panel1 = new JPanel(new GridLayout(1, 3)); //arranges the components in a grid
        panel1.add(new JLabel());
        panel1.add(new JLabel());
        panel1.add(closeButton);
        panel1.setSize(600, 100);

        // Set up the frame
        frame.setSize(600, 600);
        frame.setLocationRelativeTo(null); //center the JFrame on the screen
        frame.setLayout(new BorderLayout());
        frame.add(panel, BorderLayout.CENTER);
        frame.add(panel1, BorderLayout.PAGE_END);

        // Display the frame
        frame.setVisible(true);
    }


}
