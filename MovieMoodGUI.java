import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.net.URL;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.util.List;

public class MovieMoodGUI extends JFrame {
    // Controllers and data
    private FilmController filmController;
    private UserController userController;
    private User currentUser;
    private Movie selectedMovie;
    
    // UI Components
    private JPanel mainPanel, headerPanel, contentPanel;
    private JPanel northPanel, southPanel;
    private JLabel titleLabel;
    private JButton homeButton, exploreButton, myListButton, moviesButton, profileButton, chatButton;
    
    // Movie info components
    private JLabel movieTitleLabel, directorLabel, releaseDateLabel, genresLabel;
    private JLabel imdbRatingLabel, userRatingLabel;
    private JTextArea overviewArea;
    private JLabel posterLabel;
    
    // Colors
    private Color darkBackground = new Color(25, 25, 25);
    private Color brightRed = new Color(230, 0, 0);
    private Color panelBackground = new Color(30, 30, 30);
    
    public MovieMoodGUI(FilmController filmController, UserController userController, User currentUser, Movie movie) {
        this.filmController = filmController;
        this.userController = userController;
        this.currentUser = currentUser;
        this.selectedMovie = movie;
        
        setTitle("Movie Mood - " + (movie != null ? movie.getTitle() : "Movie Details"));
        setSize(1200, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        initComponents();
        layoutComponents();
        
        if (selectedMovie != null) {
            displayMovieDetails(selectedMovie);
        }
        
        setVisible(true);
    }
//it can take the movie objects arraylist or something 
    public MovieMoodGUI(Movie movie) {
        setTitle("Movie Mood - Details");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        //change 
        commentsList = new ArrayList<>();
        commentsList.add(new Comment("Ahmet Mert", "The movie was really amusing"));
        commentsList.add(new Comment("Bartu Güner", "I think the movie was not that good"));

        add(createNavBar(), BorderLayout.NORTH);

        JPanel topPanel = new JPanel(new GridLayout(1, 2));
        topPanel.setPreferredSize(new Dimension(900, 450));

        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setBackground(darkBackground);
        
        // Ratings panel
        JPanel ratingsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        ratingsPanel.setBackground(darkBackground);
        ratingsPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        imdbRatingLabel = new JLabel("IMDb: N/A");
        imdbRatingLabel.setFont(new Font("Arial", Font.BOLD, 24));
        imdbRatingLabel.setForeground(Color.YELLOW);
        
        userRatingLabel = new JLabel("   User Rating: N/A");
        userRatingLabel.setFont(new Font("Arial", Font.BOLD, 24));
        userRatingLabel.setForeground(Color.CYAN);
        
        ratingsPanel.add(imdbRatingLabel);
        ratingsPanel.add(userRatingLabel);
        
        // Buttons panel
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 15));
        buttonsPanel.setBackground(darkBackground);
        buttonsPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JButton seeCommentsBtn = createActionButton("Click to See Comments");
        JButton addCommentBtn = createActionButton("Add a Comment");
        JButton addRatingBtn = createActionButton("Add Rating");
        
        seeCommentsBtn.addActionListener(e -> showComments());
        addCommentBtn.addActionListener(e -> addComment());
        addRatingBtn.addActionListener(e -> addRating());
        
        buttonsPanel.add(seeCommentsBtn);
        buttonsPanel.add(addCommentBtn);
        buttonsPanel.add(addRatingBtn);
        
        // Add components to left panel
        leftPanel.add(ratingsPanel);
        leftPanel.add(Box.createRigidArea(new Dimension(0, 30)));
        leftPanel.add(buttonsPanel);
        
        // Right panel - movie poster
        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.setBackground(panelBackground);
        rightPanel.setBorder(BorderFactory.createLineBorder(Color.GRAY, 2));
        rightPanel.setPreferredSize(new Dimension(400, 500));
        
        posterLabel = new JLabel();
        posterLabel.setHorizontalAlignment(JLabel.CENTER);
        posterLabel.setVerticalAlignment(JLabel.CENTER);
        rightPanel.add(posterLabel, BorderLayout.CENTER);
        
        northPanel.add(leftPanel);
        northPanel.add(rightPanel);
        
