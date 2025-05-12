import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;
import javax.swing.Timer;
import java.net.URL;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

public class ExploreFrame extends JFrame {

// Components
    private JPanel mainPanel, headerPanel, searchPanel, resultsPanel;
    private JTextField searchField;
    private JButton searchButton;
    private JLabel titleLabel;
    private JButton homeButton, exploreButton, myListButton, moviesButton, profileButton, chatButton;
    
    // Controllers
    private FilmController filmController;
    private UserController userController;
    private User currentUser;
    
    // Colors
    private Color darkBackground = new Color(25, 25, 25);
    private Color brightRed = new Color(230, 0, 0);
    
    public ExploreFrame(FilmController filmController, UserController userController, User currentUser) {
        this.filmController = filmController;
        this.userController = userController;
        this.currentUser = currentUser;
        
        setTitle("Movie Mood - Explore");
        setSize(1200, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        initComponents();
        layoutComponents();
        
        setVisible(true);
    }
    
    private void initComponents() {
        mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(darkBackground);
        
        createHeader();
        createSearchPanel();
        createResultsPanel();
    }
    
    private void createHeader() {
        headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(darkBackground);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        
        // Logo
        titleLabel = new JLabel("Movie Mood");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 28));
        titleLabel.setForeground(brightRed);
        headerPanel.add(titleLabel, BorderLayout.WEST);
        
        // Navigation buttons
        JPanel navPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        navPanel.setOpaque(false);
        
        homeButton = createNavButton("Home");
        exploreButton = createNavButton("Explore");
        exploreButton.setForeground(Color.WHITE); // Highlight current page
        myListButton = createNavButton("My List");
        moviesButton = createNavButton("Movies");
        profileButton = createNavButton("My Profile");
        
        navPanel.add(homeButton);
        navPanel.add(exploreButton);
        navPanel.add(myListButton);
        navPanel.add(moviesButton);
        navPanel.add(profileButton);
        
        headerPanel.add(navPanel, BorderLayout.CENTER);
        
