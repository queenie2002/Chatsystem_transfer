package chatsystem.observers;

import chatsystem.contacts.User;
import chatsystem.network.TCPMessage;
import chatsystem.network.UDPMessage;

import javax.swing.*;
import java.io.IOException;
import java.net.UnknownHostException;
import java.sql.SQLException;

public interface MyObserver {



    // ---------------------------DATABASE-------------------------//

    /** Method that is called each time a contact is added/updated */
    void contactAddedOrUpdated(User user);



    // ---------------------------UDP SERVER-------------------------//

    /** Method that is called each time a message is received*/
    void handle(UDPMessage received) throws RuntimeException;

    void changingNickname(String nickname) throws IOException, SQLException;





    // ---------------------------TCP SERVER-------------------------//
    void handleTCPMessage(TCPMessage msg);





    // ---------------------------VIEW-------------------------//

    /** Method that is called when we're trying to close the app */
    void toCloseApp(JFrame frame);
    /** Method that is called when we're trying to close the app and to disconnect as well*/
    void toDisconnect(JFrame frame, User me) throws IOException ;


    /** Method that is called when we're trying to see if we can register */
    void canRegister(JFrame frame) throws IOException;
    /** Method that is called when we're trying to see if we can login */
    void canLogin(JFrame frame) ;

    /** Method that is called when we're trying to register */
    void registerFunction(String nicknameInfo, String passwordInfo,JFrame frame);
    /** Method that is called when we're trying to login */
    void loginFunction(String nicknameInput, String passwordInput, JFrame frame) throws UnknownHostException, SQLException;

}
