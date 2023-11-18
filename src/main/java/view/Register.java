package view;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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
    public Register() {
        super("User Registration");

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
                registerUser(); //when the button is clicked it calls the registerUser method
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
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //exits application when the JFrame is closed
        setSize(400, 300);
        setLocationRelativeTo(null); //center the JFrame on the screen
        setLayout(new BorderLayout());
        add(panel, BorderLayout.CENTER);

        // Display the frame
        setVisible(true);
    }

    private void registerUser() {
        String firstName = this.firstName.getText();
        String lastName = this.lastName.getText();
        String username = this.username.getText();
        String birthday = this.birthday.getText();
        char[] password = this.password.getPassword();

        // Tests : prints the information to the console
        System.out.println("User Registered:");
        System.out.println("First Name: " + firstName);
        System.out.println("Last Name: " + lastName);
        System.out.println("Username: " + username);
        System.out.println("Birthday: " + birthday);
        System.out.println("Password: " + new String(password));
    }

    /*
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new Register();
            }
        });

    }
    */
}
