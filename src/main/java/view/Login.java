package view;

import controller.ReceiveMessage;
import controller.SendMessage;
import model.ContactList;
import model.User;

import java.awt.*;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Objects;

public class Login {

    private JTextField jtField = new JTextField();
    private JPasswordField jpasswordField = new JPasswordField();

    public  Login(User me, ReceiveMessage r, SendMessage s) {

        // Create and set up the window
        JFrame frame = new JFrame("Login");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


        //Create buttons
        JButton button_login = new JButton("login");
        button_login.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String nicknameInput = getNickname();
                String passwordInput = getPassword();

                //we check if someone with that nickname exists
                ContactList instance = ContactList.getInstance();
                if (instance.existsContactWithNickname(nicknameInput)) {
                    User me = instance.getContactWithNickname(nicknameInput);

                    if ((Objects.equals(me.getNickname(), nicknameInput)) && Objects.equals(me.getPassword(), passwordInput)) {
                        HomeTab hometab = new HomeTab(me, r, s);
                        frame.dispose();
                    }
                    else {
                        //----------------------------POP UP FRAME THAT SAYS WRONG PASSWORD AND LOGIN
                        System.out.println("error: wrong password and login");
                    }
                }
                else {
                    //----------------------------POP UP FRAME THAT SAYS NICKNAME DOESNT EXIST
                    System.out.println("error: there doesn't exist someone with that nickname");
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