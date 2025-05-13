import java.awt.*;
import java.awt.event.*;
import java.util.List;
import javax.swing.*;

public class HomePage extends JFrame {
    private FilmController filmController;
    private UserController userController;
    private User currentUser;
    private JPanel mainPanel;

    public HomePage(FilmController filmController, UserController userController, User currentUser) {
        this.filmController = filmController;
        this.userController = userController;
        this.currentUser = currentUser;
        
        if (currentUser == null) {
            JOptionPane.showMessageDialog(null, "User not logged in!", "Error", JOptionPane.ERROR_MESSAGE);
            System.exit(0);
            return;
        }
        
        setTitle("Movie Mood - Home");
        setSize(1200, 900);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        // Main panel setup
        mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.BLACK);
        setContentPane(mainPanel);
        
        // Create components
        createNavBar();
        createContentPanel();
        
        setVisible(true);
    }
    
    private void styleButton(JButton button, boolean selected) {
        button.setForeground(selected ? Color.WHITE : Color.LIGHT_GRAY);
        button.setBackground(null);
        button.setBorder(null);
        button.setContentAreaFilled(false);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setFont(new Font("Arial", Font.PLAIN, 16));
    }
    
    private void createNavBar() {
        JPanel navPanel = new JPanel(new BorderLayout());
        navPanel.setBackground(Color.BLACK);
        navPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        
        // Logo
        JLabel logoLabel = new JLabel("Movie Mood");
        logoLabel.setFont(new Font("Arial", Font.BOLD, 24));
        logoLabel.setForeground(Color.RED);
        navPanel.add(logoLabel, BorderLayout.WEST);
        
        // Navigation buttons
        JPanel menuPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        menuPanel.setOpaque(false);
        
        String[] navItems = {"Home", "Explore", "My List", "Movies", "My Profile"};
        for (String item : navItems) {
            JButton navButton = new JButton(item);
            styleButton(navButton, item.equals("Home"));
            
            // Add ActionListeners for navigation buttons
            if (item.equals("Home")) {
                navButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        // Already on home page - do nothing
                    }
                });
            } else if (item.equals("Explore")) {
                navButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        // Create a new ExploreFrame with the current user
                        ExploreFrame exploreFrame = new ExploreFrame(filmController, userController, currentUser);
                        // Dispose this HomePage
                        dispose();
                    }
                });
            } else if (item.equals("My List")) {
                navButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        // Create a new MyListPanel with the current user
                        MyListPanel myListPanel = new MyListPanel(currentUser);
                        // Dispose this HomePage
                        dispose();
                    }
                });
            } else if (item.equals("Movies")) {
                navButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        // Create a new MoviesPage with the current user
                        MoviesPage moviesPage = new MoviesPage(filmController, userController, currentUser);
                        // Dispose this HomePage
                        dispose();
                    }
                });
            } else if (item.equals("My Profile")) {
                navButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        // Create a new ProfileFrame with the current user
                        ProfileFrame profileFrame = new ProfileFrame(currentUser);
                        // Dispose this HomePage
                        dispose();
                    }
                });
            }
            
            menuPanel.add(navButton);
        }
        navPanel.add(menuPanel, BorderLayout.CENTER);

        
        mainPanel.add(navPanel, BorderLayout.NORTH);
    }
    
    private void createContentPanel() {
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(Color.BLACK);
        
        // Add user's movie lists
        if (currentUser != null && currentUser.getFilmLists() != null) {
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
        }
        
        // Add recommended movies
        if (currentUser != null) {
            currentUser.setRecommendedMovies();
            List<Movie> recommendedList = currentUser.getRecommendedMovies();
            if (recommendedList != null && !recommendedList.isEmpty()) {
                contentPanel.add(createSectionFromMovies("Recommended For You", recommendedList));
            }
        }
        
        // Create scroll pane
        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.setBorder(null);
        scrollPane.setBackground(Color.BLACK);
        
        mainPanel.add(scrollPane, BorderLayout.CENTER);
    }
    
    private JPanel createSectionFromMovies(String title, List<Movie> movies) {
        JPanel sectionPanel = new JPanel();
        sectionPanel.setLayout(new BoxLayout(sectionPanel, BoxLayout.Y_AXIS));
        sectionPanel.setBackground(Color.BLACK);
        sectionPanel.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));
        
        // Section title
        JLabel titleLabel = new JLabel(title);
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        sectionPanel.add(titleLabel);
        
        // Movie images row
        JPanel imageRow = new JPanel();
        imageRow.setLayout(new BoxLayout(imageRow, BoxLayout.X_AXIS));
        imageRow.setBackground(Color.BLACK);
        imageRow.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        
        // Add movie posters
        for (Movie movie : movies) {
            try {
                JPanel moviePanel = new JPanel();
                moviePanel.setLayout(new BorderLayout());
                moviePanel.setBackground(Color.BLACK);
                moviePanel.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5));
                moviePanel.setCursor(new Cursor(Cursor.HAND_CURSOR));
                
                // Create poster label with scaled image
                JLabel posterLabel = new JLabel();
                ImageIcon icon = new ImageIcon(new java.net.URL(movie.getPosterUrl()));
                Image scaled = icon.getImage().getScaledInstance(100, 150, Image.SCALE_SMOOTH);
                posterLabel.setIcon(new ImageIcon(scaled));
                
                moviePanel.add(posterLabel);
                
                // Add click listener to open movie details
                moviePanel.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        // Open MovieMoodGUI with selected movie
                        MovieMoodGUI movieMoodGUI = new MovieMoodGUI(filmController, userController, currentUser, movie);
                        dispose();
                    }
                });
                
                imageRow.add(moviePanel);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        
        // Create horizontal scroll pane for movies
        JScrollPane scrollPane = new JScrollPane(imageRow);
        scrollPane.setPreferredSize(new Dimension(850, 180));
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
        scrollPane.setBorder(null);
        scrollPane.setBackground(Color.BLACK);
        
        sectionPanel.add(scrollPane);
        return sectionPanel;
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                // Initialize controllers
                FilmController filmController = new FilmController();
                UserController userController = new UserController();
                
                // Seed test data
                MovieSeeder.seedMovies(filmController);
                
                // Create test user
                userController.register("test@example.com", "Test", "User", "password");
                User currentUser = userController.login("test@example.com", "password");
                
                // Create home page
                HomePage homePage = new HomePage(filmController, userController, currentUser);
            }
        });
    }
}