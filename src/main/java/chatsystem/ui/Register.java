package chatsystem.ui;

import chatsystem.MainClass;
import chatsystem.observers.MyObserver;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;


public class Register {

    private JTextField nickname;
    private JPasswordField password;



    //OBSERVERS
    ArrayList<MyObserver> observers = new ArrayList<>();
    public synchronized void addObserver(MyObserver obs) {
        this.observers.add(obs);
    }



    public Register() {

        // Create and set up the window
        JFrame frame = new JFrame("User Registration");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 600);
        frame.setLocationRelativeTo(null);
        frame.setLayout(new BorderLayout());



        JPanel panel = new JPanel(new FlowLayout());
        JLabel registerLabel = new JLabel("Register", JLabel.CENTER);
        registerLabel.setPreferredSize(new Dimension(175, 100));
        panel.add(registerLabel);





        JPanel panel1 = new JPanel(new GridLayout(2, 2)); //arranges the components in a grid

        JButton registerButton = new JButton("Register");
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                String nicknameInfo = nickname.getText();
                char[] passwordInfo = password.getPassword();


                for (MyObserver obs : observers) {
                    obs.registerFunction(nicknameInfo,String.valueOf(passwordInfo),frame);
                }
            }
        });

        nickname = new JTextField(20);
        password = new JPasswordField(20);
        panel1.add(new JLabel("Nickname:"));
        panel1.add(nickname);
        panel1.add(new JLabel("Password:"));
        panel1.add(password);






        //Redirection Panel
        JPanel panel2 = new JPanel(new GridLayout(1, 3)); //arranges the components in a grid

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

        panel2.add(closeButton);
        panel2.add(previousButton);
        panel2.add(registerButton);
        panel2.setSize(600, 100);



        frame.add(panel, BorderLayout.PAGE_START);
        frame.add(panel1, BorderLayout.CENTER);
        frame.add(panel2, BorderLayout.PAGE_END);
        frame.setVisible(true);

    }


}