        // South panel - movie basic info
        southPanel = new JPanel();
        southPanel.setLayout(new BoxLayout(southPanel, BoxLayout.Y_AXIS));
        southPanel.setBackground(panelBackground);
        southPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.GRAY),
            BorderFactory.createEmptyBorder(20, 30, 20, 30)
        ));
        
        movieTitleLabel = new JLabel("Movie Title");
        movieTitleLabel.setFont(new Font("Arial", Font.BOLD, 36));
        movieTitleLabel.setForeground(Color.WHITE);
        movieTitleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        directorLabel = new JLabel("Director: N/A");
        directorLabel.setFont(new Font("Arial", Font.PLAIN, 20));
        directorLabel.setForeground(Color.LIGHT_GRAY);
        directorLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        releaseDateLabel = new JLabel("Release Date: N/A");
        releaseDateLabel.setFont(new Font("Arial", Font.PLAIN, 20));
        releaseDateLabel.setForeground(Color.LIGHT_GRAY);
        releaseDateLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        genresLabel = new JLabel("Genres: N/A");
        genresLabel.setFont(new Font("Arial", Font.PLAIN, 20));
        genresLabel.setForeground(Color.LIGHT_GRAY);
        genresLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        // Overview section
        JPanel overviewPanel = new JPanel(new BorderLayout());
        overviewPanel.setBackground(panelBackground);
        overviewPanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(Color.GRAY),
            "Overview",
            TitledBorder.LEFT,
            TitledBorder.TOP,
            new Font("Arial", Font.BOLD, 18),
            Color.WHITE
        ));
        overviewPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        overviewArea = new JTextArea("No overview available.");
        overviewArea.setFont(new Font("Arial", Font.PLAIN, 16));
        overviewArea.setForeground(Color.WHITE);
        overviewArea.setBackground(panelBackground);
        overviewArea.setWrapStyleWord(true);
        overviewArea.setLineWrap(true);
        overviewArea.setEditable(false);
        overviewArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JScrollPane overviewScrollPane = new JScrollPane(overviewArea);
        overviewScrollPane.setBorder(BorderFactory.createEmptyBorder());
        overviewScrollPane.getViewport().setBackground(panelBackground);
        overviewScrollPane.setPreferredSize(new Dimension(1000, 150));
        
        overviewPanel.add(overviewScrollPane, BorderLayout.CENTER);
        
        southPanel.add(movieTitleLabel);
        southPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        southPanel.add(directorLabel);
        southPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        southPanel.add(releaseDateLabel);
        southPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        southPanel.add(genresLabel);
        southPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        southPanel.add(overviewPanel);
        
        contentPanel.add(northPanel, BorderLayout.NORTH);
        contentPanel.add(southPanel, BorderLayout.CENTER);
    }
    
    private void displayMovieDetails(Movie movie) {
        // Update all UI components with movie data
        movieTitleLabel.setText(movie.getTitle());
        directorLabel.setText("Director: " + movie.getDirector());
        releaseDateLabel.setText("Release Date: " + movie.getReleaseYear());
        genresLabel.setText("Genres: " + String.join(", ", movie.getGenres()));
        
        // Update ratings
        imdbRatingLabel.setText("IMDb: " + String.format("%.1f", movie.getRating()));
        userRatingLabel.setText("   User Rating: " + String.format("%.1f", movie.getAverageRating()));
        
        // Update overview
        overviewArea.setText(movie.getOverview());
        
        // Load poster image
        loadPosterImage(movie);
    }
    
    private void loadPosterImage(Movie movie) {
        SwingWorker<ImageIcon, Void> imageLoader = new SwingWorker<ImageIcon, Void>() {
            @Override
            protected ImageIcon doInBackground() throws Exception {
                try {
                    String posterUrl = movie.getPosterUrl();
                    if (posterUrl != null && !posterUrl.isEmpty()) {
                        URL url = new URL(posterUrl);
                        BufferedImage img = ImageIO.read(url);
                        Image scaledImg = img.getScaledInstance(380, 480, Image.SCALE_SMOOTH);
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
                    } else {
                        posterLabel.setText("🎬");
                        posterLabel.setFont(new Font("Arial", Font.BOLD, 80));
                        posterLabel.setForeground(new Color(60, 60, 60));
                    }
                } catch (Exception e) {
                    posterLabel.setText("🎬");
                    posterLabel.setFont(new Font("Arial", Font.BOLD, 80));
                    posterLabel.setForeground(new Color(60, 60, 60));
                }
            }
        };
        imageLoader.execute();
    }
    
    private JButton createNavButton(String text) {
        JButton button = new JButton(text);
        button.setForeground(text.equals("Movies") ? Color.WHITE : Color.LIGHT_GRAY);
        button.setBackground(null);
        button.setBorder(null);
        button.setContentAreaFilled(false);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setFont(new Font("Arial", Font.PLAIN, 16));
        
        return button;
    }
    
    private JButton createActionButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 16));
        button.setBackground(brightRed);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setPreferredSize(new Dimension(200, 45));
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
        if (selectedMovie == null) {
            JOptionPane.showMessageDialog(this, "No movie selected!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        List<Comment> comments = filmController.getCommentsForMovie(selectedMovie);
        
        JDialog commentsDialog = new JDialog(this, "Comments - " + selectedMovie.getTitle(), true);
        commentsDialog.setSize(700, 500);
        commentsDialog.setLocationRelativeTo(this);
        commentsDialog.getContentPane().setBackground(darkBackground);
        
        JPanel commentsPanel = new JPanel();
        commentsPanel.setLayout(new BoxLayout(commentsPanel, BoxLayout.Y_AXIS));
        commentsPanel.setBackground(darkBackground);
        commentsPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        if (comments.isEmpty()) {
            JLabel noCommentsLabel = new JLabel("No comments yet. Be the first to comment!");
            noCommentsLabel.setForeground(Color.GRAY);
            noCommentsLabel.setFont(new Font("Arial", Font.PLAIN, 18));
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
    private void showCommentsList() {
        JDialog dialog = new JDialog(this, "Comments", true);
        dialog.setSize(500, 400);
        dialog.setLayout(new BorderLayout());

        // HEADER
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(Color.DARK_GRAY);
        JButton back = new JButton("BACK");
        back.setBackground(Color.RED);
        back.setForeground(Color.WHITE);
        back.addActionListener(e -> dialog.dispose());
        JLabel title = new JLabel("COMMENTS:", SwingConstants.CENTER);
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Arial", Font.BOLD, 16));
        header.add(back, BorderLayout.WEST);
        header.add(title, BorderLayout.CENTER);

        // CENTER
        JPanel center = new JPanel();
        center.setLayout(new BoxLayout(center, BoxLayout.Y_AXIS));
        center.setBackground(Color.LIGHT_GRAY);
        center.setBorder(new EmptyBorder(20, 30, 20, 30));

        
        for (Comment comment : commentsList) {
            JPanel commentPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            commentPanel.setBackground(Color.LIGHT_GRAY);

            JLabel usernameLabel = new JLabel(comment.username + ": ");
            usernameLabel.setFont(new Font("Arial", Font.BOLD, 14));
            usernameLabel.setForeground(Color.RED);

            JLabel commentLabel = new JLabel(comment.commentText);
            commentLabel.setFont(new Font("Arial", Font.PLAIN, 12));
            commentLabel.setForeground(Color.BLACK);

            commentPanel.add(usernameLabel);
            commentPanel.add(commentLabel);
            center.add(commentPanel);
            center.add(Box.createRigidArea(new Dimension(0, 15)));
        }
        
        JDialog commentDialog = new JDialog(this, "Add Comment - " + selectedMovie.getTitle(), true);
        commentDialog.setSize(600, 400);
        commentDialog.setLocationRelativeTo(this);
        commentDialog.getContentPane().setBackground(darkBackground);
        commentDialog.setLayout(new BorderLayout());
        
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(darkBackground);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JLabel instructionLabel = new JLabel("Write your comment:");
        instructionLabel.setFont(new Font("Arial", Font.BOLD, 18));
        instructionLabel.setForeground(Color.WHITE);
        
        JTextArea commentArea = new JTextArea();
        commentArea.setFont(new Font("Arial", Font.PLAIN, 16));
        commentArea.setBackground(new Color(40, 40, 40));
        commentArea.setForeground(Color.WHITE);
        commentArea.setCaretColor(Color.WHITE);
        commentArea.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.GRAY, 1),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        
        JButton submitButton = new JButton("Submit");
        submitButton.setFont(new Font("Arial", Font.BOLD, 18));
        submitButton.setBackground(brightRed);
        submitButton.setForeground(Color.WHITE);
        submitButton.setFocusPainted(false);
        submitButton.setBorderPainted(false);
        submitButton.setPreferredSize(new Dimension(150, 45));
        submitButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        submitButton.addActionListener(e -> {
            String commentText = commentArea.getText().trim();
            if (!commentText.isEmpty()) {
                filmController.addCommentToMovie(selectedMovie, currentUser, commentText);
                JOptionPane.showMessageDialog(commentDialog, 
                    "Comment added successfully!", 
                    "Success", 
                    JOptionPane.INFORMATION_MESSAGE);
                commentDialog.dispose();
            } else {
                JOptionPane.showMessageDialog(commentDialog,
                    "Please write a comment before submitting!",
                    "Warning",
                    JOptionPane.WARNING_MESSAGE);
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
        if (selectedMovie == null) {
            JOptionPane.showMessageDialog(this, "No movie selected!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        JDialog ratingDialog = new JDialog(this, "Add Rating - " + selectedMovie.getTitle(), true);
        ratingDialog.setSize(450, 350);
        ratingDialog.setLocationRelativeTo(this);
        ratingDialog.getContentPane().setBackground(darkBackground);
        ratingDialog.setLayout(new BorderLayout());
        
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(darkBackground);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JLabel instructionLabel = new JLabel("Rate this movie (1-10):");
        instructionLabel.setFont(new Font("Arial", Font.BOLD, 20));
        instructionLabel.setForeground(Color.WHITE);
        instructionLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Rating buttons panel
        JPanel ratingPanel = new JPanel(new GridLayout(2, 5, 10, 10));
        ratingPanel.setBackground(darkBackground);
        ratingPanel.setMaximumSize(new Dimension(400, 100));
        
        ButtonGroup ratingGroup = new ButtonGroup();
        JRadioButton[] ratingButtons = new JRadioButton[10];
        
        for (int i = 0; i < 10; i++) {
            JRadioButton button = new JRadioButton(String.valueOf(i + 1));
            button.setFont(new Font("Arial", Font.BOLD, 18));
            button.setForeground(Color.WHITE);
            button.setBackground(darkBackground);
            button.setFocusPainted(false);
            ratingButtons[i] = button;
            ratingGroup.add(button);
            ratingPanel.add(button);
        }
        
        JButton submitButton = new JButton("Submit");
        submitButton.setFont(new Font("Arial", Font.BOLD, 18));
        submitButton.setBackground(brightRed);
        submitButton.setForeground(Color.WHITE);
        submitButton.setFocusPainted(false);
        submitButton.setBorderPainted(false);
        submitButton.setPreferredSize(new Dimension(150, 45));
        submitButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        submitButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        submitButton.addActionListener(e -> {
            for (int i = 0; i < ratingButtons.length; i++) {
                if (ratingButtons[i].isSelected()) {
                    double rating = i + 1;
                    filmController.rateMovie(selectedMovie, currentUser, rating);
                    JOptionPane.showMessageDialog(ratingDialog, 
                        "Rating submitted successfully!", 
                        "Success", 
                        JOptionPane.INFORMATION_MESSAGE);
                    ratingDialog.dispose();
                    
                    // Update the displayed rating
                    userRatingLabel.setText("   User Rating: " + 
                        String.format("%.1f", selectedMovie.getAverageRating()));
                    return;
                }
            }
            JOptionPane.showMessageDialog(ratingDialog,
                "Please select a rating before submitting!",
                "Warning",
                JOptionPane.WARNING_MESSAGE);
        });
        
        contentPanel.add(instructionLabel);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 30)));
        contentPanel.add(ratingPanel);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 40)));
        contentPanel.add(submitButton);
        
        ratingDialog.add(contentPanel);
        ratingDialog.setVisible(true);
    }
    
    private void layoutComponents() {
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(contentPanel, BorderLayout.CENTER);
        
        add(mainPanel);
    }
    
    // Navigation methods
    private void navigateToHome() {
        dispose();
        new HomePage(filmController, userController, currentUser);
    }
    
    private void navigateToExplore() {
        dispose();
        new ExploreFrame(filmController, userController, currentUser);
    }
    
    private void navigateToMyList() {
        dispose();
        new MyListPanel();
    }
    
    private void navigateToProfile() {
        dispose();
        new ProfileFrame(currentUser.getUsername());
    }
    
    // Main method for testing
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // Initialize controllers
            FilmController filmController = new FilmController();
            UserController userController = new UserController();
            
            // Seed movies
            MovieSeeder.seedMovies(filmController);

            
            // Get a sample movie for testing
            List<Movie> movies = filmController.getAllMovies();
            Movie sampleMovie = movies.isEmpty() ? null : movies.get(0);
            
        });
    }
}