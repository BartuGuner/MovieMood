import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.net.URL;
import java.io.File;

public class HomePage extends JFrame {
    private FilmController filmController;
    private UserController userController;
    private User currentUser;

    public HomePage(FilmController filmController, UserController userController, User currentUser) {
        this.filmController = filmController;
        this.userController = userController;
        this.currentUser = currentUser;
        
        setTitle("Movie Mood - Home");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        add(createNavBar(), BorderLayout.NORTH);

        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(Color.BLACK);

        List<FilmList> userLists = currentUser.getFilmLists();
        if (!userLists.isEmpty()) {
            FilmList firstList = userLists.get(0);
            List<Movie> movies = firstList.getMovies();
            contentPanel.add(createSectionFromMovies("My List", movies)); 
        } else {
            JLabel noListLabel = new JLabel("You don't have any movie lists.");
            noListLabel.setForeground(Color.WHITE);
            noListLabel.setFont(new Font("Arial", Font.ITALIC, 16));
            contentPanel.add(noListLabel);
        }

        currentUser.setRecommendedMovies();
        List<Movie> recommendedList = currentUser.getRecommendedMovies();
        contentPanel.add(createSectionFromMovies("Recommended For You", recommendedList));

        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.setBorder(null);
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
                        // Already on home page
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
                        new MoviesPage(filmController, userController, currentUser);
                        dispose();
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

    private JPanel createSectionFromMovies(String title, List<Movie> movies) {
        JPanel sectionPanel = new JPanel();
        sectionPanel.setLayout(new BoxLayout(sectionPanel, BoxLayout.Y_AXIS));
        sectionPanel.setBackground(Color.BLACK);
        sectionPanel.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        sectionPanel.add(titleLabel);

        JPanel imageRow = new JPanel();
        imageRow.setLayout(new BoxLayout(imageRow, BoxLayout.X_AXIS));
        imageRow.setBackground(Color.BLACK);
        imageRow.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        for (Movie movie : movies) {
            try {
                JPanel moviePanel = new JPanel();
                moviePanel.setLayout(new BorderLayout());
                moviePanel.setBackground(Color.BLACK);
                moviePanel.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5));
                moviePanel.setCursor(new Cursor(Cursor.HAND_CURSOR));
                
                JLabel posterLabel = new JLabel();
                ImageIcon icon = new ImageIcon(new java.net.URL(movie.getPosterUrl()));
                Image scaled = icon.getImage().getScaledInstance(100, 150, Image.SCALE_SMOOTH);
                posterLabel.setIcon(new ImageIcon(scaled));
                
                moviePanel.add(posterLabel);
                
                moviePanel.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        new MovieMoodGUI(filmController, userController, currentUser, movie);
                        dispose();
                    }
                });
                
                imageRow.add(moviePanel);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        JScrollPane scroll = new JScrollPane(imageRow);
        scroll.setPreferredSize(new Dimension(850, 180));
        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
        scroll.setBorder(null);

        sectionPanel.add(scroll);
        return sectionPanel;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            FilmController filmController = new FilmController();
            UserController userController = new UserController();
            
            MovieSeeder.seedMovies(filmController);
            
            userController.register("test@example.com", "Test", "User", "password");
            User currentUser = userController.login("test@example.com", "password");
            
            new HomePage(filmController, userController, currentUser);
        });
    }
}