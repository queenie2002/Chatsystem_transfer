package view;

import controller.*;
import model.*;
import run.MainClass;

import java.awt.*;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.Objects;

public class Login {

    private JTextField jtField = new JTextField();
    private JPasswordField jpasswordField = new JPasswordField();

    public  Login(ReceiveMessage r, SendMessage s) {

        // Create and set up the window
        JFrame frame = new JFrame("Login");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);






        //Create buttons
        JButton button_login = new JButton("login");
        button_login.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                boolean found = false;
                found = loginFunction(found);

                if (found) {
                    HomeTab hometab = new HomeTab(r, s);
                    frame.dispose();
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

    public boolean loginFunction(boolean found) {

        //faut récup la database


        String nicknameInput = getNickname();
        String passwordInput = getPassword();


        //we check if someone with that nickname exists
        ContactList instance = ContactList.getInstance();
        if (instance.existsContactWithNickname(nicknameInput)) {

            User databaseUser = instance.getContactWithNickname(nicknameInput);

            if ((Objects.equals(databaseUser.getNickname(), nicknameInput)) && Objects.equals(databaseUser.getPassword(), passwordInput)) {



                MainClass.me.setFirstName(databaseUser.getFirstName());
                MainClass.me.setLastName(databaseUser.getLastName());
                MainClass.me.setNickname(databaseUser.getNickname());
                MainClass.me.setBirthday(databaseUser.getBirthday());
                MainClass.me.setPassword(databaseUser.getPassword());
                MainClass.me.setId("id"+databaseUser.getNickname());
                MainClass.me.setStatus(true);
                MainClass.me.setIpAddress(databaseUser.getIpAddress());

                instance.printContact(MainClass.me);


                try {
                    SendMessage.sendIAmConnected(MainClass.me);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                return true;
            }
            else { //-----------------------------------------COMMENT FAIRE POUR TOURNER EN BOUCLE
                PopUpTab popup = new PopUpTab("wrong login information try again");
                System.out.println("error: wrong password and login");
            }
        }
        else {
            PopUpTab popup = new PopUpTab("wrong nickname");
            System.out.println("error: there doesn't exist someone with that nickname");
        }
        return false;
    }
}