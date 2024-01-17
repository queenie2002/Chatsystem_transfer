package chatsystem.ui;

import chatsystem.controller.Controller;
import chatsystem.network.UDPSender;
import chatsystem.network.UDPReceiver;
import chatsystem.contacts.ContactList;
import chatsystem.contacts.User;
import chatsystem.MainClass;

import java.awt.*;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Login {

    private JTextField jtField = new JTextField();
    private JPasswordField jpasswordField = new JPasswordField();

    public static JFrame frame;

    public interface Observer {
        void loginFunction(String nicknameInput, String passwordInput);
    }
    List<Login.Observer> observers = new ArrayList<>();
    public synchronized void addObserver(Login.Observer obs) {
        this.observers.add(obs);
    }





    public  Login() {

        // Create and set up the window
        frame = new JFrame("Login");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);






        //Create buttons
        JButton button_login = new JButton("login");
        button_login.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                String nicknameInput = getNickname();
                String passwordInput = getPassword();


                for (Login.Observer obs : observers) {
                    obs.loginFunction(nicknameInput,  passwordInput);
                }
            }
        });



        JLabel emptyLabel = new JLabel("Login", JLabel.CENTER);
        emptyLabel.setPreferredSize(new Dimension(175, 100));
        frame.getContentPane().add(emptyLabel, BorderLayout.PAGE_START);

        JPanel p = new JPanel(new GridLayout(1, 2));
        p.add(new JLabel("Nickname"));
        p.add(jtField);
        p.add(new JLabel("Password"));
        p.add(jpasswordField);

        p.add(button_login);
        frame.add(p);

        //to make the size of the frame the size of its content
        frame.pack();
        frame.setLocationRelativeTo(null); //center the JFrame on the screen

        // Display the window.
        frame.setVisible(true);
    }

    public String getNickname() { return this.jtField.getText();}

    public String getPassword() {
        return this.jpasswordField.getText();
    }


}