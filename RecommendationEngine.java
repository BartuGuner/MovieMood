
// RecommendationEngine.java
import java.util.*;

public class RecommendationEngine {
    private static List<Movie> allMovies;

    public RecommendationEngine() {
        FilmController filmController = new FilmController();
        this.allMovies = filmController.getAllMovies();
    }

    public static List<Movie> recommendMovies(User user) {
        Map<String, Integer> genreFreq = new HashMap<>();

        for (Movie m : user.getRecentlyWatched()) {
            for (String g : m.getGenres()) {
                genreFreq.put(g, genreFreq.getOrDefault(g, 0) + 1);
            }
        }

        List<String> topGenres = genreFreq.entrySet().stream()
                .sorted((a, b) -> b.getValue() - a.getValue())
                .limit(3)
                .map(Map.Entry::getKey)
                .toList();

        List<Movie> recommended = new ArrayList<>();
        for (Movie movie : allMovies) {
            if (user.getRecentlyWatched().contains(movie))
                continue;
            for (String genre : movie.getGenres()) {
                if (topGenres.contains(genre)) {
                    recommended.add(movie);
                    break;
                }
            }
        }

        return recommended;
    }
}
