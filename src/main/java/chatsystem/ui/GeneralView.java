package chatsystem.ui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GeneralView extends JFrame {

    JFrame frame = new JFrame("The ChatSystem");
    JPanel mainPanel;
    JPanel panelWelcome = new JPanel(new GridLayout(1, 2));
    JPanel panelClose = new JPanel(new GridLayout(1, 2));




    JButton registerButton = new JButton("Register");
    JButton loginButton = new JButton("Login");
    JButton closeButton = new JButton("Close");



    //constructor: sets up the basic properties of the window like the title
    public GeneralView() {

        mainPanel = new JPanel();
        mainPanel.setLayout(new FlowLayout());




        //PANEL WELCOME
        panelWelcome.add(registerButton);
        panelWelcome.add(loginButton);
        panelWelcome.setBorder(new EmptyBorder(200, 100, 200, 100));

        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainPanel.remove(panelWelcome);
                mainPanel.add(panelClose);
                frame.getContentPane().setVisible(false); // hide the content pane
                frame.getContentPane().setVisible(true);
                //updatePanel();
            }
        });

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //Login login = new Login();
                panelWelcome.setVisible(false);
                //updatePanel();
            }
        });
        mainPanel.add(panelWelcome);





        //PANEL CLOSE
        panelClose.add(closeButton);

        closeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //frame.dispose();
            }
        });
        mainPanel.add(panelClose);
        panelClose.setVisible(false);












        frame.add(mainPanel);

        // Set up the frame
        frame.setTitle("The ChatSystem");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null); //center the JFrame on the screen
        frame.setSize(600, 600);

        // Set layout manager (e.g., GridLayout)
        frame.setLayout(new FlowLayout());

        // Display the frame
        frame.setVisible(true);
    }

    private void updatePanel() {
        frame.revalidate();
        frame.repaint();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new GeneralView());
    }

}
