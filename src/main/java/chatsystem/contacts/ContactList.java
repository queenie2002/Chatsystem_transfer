package chatsystem.contacts;



import chatsystem.MainClass;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/** Contact List */
public class ContactList {

    private static final Logger LOGGER = LogManager.getLogger(MainClass.class);

    public interface Observer {
        void newContactAdded(User user) throws SQLException;
        void nicknameChanged(User newUser, String previousNickname);
    }
    List<Observer> observers = new ArrayList<>();
    public synchronized void addObserver(Observer obs) {
        this.observers.add(obs);
    }






    private final static ContactList INSTANCE = new ContactList();
    private final ArrayList<User> myContactList;
    private ContactList() { //i'm adding a constructor qui est empty
        myContactList = new ArrayList<User>();
    }
    public synchronized static ContactList getInstance() {
        return INSTANCE;
    }

    public synchronized ArrayList<User> getContactList() {
        return myContactList;
    }
    public synchronized ArrayList<User> getConnectedContactsList() {
        ArrayList<User> connectedList = new ArrayList<>();
        for (User u : myContactList) {
            if (u.getStatus()) {
                connectedList.add(u);
            }
        }
        return connectedList;
    }
    public synchronized void clearContactList() {
        myContactList.clear();
    }







    public synchronized void addContact(User user) throws ContactAlreadyExists, SQLException {
        String nickname = user.getNickname();
        if (existsContactWithNickname(nickname)) {
            throw new ContactAlreadyExists(nickname);
        }
        else {
            myContactList.add(user);
            LOGGER.info("Added user to myContactList " + nickname);
            for (Observer obs : observers) {
                obs.newContactAdded(user);
            }
        }
    }
    public synchronized void updateContact(User user) throws SQLException {
        int index=-1;
        for (User aUser : myContactList) {
            if (Objects.equals(aUser.getNickname(), user.getNickname())) {
                index = myContactList.indexOf(aUser);
            }
        }

        if (index != -1) {
            // Replace the element at the found index
            myContactList.set(index, user);
            LOGGER.info("Updated user in ContactList " + user.getNickname());
        } else {
            LOGGER.error("Couldn't find user in ContactList " + user.getNickname());
        }

        for (Observer obs : observers) {
            obs.newContactAdded(user);
        }
    }





    public synchronized User getContactWithNickname(String nickname) {
        for (User aUser : myContactList) {
            if (Objects.equals(aUser.getNickname(), nickname)) {
                return aUser;
            }
        }
        LOGGER.error("Couldn't find the user with nickname: " + nickname);
        return null;
    }



    public synchronized Boolean existsContactWithNickname(String nickname) {
        for (User aUser : myContactList) {
            if (Objects.equals(aUser.getNickname(), nickname)) {
                return true;
            }
        }
        return false;
    }



    public synchronized void deleteContactWithNickname(String nickname) {
        if (existsContactWithNickname(nickname)) {
            User aUser = getContactWithNickname(nickname);
            myContactList.remove(aUser);
            LOGGER.info("Deleted user in ContactList " + nickname);
        }
        else {
            LOGGER.error("Couldn't find user to delete in ContactList " + nickname);
        }

    }





    public synchronized void printContactList() {
        for (User user : myContactList) {
            printContact(user);
        }
    }
    public synchronized void printContact(User user) {

        //if it's me
        if (user.getNickname().equals(MainClass.me.getNickname())) {
            System.out.println(" nickname: "+user.getNickname()+" firstname: "+user.getFirstName()+" lastname: "+user.getLastName()+" birthday: "+user.getBirthday()+/*" password: "+user.getPassword()+*/" status: "+user.getStatus()+" ip address: "+user.getIpAddress());

        } else {
            //if it's someone else
            System.out.println(" nickname: "+user.getNickname()+" status: "+user.getStatus()+" ip address: "+user.getIpAddress());

        }





    }

}




