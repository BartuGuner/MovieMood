import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;
import javax.swing.Timer;

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
    
    private JPanel createMovieCard(Movie movie) {
        JPanel card = new JPanel();
        card.setLayout(new BorderLayout());
        card.setBackground(new Color(30, 30, 30));
        card.setPreferredSize(new Dimension(160, 240));
        card.setBorder(BorderFactory.createEmptyBorder());
        card.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Movie poster panel
        JPanel posterPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();
                
                // Draw placeholder
                g2d.setColor(new Color(20, 20, 20));
                g2d.fillRect(0, 0, getWidth(), getHeight());
                
                // Draw movie icon placeholder
                g2d.setColor(new Color(60, 60, 60));
                g2d.setFont(new Font("Arial", Font.BOLD, 40));
                FontMetrics fm = g2d.getFontMetrics();
                String text = "🎬";
                int textWidth = fm.stringWidth(text);
                int textHeight = fm.getHeight();
                
                g2d.drawString(text, (getWidth() - textWidth) / 2, 
                              (getHeight() - textHeight) / 2 + fm.getAscent());
                
                g2d.dispose();
            }
        };
        posterPanel.setPreferredSize(new Dimension(160, 200));
        
        // Movie title
        JLabel titleLabel = new JLabel(movie.getTitle(), SwingConstants.CENTER);
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        
        card.add(posterPanel, BorderLayout.CENTER);
        card.add(titleLabel, BorderLayout.SOUTH);
        
        // Add click listener to open movie details
        card.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                openMovieDetails(movie);
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
    
    private void openMovieDetails(Movie movie) {
        MovieDetailsDialog detailsDialog = new MovieDetailsDialog(this, movie);
        detailsDialog.setVisible(true);
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
    
    // Inner class for movie details dialog
    private class MovieDetailsDialog extends JDialog {
        private Movie movie;
        
        public MovieDetailsDialog(JFrame parent, Movie movie) {
            super(parent, "Movie Details", true);
            this.movie = movie;
            
            setSize(800, 600);
            setLocationRelativeTo(parent);
            getContentPane().setBackground(darkBackground);
            
            initDetailsComponents();
            loadMovieData();
        }
        
        private void initDetailsComponents() {
            setLayout(new BorderLayout());
            
            // Main content panel
            JPanel contentPanel = new JPanel();
            contentPanel.setLayout(new BorderLayout());
            contentPanel.setBackground(darkBackground);
            contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
            
            // Top panel with movie info and poster
            JPanel topPanel = new JPanel(new GridLayout(1, 2, 30, 0));
            topPanel.setBackground(darkBackground);
            
            // Left side - Movie info
            JPanel infoPanel = new JPanel();
            infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
            infoPanel.setBackground(darkBackground);
            
            // Movie title
            JLabel titleLabel = new JLabel(movie.getTitle());
            titleLabel.setFont(new Font("Arial", Font.BOLD, 28));
            titleLabel.setForeground(Color.WHITE);
            titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
            
            // Director
            JLabel directorLabel = new JLabel("Director: " + movie.getDirector());
            directorLabel.setFont(new Font("Arial", Font.PLAIN, 16));
            directorLabel.setForeground(Color.LIGHT_GRAY);
            directorLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
            
            // Release date
            JLabel releaseLabel = new JLabel("Release Date: " + movie.getReleaseDate());
            releaseLabel.setFont(new Font("Arial", Font.PLAIN, 16));
            releaseLabel.setForeground(Color.LIGHT_GRAY);
            releaseLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
            
            // Genres
            String genresText = "Genres: " + String.join(", ", movie.getGenres());
            JLabel genresLabel = new JLabel(genresText);
            genresLabel.setFont(new Font("Arial", Font.PLAIN, 16));
            genresLabel.setForeground(Color.LIGHT_GRAY);
            genresLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
            
            // Ratings
            JPanel ratingsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            ratingsPanel.setBackground(darkBackground);
            ratingsPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
            
            JLabel imdbLabel = new JLabel("IMDb: " + String.format("%.1f", movie.getRating()));
            imdbLabel.setFont(new Font("Arial", Font.BOLD, 18));
            imdbLabel.setForeground(Color.YELLOW);
            
            JLabel userRatingLabel = new JLabel("   User Rating: " + 
                String.format("%.1f", movie.getAverageRating()));
            userRatingLabel.setFont(new Font("Arial", Font.BOLD, 18));
            userRatingLabel.setForeground(Color.CYAN);
            
            ratingsPanel.add(imdbLabel);
            ratingsPanel.add(userRatingLabel);
            
            infoPanel.add(titleLabel);
            infoPanel.add(Box.createRigidArea(new Dimension(0, 10)));
            infoPanel.add(directorLabel);
            infoPanel.add(Box.createRigidArea(new Dimension(0, 5)));
            infoPanel.add(releaseLabel);
            infoPanel.add(Box.createRigidArea(new Dimension(0, 5)));
            infoPanel.add(genresLabel);
            infoPanel.add(Box.createRigidArea(new Dimension(0, 20)));
            infoPanel.add(ratingsPanel);
            
            // Right side - Movie poster
            JPanel posterPanel = new JPanel();
            posterPanel.setBackground(new Color(30, 30, 30));
            posterPanel.setBorder(BorderFactory.createLineBorder(Color.GRAY, 2));
            posterPanel.setPreferredSize(new Dimension(300, 400));
            
            topPanel.add(infoPanel);
            topPanel.add(posterPanel);
            
            // Buttons panel
            JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));
            buttonsPanel.setBackground(darkBackground);
            
            JButton seeCommentsBtn = createActionButton("Click to See Comments");
            JButton addCommentBtn = createActionButton("Add A Comment");
            JButton addRatingBtn = createActionButton("Add Rating");
            
            seeCommentsBtn.addActionListener(e -> showComments());
            addCommentBtn.addActionListener(e -> addComment());
            addRatingBtn.addActionListener(e -> addRating());
            
            buttonsPanel.add(seeCommentsBtn);
            buttonsPanel.add(addCommentBtn);
            buttonsPanel.add(addRatingBtn);
            
            // Overview panel
            JPanel overviewPanel = new JPanel();
            overviewPanel.setLayout(new BorderLayout());
            overviewPanel.setBackground(darkBackground);
            overviewPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.GRAY), 
                "Overview", 
                TitledBorder.LEFT, 
                TitledBorder.TOP, 
                new Font("Arial", Font.BOLD, 16), 
                Color.WHITE
            ));
            
            JTextArea overviewArea = new JTextArea(movie.getOverview());
            overviewArea.setFont(new Font("Arial", Font.PLAIN, 14));
            overviewArea.setForeground(Color.LIGHT_GRAY);
            overviewArea.setBackground(darkBackground);
            overviewArea.setWrapStyleWord(true);
            overviewArea.setLineWrap(true);
            overviewArea.setEditable(false);
            overviewArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
            
            overviewPanel.add(new JScrollPane(overviewArea), BorderLayout.CENTER);
            overviewPanel.setPreferredSize(new Dimension(760, 150));
            
            contentPanel.add(topPanel, BorderLayout.NORTH);
            contentPanel.add(buttonsPanel, BorderLayout.CENTER);
            contentPanel.add(overviewPanel, BorderLayout.SOUTH);
            
            add(contentPanel);
        }
        
        private void loadMovieData() {
            // In a real application, you would load movie data from backend
            // For now, we're using the data already in the Movie object
        }
        
        private JButton createActionButton(String text) {
            JButton button = new JButton(text);
            button.setFont(new Font("Arial", Font.BOLD, 14));
            button.setBackground(brightRed);
            button.setForeground(Color.WHITE);
            button.setFocusPainted(false);
            button.setBorderPainted(false);
            button.setPreferredSize(new Dimension(180, 40));
            button.setCursor(new Cursor(Cursor.HAND_CURSOR));
            
            button.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    button.setBackground(brightRed.darker());
                }
                
                @Override
                public void mouseExited(MouseEvent e) {
                    button.setBackground(brightRed);
                }
            });
            
            return button;
        }
        
        private void showComments() {
            List<Comment> comments = filmController.getCommentsForMovie(movie);
            
            JDialog commentsDialog = new JDialog(this, "Comments", true);
            commentsDialog.setSize(600, 400);
            commentsDialog.setLocationRelativeTo(this);
            commentsDialog.getContentPane().setBackground(darkBackground);
            
            JPanel commentsPanel = new JPanel();
            commentsPanel.setLayout(new BoxLayout(commentsPanel, BoxLayout.Y_AXIS));
            commentsPanel.setBackground(darkBackground);
            
            if (comments.isEmpty()) {
                JLabel noCommentsLabel = new JLabel("No comments yet. Be the first to comment!");
                noCommentsLabel.setForeground(Color.GRAY);
                noCommentsLabel.setFont(new Font("Arial", Font.PLAIN, 16));
                noCommentsLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
                commentsPanel.add(noCommentsLabel);
            } else {
                for (Comment comment : comments) {
                    JPanel commentPanel = createCommentPanel(comment);
                    commentsPanel.add(commentPanel);
                    commentsPanel.add(Box.createRigidArea(new Dimension(0, 10)));
                }
            }
            
            JScrollPane scrollPane = new JScrollPane(commentsPanel);
            scrollPane.setBorder(BorderFactory.createEmptyBorder());
            scrollPane.getViewport().setBackground(darkBackground);
            
            commentsDialog.add(scrollPane);
            commentsDialog.setVisible(true);
        }
        
        private JPanel createCommentPanel(Comment comment) {
            JPanel panel = new JPanel(new BorderLayout());
            panel.setBackground(new Color(40, 40, 40));
            panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.GRAY, 1),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
            ));
            
            JLabel authorLabel = new JLabel(comment.getAuthor().getUsername());
            authorLabel.setFont(new Font("Arial", Font.BOLD, 14));
            authorLabel.setForeground(brightRed);
            
            JLabel textLabel = new JLabel(comment.getText());
            textLabel.setFont(new Font("Arial", Font.PLAIN, 14));
            textLabel.setForeground(Color.WHITE);
            
            JLabel timestampLabel = new JLabel(comment.getTimestamp().toString());
            timestampLabel.setFont(new Font("Arial", Font.PLAIN, 12));
            timestampLabel.setForeground(Color.GRAY);
            
            panel.add(authorLabel, BorderLayout.NORTH);
            panel.add(textLabel, BorderLayout.CENTER);
            panel.add(timestampLabel, BorderLayout.SOUTH);
            
            return panel;
        }
        
        private void addComment() {
            JDialog commentDialog = new JDialog(this, "Add Comment", true);
            commentDialog.setSize(500, 300);
            commentDialog.setLocationRelativeTo(this);
            commentDialog.getContentPane().setBackground(darkBackground);
            commentDialog.setLayout(new BorderLayout());
            
            JPanel contentPanel = new JPanel(new BorderLayout());
            contentPanel.setBackground(darkBackground);
            contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
            
            JLabel instructionLabel = new JLabel("Write your comment:");
            instructionLabel.setFont(new Font("Arial", Font.BOLD, 16));
            instructionLabel.setForeground(Color.WHITE);
            
            JTextArea commentArea = new JTextArea();
            commentArea.setFont(new Font("Arial", Font.PLAIN, 14));
            commentArea.setBackground(new Color(40, 40, 40));
            commentArea.setForeground(Color.WHITE);
            commentArea.setCaretColor(Color.WHITE);
            commentArea.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.GRAY, 1),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
            ));
            
            JButton submitButton = new JButton("Submit");
            submitButton.setFont(new Font("Arial", Font.BOLD, 16));
            submitButton.setBackground(brightRed);
            submitButton.setForeground(Color.WHITE);
            submitButton.setFocusPainted(false);
            submitButton.setBorderPainted(false);
            submitButton.setPreferredSize(new Dimension(150, 40));
            submitButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
            
            submitButton.addActionListener(e -> {
                String commentText = commentArea.getText().trim();
                if (!commentText.isEmpty()) {
                    filmController.addCommentToMovie(movie, currentUser, commentText);
                    JOptionPane.showMessageDialog(commentDialog, 
                        "Comment added successfully!", 
                        "Success", 
                        JOptionPane.INFORMATION_MESSAGE);
                    commentDialog.dispose();
                }
            });
            
            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
            buttonPanel.setBackground(darkBackground);
            buttonPanel.add(submitButton);
            
            contentPanel.add(instructionLabel, BorderLayout.NORTH);
            contentPanel.add(new JScrollPane(commentArea), BorderLayout.CENTER);
            contentPanel.add(buttonPanel, BorderLayout.SOUTH);
            
            commentDialog.add(contentPanel);
            commentDialog.setVisible(true);
        }
        
        private void addRating() {
            JDialog ratingDialog = new JDialog(this, "Add Rating", true);
            ratingDialog.setSize(400, 300);
            ratingDialog.setLocationRelativeTo(this);
            ratingDialog.getContentPane().setBackground(darkBackground);
            ratingDialog.setLayout(new BorderLayout());
            
            JPanel contentPanel = new JPanel();
            contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
            contentPanel.setBackground(darkBackground);
            contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
            
            JLabel instructionLabel = new JLabel("Rate this movie (1-10):");
            instructionLabel.setFont(new Font("Arial", Font.BOLD, 18));
            instructionLabel.setForeground(Color.WHITE);
            instructionLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            
            // Rating buttons panel
            JPanel ratingPanel = new JPanel(new GridLayout(2, 5, 10, 10));
            ratingPanel.setBackground(darkBackground);
            ratingPanel.setMaximumSize(new Dimension(350, 100));
            
            ButtonGroup ratingGroup = new ButtonGroup();
            JRadioButton[] ratingButtons = new JRadioButton[10];
            
            for (int i = 0; i < 10; i++) {
                JRadioButton button = new JRadioButton(String.valueOf(i + 1));
                button.setFont(new Font("Arial", Font.BOLD, 16));
                button.setForeground(Color.WHITE);
                button.setBackground(darkBackground);
                button.setFocusPainted(false);
                ratingButtons[i] = button;
                ratingGroup.add(button);
                ratingPanel.add(button);
            }
            
            JButton submitButton = new JButton("Submit");
            submitButton.setFont(new Font("Arial", Font.BOLD, 16));
            submitButton.setBackground(brightRed);
            submitButton.setForeground(Color.WHITE);
            submitButton.setFocusPainted(false);
            submitButton.setBorderPainted(false);
            submitButton.setPreferredSize(new Dimension(150, 40));
            submitButton.setAlignmentX(Component.CENTER_ALIGNMENT);
            submitButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
            
            submitButton.addActionListener(e -> {
                for (int i = 0; i < ratingButtons.length; i++) {
                    if (ratingButtons[i].isSelected()) {
                        double rating = i + 1;
                        filmController.rateMovie(movie, currentUser, rating);
                        JOptionPane.showMessageDialog(ratingDialog, 
                            "Rating submitted successfully!", 
                            "Success", 
                            JOptionPane.INFORMATION_MESSAGE);
                        ratingDialog.dispose();
                        break;
                    }
                }
            });
            
            contentPanel.add(instructionLabel);
            contentPanel.add(Box.createRigidArea(new Dimension(0, 20)));
            contentPanel.add(ratingPanel);
            contentPanel.add(Box.createRigidArea(new Dimension(0, 30)));
            contentPanel.add(submitButton);
            
            ratingDialog.add(contentPanel);
            ratingDialog.setVisible(true);
        }
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
            userController.register("testuser", "password");
            User testUser = userController.login("testuser", "password");
            
            // Create and show the explore frame
            new ExploreFrame(filmController, userController, testUser);
        });
    }
}
