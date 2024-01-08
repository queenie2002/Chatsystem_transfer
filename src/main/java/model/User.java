package model;

import controller.ReceiveMessage;
import controller.SendMessage;

import java.io.*;
import java.net.*;

public class User{
    //ATTRIBUTES
    private String nickname;
    private String firstName;
    private String lastName;
    private String birthday;
    private String password;                        //ATTENTION PASSWORD IS A STRING
    private Boolean status;                         //if disconnected = false, connected = true

    private InetAddress ipAddress;
    private ReceiveMessage receiveMessage;
    private SendMessage sendMessage;
    private static int mySocket=2000;
    private int theirSocket=3000;


    //CONSTRUCTOR
    public User (String nickname, String firstName, String lastName, String birthday, String password, Boolean status, InetAddress ipAddress) {
        this.nickname = nickname;
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthday=birthday;
        this.password=password;
        this.status=status;
        this.ipAddress=ipAddress;
    }

    public User(String testUser, InetAddress localHost) {
    }

    //METHODS
    public String getNickname (){ return this.nickname;}
    public String getFirstName (){ return this.firstName;}
    public String getLastName(){ return this.lastName;}
    public String getBirthday (){ return this.birthday;}
    public String getPassword (){ return this.password;}
    public Boolean getStatus (){ return this.status;}

    public InetAddress getIpAddress() { return this.ipAddress; }

    public int getMySocket() { return mySocket;}
    public int getTheirSocket() { return theirSocket;}
    public ReceiveMessage getReceiveMessage() { return this.receiveMessage; }
    public SendMessage getSendMessage() { return this.sendMessage; }
    public void setNickname (String nickname) { this.nickname=nickname;}
    public void setFirstName (String firstName) { this.firstName=firstName;}
    public void setLastName(String lastName) { this.lastName=lastName;}
    public void setBirthday (String birthday) { this.birthday=birthday;}
    public void setPassword (String password) { this.password=password;}
    public void setStatus (Boolean status) { this.status=status;}

    public void setIpAddress(InetAddress ipAddress) { this.ipAddress=ipAddress; }
    public void setTheirSocket(int theirSocket) { this.theirSocket=theirSocket; }
    public void setMySocket(int newSocket) { mySocket=newSocket; }

} 
