package chatsystem.ui;

import chatsystem.MainClass;
import chatsystem.observers.MyObserver;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.Border;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.UnknownHostException;
import java.sql.SQLException;
import java.util.ArrayList;

/** Login Tab */
public class Login {

    
    //OBSERVERS
    ArrayList<MyObserver> observers = new ArrayList<>();
    public synchronized void addObserver(MyObserver obs) {
        this.observers.add(obs);
    }


    

    private JTextField jtField = new JTextField();
    private JPasswordField jpasswordField = new JPasswordField();


    


    public  Login() {

        // Create and set up the window
        JFrame frame = new JFrame("Login");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 600);
        frame.setLocationRelativeTo(null); //center the JFrame on the screen
        frame.setLayout(new BorderLayout());



        JButton button_login = new JButton("Login");
        button_login.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                String nicknameInput = getNickname();
                String passwordInput = getPassword();


                for (MyObserver obs : observers) {
                    try {
                        obs.loginFunction(nicknameInput,  passwordInput, frame);
                    } catch (UnknownHostException | SQLException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            }
        });


        JPanel labelPanel = new JPanel(new FlowLayout());
        JLabel loginLabel = new JLabel("Login", JLabel.CENTER);
        loginLabel.setPreferredSize(new Dimension(175, 100));
        labelPanel.add(loginLabel);

        JPanel loginPanel = new JPanel(new GridLayout(1, 4));
        loginPanel.add(new JLabel("Nickname"));
        loginPanel.add(jtField);
        loginPanel.add(new JLabel("Password"));
        loginPanel.add(jpasswordField);
        loginPanel.setSize(new Dimension(400, 400));

        JPanel biggerPanel = new JPanel(new BorderLayout());
        Border marginBorder = BorderFactory.createEmptyBorder(200, 70, 200, 100);
        loginPanel.setBorder(marginBorder);
        biggerPanel.add(loginPanel);





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
        redirectionPanel.add(closeButton);
        redirectionPanel.add(previousButton);
        redirectionPanel.add(button_login);
        redirectionPanel.setSize(600, 100);


        frame.add(labelPanel, BorderLayout.PAGE_START);
        frame.add(biggerPanel, BorderLayout.CENTER);
        frame.add(redirectionPanel, BorderLayout.PAGE_END);
        frame.setVisible(true);
    }

    public String getNickname() { return this.jtField.getText();}

    public String getPassword() {
        return this.jpasswordField.getText();
    }


}