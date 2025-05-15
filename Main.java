/*import javax.swing.*;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        UserController userController = new UserController();
        FilmController filmController = new FilmController();
        MovieSeeder.seedMovies(filmController);
        // userController.register("Badu", "12345678");
        // userController.register("Ahmet", "12345678");
        // userController.register("Bartu", "12345678");
        // System.out.println(userController.getAllUsers());
        // System.out.println(userController.getUserByUsername("Badu"));
        System.out.println(filmController.searchByGenre("Thriller"));
    }
} */

import javax.swing.*;

public class Main {
    public static void main(String[] args) {

        UserController userController = new UserController();
        FilmController filmController = new FilmController();
        FilmListController filmListController = new FilmListController();
        
        // Film veritabanını doldur
        System.out.println("Filmler yükleniyor...");
        MovieSeeder.seedMovies(filmController);
        System.out.println("Toplam " + filmController.getAllMovies().size() + " film yüklendi");
        
        SwingUtilities.invokeLater(() -> {
            try {
                // Test kullanıcısını oluştur veya var olan kullanıcıyı al
                userController.register("test@example.com", "Test", "User", "password123");
                User testUser = userController.login("test@example.com", "password123");
                
                if (testUser != null) {
                    System.out.println("Test kullanıcısı başarıyla oluşturuldu/giriş yapıldı: " + testUser.getUsername());
                    
                    createTestFilmLists(testUser, filmController, filmListController);
                    
                    MyListPanel panel = new MyListPanel(testUser);
                    panel.setFilmController(filmController);
                    panel.setFilmListController(filmListController);
                    
                    System.out.println("MyListPanel başarıyla açıldı.");
                } else {
                    System.err.println("Kullanıcı giriş hatası!");
                }
            } catch (Exception e) {
                System.err.println("Test sırasında hata oluştu:");
                e.printStackTrace();
            }
        });
    }
    

    private static void createTestFilmLists(User user, FilmController filmController, FilmListController filmListController) {

        java.util.List<Movie> allMovies = filmController.getAllMovies();
        
        filmListController.createList(user, "My Favorites");
        FilmList favorites = filmListController.getFilmListByName(user, "My Favorites");
        
        // İlk 3 filmi favorilere ekle
        for (int i = 0; i < 3 && i < allMovies.size(); i++) {
            filmListController.addMovieToList(favorites, allMovies.get(i));
            System.out.println("Favorilere eklendi: " + allMovies.get(i).getTitle());
        }
        
        // "İzlenecekler" listesi oluştur
        filmListController.createList(user, "İzlenecekler");
        FilmList watchlist = filmListController.getFilmListByName(user, "İzlenecekler");
        
        // 4-7 arası filmleri izleneceklere ekle
        for (int i = 3; i < 7 && i < allMovies.size(); i++) {
            filmListController.addMovieToList(watchlist, allMovies.get(i));
            System.out.println("İzleneceklere eklendi: " + allMovies.get(i).getTitle());
        }

        filmListController.createList(user, "Bilim Kurgu");
        FilmList sciFi = filmListController.getFilmListByName(user, "Bilim Kurgu");

        
        for (Movie movie : allMovies) {
            if (movie.getGenres().contains("Science Fiction")) {
                filmListController.addMovieToList(sciFi, movie);
                System.out.println("Bilim Kurgu listesine eklendi: " + movie.getTitle());
            }
        }

        
        filmListController.createList(user, "Aile");
        FilmList deneme = filmListController.getFilmListByName(user, "Aile");

        for (Movie movie : allMovies) {
            if (movie.getGenres().contains("Family")) {
                filmListController.addMovieToList(deneme, movie);
                System.out.println("Aile Kurgu listesine eklendi: " + movie.getTitle());
            }
        }

        
        System.out.println("Test film listeleri başarıyla oluşturuldu.");
    }
}