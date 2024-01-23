package chatsystem.ui;

import chatsystem.observers.MyObserver;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/** Beginning Tab, allows user to choose between register or login */
public class Beginning extends JFrame {


    //OBSERVERS
    List<MyObserver> observers = new ArrayList<>();
    public void addObserver(MyObserver obs) {
        this.observers.add(obs);
    }



    public Beginning() throws IOException {

        JFrame frame = new JFrame("Welcome To The ChatSystem");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 600);
        frame.setLocationRelativeTo(null);
        frame.setLayout(new BorderLayout());




        JPanel registerOrLoginPanel = new JPanel(new GridLayout(1, 2));

        JButton registerButton = new JButton("Register");
        JButton loginButton = new JButton("Login");
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                for (MyObserver obs : observers) {
                    try {
                        obs.canRegister(frame);
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            }
        });
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                for (MyObserver obs : observers) {
                    obs.canLogin(frame);
                }
            }
        });

        registerOrLoginPanel.add(registerButton);
        registerOrLoginPanel.add(loginButton);
        registerOrLoginPanel.setBorder(new EmptyBorder(200, 100, 200, 100));








        //Redirection panel
        JPanel closePanel = new JPanel(new GridLayout(1, 3)); //arranges the components in a grid

        JButton closeButton = new JButton("Close");
        closeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                for (MyObserver obs : observers) {
                    obs.toCloseApp(frame);
                }
            }
        });

        closePanel.add(new JLabel());
        closePanel.add(new JLabel());
        closePanel.add(closeButton);
        closePanel.setSize(600, 100);





        frame.add(registerOrLoginPanel, BorderLayout.CENTER);
        frame.add(closePanel, BorderLayout.PAGE_END);
        frame.setVisible(true);
    }


}
