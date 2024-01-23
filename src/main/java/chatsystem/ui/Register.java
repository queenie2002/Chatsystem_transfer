package chatsystem.ui;

import chatsystem.MainClass;
import chatsystem.observers.MyObserver;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;


/** Opens a Register tab */
public class Register {

    //OBSERVERS
    ArrayList<MyObserver> observers = new ArrayList<>();
    public synchronized void addObserver(MyObserver obs) {
        this.observers.add(obs);
    }




    private JTextField nickname;
    private JPasswordField password;






    public Register() {

        // Create and set up the window
        JFrame frame = new JFrame("User Registration");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 600);
        frame.setLocationRelativeTo(null); //center
        frame.setLayout(new BorderLayout());


        //Label Panel
        JPanel labelPanel = new JPanel(new FlowLayout());
        JLabel registerLabel = new JLabel("Register", JLabel.CENTER);
        registerLabel.setPreferredSize(new Dimension(175, 100));
        labelPanel.add(registerLabel);




        //Register Panel
        JPanel registerPanel = new JPanel(new GridLayout(2, 2));

        JButton registerButton = new JButton("Register");
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                //get inputs
                String nicknameInfo = nickname.getText();
                char[] passwordInfo = password.getPassword();

                //notify observer
                for (MyObserver obs : observers) {
                    obs.registerFunction(nicknameInfo,String.valueOf(passwordInfo),frame);
                }
            }
        });


        //create the text fields
        nickname = new JTextField(20);
        password = new JPasswordField(20);
        registerPanel.add(new JLabel("Nickname:"));
        registerPanel.add(nickname);
        registerPanel.add(new JLabel("Password:"));
        registerPanel.add(password);






        //Redirection Panel
        JPanel redirectionPanel = new JPanel(new GridLayout(1, 3));

        JButton closeButton = new JButton("Close");
        closeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //notifies observers
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
                    //goes to previous tab
                    Beginning beginning = new Beginning();
                    beginning.addObserver(MainClass.controller);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                frame.dispose();
            }
        });
        redirectionPanel.add(closeButton);
        redirectionPanel.add(previousButton);
        redirectionPanel.add(registerButton);
        redirectionPanel.setSize(600, 100);


        //add all panels to frame
        frame.add(labelPanel, BorderLayout.PAGE_START);
        frame.add(registerPanel, BorderLayout.CENTER);
        frame.add(redirectionPanel, BorderLayout.PAGE_END);
        frame.setVisible(true);

    }


}
