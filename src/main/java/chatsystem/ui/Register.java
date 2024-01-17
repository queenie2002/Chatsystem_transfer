package chatsystem.ui;

import chatsystem.controller.Controller;
import chatsystem.network.UDPSender;
import chatsystem.network.UDPReceiver;
import chatsystem.contacts.ContactList;
import chatsystem.MainClass;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.sql.SQLException;

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
    private JTextField firstName, lastName, nickname, birthday;

    //JPasswordField used for password input
    private JPasswordField password;

    //constructor: sets up the basic properties of the window like the title
    public Register(UDPReceiver r, UDPSender s) throws IOException {

        Controller.sendToChooseNickname();


        // Create and set up the window
        JFrame frame = new JFrame("User Registration");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Create components
        firstName = new JTextField(20);
        lastName = new JTextField(20);
        nickname = new JTextField(20);
        birthday = new JTextField(20);
        password = new JPasswordField(20);

        JButton registerButton = new JButton("Register");
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    registerUser(r,s, frame); //when the button is clicked it calls the registerUser method
                } catch (IOException | SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

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
                try {
                    Controller.toDisconnect();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                frame.dispose();
            }
        });

        JButton previousButton = new JButton("Previous");
        previousButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    Beginning beginning = new Beginning(r, s);
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

    private void registerUser(UDPReceiver r, UDPSender s, Frame frame) throws IOException, SQLException {

        //faut créer la database et l'initialiser


        String firstName = this.firstName.getText();
        String lastName = this.lastName.getText();
        String nickname = this.nickname.getText();
        String birthday = this.birthday.getText();
        char[] password = this.password.getPassword();

        MainClass.me.setFirstName(firstName);
        MainClass.me.setLastName(lastName);
        MainClass.me.setNickname(nickname);
        MainClass.me.setBirthday(birthday);
        MainClass.me.setPassword(String.valueOf(password));
        MainClass.me.setStatus(true);


        ContactList instance = ContactList.getInstance();
        if (instance.existsContactWithNickname(nickname)) { //if someone already has nickname
            PopUpTab popup1 = new PopUpTab("choose another nickname");
        }
        else { //if unique i go to next tab and tell people i am connected
            HomeTab hometab = new HomeTab(r, s);
            try {
                Controller.sendIAmConnected(MainClass.me);

                System.out.println();
                System.out.println("Printing Contact List after chose nickname ");
                instance.printContactList();


            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
            frame.dispose();
        }
    }
}
