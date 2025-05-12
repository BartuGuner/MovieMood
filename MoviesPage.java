import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.List;  
import javax.imageio.ImageIO;
import javax.swing.*;  

public class MoviesPage extends JFrame {
    private FilmController filmController;
    private UserController userController;
    private User currentUser;

    public MoviesPage(FilmController filmController, UserController userController, User currentUser) {
        this.filmController = filmController;
        this.userController = userController;
        this.currentUser = currentUser;
        
        setTitle("Movie Mood - Movies");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        add(createNavBar(), BorderLayout.NORTH);

        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setBackground(Color.BLACK);

        JLabel genreLabel = new JLabel("Movies by Genre");
        genreLabel.setForeground(Color.WHITE);
        genreLabel.setFont(new Font("Arial", Font.BOLD, 16));
        genreLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        genreLabel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 0));
        centerPanel.add(genreLabel);

        JPanel genreRow = new JPanel(new GridLayout(1, 3));
        genreRow.setBackground(Color.BLACK);
        genreRow.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));

        genreRow.add(createCategoryPanel("Science Fiction"));
        genreRow.add(createCategoryPanel("Action"));
        genreRow.add(createCategoryPanel("Romance"));
        centerPanel.add(genreRow);

        JLabel yearLabel = new JLabel("Movies by Release Year");
        yearLabel.setForeground(Color.WHITE);
        yearLabel.setFont(new Font("Arial", Font.BOLD, 16));
        yearLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        yearLabel.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 0));
        centerPanel.add(yearLabel);

        JPanel yearRow = new JPanel(new GridLayout(1, 3));
        yearRow.setBackground(Color.BLACK);
        yearRow.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));

        yearRow.add(createYearPanel("2000-", 2000, 3000));
        yearRow.add(createYearPanel("1990-2000", 1990, 1999));
        yearRow.add(createYearPanel("1900-1990", 1900, 1989));
        centerPanel.add(yearRow);

        JScrollPane scrollPane = new JScrollPane(centerPanel);
        add(scrollPane, BorderLayout.CENTER);

        setVisible(true);
    }

    private JPanel createNavBar() {
        JPanel navPanel = new JPanel(new BorderLayout());
        navPanel.setBackground(Color.BLACK);
        navPanel.setPreferredSize(new Dimension(900, 40));

        JLabel logo = new JLabel("Movie Mood");
        logo.setForeground(new Color(204, 0, 0));
        logo.setFont(new Font("Arial", Font.BOLD, 18));
        logo.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));

        JPanel menuButtons = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        menuButtons.setBackground(Color.BLACK);

        String[] items = {"Home", "Explore", "My List", "Movies", "My Profile"};
        for (String item : items) {
            JButton btn = new JButton(item);
            btn.setForeground(Color.WHITE);
            btn.setBackground(Color.BLACK);
            btn.setFocusPainted(false);
            btn.setBorderPainted(false);
            btn.setFont(new Font("Arial", Font.PLAIN, 14));

            btn.addActionListener(e -> {
                switch (item) {
                    case "Home":
                        new HomePage(filmController, userController, currentUser);
                        dispose();
                        break;
                    case "Explore":
                        new ExploreFrame(filmController, userController, currentUser);
                        dispose();
                        break;
                    case "My List":
                        new MyListPanel(currentUser);
                        dispose();
                        break;
                    case "Movies":
                        // Already on movies page
                        break;
                    case "My Profile":
                        new ProfileFrame(currentUser);
                        dispose();
                        break;
                }
            });

            menuButtons.add(btn);
        }

        JButton chatBtn = new JButton("💬");
        chatBtn.setForeground(Color.WHITE);
        chatBtn.setBackground(Color.BLACK);
        chatBtn.setFocusPainted(false);
        chatBtn.setBorderPainted(false);
        menuButtons.add(chatBtn);

        navPanel.add(logo, BorderLayout.WEST);
        navPanel.add(menuButtons, BorderLayout.EAST);
        return navPanel;
    }

    private JPanel createYearPanel(String label, int start, int end) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.BLACK);

        JButton title = new JButton(label);
        title.setForeground(Color.WHITE);
        title.setBackground(Color.BLACK);
        title.setFocusPainted(false);
        title.setBorderPainted(false);
        title.setFont(new Font("Arial", Font.BOLD, 14));
        title.addActionListener(e -> {
            new GenrePage(filmController, userController, currentUser, label, start, end);
            dispose();
        });

        JPanel posters = createPostersPanel(filmController.searchByReleaseYearInterval(start, end));
        panel.add(title, BorderLayout.NORTH);
        panel.add(posters, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createPostersPanel(List<Movie> movies) {
        JPanel panel = new JPanel();
        panel.setLayout(new OverlayLayout(panel));
        panel.setBackground(Color.BLACK);
        int offset = 25;
        int count = 0;

        for (Movie movie : movies) {
            if (count >= 4) break;
            try {
                ImageIcon icon = loadImageFromURL(movie.getPosterUrl());
                Image scaled = icon.getImage().getScaledInstance(100, 150, Image.SCALE_SMOOTH);
                JLabel poster = new JLabel(new ImageIcon(scaled));
                poster.setAlignmentX(0.0f);
                poster.setAlignmentY(0.0f);
                poster.setBorder(BorderFactory.createEmptyBorder(0, count * offset, 0, 0));
                poster.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

                poster.addMouseListener(new MouseAdapter() {
                    public void mouseClicked(MouseEvent e) {
                        new MovieMoodGUI(filmController, userController, currentUser, movie);
                        dispose();
                    }
                });

                panel.add(poster);
                count++;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return panel;
    }

    private JPanel createCategoryPanel(String categoryName) {
        JPanel categoryPanel = new JPanel();
        categoryPanel.setLayout(new BorderLayout());
        categoryPanel.setBackground(Color.BLACK);

        JButton titleButton = new JButton(categoryName);
        titleButton.setFont(new Font("Arial", Font.BOLD, 14));
        titleButton.setForeground(Color.WHITE);
        titleButton.setBackground(Color.BLACK);
        titleButton.setFocusPainted(false);
        titleButton.setBorderPainted(false);
        titleButton.addActionListener(e -> {
            new GenrePage(filmController, userController, currentUser, categoryName);
            dispose();
        });

        JPanel posters = new JPanel();
        posters.setLayout(new OverlayLayout(posters));
        posters.setBackground(Color.BLACK);
        int offset = 25;

        List<Movie> movies = filmController.searchByGenre(categoryName);

        int count = 0;
        for (Movie movie : movies) {
            if (count >= 4) break;
            try {
                String imageUrl = movie.getPosterUrl();
                ImageIcon icon = loadImageFromURL(imageUrl);
                Image scaledImage = icon.getImage().getScaledInstance(100, 150, Image.SCALE_SMOOTH);

                JLabel poster = new JLabel(new ImageIcon(scaledImage));
                poster.setAlignmentX(0.0f);
                poster.setAlignmentY(0.0f);
                poster.setBorder(BorderFactory.createEmptyBorder(0, count * offset, 0, 0));
                poster.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

                poster.addMouseListener(new MouseAdapter() {
                    public void mouseClicked(MouseEvent e) {
                        new MovieMoodGUI(filmController, userController, currentUser, movie);
                        dispose();
                    }
                });

                posters.add(poster);
                count++;
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        categoryPanel.add(titleButton, BorderLayout.NORTH);
        categoryPanel.add(posters, BorderLayout.CENTER);
        return categoryPanel;
    }

    public static ImageIcon loadImageFromURL(String imageUrl) {
        try {
            URL url = new URL(imageUrl);
            BufferedImage image = ImageIO.read(url);
            return new ImageIcon(image);
        } catch (Exception e) {
            System.err.println("Image could not be loaded: " + e.getMessage());
            return new ImageIcon();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            FilmController filmController = new FilmController();
            UserController userController = new UserController();
            MovieSeeder.seedMovies(filmController);
            
            userController.register("test@example.com", "Test", "User", "password");
            User testUser = userController.login("test@example.com", "password");
            
            new MoviesPage(filmController, userController, testUser);
        });
    }
}
