package chatsystem.contacts;

/** Error that is thrown when a contact is not found in the list */
public class ContactDoesntExist extends Exception {

    private final String nickname;

    public ContactDoesntExist(String nickname) {
        this.nickname = nickname;
    }

    @Override
    public String toString() {
        return "ContactDoesntExist{" +
                "nickname='" + nickname + '\'' +
                '}';
    }
}
