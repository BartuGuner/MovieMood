import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.border.*;
import java.util.ArrayList;

public class ProfileFrame extends JFrame {
    
    // Components
    private JPanel headerPanel, mainPanel, profilePanel, friendsPanel, moviesPanel;
    private JLabel titleLabel, usernameLabel, friendsLabel, recentMoviesLabel;
    private JButton homeButton, exploreButton, myListButton, moviesButton, profileButton, chatButton, addFriendButton;
    private ArrayList<FriendCircle> friendCircles;
    private ArrayList<MoviePoster> moviePosters;
    
    // Colors
    private Color darkBackground = new Color(25, 25, 25);
    private Color brightRed = new Color(230, 0, 0);
    
    // User info
    private User newUser;
    
    public ProfileFrame(User newUser) {
        this.newUser = newUser;
        
        setTitle("Movie Mood - Profile");
        setSize(1000, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(darkBackground);
        setLayout(new BorderLayout());
        
        initComponents();
        layoutComponents();
        
        setVisible(true);
    }
    
    private void initComponents() {
        // Initialize panels
        headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(darkBackground);
        
        mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(darkBackground);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 70, 20, 70));
        
        // Header components - Movie Mood title on left, navigation on right
        titleLabel = new JLabel("Movie Mood");
        titleLabel.setForeground(brightRed);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 28));
        
        JPanel navPanel = new JPanel();
        navPanel.setLayout(new FlowLayout(FlowLayout.RIGHT, 30, 10));
        navPanel.setBackground(darkBackground);
        
        // Navigation buttons
        homeButton = createNavButton("Home");
        exploreButton = createNavButton("Explore");
        myListButton = createNavButton("My List");
        moviesButton = createNavButton("Movies");
        profileButton = createNavButton("My Profile");
        
        // Chat button (white square)
        chatButton = new JButton();
        chatButton.setBackground(Color.WHITE);
        chatButton.setPreferredSize(new Dimension(40, 40));
        chatButton.setBorderPainted(false);
        chatButton.setFocusPainted(false);
        
        // Profile section - picture on left, username to the right
        profilePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 20));
        profilePanel.setBackground(darkBackground);
        profilePanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        // Create profile picture circle
        JPanel profileCircle = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.setColor(Color.LIGHT_GRAY);
                g.fillOval(0, 0, 150, 150);
            }
        };
        profileCircle.setPreferredSize(new Dimension(150, 150));
        
        // Username next to profile picture
        usernameLabel = new JLabel(newUser.getUsername());
        usernameLabel.setFont(new Font("Arial", Font.BOLD, 28));
        usernameLabel.setForeground(Color.WHITE);
        usernameLabel.setBorder(BorderFactory.createEmptyBorder(50, 20, 0, 0));
        
        // Friends section
        friendsPanel = new JPanel();
        friendsPanel.setLayout(new BorderLayout());
        friendsPanel.setBackground(darkBackground);
        friendsPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        // Friends header with label on left, add friend button on right
        JPanel friendsHeaderPanel = new JPanel(new BorderLayout());
        friendsHeaderPanel.setBackground(darkBackground);
        friendsHeaderPanel.setBorder(BorderFactory.createEmptyBorder(30, 0, 10, 0));
        
        friendsLabel = new JLabel("Friends");
        friendsLabel.setFont(new Font("Arial", Font.BOLD, 24));
        friendsLabel.setForeground(Color.WHITE);
        
        // Add Friend button (white with text)
        addFriendButton = new JButton("+ Add Friend");
        addFriendButton.setBackground(Color.WHITE);
        addFriendButton.setForeground(Color.BLACK);
        addFriendButton.setFocusPainted(false);
        addFriendButton.setBorderPainted(false);
        
        // Friends row panel
        JPanel friendsRowPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 10));
        friendsRowPanel.setBackground(darkBackground);
        
        // Create friend circles
        friendCircles = new ArrayList<>();
        
        // Check if user has friends
        if (newUser.getFriends().isEmpty()) {
            // Display "No friends yet" message
            JLabel noFriendsLabel = new JLabel("No friends yet");
            noFriendsLabel.setForeground(Color.LIGHT_GRAY);
            noFriendsLabel.setFont(new Font("Arial", Font.ITALIC, 16));
            friendsRowPanel.add(noFriendsLabel);
        } else {
            // Create friend circles for existing friends
            for (int i = 0; i < newUser.getFriends().size(); i++) {
                friendCircles.add(new FriendCircle(newUser.getFriends().get(i).getUsername()));
            }
            
            // Add friends to panel
            for (FriendCircle friend : friendCircles) {
                friendsRowPanel.add(friend);
            }
        }
        
        // Recent movies section
        moviesPanel = new JPanel();
        moviesPanel.setLayout(new BorderLayout());
        moviesPanel.setBackground(darkBackground);
        moviesPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        recentMoviesLabel = new JLabel("Recent Movies");
        recentMoviesLabel.setFont(new Font("Arial", Font.BOLD, 24));
        recentMoviesLabel.setForeground(Color.WHITE);
        recentMoviesLabel.setBorder(BorderFactory.createEmptyBorder(30, 0, 10, 0));
        
        // Movies row panel
        JPanel movieRowPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        movieRowPanel.setBackground(darkBackground);
        
        // Check if user has recently watched movies or favorite movies
        if (newUser.getRecentlyWatched().isEmpty() && newUser.getFavoriteMovies().isEmpty()) {
            // Display "No movies yet" message
            JLabel noMoviesLabel = new JLabel("No movies yet");
            noMoviesLabel.setForeground(Color.LIGHT_GRAY);
            noMoviesLabel.setFont(new Font("Arial", Font.ITALIC, 16));
            movieRowPanel.add(noMoviesLabel);
        } else {
            // Create movie posters
            moviePosters = new ArrayList<>();
            
            // Add posters for favorite movies if available
            if (!newUser.getFavoriteMovies().isEmpty()) {
                // Convert set to array to access elements
                Movie[] favMovies = newUser.getFavoriteMovies().toArray(new Movie[0]);
                for (int i = 0; i < Math.min(5, newUser.getFavoriteMovies().size()); i++) {
                    moviePosters.add(new MoviePoster(favMovies[i]));
                }
            } 
            // Fill remaining slots with recently watched movies if needed
            else if (!newUser.getRecentlyWatched().isEmpty()) {
                for (int i = 0; i < Math.min(5, newUser.getRecentlyWatched().size()); i++) {
                    moviePosters.add(new MoviePoster(newUser.getRecentlyWatched().get(i)));
                }
            }
            // If we still need more posters, add blank ones
            while (moviePosters.size() < 5) {
                moviePosters.add(new MoviePoster());
            }
            
            // Add movies to panel
            for (MoviePoster poster : moviePosters) {
                movieRowPanel.add(poster);
            }
        }
        
        // Add components to panels
        navPanel.add(homeButton);
        navPanel.add(exploreButton);
        navPanel.add(myListButton);
        navPanel.add(moviesButton);
        navPanel.add(profileButton);
        navPanel.add(chatButton);
        
        profilePanel.add(profileCircle);
        profilePanel.add(usernameLabel);
        
        JPanel friendsTitlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        friendsTitlePanel.setBackground(darkBackground);
        friendsTitlePanel.add(friendsLabel);
        
        JPanel addFriendButtonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        addFriendButtonPanel.setBackground(darkBackground);
        addFriendButtonPanel.add(addFriendButton);
        
        friendsHeaderPanel.add(friendsTitlePanel, BorderLayout.WEST);
        friendsHeaderPanel.add(addFriendButtonPanel, BorderLayout.EAST);
        
        friendsPanel.add(friendsHeaderPanel, BorderLayout.NORTH);
        friendsPanel.add(friendsRowPanel, BorderLayout.CENTER);
        
        moviesPanel.add(recentMoviesLabel, BorderLayout.NORTH);
        moviesPanel.add(movieRowPanel, BorderLayout.CENTER);
    }
    
    private void layoutComponents() {
        // Add header components
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        titlePanel.setBackground(darkBackground);
        titlePanel.add(titleLabel);
        
        headerPanel.add(titlePanel, BorderLayout.WEST);
        headerPanel.add(new JSeparator(JSeparator.HORIZONTAL), BorderLayout.SOUTH);
        
        JPanel navContainer = new JPanel();
        navContainer.setLayout(new FlowLayout(FlowLayout.RIGHT));
        navContainer.setBackground(darkBackground);
        
        JPanel navigationPanel = new JPanel();
        navigationPanel.setLayout(new FlowLayout(FlowLayout.RIGHT, 30, 10));
        navigationPanel.setBackground(darkBackground);
        navigationPanel.add(homeButton);
        navigationPanel.add(exploreButton);
        navigationPanel.add(myListButton);
        navigationPanel.add(moviesButton);
        navigationPanel.add(profileButton);
        navigationPanel.add(chatButton);
        
        navContainer.add(navigationPanel);
        headerPanel.add(navContainer, BorderLayout.EAST);
        
        // Add all sections to main panel
        mainPanel.add(profilePanel);
        mainPanel.add(friendsPanel);
        mainPanel.add(moviesPanel);
        
        // Add main panels to frame
        add(headerPanel, BorderLayout.NORTH);
        add(mainPanel, BorderLayout.CENTER);
    }
    
    private JButton createNavButton(String text) {
        JButton button = new JButton(text);
        button.setBackground(darkBackground);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setFont(new Font("Arial", Font.PLAIN, 16));
        
        return button;
    }
    
    // Inner class for friend circles
    class FriendCircle extends JPanel {
        private String name;
        
        public FriendCircle(String name) {
            this.name = name;
            setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
            setBackground(darkBackground);
            
            // Create gray circle
            JPanel circle = new JPanel() {
                @Override
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    g.setColor(Color.GRAY);
                    g.fillOval(0, 0, 100, 100);
                }
            };
            circle.setPreferredSize(new Dimension(100, 100));
            circle.setMaximumSize(new Dimension(100, 100));
            circle.setAlignmentX(Component.CENTER_ALIGNMENT);
            
            // Friend name below circle
            JLabel nameLabel = new JLabel(name);
            nameLabel.setForeground(Color.WHITE);
            nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            
            add(circle);
            add(Box.createRigidArea(new Dimension(0, 5)));
            add(nameLabel);
        }
    }
    
    // Inner class for movie posters
    class MoviePoster extends JPanel {
        private Movie movie;
        
        public MoviePoster() {
            this(null); // Default constructor calls the parameterized one with null
        }
        
        public MoviePoster(Movie movie) {
            this.movie = movie;
            setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
            setBackground(darkBackground);
            
            // Create gray rectangle for movie poster
            JPanel poster = new JPanel() {
                @Override
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    g.setColor(Color.DARK_GRAY);
                    g.fillRect(0, 0, 120, 180);
                    
                    // If we have a movie, could potentially display movie title on poster
                    if (movie != null) {
                        g.setColor(Color.WHITE);
                        
                        // Center and draw the movie title if space allows
                        String title = movie.getTitle();
                        if (title.length() > 15) {
                            title = title.substring(0, 12) + "...";
                        }
                        
                        FontMetrics fm = g.getFontMetrics();
                        int x = (120 - fm.stringWidth(title)) / 2;
                        int y = 90 + fm.getAscent();
                        
                        g.drawString(title, x, y);
                    }
                }
            };
            poster.setPreferredSize(new Dimension(120, 180));
            poster.setBorder(BorderFactory.createLineBorder(Color.GRAY));
            
            add(poster);
            
            // If movie is not null, add title below poster
            if (movie != null) {
                JLabel titleLabel = new JLabel(movie.getTitle());
                titleLabel.setForeground(Color.WHITE);
                titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
                // Ensure the title label doesn't exceed poster width
                if (titleLabel.getPreferredSize().width > 120) {
                    String title = movie.getTitle();
                    if (title.length() > 15) {
                        titleLabel.setText(title.substring(0, 12) + "...");
                    }
                }
                
                add(Box.createRigidArea(new Dimension(0, 5)));
                add(titleLabel);
            }
        }
    }
}