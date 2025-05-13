import java.util.*;

public class RecommendationEngine {
    private static List<Movie> allMovies;
    
    // Statik başlatıcı eklendi - allMovies değişkenini başlatmak için
    public static void initializeMovies() {
        if (allMovies == null) {
            allMovies = FilmController.getAllMovies();
            System.out.println("RecommendationEngine initialized with " + allMovies.size() + " movies");
        }
    }
    
    // Constructor, başlatma işlemini statik metoda yönlendirir
    public RecommendationEngine() {
        initializeMovies();
    }
    
    public static List<Movie> recommendMovies(User user) {
        // Filmler başlatılmamışsa başlat
        if (allMovies == null) {
            initializeMovies();
        }
        
        // Filmler hala null ise (örn. filmler yüklenmemişse), boş liste döndür
        if (allMovies == null || allMovies.isEmpty()) {
            System.out.println("Warning: No movies available for recommendations");
            return new ArrayList<>();
        }
        
        Map<String, Integer> genreFreq = new HashMap<>();
        
        // Kullanıcının izlediği filmlere göre tür sıklıklarını hesapla
        if (user.getRecentlyWatched() != null && !user.getRecentlyWatched().isEmpty()) {
            for (Movie m : user.getRecentlyWatched()) {
                for (String g : m.getGenres()) {
                    genreFreq.put(g, genreFreq.getOrDefault(g, 0) + 1);
                }
            }
        } else if (user.getFavoriteMovies() != null && !user.getFavoriteMovies().isEmpty()) {
            // Son izlenen filmler yoksa, favori filmleri kullan
            for (Movie m : user.getFavoriteMovies()) {
                for (String g : m.getGenres()) {
                    genreFreq.put(g, genreFreq.getOrDefault(g, 0) + 1);
                }
            }
        } else {
            // Kullanıcı hiç film izlememiş veya favori eklememiş - rastgele film öner
            System.out.println("User has no watched or favorite movies. Recommending random selection.");
            List<Movie> randomMovies = new ArrayList<>(allMovies);
            Collections.shuffle(randomMovies);
            return randomMovies.subList(0, Math.min(5, randomMovies.size()));
        }
        
        // En popüler türleri bul
        List<String> topGenres = genreFreq.entrySet().stream()
                .sorted((a, b) -> b.getValue() - a.getValue())
                .limit(3)
                .map(Map.Entry::getKey)
                .toList();
        
        List<Movie> recommended = new ArrayList<>();
        // Kullanıcının izlemediği ve tercih ettiği türdeki filmleri öner
        for (Movie movie : allMovies) {
            // Kullanıcının zaten izlediği filmleri önerme
            if (user.getRecentlyWatched() != null && user.getRecentlyWatched().contains(movie)) {
                continue;
            }
            // Kullanıcının zaten favori listesinde olan filmleri önerme
            if (user.getFavoriteMovies() != null && user.getFavoriteMovies().contains(movie)) {
                continue;
            }
            
            // Filmin türleri, kullanıcının tercih ettiği türlerde mi?
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