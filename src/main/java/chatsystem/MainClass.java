package chatsystem;

import chatsystem.controller.DatabaseMethods;
import chatsystem.controller.UDPSendMessage;
import chatsystem.controller.UDPReceiveMessage;
import chatsystem.model.User;
import chatsystem.view.Beginning;

import java.io.IOException;
import java.net.InetAddress;
import java.sql.SQLException;

public class MainClass {

    //we create an empty user to keep my information
    public static User me;

    static {
        try {
            me = new User("[]", "[]", "[]", "[]" , "[]", null, InetAddress.getByName("0.0.0.0"));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    public static void main(String[] args) throws IOException, SQLException {

        UDPReceiveMessage UDPReceiveMessage = new UDPReceiveMessage();
        UDPSendMessage UDPSendMessage = new UDPSendMessage();
        UDPReceiveMessage.start();

        DatabaseMethods.startConnection(me);


        Beginning beginning = new Beginning(UDPReceiveMessage, UDPSendMessage);

        /*tcpServer myServer = new tcpServer();

        Chat chat = new Chat("content","date","fromUserIP","toUserIP");
        myServer.addObserver(new tcpServer.MessageObserver() {
            @Override
            public void handleMessage(String msg) throws SQLException {
                DatabaseMethods.addMessage(chat);
            }
        });*/
    }


}
