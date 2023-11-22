package view;

import javax.swing.*;
import java.awt.*;

//this is a pop-up tab that shows a message
public class PopUpTab {
    public PopUpTab(String message) {

        // create the window
        JFrame frame = new JFrame("ChatSystem");

        //when we close, we just close the frame and keep running the app, it's a pop-up
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        //we set the layout
        frame.setLayout(new FlowLayout());

        //we create and set a label with the message
        JLabel messageLabel = new JLabel(message, JLabel.CENTER);
        messageLabel.setPreferredSize(new Dimension(175, 100));

        //we add it to the frame
        frame.getContentPane().add(messageLabel, BorderLayout.PAGE_START);

        // Size of frame
        frame.setSize(200, 100);
        // Center the frame on the screen
        frame.setLocationRelativeTo(null);
        // Display the window.
        frame.setVisible(true);
    }
}
