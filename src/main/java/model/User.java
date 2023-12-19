package model;

import controller.ReceiveMessage;
import controller.SendMessage;

import java.io.*;
import java.net.*;

public class User{
    //ATTRIBUTES
    private String idUser;
    private String nickname;
    private String firstName;
    private String lastName;
    private String birthday;
    private String password;                        //ATTENTION PASSWORD IS A STRING
    private Boolean status;                         //if disconnected = false, connected = true

    private InetAddress ipAddress;

    private ReceiveMessage receiveMessage;
    private SendMessage sendMessage;
    private static int socket=1025;

    //CONSTRUCTOR
    public User (String idUser, String nickname, String firstName, String lastName, String birthday, String password, Boolean status, InetAddress ipAddress) throws SocketException {
        this.idUser = idUser;
        this.nickname = nickname;
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthday=birthday;
        this.password=password;
        this.status=status;
        this.ipAddress=ipAddress;
        socket+=1;
        receiveMessage = new ReceiveMessage(socket);
        sendMessage = new SendMessage(socket);
        receiveMessage.start(); //quand est ce quon larrete?
    }

    public User(String testUser, InetAddress localHost) {
    }

    //METHODS
    public String getId() { return this.idUser; }
    public String getNickname (){ return this.nickname;}
    public String getFirstName (){ return this.firstName;}
    public String getLastName(){ return this.lastName;}
    public String getBirthday (){ return this.birthday;}
    public String getPassword (){ return this.password;}
    public Boolean getStatus (){ return this.status;}

    public InetAddress getIpAddress() { return this.ipAddress; }

    public int getSocket() { return socket; }
    public ReceiveMessage getReceiveMessage() { return this.receiveMessage; }
    public SendMessage getSendMessage() { return this.sendMessage; }

    public void setId(String idUser) { this.idUser = idUser; }
    public void setNickname (String nickname) { this.nickname=nickname;}
    public void setFirstName (String firstName) { this.firstName=firstName;}
    public void setLastName(String lastName) { this.lastName=lastName;}
    public void setBirthday (String birthday) { this.birthday=birthday;}
    public void setPassword (String password) { this.password=password;}
    public void setStatus (Boolean status) { this.status=status;}

    public void setIpAddress(InetAddress ipAddress) { this.ipAddress=ipAddress; }

} 
