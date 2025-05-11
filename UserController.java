import java.util.ArrayList;
import java.util.List;

public class UserController {
    private List<User> users = new ArrayList<>();
    private List<Integer> usedIds = new ArrayList<>();

    private int generateUniqueId() {
        int id;
        do {
            id = 1000 + (int) (Math.random() * 9000);
        } while (usedIds.contains(id));
        usedIds.add(id);
        return id;
    }

    public boolean register(String username, String passwordHash) {
        if (getUserByUsername(username) != null)
            return false;
        int uniqueId = generateUniqueId();
        users.add(new User(username, passwordHash, uniqueId, ""));
        return true;
    }

    public User login(String username, String passwordHash) {
        User user = getUserByUsername(username);
        if (user != null && user.getPasswordHash().equals(passwordHash)) {
            return user;
        }
        return null;
    }

    public User getUserByUsername(String username) {
        for (User user : users) {
            if (user.getUsername().equals(username))
                return user;
        }
        return null;
    }

    public List<User> getAllUsers() {
        return users;
    }

    public boolean addFriend(int userId, int friendId) {
        User user = getUserById(userId);
        User friend = getUserById(friendId);

        if (user != null && friend != null && user != friend) {
            user.addFriend(friend);
            friend.addFriend(user); // optional: mutual friendship
            return true;
        }
        return false;
    }

    public boolean removeFriend(int userId, int friendId) {
        User user = getUserById(userId);
        User friend = getUserById(friendId);

        if (user != null && friend != null) {
            user.getFriends().remove(friend);
            friend.getFriends().remove(user); // optional: mutual removal
            return true;
        }
        return false;
    }

    public User getUserById(int id) {
        for (User user : users) {
            if (user.getUserId() == id)
                return user;
        }
        return null;
    }
}
