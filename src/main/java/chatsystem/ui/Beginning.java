package chatsystem.ui;

import chatsystem.contacts.ContactList;
import chatsystem.contacts.User;
import chatsystem.controller.Controller;
import chatsystem.database.DatabaseMethods;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class Beginning extends JFrame {



    public interface Observer {
        void toDisconnect(JFrame frame) throws IOException;
        void canRegister(JFrame frame);
        void canLogin(JFrame frame);

    }
    List<Beginning.Observer> observers = new ArrayList<>();
    public void addObserver(Beginning.Observer obs) {
        this.observers.add(obs);
    }




    public Beginning() throws IOException {

        JFrame frame = new JFrame("Welcome To The ChatSystem");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


        addObserver(new Beginning.Observer() {
            @Override
            public void toDisconnect(JFrame frame) throws IOException {

            }

            @Override
            public void canRegister(JFrame frame) {

            }

            @Override
            public void canLogin(JFrame frame) {

            }

        });



        JButton registerButton = new JButton("Register");
        JButton loginButton = new JButton("Login");



        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    Controller.canRegister(frame);
                } catch (SQLException | IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });


        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    Controller.canLogin(frame);
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
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
                for (Beginning.Observer obs : observers) {
                    try {
                        obs.toDisconnect(frame);
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                }
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
