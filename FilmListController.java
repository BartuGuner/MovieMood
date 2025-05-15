import java.util.ArrayList;
import java.util.List;

public class FilmListController {
    public static void createList(User user, String listName) {
        user.addFilmList(new FilmList(listName));
    }

public static void addMovieToList(FilmList list, Movie movie) {
    if (!list.getMovies().contains(movie)) {
        list.addMovie(movie);
    }
}

    public static void removeMovieFromList(FilmList list, Movie movie) {
        list.removeMovie(movie);
    }

    public static void removeFilmList(User user, String listName) {
        FilmList target = null;
        for (FilmList list : user.getFilmLists()) {
            if (list.getName().equalsIgnoreCase(listName)) {
                target = list;
                break;
            }
        }
        if (target != null) {
            user.getFilmLists().remove(target);
        }
    }

    public static List<FilmList> getListsWithMovie(User user, Movie movie) {
        List<FilmList> result = new ArrayList<>();
        for (FilmList list : user.getFilmLists()) {
            if (list.getMovies().contains(movie)) {
                result.add(list);
            }
        }
        return result;
    }

    public static List<FilmList> getAllFilmLists(User user) {
        return user.getFilmLists();
    }

    public static FilmList getFilmListByName(User user, String listName) {
        for (FilmList list : user.getFilmLists()) {
            if (list.getName().equalsIgnoreCase(listName)) {
                return list;
            }
        }
        return null;
    }
}
