package chatsystem.observers;

import chatsystem.contacts.User;

import java.sql.SQLException;

public interface MyObserver {
    void showOnlineContacts();
    void showAllContacts();

    void newContactAdded(User user) throws SQLException;
    void nicknameChanged(User newUser, String previousNickname);




}
