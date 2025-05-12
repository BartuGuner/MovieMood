import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class User {
    private String firstname;
    private String lastname;
    private String email;

    public String getEmail() {
        return email;
    }

    private String passwordHash;
    private String profilePicturePath; // optional
    private int userId; // unique 4-digit ID

    public String getPasswordHash() {
        return passwordHash;
    }

    private List<Movie> recentlyWatched = new ArrayList<>();
    private List<FilmList> filmLists = new ArrayList<>();
    private List<User> friends = new ArrayList<>();
    private List<Movie> recommendedMovies = new ArrayList<>();
    private Set<Movie> favoriteMovies = new HashSet<>();

    public User(String firstname, String lastname, String email, String passwordHash, int userId,
            String profilePicturePath) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.passwordHash = passwordHash;
        this.userId = userId;
        this.profilePicturePath = profilePicturePath;
    }

    public void watchMovie(Movie movie) {
        recentlyWatched.add(movie);
        if (recentlyWatched.size() > 20)
            recentlyWatched.remove(0); // keep recent 20
    }

    public void addFriend(User friend) {
        if (!friends.contains(friend))
            friends.add(friend);
    }

    public int getUserId() {
        return userId;
    }

    public String getProfilePicturePath() {
        return profilePicturePath;
    }

    public void addFilmList(FilmList list) {
        filmLists.add(list);
    }

    public List<Movie> getRecentlyWatched() {
        return recentlyWatched;
    }

    public List<User> getFriends() {
        return friends;
    }

    public String getUsername() {
        return firstname;
    }

    public List<FilmList> getFilmLists() {
        return filmLists;
    }

    public List<Movie> getRecommendedMovies() {
        return recommendedMovies;
    }

    public void setRecommendedMovies() {
        RecommendationEngine recommendationEngine = new RecommendationEngine();
        this.recommendedMovies = recommendationEngine.recommendMovies(this);
    }

    public void addFavorite(Movie movie) {
        favoriteMovies.add(movie);
    }

    public void removeFavorite(Movie movie) {
        favoriteMovies.remove(movie);
    }

    public boolean isFavorite(Movie movie) {
        return favoriteMovies.contains(movie);
    }

    public Set<Movie> getFavoriteMovies() {
        return favoriteMovies;
    }
}
