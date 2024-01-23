package chatsystem.contacts;

import java.net.*;


/** A User */
public class User{
    //ATTRIBUTES
    private String nickname;
    private String password;
    private Boolean status;                         //if disconnected = false, connected = true
    private InetAddress ipAddress;

    //CONSTRUCTORS
    public User (String nickname, String password, Boolean status, InetAddress ipAddress) {
        this.nickname = nickname;
        this.password=password;
        this.status=status;
        this.ipAddress=ipAddress;
    }

    public User() {
    }






    //GETTERS
    public String getNickname (){ return this.nickname;}
    public String getPassword (){ return this.password;}
    public Boolean getStatus (){ return this.status;}
    public InetAddress getIpAddress() { return this.ipAddress; }





    //SETTERS
    public void setNickname (String nickname) { this.nickname=nickname;}
    public void setPassword (String password) { this.password=password;}
    public void setStatus (Boolean status) { this.status=status;}
    public void setIpAddress(InetAddress ipAddress) { this.ipAddress=ipAddress; }

}