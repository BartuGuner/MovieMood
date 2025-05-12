import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.List;
import javax.imageio.ImageIO;
import javax.swing.*;

public class GenrePage extends JFrame {

    private FilmController controller = new FilmController();

    //for release year 
    public GenrePage(String label, int startYear, int endYear) {
        MovieSeeder.seedMovies(controller);
        List<Movie> movies = controller.searchByReleaseYearInterval(startYear, endYear);
        initializeUI(label + " Movies", movies);
    }

    //for genre 
    public GenrePage(String genre) {
        MovieSeeder.seedMovies(controller);
        List<Movie> movies = controller.searchByGenre(genre);
        initializeUI(genre.toUpperCase() + " Movies", movies);
    }

    private void initializeUI(String titleText, List<Movie> movies) {
        setTitle("Movie Mood - " + titleText);
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // top panel
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(Color.BLACK);
        header.setPreferredSize(new Dimension(900, 40));

        JButton back = new JButton("BACK");
        back.setForeground(Color.WHITE);
        back.setBackground(Color.RED);
        back.addActionListener(e -> {
            dispose();
            new MoviesPage();
        });

        JLabel title = new JLabel(titleText, SwingConstants.CENTER);
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Arial", Font.BOLD, 16));

        header.add(back, BorderLayout.WEST);
        header.add(title, BorderLayout.CENTER);
        add(header, BorderLayout.NORTH);

        // poster
        JPanel moviePanel = new JPanel();
        moviePanel.setBackground(Color.BLACK);
        moviePanel.setLayout(new FlowLayout(FlowLayout.LEFT, 20, 20));

        //first 20 movies for speed neden aynı filmleri tekrarlıyor??
        int count = 0;
        for (Movie movie : movies) {
            if (count >= 20) break;
            try {
                String imageUrl = movie.getPosterUrl();
                ImageIcon icon = loadImageFromURL(imageUrl);

                Image scaled = icon.getImage().getScaledInstance(120, 180, Image.SCALE_SMOOTH);
                JLabel poster = new JLabel(new ImageIcon(scaled));
                poster.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

                poster.addMouseListener(new MouseAdapter() {
                    public void mouseClicked(MouseEvent e) {
                        dispose();
                        new MovieMoodGUI(movie); // Detay sayfası
                    }
                });

                moviePanel.add(poster);
                count++;
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        JScrollPane scrollPane = new JScrollPane(moviePanel);
        scrollPane.setBorder(null);
        add(scrollPane, BorderLayout.CENTER);

        setVisible(true);
    }

    // URL'den resim çekme metodu
    public static ImageIcon loadImageFromURL(String imageUrl) {
        try {
            URL url = new URL(imageUrl);
            BufferedImage image = ImageIO.read(url);
            return new ImageIcon(image);
        } catch (Exception e) {
            System.err.println("Resim yüklenemedi: " + e.getMessage());
            return new ImageIcon(); // empty icon
        }
    }
}

