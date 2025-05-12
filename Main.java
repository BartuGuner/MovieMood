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
}
