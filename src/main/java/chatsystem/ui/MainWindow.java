package chatsystem.ui;

import chatsystem.MainClass;
import chatsystem.contacts.ContactList;
import chatsystem.contacts.User;
import chatsystem.network.TCPMessage;
import chatsystem.network.UDPMessage;
import chatsystem.observers.MyObserver;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class MainWindow implements MyObserver {


    //LOGGER
    private static final Logger LOGGER = LogManager.getLogger(HomeTab.class);

    private JButton activeUserButton = null;



    //OBSERVERS
    ArrayList<MyObserver> observers = new ArrayList<>();
    public synchronized void addObserver(MyObserver obs) {
        this.observers.add(obs);
    }



    private JPanel userPanel;
    private JPanel onlineUsersPanel;
    private JPanel chatPanel;
    private static HashMap<String, ChatWindow> chatSessions = new HashMap<>();

    public MainWindow(){
        JFrame frame = new JFrame("My chats");
        frame.setSize(800,800);
        //we don't want to close right away
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //we add a window listener
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                for (MyObserver obs : observers) {
                    //it notifies todisconnect
                    try {
                        obs.toDisconnect(frame, MainClass.me);
                    } catch (IOException ex) {
                        LOGGER.error("Couldn't warn others that I'm disconnected");
                        throw new RuntimeException(ex);
                    }
                }
            }
        });
        frame.setLayout(new BorderLayout());

        initializeUserPanel(frame);
        initializeOnlineUsersPanel();
        initializeChatPanel();

        frame.add(userPanel,BorderLayout.NORTH);
        frame.add(onlineUsersPanel, BorderLayout.WEST);
        frame.add(chatPanel, BorderLayout.CENTER);
        frame.setVisible(true);
    }

    private void initializeUserPanel(JFrame frame){
        userPanel = new JPanel();
        JLabel userLabel = new JLabel("My Nickname : " + MainClass.me.getNickname());
        userPanel.add(userLabel);

        JButton changeNicknameButton = new JButton("Change Nickname");
        changeNicknameButton.addActionListener(e -> onChangeNickname());
        userPanel.add(changeNicknameButton);

        JButton disconnectButton = new JButton("Disconnect");
        disconnectButton.addActionListener(e -> onDisconnect(frame));
        userPanel.add(disconnectButton);

    }

    private void initializeOnlineUsersPanel(){
        onlineUsersPanel = new JPanel();
        onlineUsersPanel.setLayout(new BoxLayout(onlineUsersPanel, BoxLayout.Y_AXIS));
        JLabel onlineUsersLabel = new JLabel("Online Contacts");
        onlineUsersPanel.add(onlineUsersLabel);
        List<User> onlineUsers = getOnlineUsers();
        for (User user:onlineUsers){
            JButton userButton = new JButton(user.getNickname());
            userButton.addActionListener(e->switchChatWindow(user));
            onlineUsersPanel.add(userButton);
        }
    }

    private void initializeChatPanel(){
        chatPanel = new JPanel(new CardLayout());
    }

    private void switchChatWindow(User user){
        String userKey = user.getIpAddress().getHostAddress();
        CardLayout cardLayout = (CardLayout) chatPanel.getLayout();
        if(!chatSessions.containsKey(userKey)){
            ChatWindow chatWindow = new ChatWindow(user);
            chatSessions.put(userKey, chatWindow);
            chatPanel.add(chatWindow, userKey);
        }
        cardLayout.show(chatPanel, userKey);
        chatPanel.revalidate();
        chatPanel.repaint();

        // Update the appearance of buttons
        if (activeUserButton != null) {
            // Reset the appearance of the previously active button
            activeUserButton.setBackground(null);
        }

        // Find the button for the current user and update its appearance
        for (Component comp : onlineUsersPanel.getComponents()) {
            if (comp instanceof JButton) {
                JButton button = (JButton) comp;
                if (button.getText().equals(user.getNickname())) {
                    button.setBackground(Color.LIGHT_GRAY); // Change the background color
                    activeUserButton = button;
                    break;
                }
            }
        }
    }

    private List<User> getOnlineUsers(){
        return ContactList.getInstance().getConnectedContactsList();
    }

    public static ChatWindow getChatWindowForUser(String userKey) {
        return chatSessions.get(userKey);
    }

    /** when we click on the button to change nickname
     * we check if the nickname is not empty, doesn't contain blank spaces and is not already taken
     * if not, we change our nickname and update the view
     * */
    private void onChangeNickname() {

        String newNickname = JOptionPane.showInputDialog(this, "Enter new nickname:");
        if (newNickname.isEmpty()) {
            new PopUpTab("Nickname can't be empty. Please choose one.");
       } else if (newNickname.contains(" ")) {
            new PopUpTab("Nicknames can't have blank spaces. Please choose another one.");
       } else {
            User auxUser = MainClass.me;
            auxUser.setNickname(newNickname);

            if (ContactList.getInstance().existsContactWithNickname(newNickname)) { //if someone already has nickname
                new PopUpTab("Nickname already taken. Please choose another one");
            } else {
                try {
                    MainClass.controller.changingNickname(newNickname);
                    refreshUserLabel();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void refreshUserLabel() {
        // Update the user label to reflect the new nickname
        JLabel userLabel = (JLabel) userPanel.getComponent(0);
        userLabel.setText("My Nickname : " + MainClass.me.getNickname());
        userPanel.revalidate();
        userPanel.repaint();
    }

    @Override
    public void contactAddedOrUpdated(User user) {
        // Update OnlineUsersPanel here
        SwingUtilities.invokeLater(this::updateOnlineUsersPanel);
        // Update the button text if this user is the active chat
        if (activeUserButton != null && chatSessions.containsKey(user.getIpAddress().getHostAddress())) {
            activeUserButton.setText(user.getNickname());
        }
    }

    private void updateOnlineUsersPanel() {
        // Assuming the first component in the panel is the "Online Contacts" label
        while (onlineUsersPanel.getComponentCount() > 1) {
            onlineUsersPanel.remove(1);
        }

        // Retrieve the updated list of online users
        List<User> onlineUsers = getOnlineUsers();

        // Check if there are no online users
        if (onlineUsers.isEmpty() || onlineUsers.stream().noneMatch(User::getStatus)) {
            // Hide or remove the ChatWindow
            chatPanel.setVisible(false); // This hides the chat panel
        } else {
            // Iterate through the list of online users and create a button for each
            for (User user : onlineUsers) {
                if (user.getStatus()) { // Check if the user is online
                    JButton userButton = new JButton(user.getNickname());
                    userButton.addActionListener(e -> switchChatWindow(user));
                    onlineUsersPanel.add(userButton);
                }
            }

            // Make sure the chat panel is visible if there are online users
            chatPanel.setVisible(true);
        }

        // Revalidate and repaint the panel to reflect the changes
        onlineUsersPanel.revalidate();
        onlineUsersPanel.repaint();
    }


    private void onDisconnect(JFrame frame) {
        try {
            MainClass.controller.toDisconnect(frame, MainClass.me);
        } catch (IOException ex) {
            ex.printStackTrace(); 
        }
    }

    @Override
    public void handle(UDPMessage received) {    }
    @Override
    public void changingNickname(String nickname)  {}
    @Override
    public void handleTCPMessage(TCPMessage msg)  {}
    @Override
    public void toCloseApp(JFrame frame) {
        MainClass.controller.toCloseApp(frame);
    }
    @Override
    public void toDisconnect(JFrame frame, User me) throws IOException {MainClass.controller.toDisconnect(frame, me);
    }
    @Override
    public void canRegister(JFrame frame) {    }
    @Override
    public void canLogin(JFrame frame) {    }
    @Override
    public void registerFunction(String nicknameInfo, String passwordInfo, JFrame frame) {}
    @Override
    public void loginFunction(String nicknameInput, String passwordInput, JFrame frame)  {}
    @Override
    public void mainWindowFunction(JFrame frame) {    }
}

