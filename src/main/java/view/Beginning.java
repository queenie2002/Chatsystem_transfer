package view;

import controller.*;
import model.User;
import run.MainClass;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import static run.MainClass.me;

/*Reminders on swing
 * JLabel -> display area for text/images
 * JPanel -> component that serves as a container for other components
 * JButton -> button that performs an action when clicked on
 * BorderLayout -> places components in 5 areas : N,S,E,W, or center
 * FlowLayout -> places components in a row, sized at their preferred size. If the horizontal space is too small, the next available row is used
 * GridLayout -> places components in a grid of same size cells. Each component takes all available space in the cell
 * */

public class Beginning extends JFrame {

    //constructor: sets up the basic properties of the window like the title
    public Beginning(ReceiveMessage r, SendMessage s) throws IOException {

        JFrame frame = new JFrame("Welcome To The ChatSystem");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JButton registerButton = new JButton("Register");
        JButton loginButton = new JButton("Login");






        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                try {
                    Register register = new Register(r, s);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }

                frame.dispose();
            }
        });






        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Login login = new Login(r, s);
                frame.dispose();
            }
        });





        // Create layout
        JPanel panel = new JPanel(new GridLayout(1, 2)); //arranges the components in a grid
        panel.add(registerButton);
        panel.add(loginButton);










        JButton closeButton = new JButton("Close");
        closeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    SendMessage.toDisconnect();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                frame.dispose();
            }
        });

        // Create layout
        JPanel panel1 = new JPanel(new GridLayout(1, 3)); //arranges the components in a grid
        panel1.add(new JLabel());
        panel1.add(new JLabel());
        panel1.add(closeButton);


        // Set up the frame
        frame.setSize(600, 100);
        frame.setLocationRelativeTo(null); //center the JFrame on the screen
        frame.setLayout(new BorderLayout());
        frame.add(panel, BorderLayout.CENTER);
        frame.add(panel1, BorderLayout.PAGE_END);

        // Display the frame
        frame.setVisible(true);
    }


}
