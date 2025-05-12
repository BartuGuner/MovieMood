import java.awt.*;
import java.net.URL;
import java.util.List;
import javax.swing.*;

public class HomePage extends JFrame {
    // Controllers needed for ExploreFrame
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
        setLayout(new BorderLayout());

        add(createNavBar(), BorderLayout.NORTH);

        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(Color.BLACK);

        // Örnek statik içerik
        contentPanel.add(createSection("My List", new String[]{
                "mouse (1).png", "mouse (1).png", "mouse (1).png",
                "matrix.png", "matrix.png", "matrix.png"
        }));

        // Kullanıcı nesnesi
        User user = new User("Ece","ei", 1,"1");
        user.setRecommendedMovies(); // Bu satır öneri listesini oluşturmalı
        List<Movie> recommendedList = user.getRecommendedMovies();

        // Dinamik posterlerle önerilenler bölümü
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
                dispose();
                switch (item) {
                    case "Home":
                        new HomePage(filmController, userController, currentUser);
                        break;
                    case "Explore":
                        new ExploreFrame(filmController, userController, currentUser);
                        break;
                    case "My List":
                        new MyListPanel(); // Kim yaptıysa constructor eklesin
                        break;
                    case "Movies":
                        new MoviesPage(); //Kim yaptıysa constructor eklesin
                        break;
                    case "My Profile":
                        new ProfileFrame(currentUser.getUsername());
                        break;
                    default:
                    
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

    private JPanel createSection(String title, String[] imagePaths) {
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

        for (String path : imagePaths) {
            JLabel posterLabel = new JLabel();
            ImageIcon icon = new ImageIcon(path);
            Image scaled = icon.getImage().getScaledInstance(100, 150, Image.SCALE_SMOOTH);
            posterLabel.setIcon(new ImageIcon(scaled));
            posterLabel.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5));
            imageRow.add(posterLabel);
        }

        JScrollPane scroll = new JScrollPane(imageRow);
        scroll.setPreferredSize(new Dimension(850, 180));
        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
        scroll.setBorder(null);

        sectionPanel.add(scroll);
        return sectionPanel;
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
                JLabel posterLabel = new JLabel();
                String fullUrl = movie.getPosterUrl();
                ImageIcon icon = new ImageIcon(new URL(fullUrl));
                Image scaled = icon.getImage().getScaledInstance(100, 150, Image.SCALE_SMOOTH);
                posterLabel.setIcon(new ImageIcon(scaled));
                posterLabel.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5));
                imageRow.add(posterLabel);
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
            // Initialize controllers
            FilmController filmController = new FilmController();
            UserController userController = new UserController();
            
            // Seed movies
            MovieSeeder.seedMovies(filmController);
            
            // Create or login a user
            userController.register("testuser", "password");
            User currentUser = userController.login("testuser", "password");
            
            // Create HomePage with controllers
            new HomePage(filmController, userController, currentUser);
        });
    }
}