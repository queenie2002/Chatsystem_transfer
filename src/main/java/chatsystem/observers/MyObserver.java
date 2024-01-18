package chatsystem.observers;

import chatsystem.contacts.User;
import chatsystem.network.UDPMessage;

import javax.swing.*;
import java.io.IOException;
import java.net.UnknownHostException;
import java.sql.SQLException;

public interface MyObserver {



    // ---------------------------DATABASE-------------------------//

    /** Method that is called each time a contact is added/updated */
    void newContactAdded(User user) throws SQLException;



    // ---------------------------UDP SERVER-------------------------//

    /** Method that is called each time a message is received*/
    void handle(UDPMessage received);



    // ---------------------------VIEW-------------------------//

    void toCloseApp(JFrame frame);
    void toDisconnect(JFrame frame) throws IOException ;

    void canRegister(JFrame frame)throws SQLException, IOException;
    void canLogin(JFrame frame) throws SQLException ;


    void loginFunction(String nicknameInput, String passwordInput, JFrame frame) throws UnknownHostException, SQLException;
    void registerFunction(String nicknameInfo,String firstNameInfo, String lastNameInfo, String birthdayInfo, String passwordInfo,JFrame frame) throws SQLException;








    void showOnlineContacts();
    void showAllContacts();



}
