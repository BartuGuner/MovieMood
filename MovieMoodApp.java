import javax.swing.*;

public class MovieMoodApp {
    public static void main(String[] args) {
        try {

            // Controller nesnelerini oluştur
            UserController userController = new UserController();
            FilmController filmController = new FilmController();
            FilmListController filmListController = new FilmListController();

            // Film veritabanını doldur
            System.out.println("Filmler yükleniyor...");
            MovieSeederWithYoutube.seedMovies(filmController);
            System.out.println("Toplam " + filmController.getAllMovies().size() + " film yüklendi");
            UserController.register("mert@gmail.com", "Ahmet", "Mehmet", "12345678");
            UserController.register("Musti@gmail.com", "çapkin", "kamu", "12345678");
            UserController.getAllUsers().get(1).setId(0);
            // Set system look and feel
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        SwingUtilities.invokeLater(() -> new MovieMoodLoginUI());
    }
}
