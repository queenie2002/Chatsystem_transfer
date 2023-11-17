package model;

import java.io.*;
import java.net.*;

public class User{
    //ATTRIBUTES
    private int idUser; 
    private String nickname;
    private String firstName;
    private String lastName;
    private String birthday;
    private String password;                        //ATTENTION PASSWORD IS A STRING
    private Boolean status;                         //if disconnected = false, connected = true

    private InetAddress ipAddress;
    
    //CONSTRUCTOR
    public User (int idUser, String nickname, String firstName, String lastName, String birthday, String password, Boolean status, InetAddress ipAddress){
        this.idUser = idUser;
        this.nickname = nickname;
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthday=birthday;
        this.password=password;
        this.status=status;
        this.ipAddress=ipAddress;
    } 

    //METHODS
    public int getId() { return this.idUser; }
    public String getNickname (){ return this.nickname;}
    public String getFirstName (){ return this.firstName;}
    public String getLastName(){ return this.lastName;}
    public String getBirthday (){ return this.birthday;}
    public String getPassword (){ return this.password;}
    public Boolean getStatus (){ return this.status;}

    public InetAddress getIpAddress() { return this.ipAddress; }

    /*public InetAddress getIpAddress() {
        InetAddress localHost = null;
        try {
            localHost = InetAddress.getLocalHost();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return localHost;
    }*/

    /*public int createIdUser(ArrayList<User> ContactList) {
        return (ContactList.getChatFromIndex(ContactList.size() - 1)).getChatId() + 1 ;
    }*/

} 
