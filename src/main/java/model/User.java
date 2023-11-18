package model;

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

    public static int uniqueNickname = 0;  //0 si on sait pas s'il est unique, 1 si PAS unique et 2 si unique
    
    //CONSTRUCTOR
    public User (String idUser, String nickname, String firstName, String lastName, String birthday, String password, Boolean status, InetAddress ipAddress){
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
    public String getId() { return this.idUser; }
    public String getNickname (){ return this.nickname;}
    public String getFirstName (){ return this.firstName;}
    public String getLastName(){ return this.lastName;}
    public String getBirthday (){ return this.birthday;}
    public String getPassword (){ return this.password;}
    public Boolean getStatus (){ return this.status;}

    public InetAddress getIpAddress() { return this.ipAddress; }

    public void setId(String idUser) { this.idUser = idUser; }
    public void setNickname (String nickname) { this.nickname=nickname;}
    public void setFirstName (String firstName) { this.firstName=firstName;}
    public void setLastName(String lastName) { this.lastName=lastName;}
    public void setBirthday (String birthday) { this.birthday=birthday;}
    public void setPassword (String password) { this.password=password;}
    public void setStatus (Boolean status) { this.status=status;}

    public void setIpAddress(InetAddress ipAddress) { this.ipAddress=ipAddress; }

} 
