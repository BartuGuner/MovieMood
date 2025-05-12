import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.border.*;
import java.util.ArrayList;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.net.URL;

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
        
        // Get the profile picture path
        String profilePath = newUser.getProfilePicturePath();
        System.out.println("Profil fotoğrafı yolu: " + profilePath);
        
        // Create profile picture circle using the user's profile picture URL
        BufferedImage profileImage = tryLoadImage(profilePath);
        
        // YENİ: Dairesel PicturePanel oluştur (TAMAMEN YENİ SINIF)
        CircularPicturePanel profileCircle = new CircularPicturePanel(profileImage, 150, darkBackground);
        
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
                User friend = newUser.getFriends().get(i);
                // Load friend profile image
                BufferedImage friendImage = tryLoadImage(friend.getProfilePicturePath());
                // Create friend circle with their profile picture
                CircularPicturePanel friendCircle = new CircularPicturePanel(friendImage, 100, darkBackground, friend.getUsername());
                friendCircles.add(new FriendCircle(friend.getUsername(), friendCircle));
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
    
    /**
     * Resim dosyasını birden fazla yöntemle yüklemeyi dener
     */
    private BufferedImage tryLoadImage(String imagePath) {
        BufferedImage image = null;
        
        // 1. Yöntem: Doğrudan dosya yolu ile yükleme
        try {
            File file = new File(imagePath);
            if (file.exists() && file.canRead()) {
                image = ImageIO.read(file);
                if (image != null) {
                    System.out.println("Resim doğrudan dosya yoluyla yüklendi: " + imagePath);
                    return image;
                }
            }
        } catch (Exception e) {
            System.err.println("Doğrudan dosya yolu ile yükleme başarısız: " + e.getMessage());
        }
        
        // 2. Yöntem: Sınıf yükleyicisi üzerinden kaynak olarak yükleme
        try {
            URL resourceUrl = getClass().getResource("/" + imagePath);
            if (resourceUrl != null) {
                image = ImageIO.read(resourceUrl);
                if (image != null) {
                    System.out.println("Resim kaynak olarak yüklendi: " + imagePath);
                    return image;
                }
            }
        } catch (Exception e) {
            System.err.println("Kaynak olarak yükleme başarısız: " + e.getMessage());
        }
        
        // 3. Yöntem: ImageIcon kullanarak yükleme
        try {
            ImageIcon icon = new ImageIcon(imagePath);
            if (icon.getIconWidth() > 0) {
                // ImageIcon'dan BufferedImage'e dönüştür
                image = new BufferedImage(
                    icon.getIconWidth(),
                    icon.getIconHeight(),
                    BufferedImage.TYPE_INT_ARGB
                );
                Graphics g = image.createGraphics();
                icon.paintIcon(null, g, 0, 0);
                g.dispose();
                System.out.println("Resim ImageIcon ile yüklendi: " + imagePath);
                return image;
            }
        } catch (Exception e) {
            System.err.println("ImageIcon ile yükleme başarısız: " + e.getMessage());
        }
        
        // 4. Yöntem: Alternatif yollarla yüklemeyi dene
        String[] possiblePaths = {
            imagePath,
            "images/" + new File(imagePath).getName(),
            System.getProperty("user.dir") + File.separator + imagePath,
            System.getProperty("user.dir") + File.separator + "images" + File.separator + new File(imagePath).getName(),
            ".." + File.separator + imagePath,
            ".." + File.separator + "images" + File.separator + new File(imagePath).getName(),
            "src" + File.separator + imagePath,
            "src" + File.separator + "images" + File.separator + new File(imagePath).getName()
        };
        
        for (String path : possiblePaths) {
            try {
                File file = new File(path);
                if (file.exists() && file.canRead()) {
                    image = ImageIO.read(file);
                    if (image != null) {
                        System.out.println("Resim alternatif yolla yüklendi: " + path);
                        return image;
                    }
                }
            } catch (Exception e) {
                // Bu yol çalışmadı, bir sonrakine geç
            }
        }
        
        // Hiçbir yöntem işe yaramadı, null döndür
        System.err.println("Hiçbir yöntem ile resim yüklenemedi: " + imagePath);
        return null;
    }
    
    /**
     * Movie sınıfından posterPath değerini almak için yardımcı metot
     */
    private String getPosterPathFromMovie(Movie movie) {
        if (movie == null) {
            return null;
        }
        
        try {
            // getMovie() metoduyla posterPath'e erişmeyi deneyelim
            Map<String, Object> movieData = movie.getMovie();
            if (movieData != null && movieData.containsKey("posterPath")) {
                return (String) movieData.get("posterPath");
            }
        } catch (Exception e) {
            System.err.println("getMovie() metodu ile erişimde hata: " + e.getMessage());
        }
        
        // Eğer yukarıdaki çalışmazsa null döndür
        return null;
    }
    
    /**
     * YENİ SINIF: Dairesel resim paneli - Özel olarak daire içine resim çizmek için
     */
    static class CircularPicturePanel extends JPanel {
        private BufferedImage image;
        private int diameter;
        private String label;
        private Color backgroundColor;
        
        public CircularPicturePanel(BufferedImage image, int diameter, Color backgroundColor) {
            this(image, diameter, backgroundColor, null);
        }
        
        public CircularPicturePanel(BufferedImage image, int diameter, Color backgroundColor, String label) {
            this.image = image;
            this.diameter = diameter;
            this.label = label;
            this.backgroundColor = backgroundColor;
            setPreferredSize(new Dimension(diameter, diameter));
            setMinimumSize(new Dimension(diameter, diameter));
            setMaximumSize(new Dimension(diameter, diameter));
            // Önemli: Panelin arkaplanını saydam yap ve layout manager'ı null yap
            setOpaque(false);
            setBackground(new Color(0, 0, 0, 0)); // Tamamen saydam
        }
        
        @Override
        protected void paintComponent(Graphics g) {
            // Tamamen saydam arkaplan
            g.setColor(new Color(0, 0, 0, 0));
            g.fillRect(0, 0, getWidth(), getHeight());
            
            Graphics2D g2d = (Graphics2D) g.create();
            
            // Düzgün kenarlar için anti-aliasing
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            
            if (image != null) {
                // Dairesel kırpma alanı oluştur
                g2d.setClip(new java.awt.geom.Ellipse2D.Float(0, 0, diameter, diameter));
                
                // Resmi daireye sığdır
                g2d.drawImage(image, 0, 0, diameter, diameter, null);
            } else {
                // Resim yoksa gri daire göster
                g2d.setColor(Color.LIGHT_GRAY);
                g2d.fillOval(0, 0, diameter, diameter);
                
                // Eğer etiket varsa, baş harfini göster
                if (label != null && !label.isEmpty()) {
                    g2d.setColor(Color.WHITE);
                    g2d.setFont(new Font("Arial", Font.BOLD, diameter/3));
                    String text = label.substring(0, 1).toUpperCase();
                    FontMetrics fm = g2d.getFontMetrics();
                    int textWidth = fm.stringWidth(text);
                    int textHeight = fm.getHeight();
                    g2d.drawString(text, (diameter - textWidth)/2, diameter/2 + textHeight/4);
                }
            }
            
            g2d.dispose();
        }
    }
    
    // Inner class for friend circles
    class FriendCircle extends JPanel {
        private String name;
        
        public FriendCircle(String name, CircularPicturePanel picturePanel) {
            this.name = name;
            setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
            setBackground(darkBackground);
            
            picturePanel.setAlignmentX(Component.CENTER_ALIGNMENT);
            
            // Friend name below circle
            JLabel nameLabel = new JLabel(name);
            nameLabel.setForeground(Color.WHITE);
            nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            
            add(picturePanel);
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
            
            // Film poster yolunu al
            String posterPath = movie != null ? getPosterPathFromMovie(movie) : null;
            
            // Film poster paneli
            JPanel poster;
            
            if (posterPath != null && !posterPath.isEmpty()) {
                // Poster resmini yükle
                final BufferedImage posterImage = tryLoadImage(posterPath);
                
                poster = new JPanel() {
                    @Override
                    protected void paintComponent(Graphics g) {
                        super.paintComponent(g);
                        
                        if (posterImage != null) {
                            // Poster resmini çiz
                            g.drawImage(posterImage, 0, 0, 120, 180, this);
                        } else {
                            // Resim yüklenemediyse koyu gri arkaplan ile başlığı göster
                            g.setColor(Color.DARK_GRAY);
                            g.fillRect(0, 0, 120, 180);
                            
                            // Film başlığını göster
                            if (movie != null) {
                                g.setColor(Color.WHITE);
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
                    }
                };
            } else {
                // Film posteri yoksa, gri dikdörtgen göster
                poster = new JPanel() {
                    @Override
                    protected void paintComponent(Graphics g) {
                        super.paintComponent(g);
                        g.setColor(Color.DARK_GRAY);
                        g.fillRect(0, 0, 120, 180);
                        
                        // Eğer film varsa başlığını göster
                        if (movie != null) {
                            g.setColor(Color.WHITE);
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
            }
            
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