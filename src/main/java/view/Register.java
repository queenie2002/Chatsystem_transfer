package view;

import controller.*;
import model.*;
import run.MainClass;
import view.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.Arrays;

import static model.ContactList.getInstance;

/*Reminders on swing
* JLabel -> display area for text/images
* JPanel -> component that serves as a container for other components
* JButton -> button that performs an action when clicked on
* BorderLayout -> places components in 5 areas : N,S,E,W, or center
* FlowLayout -> places components in a row, sized at their preferred size. If the horizontal space is too small, the next available row is used
* GridLayout -> places components in a grid of same size cells. Each component takes all available space in the cell
* */

/*TO DO
when register button is clicked, open new window
* */

public class Register {

    //JTextField used for text input
    private JTextField firstName, lastName, username, birthday;

    //JPasswordField used for password input
    private JPasswordField password;

    //constructor: sets up the basic properties of the window like the title
    public Register(ReceiveMessage r, SendMessage s) throws IOException {

        SendMessage.sendToChooseNickname(MainClass.me);

        // Create and set up the window
        JFrame frame = new JFrame("User Registration");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Create components
        firstName = new JTextField(20);
        lastName = new JTextField(20);
        username = new JTextField(20);
        birthday = new JTextField(20);
        password = new JPasswordField(20);

        JButton registerButton = new JButton("Register");
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    registerUser(r,s, frame); //when the button is clicked it calls the registerUser method
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

        // Create layout
        JPanel panel = new JPanel(new GridLayout(6, 2)); //arranges the components in a grid
        panel.add(new JLabel("First Name:"));
        panel.add(firstName);
        panel.add(new JLabel("Last Name:"));
        panel.add(lastName);
        panel.add(new JLabel("Username:"));
        panel.add(username);
        panel.add(new JLabel("Birthday: (YYYY-MM-DD)"));
        panel.add(birthday);
        panel.add(new JLabel("Password:"));
        panel.add(password);
        panel.add(new JLabel()); // Empty label for spacing
        panel.add(registerButton);

        // Set up the frame
        frame.setSize(600, 250);
        frame.setLocationRelativeTo(null); //center the JFrame on the screen
        frame.setLayout(new BorderLayout());
        frame.add(panel, BorderLayout.CENTER);

        // Display the frame
        frame.setVisible(true);
    }

    private void registerUser(ReceiveMessage r, SendMessage s, Frame frame) throws IOException {

        String firstName = this.firstName.getText();
        String lastName = this.lastName.getText();
        String username = this.username.getText();
        String birthday = this.birthday.getText();
        char[] password = this.password.getPassword();

        MainClass.me.setFirstName(firstName);
        MainClass.me.setLastName(lastName);
        MainClass.me.setNickname(username);
        MainClass.me.setBirthday(birthday);
        MainClass.me.setPassword(Arrays.toString(password));
        MainClass.me.setId("id"+username);

        PopUpTab popup = new PopUpTab("we're checking for unicity of your nickname");

        //wait a little for the answers to come in, 8 sec
        try {
            Thread.sleep(8000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        ContactList instance = getInstance();
        if (instance.existsContactWithNickname(username)) { //if someone already has nickname
            PopUpTab popup1 = new PopUpTab("choose another nickname");
            return;
        }
        else { //if unique i go to next tab and tell people i am connected
            HomeTab hometab = new HomeTab(r, s);
            try {
                SendMessage.sendIAmConnected(MainClass.me);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
            frame.dispose();
        }

    }
}