        // Chat button
        chatButton = new JButton("💬");
        chatButton.setFont(new Font("Dialog", Font.PLAIN, 20));
        chatButton.setBackground(Color.WHITE);
        chatButton.setForeground(Color.BLACK);
        chatButton.setFocusPainted(false);
        chatButton.setBorderPainted(false);
        chatButton.setPreferredSize(new Dimension(40, 40));
        chatButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        JPanel chatPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        chatPanel.setOpaque(false);
        chatPanel.add(chatButton);
        headerPanel.add(chatPanel, BorderLayout.EAST);
    }
    
    private void createSearchPanel() {
        searchPanel = new JPanel();
        searchPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 30));
        searchPanel.setBackground(darkBackground);
        searchPanel.setBorder(BorderFactory.createEmptyBorder(30, 0, 30, 0));
        
        // Search field
        searchField = new JTextField();
        searchField.setPreferredSize(new Dimension(600, 50));
        searchField.setFont(new Font("Arial", Font.PLAIN, 18));
        searchField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.GRAY, 1),
            BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));
        searchField.setBackground(new Color(40, 40, 40));
        searchField.setForeground(Color.WHITE);
        searchField.setCaretColor(Color.WHITE);
        
        // Add placeholder text
        searchField.setText("Search for movies...");
        searchField.setForeground(Color.GRAY);
        
        searchField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (searchField.getText().equals("Search for movies...")) {
                    searchField.setText("");
                    searchField.setForeground(Color.WHITE);
                }
            }
            
            @Override
            public void focusLost(FocusEvent e) {
                if (searchField.getText().isEmpty()) {
                    searchField.setText("Search for movies...");
                    searchField.setForeground(Color.GRAY);
                }
            }
        });
        
        // Search button
        searchButton = new JButton("Search");
        searchButton.setFont(new Font("Arial", Font.BOLD, 16));
        searchButton.setPreferredSize(new Dimension(120, 50));
        searchButton.setBackground(brightRed);
        searchButton.setForeground(Color.WHITE);
        searchButton.setFocusPainted(false);
        searchButton.setBorderPainted(false);
        searchButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        searchButton.addActionListener(e -> performSearch());
        
        // Add Enter key listener to search field
        searchField.addActionListener(e -> performSearch());
        
        searchPanel.add(searchField);
        searchPanel.add(searchButton);
    }
    
    private void createResultsPanel() {
        resultsPanel = new JPanel();
        resultsPanel.setLayout(new GridBagLayout());
        resultsPanel.setBackground(darkBackground);
        
        // Initial empty state message
        showEmptyState();
    }
    
    private void showEmptyState() {
        resultsPanel.removeAll();
        
        JLabel emptyLabel = new JLabel("Start searching for movies!");
        emptyLabel.setFont(new Font("Arial", Font.PLAIN, 24));
        emptyLabel.setForeground(Color.GRAY);
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(50, 0, 0, 0);
        
        resultsPanel.add(emptyLabel, gbc);
        resultsPanel.revalidate();
        resultsPanel.repaint();
    }
    
    private void performSearch() {
        String query = searchField.getText().trim();
        
        if (query.isEmpty() || query.equals("Search for movies...")) {
            return;
        }
        
        // Search for movies
        List<Movie> searchResults = filmController.searchByTitle(query);
        
        // Display results
        displaySearchResults(searchResults);
    }
    
    private void displaySearchResults(List<Movie> movies) {
        resultsPanel.removeAll();
        resultsPanel.setLayout(new GridLayout(0, 5, 20, 20));
        resultsPanel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));
        
        if (movies.isEmpty()) {
            resultsPanel.setLayout(new GridBagLayout());
            JLabel noResultsLabel = new JLabel("No movies found for your search.");
            noResultsLabel.setFont(new Font("Arial", Font.PLAIN, 20));
            noResultsLabel.setForeground(Color.GRAY);
            resultsPanel.add(noResultsLabel);
        } else {
            for (Movie movie : movies) {
                resultsPanel.add(createMovieCard(movie));
            }
        }
        
        resultsPanel.revalidate();
        resultsPanel.repaint();
    }

    
    private JButton createNavButton(String text) {
        JButton button = new JButton(text);
        button.setForeground(text.equals("Explore") ? Color.WHITE : Color.LIGHT_GRAY);
        button.setBackground(null);
        button.setBorder(null);
        button.setContentAreaFilled(false);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setFont(new Font("Arial", Font.PLAIN, 16));
        
        return button;
    }
    
    private void layoutComponents() {
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(searchPanel, BorderLayout.CENTER);
        
        // Scroll pane for results
        JScrollPane scrollPane = new JScrollPane(resultsPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(darkBackground);
        
        mainPanel.add(scrollPane, BorderLayout.SOUTH);
        scrollPane.setPreferredSize(new Dimension(getWidth(), 500));
        
        add(mainPanel);
    }
    

    // In ExploreFrame.java, replace the createMovieCard method:

    private JPanel createMovieCard(Movie movie) {
        JPanel card = new JPanel();
        card.setLayout(new BorderLayout());
        card.setBackground(new Color(30, 30, 30));
        card.setPreferredSize(new Dimension(160, 240));
        card.setBorder(BorderFactory.createEmptyBorder());
        card.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Create poster label
        JLabel posterLabel = new JLabel();
        posterLabel.setPreferredSize(new Dimension(160, 200));
        posterLabel.setHorizontalAlignment(JLabel.CENTER);
        posterLabel.setVerticalAlignment(JLabel.CENTER);
        posterLabel.setBackground(new Color(20, 20, 20));
        posterLabel.setOpaque(true);
        
        // Load poster image in a background thread
        SwingWorker<ImageIcon, Void> imageLoader = new SwingWorker<ImageIcon, Void>() {
            @Override
            protected ImageIcon doInBackground() throws Exception {
                try {
                    String posterUrl = movie.getPosterUrl();
                    if (posterUrl != null && !posterUrl.isEmpty()) {
                        URL url = new URL(posterUrl);
                        BufferedImage img = ImageIO.read(url);
                        Image scaledImg = img.getScaledInstance(160, 200, Image.SCALE_SMOOTH);
                        return new ImageIcon(scaledImg);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }
            
            @Override
            protected void done() {
                try {
                    ImageIcon icon = get();
                    if (icon != null) {
                        posterLabel.setIcon(icon);
                        posterLabel.setText("");
                    } else {
                        // Show placeholder
                        showPlaceholder(posterLabel);
                    }
                } catch (Exception e) {
                    showPlaceholder(posterLabel);
                }
            }
        };
        
        // Show loading placeholder initially
        showPlaceholder(posterLabel);
        
        // Start loading the image
        imageLoader.execute();
        
        // Movie title
        JLabel titleLabel = new JLabel(movie.getTitle(), SwingConstants.CENTER);
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        
        card.add(posterLabel, BorderLayout.CENTER);
        card.add(titleLabel, BorderLayout.SOUTH);
        
            // In the createMovieCard method, replace the mouseClicked part with this debug version:
        card.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                System.out.println("Movie card clicked: " + movie.getTitle()); // ADD THIS
                try {
                    // Open MovieMoodGUI with the selected movie
                    SwingUtilities.invokeLater(() -> {
                        try {
                            System.out.println("Creating MovieMoodGUI..."); // ADD THIS
                            // Create the new frame first
                            MovieMoodGUI movieGUI = new MovieMoodGUI(filmController, userController, currentUser, movie);
                            System.out.println("MovieMoodGUI created successfully"); // ADD THIS
                            
                            // Only dispose the current frame after successful creation
                            dispose();
                        } catch (Exception ex) {
                            ex.printStackTrace();
                            JOptionPane.showMessageDialog(ExploreFrame.this, 
                                "Error opening movie details: " + ex.getMessage(), 
                                "Error", 
                                JOptionPane.ERROR_MESSAGE);
                        }
                    });
                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(ExploreFrame.this, 
                        "Error: " + ex.getMessage(), 
                        "Error", 
                        JOptionPane.ERROR_MESSAGE);
                }
            }
    
    @Override
    public void mouseEntered(MouseEvent e) {
        card.setBackground(new Color(50, 50, 50));
    }
    
    @Override
    public void mouseExited(MouseEvent e) {
        card.setBackground(new Color(30, 30, 30));
    }
        });
        
        return card;
    }
    
    private void showPlaceholder(JLabel label) {
        label.setText("🎬");
        label.setFont(new Font("Arial", Font.BOLD, 40));
        label.setForeground(new Color(60, 60, 60));
        label.setBackground(new Color(20, 20, 20));
        label.setOpaque(true);
    }
    
    // Main method for testing
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // Initialize controllers
            FilmController filmController = new FilmController();
            UserController userController = new UserController();
            
            // Seed movies
            MovieSeeder.seedMovies(filmController);
            
            // Create a test user

            User testUser = userController.login("Alice", "pass1");
            
            // Create and show the explore frame
            new ExploreFrame(filmController, userController, testUser);
        });
    }
}
