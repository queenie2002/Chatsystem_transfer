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
        frame.setLocationRelativeTo(null); //center the JFrame on the screen
        frame.setLayout(new BorderLayout());




        JPanel panel = new JPanel(new GridLayout(1, 2));

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

        panel.add(registerButton);
        panel.add(loginButton);
        panel.setBorder(new EmptyBorder(200, 100, 200, 100));








        //Redirection panel
        JPanel panel1 = new JPanel(new GridLayout(1, 3)); //arranges the components in a grid

        JButton closeButton = new JButton("Close");
        closeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                for (MyObserver obs : observers) {
                    obs.toCloseApp(frame);
                }
            }
        });

        panel1.add(new JLabel());
        panel1.add(new JLabel());
        panel1.add(closeButton);
        panel1.setSize(600, 100);





        frame.add(panel, BorderLayout.CENTER);
        frame.add(panel1, BorderLayout.PAGE_END);
        frame.setVisible(true);
    }


}
