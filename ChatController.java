import java.util.ArrayList;
import java.util.List;

public class ChatController {
    private List<Chat> chats = new ArrayList<>();

    public Chat getOrCreateChat(User a, User b) {
        for (Chat chat : chats) {
            if (chat.isBetween(a, b))
                return chat;
        }
        Chat newChat = new Chat(a, b);
        chats.add(newChat);
        return newChat;
    }

    public void sendMessage(User from, User to, String message) {
        Chat chat = getOrCreateChat(from, to);
        chat.sendMessage(from, message);
    }
}
