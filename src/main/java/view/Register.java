package view;
import controller.ReceiveMessage;
import controller.SendMessage;
import model.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.Arrays;

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

public class Register extends JFrame{

    //JTextField used for text input
    private JTextField firstName, lastName, username, birthday;

    //JPasswordField used for password input
    private JPasswordField password;

    //constructor: sets up the basic properties of the window like the title
    public Register(User me, ReceiveMessage r, SendMessage s) {

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
                    registerUser(me,r,s); //when the button is clicked it calls the registerUser method

                    if (User.uniqueNickname==2){
                        HomeTab hometab = new HomeTab(me, r, s);
                        frame.dispose();

                    }

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
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //exits application when the JFrame is closed
        frame.setSize(400, 300);
        frame.setLocationRelativeTo(null); //center the JFrame on the screen
        frame.setLayout(new BorderLayout());
        frame.add(panel, BorderLayout.CENTER);

        // Display the frame
        frame.setVisible(true);
    }

    private void registerUser(User me,ReceiveMessage r, SendMessage s) throws IOException {

        String firstName = this.firstName.getText();
        String lastName = this.lastName.getText();
        String username = this.username.getText();
        String birthday = this.birthday.getText();
        char[] password = this.password.getPassword();

        me.setFirstName(firstName);
        me.setLastName(lastName);
        me.setNickname(username);
        me.setBirthday(birthday);
        me.setPassword(Arrays.toString(password));
        me.setId("id"+username);

        s.sendBroadcastBeginning(me);

        try {
            // Sleep for 3 seconds (3000 milliseconds)
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            // Handle the exception (if needed)
            e.printStackTrace();
        }

        if(User.uniqueNickname==1){//nickname is not unique
            // Nickname is not unique, open a new frame with a message
            JFrame errorMessageFrame = new JFrame("Error");
            errorMessageFrame.setSize(300, 100);
            errorMessageFrame.setLocationRelativeTo(null);
            errorMessageFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

            JLabel errorMessageLabel = new JLabel("This nickname is already taken. Please select a different one.");
            errorMessageLabel.setHorizontalAlignment(JLabel.CENTER);

            errorMessageFrame.add(errorMessageLabel);
            errorMessageFrame.setVisible(true);
        }
        else if(User.uniqueNickname==0){
            User.uniqueNickname=2;
        }



    }
    /*
        // Tests : prints the information to the console
        System.out.println("User Registered:");
        System.out.println("First Name: " + firstName);
        System.out.println("Last Name: " + lastName);
        System.out.println("Username: " + username);
        System.out.println("Birthday: " + birthday);
        System.out.println("Password: " + new String(password));
        */
}
