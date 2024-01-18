package chatsystem.ui;

import chatsystem.controller.Controller;

import java.awt.*;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.UnknownHostException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Login {

    private JTextField jtField = new JTextField();
    private JPasswordField jpasswordField = new JPasswordField();



    public interface Observer {
        void loginFunction(String nicknameInput, String passwordInput, JFrame frame) throws UnknownHostException, SQLException;
    }
    List<Login.Observer> observers = new ArrayList<>();
    public synchronized void addObserver(Login.Observer obs) {
        this.observers.add(obs);
    }





    public  Login() {

        // Create and set up the window
        JFrame frame = new JFrame("Login");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        addObserver(Controller::loginFunction);


        JButton button_login = new JButton("login");
        button_login.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                String nicknameInput = getNickname();
                String passwordInput = getPassword();


                for (Login.Observer obs : observers) {
                    try {
                        obs.loginFunction(nicknameInput,  passwordInput, frame);
                    } catch (UnknownHostException | SQLException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            }
        });


        JPanel panel = new JPanel(new FlowLayout());
        JLabel emptyLabel = new JLabel("Login", JLabel.CENTER);
        emptyLabel.setPreferredSize(new Dimension(175, 100));
        panel.add(emptyLabel);

        JPanel panel1 = new JPanel(new GridLayout(1, 4));
        panel1.add(new JLabel("Nickname"));
        panel1.add(jtField);
        panel1.add(new JLabel("Password"));
        panel1.add(jpasswordField);





        //Redirection Panel
        JButton closeButton = new JButton("Close");
        closeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
            }
        });

        JButton previousButton = new JButton("Previous");
        previousButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    new Beginning();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                frame.dispose();
            }
        });

        // Create layout
        JPanel panel2 = new JPanel(new GridLayout(1, 3)); //arranges the components in a grid
        panel2.add(closeButton);
        panel2.add(previousButton);
        panel2.add(button_login);
        panel2.setSize(600, 100);


        JPanel generalPanel = new JPanel(new GridLayout(3, 1));
        generalPanel.add(panel);
        generalPanel.add(panel1);
        generalPanel.add(panel2);




        frame.add(generalPanel);

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