package model;
public class User{
    //ATTRIBUTES
    private int idUser; 
    private String nickname;
    private String firstName;
    private String lastName;
    private String birthday;                        //ATTENTION BIRTHDAY IS A STRING
    private String password; 
    private Boolean status;                         //if disconnected = false, connected = true
    
    //CONSTRUCTOR
    public User (int idUser, String nickname, String firstName, String lastName, String birthday, String password, String status){
        this.idUser = idUser;
        this.nickname = nickname;
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthday=birthday;
        this.password=password;
        this.status=status;
    } 

    //METHODS
    public int getId() { return this.idUser; }
    public String getNickname (){ return this.nickname;}
    public String getFirstName (){ return this.firstName;}
    public String getLastName(){ return this.lastName;}
    public int getBirthday (){ return this.birthday;}
    public String getPassword (){ return this.password;}
    public String getStatus (){ return this.status;}




} 
