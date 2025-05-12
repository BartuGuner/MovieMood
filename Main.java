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
        // Controller nesnelerini oluştur
        UserController userController = new UserController();
        FilmController filmController = new FilmController();
        FilmListController filmListController = new FilmListController();
        
        // Film veritabanını doldur
        System.out.println("Filmler yükleniyor...");
        MovieSeeder.seedMovies(filmController);
        System.out.println("Toplam " + filmController.getAllMovies().size() + " film yüklendi");
        
        // GUI uygulamalarında SwingUtilities.invokeLater kullanmak önemlidir
        SwingUtilities.invokeLater(() -> {
            try {
                // Test kullanıcısını oluştur veya var olan kullanıcıyı al
                userController.register("test@example.com", "Test", "User", "password123");
                User testUser = userController.login("test@example.com", "password123");
                
                if (testUser != null) {
                    System.out.println("Test kullanıcısı başarıyla oluşturuldu/giriş yapıldı: " + testUser.getUsername());
                    
                    // Test kullanıcısı için birkaç liste oluştur
                    createTestFilmLists(testUser, filmController, filmListController);
                    
                    // MyListPanel'ı test etmek için başlat
                    MyListPanel panel = new MyListPanel(testUser);
                    // Controller nesnelerini panel'e ata
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
    
    /**
     * Test kullanıcısı için film listeleri oluşturup içlerine film ekler
     */
    private static void createTestFilmLists(User user, FilmController filmController, FilmListController filmListController) {
        // Tüm filmlerin listesini al
        java.util.List<Movie> allMovies = filmController.getAllMovies();
        
        // "Favorilerim" listesi oluştur
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
        
        // "Bilim Kurgu" listesi oluştur
        filmListController.createList(user, "Bilim Kurgu");
        FilmList sciFi = filmListController.getFilmListByName(user, "Bilim Kurgu");

        
        // "Science Fiction" türündeki filmleri ekle
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