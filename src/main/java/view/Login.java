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


                //have to check something else
                ContactList instance = ContactList.getInstance();
                if (instance.existsContactWithNickname(nicknameInput)) {
                    User me = instance.getContactWithNickname(nicknameInput);

                    System.out.println("nickname: " + me.getNickname());
                    System.out.println("nickname: " + me.getPassword());

                    if ((Objects.equals(me.getNickname(), nicknameInput)) && Objects.equals(me.getPassword(), passwordInput)) {
                        HomeTab hometab = new HomeTab(me, r, s);
                        frame.dispose();
                    }
                    else {
                        //HANDLE THIS
                        System.out.println("wrong password and login");
                    }



                }
                else {
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

        // Make the window's dimension fit its content
        frame.pack();
        // Display the window.
        frame.setVisible(true);
    }

    public String getNickname() { return this.jtField.getText();}

    public String getPassword() {
        return this.jpasswordField.getText();
    }
}