import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class FilmController {
    private static List<Movie> allMovies = new ArrayList<>();

    public static void addMovie(Movie movie) {
        allMovies.add(movie);
    }

    public static List<Movie> getAllMovies() {
        return allMovies;
    }

    public static List<Movie> searchByGenre(String genre) {
        return allMovies.stream()
                .filter(m -> m.getGenres().contains(genre))
                .collect(Collectors.toList());
    }

    public static List<Movie> getFavorites(User user) {
        return new ArrayList<>(user.getFavoriteMovies());
    }

    public static Map<String, Object> getMovieByTitle(String title) {
        for (Movie movie : allMovies) {
            if (movie.getTitle().equalsIgnoreCase(title)) {
                return movie.getMovie();
            }
        }
        return null; 
    }

    public static List<Movie> searchByTitle(String query) {
        String lowerQuery = query.toLowerCase();
        return allMovies.stream()
                .filter(m -> m.getTitle().toLowerCase().contains(lowerQuery))
                .collect(Collectors.toList());
    }

    // Add a comment to a movie
    public static void addCommentToMovie(Movie movie, User user, String text) {
        Comment comment = new Comment(user, text);
        movie.addComment(comment);
    }

    public static boolean deleteComment(Movie movie, User user, String text) {
    List<Comment> comments = movie.getComments();
    for (int i = 0; i < comments.size(); i++) {
        Comment c = comments.get(i);
        if (c.getAuthor().equals(user) && c.getText().equals(text)) {
            comments.remove(i);
            return true;
        }
    }
    return false; // comment not found or not authored by this user
}

    public static List<Comment> getUserComments(User user) {
        List<Comment> result = new ArrayList<>();
        for (Movie movie : allMovies) {
            for (Comment comment : movie.getComments()) {
                if (comment.getAuthor().equals(user)) {
                    result.add(comment);
                }
            }
        }
        return result;
    }

    // Get the latest (most recent) comment made by a user
    // Get the latest comment by a user on a specific movie
    public static Comment getLatestUserComment(Movie movie, User user) {
        Comment latest = null;
        for (Comment comment : movie.getComments()) {
            if (comment.getAuthor().equals(user)) {
                if (latest == null || comment.getTimestamp().isAfter(latest.getTimestamp())) {
                    latest = comment;
                }
            }
        }
        return latest;
    }

    public static void rateMovie(Movie movie, User user, double rating) {
        movie.rateMovie(user, rating);
    }
    public static List<Comment> getCommentsForMovie(Movie movie) {
        return movie.getComments();
    }

    public static double getAverageRating(Movie movie) {
        return movie.getAverageRating();
    }

    // Get rating of a specific user
    public static Double getUserRating(Movie movie, User user) {
        return movie.getRatingByUser(user);
    }

    public static boolean editComment(Movie movie, User user, String oldText, String newText) {
        for (Comment c : movie.getComments()) {
            if (c.getAuthor().equals(user) && c.getText().equals(oldText)) {
                c.setText(newText);
                return true;
            }
        }
        return false; // comment not found or not authored by this user
    }

    public static List<Movie> getRecommendedMovies(User user) {
        user.setRecommendedMovies();
        return user.getRecommendedMovies();
    }

    public FilmList createRecommendedMovieList(User user, String listName) {
        try {
            // Önce RecommendationEngine'i başlat
            RecommendationEngine.initializeMovies();
            
            // Kullanıcı için önerilen filmleri al
            List<Movie> recommendedMovies = RecommendationEngine.recommendMovies(user);
            
            // Kullanıcı için yeni bir film listesi oluştur
            FilmListController.createList(user, listName);
            
            // Oluşturulan film listesini al
            FilmList recommendedList = FilmListController.getFilmListByName(user, listName);
            
            if (recommendedList != null) {
                // Önerilen filmleri yeni listeye ekle
                for (Movie movie : recommendedMovies) {
                    FilmListController.addMovieToList(recommendedList, movie);
                }
                
                System.out.println("Created recommended list '" + listName + "' with " + 
                                  recommendedMovies.size() + " movies");
                
                return FilmListController.getFilmListByName(user, listName);
            } else {
                System.err.println("Error: Could not find newly created list '" + listName + "'");
                return null;
            }
        } catch (Exception e) {
            System.err.println("Error creating recommended movie list: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    public static List<Movie> searchByReleaseYearInterval(int startYear, int endYear) {
        return allMovies.stream()
                .filter(m -> m.getReleaseDate() >= startYear && m.getReleaseDate() <= endYear)
                .collect(Collectors.toList());
    }
}