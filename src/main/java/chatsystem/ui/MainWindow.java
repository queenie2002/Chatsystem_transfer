package chatsystem.ui;

import chatsystem.MainClass;
import chatsystem.contacts.ContactList;
import chatsystem.contacts.User;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.List;

public class MainWindow extends JFrame {
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

}
