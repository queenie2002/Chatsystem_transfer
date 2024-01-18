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

    private JTextField jtField = new JTextField();
    private JPasswordField jpasswordField = new JPasswordField();


    //OBSERVERS
    ArrayList<MyObserver> observers = new ArrayList<>();
    public synchronized void addObserver(MyObserver obs) {
        this.observers.add(obs);
    }




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


        JPanel panel = new JPanel(new FlowLayout());
        JLabel loginLabel = new JLabel("Login", JLabel.CENTER);
        loginLabel.setPreferredSize(new Dimension(175, 100));
        panel.add(loginLabel);

        JPanel panel1 = new JPanel(new GridLayout(1, 4));
        panel1.add(new JLabel("Nickname"));
        panel1.add(jtField);
        panel1.add(new JLabel("Password"));
        panel1.add(jpasswordField);
        panel1.setSize(new Dimension(400, 400));

        JPanel biggerPanel = new JPanel(new BorderLayout());
        Border marginBorder = BorderFactory.createEmptyBorder(200, 70, 200, 100);
        panel1.setBorder(marginBorder);
        biggerPanel.add(panel1);

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



        // Create layout
        JPanel panel2 = new JPanel(new GridLayout(1, 3)); //arranges the components in a grid
        panel2.add(closeButton);
        panel2.add(previousButton);
        panel2.add(button_login);
        panel2.setSize(600, 100);


        frame.add(panel, BorderLayout.PAGE_START);
        frame.add(biggerPanel, BorderLayout.CENTER);
        frame.add(panel2, BorderLayout.PAGE_END);


        frame.setVisible(true);
    }

    public String getNickname() { return this.jtField.getText();}

    public String getPassword() {
        return this.jpasswordField.getText();
    }


}