package chatsystem.ui;

import chatsystem.controller.Controller;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class Register {

    private JTextField firstName, lastName, nickname, birthday;
    private JPasswordField password;

    public interface Observer {
        void registerUser(String nicknameInfo,String firstNameInfo, String lastNameInfo, String birthdayInfo, String passwordInfo,JFrame frame) throws SQLException;
        }

    List<Register.Observer> observers = new ArrayList<>();
    public synchronized void addObserver(Register.Observer obs) {
        this.observers.add(obs);
    }



    public Register() throws IOException {

        // Create and set up the window
        JFrame frame = new JFrame("User Registration");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


        addObserver(Controller::registerUser);






        JButton registerButton = new JButton("Register");
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                String firstNameInfo = firstName.getText();
                String lastNameInfo = lastName.getText();
                String nicknameInfo = nickname.getText();
                String birthdayInfo = birthday.getText();
                char[] passwordInfo = password.getPassword();


                for (Register.Observer obs : observers) {
                    try {
                        obs.registerUser(nicknameInfo, firstNameInfo,lastNameInfo,birthdayInfo, String.valueOf(passwordInfo),frame);
                    } catch (SQLException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            }
        });



        firstName = new JTextField(20);
        lastName = new JTextField(20);
        nickname = new JTextField(20);
        birthday = new JTextField(20);
        password = new JPasswordField(20);


        // Create layout
        JPanel panel = new JPanel(new GridLayout(5, 2)); //arranges the components in a grid
        panel.add(new JLabel("First Name:"));
        panel.add(firstName);
        panel.add(new JLabel("Last Name:"));
        panel.add(lastName);
        panel.add(new JLabel("Nickname:"));
        panel.add(nickname);
        panel.add(new JLabel("Birthday: (YYYY-MM-DD)"));
        panel.add(birthday);
        panel.add(new JLabel("Password:"));
        panel.add(password);



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
                    Beginning beginning = new Beginning();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                frame.dispose();
            }
        });

        // Create layout
        JPanel panel1 = new JPanel(new GridLayout(1, 3)); //arranges the components in a grid
        panel1.add(closeButton);
        panel1.add(previousButton);
        panel1.add(registerButton);
        panel1.setSize(600, 100);












        // Set up the frame
        frame.setSize(600, 600);
        frame.setLocationRelativeTo(null); //center the JFrame on the screen
        frame.setLayout(new FlowLayout());
        frame.add(panel);
        frame.add(panel1);

        // Display the frame
        frame.setVisible(true);


    }


}
