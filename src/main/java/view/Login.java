package view;

import controller.ReceiveMessage;
import controller.SendMessage;
import model.User;

import java.awt.*;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Objects;

public class Login {

    private JTextField jtField;
    private JPasswordField jpasswordField;

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

                if ((Objects.equals(me.getNickname(), nicknameInput)) && Objects.equals(me.getPassword(), passwordInput)) {
                    //lead to other tab


                    frame.dispose();
                }
                else {

                    //HANDLE THIS
                    System.out.println("wrong password and login");

                }

            }
        });



        JLabel emptyLabel = new JLabel("Login", JLabel.CENTER);
        emptyLabel.setPreferredSize(new Dimension(175, 100));
        frame.getContentPane().add(emptyLabel, BorderLayout.PAGE_START);

        JPanel p = new JPanel(new GridLayout(1, 2));
        p.add(new JLabel("ID user"));
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