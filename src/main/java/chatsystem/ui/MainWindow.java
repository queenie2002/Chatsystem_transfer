package chatsystem.ui;

import chatsystem.MainClass;
import chatsystem.contacts.ContactList;
import chatsystem.contacts.User;
import chatsystem.network.TCPMessage;
import chatsystem.network.UDPMessage;
import chatsystem.observers.MyObserver;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.UnknownHostException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

public class MainWindow extends JFrame implements MyObserver {

    private JPanel userPanel;
    private JPanel onlineUsersPanel;
    private JPanel chatPanel;
    private static HashMap<String,ChatWindow2> chatSessions = new HashMap<>();

    public MainWindow(){
        setTitle("Chat System");
        setSize(800,800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        initializeUserPanel();
        initializeOnlineUsersPanel();
        initializeChatPanel();

        add(userPanel,BorderLayout.NORTH);
        add(onlineUsersPanel, BorderLayout.WEST);
        add(chatPanel, BorderLayout.CENTER);
    }

    private void initializeUserPanel(){
        userPanel = new JPanel();
        JLabel userLabel = new JLabel("My Nickname : " + MainClass.me.getNickname());
        userPanel.add(userLabel);

        JButton changeNicknameButton = new JButton("Change Nickname");
        changeNicknameButton.addActionListener(e -> onChangeNickname());
        userPanel.add(changeNicknameButton);

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
            ChatWindow2 chatWindow = new ChatWindow2(user.getIpAddress().getHostAddress());
            chatSessions.put(userKey, chatWindow);
            chatPanel.add(chatWindow, userKey);
        }
        cardLayout.show(chatPanel, userKey);
    }

    private List<User> getOnlineUsers(){
        return ContactList.getInstance().getConnectedContactsList();
    }

    public static ChatWindow2 getChatWindowForUser(String userKey) {
        return chatSessions.get(userKey);
    }

    private void onChangeNickname() {
        String newNickname = JOptionPane.showInputDialog(this, "Enter new nickname:");
        if (newNickname != null && !newNickname.trim().isEmpty()) {
            try {
                MainClass.controller.changingNickname(newNickname);
                refreshUserLabel();
            } catch (IOException | SQLException e) {
                e.printStackTrace();
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
        System.out.println("MainWindow contact added or updated");
    }

    private void updateOnlineUsersPanel() {
        // Clear the existing user buttons
        onlineUsersPanel.removeAll();

        // Retrieve the updated list of online users
        List<User> onlineUsers = getOnlineUsers();

        // Iterate through the list of online users and create a button for each
        for (User user : onlineUsers) {
            JButton userButton = new JButton(user.getNickname());
            userButton.addActionListener(e -> switchChatWindow(user));
            onlineUsersPanel.add(userButton);
        }

        // Revalidate and repaint the panel to reflect the changes
        onlineUsersPanel.revalidate();
        onlineUsersPanel.repaint();
    }

    @Override
    public void handle(UDPMessage received) throws RuntimeException {

    }

    @Override
    public void changingNickname(String nickname) throws IOException, SQLException {

    }

    @Override
    public void handleTCPMessage(TCPMessage msg) throws SQLException, UnknownHostException {

    }

    @Override
    public void toCloseApp(JFrame frame) {

    }

    @Override
    public void toDisconnect(JFrame frame, User me) throws IOException {

    }

    @Override
    public void canRegister(JFrame frame) throws IOException {

    }

    @Override
    public void canLogin(JFrame frame) {

    }

    @Override
    public void registerFunction(String nicknameInfo, String firstNameInfo, String lastNameInfo, String birthdayInfo, String passwordInfo, JFrame frame) throws SQLException {

    }

    @Override
    public void loginFunction(String nicknameInput, String passwordInput, JFrame frame) throws UnknownHostException, SQLException {

    }

    @Override
    public void showOnlineContacts() {

    }

    @Override
    public void showAllContacts() {

    }
}

