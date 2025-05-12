
import java.util.*;

public class TestMain {
    public static void main(String[] args) {
        // Instantiate controllers
        UserController userController = new UserController();
        FilmController filmController = new FilmController();
        FilmListController filmListController = new FilmListController();
        ChatController chatController = new ChatController();

        // Register and login users
        // userController.register("Alice", "pass1");
        // userController.register("Bob", "pass2");
        User alice = userController.login("Alice", "pass1");
        User bob = userController.login("Bob", "pass2");

        System.out.println("== Registered Users ==");
        userController.getAllUsers().forEach(u -> System.out.println(u.getUsername()));

        // Add friendship
        userController.addFriend(alice.getUserId(), bob.getUserId());
        System.out.println("== Alice's Friends ==");
        alice.getFriends().forEach(f -> System.out.println(f.getUsername()));

        // Seed movies
        MovieSeeder.seedMovies(filmController);
        System.out.println("== All Movies ==");
        filmController.getAllMovies().forEach(m -> System.out.println(m.getTitle()));

        // Search and comment
        Movie movie = filmController.searchByTitle("Rust").get(0);
        filmController.addCommentToMovie(movie, alice, "Amazing movie!");
        filmController.addCommentToMovie(movie, bob, "Loved it too!");

        System.out.println("== Comments on 'Rust' ==");
        filmController.getCommentsForMovie(movie).forEach(System.out::println);

        // Edit comment
        filmController.editComment(movie, alice, "Amazing movie!", "Absolutely stunning!");
        System.out.println("== After Edit ==");
        filmController.getCommentsForMovie(movie).forEach(System.out::println);

        // Rate movie
        filmController.rateMovie(movie, alice, 8.5);
        filmController.rateMovie(movie, bob, 9.0);
        System.out.println("Average Rating for Rust: " + filmController.getAverageRating(movie));

        // Favorites and recommendation
        alice.addFavorite(movie);
        System.out.println("Alice's Favorites:");
        filmController.getFavorites(alice).forEach(m -> System.out.println(m.getTitle()));
        System.out.println("Recommended for Alice:");
        filmController.getRecommendedMovies(alice).forEach(m -> System.out.println(m.getTitle()));

        // Film list operations
        filmListController.createList(alice, "Watch Later");
        FilmList list = filmListController.getFilmListByName(alice, "Watch Later");
        filmListController.addMovieToList(list, movie);
        System.out.println("Watch Later List:");
        list.getMovies().forEach(m -> System.out.println(m.getTitle()));

        // Chat messaging
        chatController.sendMessage(alice, bob, "Hi Bob!");
        chatController.sendMessage(bob, alice, "Hey Alice!");
        Chat chat = chatController.getOrCreateChat(alice, bob);
        System.out.println("== Chat between Alice and Bob ==");
        chat.getMessages().forEach(m -> System.out.println(m.getSender().getUsername() + ": " + m.getContent()));
    }
}
