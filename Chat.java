import java.util.ArrayList;
import java.util.List;

public class Chat {
    private User user1;
    private User user2;
    private List<Message> messages = new ArrayList<>();

    public Chat(User user1, User user2) {
        this.user1 = user1;
        this.user2 = user2;
    }

    public void sendMessage(User from, String content) {
        messages.add(new Message(from, content));
    }

    public List<Message> getMessages() {
        return messages;
    }

    public boolean isBetween(User a, User b) {
        return (a == user1 && b == user2) || (a == user2 && b == user1);
    }

    public User getOtherUser(User current) {
        return current == user1 ? user2 : user1;
    }
}
