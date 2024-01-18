package chatsystem.contacts;

/** Error that is thrown when a contact is added twice to the list */
public class ContactAlreadyExists extends Exception {

    private final String nickname;

    public ContactAlreadyExists(String nickname) {
        this.nickname = nickname;
    }

    @Override
    public String toString() {
        return "ContactAlreadyExists{" +
                "nickname='" + nickname + '\'' +
                '}';
    }
}
