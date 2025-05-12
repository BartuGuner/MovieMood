import java.util.ArrayList;
import java.util.List;

public class UserController {
    private static List<User> users = new ArrayList<>();
    private static List<Integer> usedIds = new ArrayList<>();

    private static int generateUniqueId() {
        int id;
        do {
            id = 1000 + (int) (Math.random() * 9000);
        } while (usedIds.contains(id));
        usedIds.add(id);
        return id;
    }

    public static boolean register(String email, String firstname, String lastname, String passwordHash) {
        if (getUserByEmail(email) != null)
            return false;
        int uniqueId = generateUniqueId();
        users.add(new User(firstname, lastname, email, passwordHash, uniqueId));
        return true;
    }

    public static User login(String email, String passwordHash) {
        User user = getUserByEmail(email);
        if (user != null && user.getPasswordHash().equals(passwordHash)) {
            return user;
        }
        return null;
    }

    public static User getUserByEmail(String email) {
        for (User user : users) {
            if (user.getEmail().equals(email))
                return user;
        }
        return null;
    }

    public static List<User> getAllUsers() {
        return users;
    }

    public static boolean addFriend(int userId, int friendId) {
        User user = getUserById(userId);
        User friend = getUserById(friendId);

        if (user != null && friend != null && user != friend) {
            user.addFriend(friend);
            friend.addFriend(user); // optional: mutual friendship
            return true;
        }
        return false;
    }

    public static boolean removeFriend(int userId, int friendId) {
        User user = getUserById(userId);
        User friend = getUserById(friendId);

        if (user != null && friend != null) {
            user.getFriends().remove(friend);
            friend.getFriends().remove(user); // optional: mutual removal
            return true;
        }
        return false;
    }

    public static User getUserById(int id) {
        for (User user : users) {
            if (user.getUserId() == id)
                return user;
        }
        return null;
    }
}
