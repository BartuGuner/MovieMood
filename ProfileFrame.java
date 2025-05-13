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
import javax.swing.filechooser.FileNameExtensionFilter;

public class ProfileFrame extends JFrame {
    
    // Components
    private JPanel headerPanel, mainPanel, profilePanel, friendsPanel, moviesPanel;
    private JLabel titleLabel, usernameLabel, friendsLabel, recentMoviesLabel;
    private JButton homeButton, exploreButton, myListButton, moviesButton, profileButton, chatButton, addFriendButton;
    private JButton editProfilePictureButton; // Button for editing profile picture
    private JButton logoutButton; // Button for logging out
    private ArrayList<FriendCircle> friendCircles;
    private ArrayList<MoviePoster> moviePosters;
    private CircularPicturePanel profileCircle; // Made this a class field to update it
    
    // Colors
    private Color darkBackground = Color.BLACK;
    private Color brightRed = new Color(230, 0, 0);
    
    // User info
    private User newUser;
    
    public ProfileFrame(User newUser) {
        this.newUser = newUser;
        
        setTitle("Movie Mood - Profile");
        setSize(1200, 900);
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
        headerPanel.setBackground(Color.BLACK);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        
        mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(darkBackground);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 70, 20, 70));
        
        // Header components - Movie Mood title on left, navigation in center, chat on right
        titleLabel = new JLabel("Movie Mood");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(Color.RED);
        headerPanel.add(titleLabel, BorderLayout.WEST);
        
        // Navigation buttons
        JPanel navPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        navPanel.setOpaque(false);
        
        homeButton = createNavButton("Home");
        exploreButton = createNavButton("Explore");
        myListButton = createNavButton("My List");
        moviesButton = createNavButton("Movies");
        profileButton = createNavButton("My Profile");
        // Current page button should be highlighted
        styleButton(profileButton, true); 
        
        // Mevcut ActionListener'lar korunuyor
        homeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // HomePage'e git
                FilmController filmController = new FilmController();
                UserController userController = new UserController();
                new HomePage(filmController, userController, newUser);
                setVisible(false); // Mevcut frame'i gizle
            }
        });
        
        exploreButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // ExploreFrame'e git
                FilmController filmController = new FilmController();
                UserController userController = new UserController();
                new ExploreFrame(filmController, userController, newUser);
                setVisible(false); // Mevcut frame'i gizle
            }
        });
        
        myListButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Create a new MyListPanel instance
                MyListPanel myListPanel = new MyListPanel(newUser);
                // Set the necessary controllers
                myListPanel.setFilmController(new FilmController());
                myListPanel.setFilmListController(new FilmListController());
                // No need to hide this frame - both can be visible
                // If you want to hide this frame, uncomment the next line:
                setVisible(false);
            }
        });
        
        moviesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // MoviesPage'e git
                FilmController filmController = new FilmController();
                UserController userController = new UserController();
                new MoviesPage(filmController, userController, newUser);
                setVisible(false); // Mevcut frame'i gizle
            }
        });
        
        navPanel.add(homeButton);
        navPanel.add(exploreButton);
        navPanel.add(myListButton);
        navPanel.add(moviesButton);
        navPanel.add(profileButton);
        
        headerPanel.add(navPanel, BorderLayout.CENTER);
        
        // Logout button
        logoutButton = new JButton("Logout");
        logoutButton.setFont(new Font("Arial", Font.PLAIN, 14));
        styleButton(logoutButton, false);
        logoutButton.setForeground(Color.LIGHT_GRAY);
        logoutButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Logout butonuna action listener ekleyin
        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Çıkış onayı iste
                int response = JOptionPane.showConfirmDialog(
                    ProfileFrame.this,
                    "Are you sure you want to log out?",
                    "Confirm Logout",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE
                );
                
                if (response == JOptionPane.YES_OPTION) {
                    // Mevcut frame'i kapat
                    setVisible(false);
                    dispose();
                    
                    // Giriş ekranını aç
                    new MovieMoodLoginUI();
                }
            }
        });
        
        // Chat button
        chatButton = new JButton("💬");
        chatButton.setFont(new Font("Dialog", Font.PLAIN, 20));
        styleButton(chatButton, false);
        chatButton.setBackground(Color.WHITE);
        chatButton.setForeground(Color.BLACK);
        chatButton.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        
        // logoutButton ve chatButton'u aynı panele ekleyin
        JPanel rightButtonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        rightButtonsPanel.setOpaque(false);
        rightButtonsPanel.add(logoutButton);
        rightButtonsPanel.add(chatButton);
        
        // Şimdi bu paneli headerPanel'e ekleyin
        headerPanel.add(rightButtonsPanel, BorderLayout.EAST);
        
        // Profile section - picture with edit button on left, username to the right
        profilePanel = new JPanel();
        profilePanel.setLayout(new BoxLayout(profilePanel, BoxLayout.X_AXIS));
        profilePanel.setBackground(darkBackground);
        profilePanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        // Left panel for profile picture and edit button
        JPanel profilePicturePanel = new JPanel();
        profilePicturePanel.setLayout(new BoxLayout(profilePicturePanel, BoxLayout.Y_AXIS));
        profilePicturePanel.setBackground(darkBackground);
        profilePicturePanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 20)); // Add right margin
        
        // Get the profile picture path
        String profilePath = newUser.getProfilePicturePath();
        System.out.println("Profil fotoğrafı yolu: " + profilePath);
        
        // Create profile picture circle using the user's profile picture URL
        BufferedImage profileImage = tryLoadImage(profilePath);
        
        // Create circular profile picture panel
        profileCircle = new CircularPicturePanel(profileImage, 150, darkBackground);
        profileCircle.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Create edit profile picture button with updated style to match the image
        editProfilePictureButton = new JButton("Edit Profile Picture");
        editProfilePictureButton.setBackground(Color.RED);
        editProfilePictureButton.setForeground(Color.WHITE);
        editProfilePictureButton.setFocusPainted(false);
        editProfilePictureButton.setContentAreaFilled(true);
        editProfilePictureButton.setOpaque(true);
        editProfilePictureButton.setBorderPainted(false);
        editProfilePictureButton.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        editProfilePictureButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        editProfilePictureButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        editProfilePictureButton.setMaximumSize(new Dimension(180, 35));
        editProfilePictureButton.setPreferredSize(new Dimension(180, 35));
        
        // Add action listener to edit profile picture button
        editProfilePictureButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                chooseProfilePicture();
            }
        });
        
        // Add profile picture and edit button to the left panel
        profilePicturePanel.add(profileCircle);
        profilePicturePanel.add(Box.createRigidArea(new Dimension(0, 10)));
        profilePicturePanel.add(editProfilePictureButton);
        
        // Right panel for username (to match layout in image)
        JPanel usernamePanel = new JPanel();
        usernamePanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        usernamePanel.setBackground(darkBackground);
        usernamePanel.setAlignmentY(Component.TOP_ALIGNMENT);
        
        // Username label with larger font to match image
        usernameLabel = new JLabel(newUser.getUsername());
        usernameLabel.setFont(new Font("Arial", Font.BOLD, 36)); // Increased font size
        usernameLabel.setForeground(Color.WHITE);
        
        usernamePanel.add(usernameLabel);
        
        // Add components to profile panel
        profilePanel.add(profilePicturePanel);
        profilePanel.add(usernamePanel);
        
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
        addFriendButton.setBackground(Color.RED);
        addFriendButton.setForeground(Color.WHITE);
        addFriendButton.setFocusPainted(false);
        addFriendButton.setContentAreaFilled(true);
        addFriendButton.setOpaque(true);
        addFriendButton.setBorderPainted(false);
        addFriendButton.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        addFriendButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Add action listener to Add Friend button
        addFriendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showAddFriendDialog();
            }
        });
        
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
           // Your existing code with MoviePoster implementation
moviePosters = new ArrayList<>();

// Add posters for favorite movies if available
if (!newUser.getFavoriteMovies().isEmpty()) {
    Movie[] favMovies = newUser.getFavoriteMovies().toArray(new Movie[0]);
    for (int i = 0; i < Math.min(5, newUser.getFavoriteMovies().size()); i++) {
        // Create a panel for each movie poster
        JPanel moviePosterPanel = new JPanel(new BorderLayout());
        moviePosterPanel.setBackground(new Color(40, 40, 40));
        moviePosterPanel.setPreferredSize(new Dimension(130, 210));
        
        // Create poster image label
        JLabel posterLabel = new JLabel();
        posterLabel.setPreferredSize(new Dimension(130, 195));
        posterLabel.setHorizontalAlignment(JLabel.CENTER);
        posterLabel.setBackground(new Color(50, 50, 50));
        posterLabel.setOpaque(true);
        
        // Load poster image asynchronously
        final Movie movie = favMovies[i];
        SwingWorker<ImageIcon, Void> worker = new SwingWorker<ImageIcon, Void>() {
            @Override
            protected ImageIcon doInBackground() throws Exception {
                try {
                    String posterUrl = movie.getPosterUrl();
                    if (posterUrl != null && !posterUrl.isEmpty()) {
                        URL url = new URL(posterUrl);
                        BufferedImage img = ImageIO.read(url);
                        Image scaledImg = img.getScaledInstance(130, 195, Image.SCALE_SMOOTH);
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
                        posterLabel.setText("No Image");
                    }
                } catch (Exception e) {
                    posterLabel.setText("Error");
                }
            }
        };
        worker.execute();
        
        moviePosterPanel.add(posterLabel, BorderLayout.CENTER);
        movieRowPanel.add(moviePosterPanel);
    }
}
        }
        
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
        // Add main panels to frame
        add(headerPanel, BorderLayout.NORTH);
        add(mainPanel, BorderLayout.CENTER);
        
        // Add all sections to main panel
        mainPanel.add(profilePanel);
        mainPanel.add(friendsPanel);
        mainPanel.add(moviesPanel);
    }
    
    private JButton createNavButton(String text) {
        JButton button = new JButton(text);
        styleButton(button, false);
        return button;
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
    
    /**
     * Method to handle profile picture selection from predefined options
     */
    private void chooseProfilePicture() {
        // Create a custom dialog for picture selection
        JDialog pictureDialog = new JDialog(this, "Choose Profile Picture", true);
        pictureDialog.setSize(700, 300);
        pictureDialog.setLocationRelativeTo(this);
        pictureDialog.setLayout(new BorderLayout());
        
        // Create a panel to hold the image options
        JPanel imagesPanel = new JPanel(new GridLayout(1, 4, 10, 10));
        imagesPanel.setBackground(darkBackground);
        imagesPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Available profile images - excluding 1.webp as requested
        String[] imageFiles = {"2.jpg", "3.jpg", "4.jpg"};
        
        // Create a button for each image option
        for (String imageFile : imageFiles) {
            // Create a full path for the image
            String imagePath = "images/" + imageFile;
            
            // Load the image
            BufferedImage img = tryLoadImage(imagePath);
            
            // Create a panel to hold the image preview
            JPanel imageOption = new JPanel();
            imageOption.setLayout(new BoxLayout(imageOption, BoxLayout.Y_AXIS));
            imageOption.setBackground(darkBackground);
            
            // Create circular preview of the image
            final CircularPicturePanel previewCircle = new CircularPicturePanel(img, 120, darkBackground);
            previewCircle.setAlignmentX(Component.CENTER_ALIGNMENT);
            
            // Create a select button for this image
            JButton selectButton = new JButton("Select");
            selectButton.setBackground(Color.RED);
            selectButton.setForeground(Color.WHITE);
            selectButton.setFocusPainted(false);
            selectButton.setContentAreaFilled(true);
            selectButton.setOpaque(true);
            selectButton.setBorderPainted(false);
            selectButton.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
            selectButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
            selectButton.setAlignmentX(Component.CENTER_ALIGNMENT);
            selectButton.setMaximumSize(new Dimension(100, 30));


            
            // Add action to the select button
            final String finalImagePath = imagePath;
            final BufferedImage finalImage = img;
            
            selectButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    // Update the User object with the new profile picture path
                    newUser.setProfilePicturePath(finalImagePath);
                    
                    // Update the profile circle with the new image
                    profileCircle.updateImage(finalImage);
                    
                    // Force repaint
                    profileCircle.repaint();
                    
                    // Close the dialog
                    pictureDialog.dispose();
                    
                    // Show confirmation message
                    JOptionPane.showMessageDialog(ProfileFrame.this, 
                        "Profile picture updated successfully!", 
                        "Success", 
                        JOptionPane.INFORMATION_MESSAGE);
                }
            });
            
            // Add components to the image option panel
            imageOption.add(previewCircle);
            imageOption.add(Box.createRigidArea(new Dimension(0, 10)));
            imageOption.add(selectButton);
            
            // Add this image option to the images panel
            imagesPanel.add(imageOption);
        }
        
        // Add a title label at the top
        JLabel titleLabel = new JLabel("Select a profile picture");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setHorizontalAlignment(JLabel.CENTER);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(15, 0, 10, 0));
        
        // Add components to the dialog
        JPanel dialogPanel = new JPanel(new BorderLayout());
        dialogPanel.setBackground(darkBackground);
        dialogPanel.add(titleLabel, BorderLayout.NORTH);
        dialogPanel.add(imagesPanel, BorderLayout.CENTER);
        
        // Add a cancel button at the bottom
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(darkBackground);
        
        JButton cancelButton = new JButton("Cancel");
        cancelButton.setBackground(Color.RED);
        cancelButton.setForeground(Color.WHITE);
        cancelButton.setFocusPainted(false);
        cancelButton.setContentAreaFilled(true);
        cancelButton.setOpaque(true);
        cancelButton.setBorderPainted(false);
        cancelButton.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        cancelButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                pictureDialog.dispose();
            }
        });
        
        buttonPanel.add(cancelButton);
        dialogPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        // Add panel to dialog and show it
        pictureDialog.add(dialogPanel);
        pictureDialog.setVisible(true);
    }
    
    /**
     * Method to show add friend dialog
     */
    private void showAddFriendDialog() {
        // Create a custom dialog for friend search
        final JDialog addFriendDialog = new JDialog(this, "Add Friend", true);
        addFriendDialog.setSize(400, 200);
        addFriendDialog.setLocationRelativeTo(this);
        addFriendDialog.setLayout(new BorderLayout());
        
        // Create main panel with dark background
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(darkBackground);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Add title label
        JLabel titleLabel = new JLabel("Enter User ID");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Create search panel
        JPanel searchPanel = new JPanel();
        searchPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        searchPanel.setBackground(darkBackground);
        searchPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Add user ID input field
        final JTextField userIdField = new JTextField(10);
        userIdField.setFont(new Font("Arial", Font.PLAIN, 14));
        
        // Add search button
        JButton searchButton = new JButton("Search");
        searchButton.setBackground(new Color(60, 60, 60));
        searchButton.setForeground(Color.WHITE);
        searchButton.setFocusPainted(false);
        
        // Add components to search panel
        searchPanel.add(userIdField);
        searchPanel.add(searchButton);
        
        // Add result panel for displaying search results
        final JPanel resultPanel = new JPanel();
        resultPanel.setLayout(new BoxLayout(resultPanel, BoxLayout.Y_AXIS));
        resultPanel.setBackground(darkBackground);
        resultPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Add components to main panel
        mainPanel.add(titleLabel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        mainPanel.add(searchPanel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        mainPanel.add(resultPanel);
        
        // Add action listener to search button
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Clear previous results
                resultPanel.removeAll();
                
                try {
                    // Get user ID from input field
                    int userId = Integer.parseInt(userIdField.getText().trim());
                    
                    // Search for user with this ID
                    User foundUser = UserController.getUserById(userId);
                    
                    if (foundUser != null) {
                        // Check if this user is already a friend
                        boolean isAlreadyFriend = false;
                        for (User friend : newUser.getFriends()) {
                            if (friend.getUserId() == userId) {
                                isAlreadyFriend = true;
                                break;
                            }
                        }
                        
                        // Check if this is the current user
                        boolean isSelf = (newUser.getUserId() == userId);
                        
                        if (isSelf) {
                            // Cannot add yourself as a friend
                            JLabel resultLabel = new JLabel("You cannot add yourself as a friend.");
                            resultLabel.setForeground(Color.ORANGE);
                            resultLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
                            resultPanel.add(resultLabel);
                        } else if (isAlreadyFriend) {
                            // User is already a friend
                            JLabel resultLabel = new JLabel(foundUser.getUsername() + " is already your friend.");
                            resultLabel.setForeground(Color.ORANGE);
                            resultLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
                            resultPanel.add(resultLabel);
                        } else {
                            // Create a panel to display user info and add button
                            JPanel userInfoPanel = new JPanel();
                            userInfoPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
                            userInfoPanel.setBackground(darkBackground);
                            userInfoPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
                            
                            // Load user profile picture
                            BufferedImage userImage = tryLoadImage(foundUser.getProfilePicturePath());
                            CircularPicturePanel userCircle = new CircularPicturePanel(userImage, 50, darkBackground);
                            
                            // Add user name
                            JLabel nameLabel = new JLabel(foundUser.getUsername());
                            nameLabel.setForeground(Color.WHITE);
                            nameLabel.setFont(new Font("Arial", Font.BOLD, 14));
                            nameLabel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
                            
                            // Add "Add Friend" button
                            JButton addButton = new JButton("Add Friend");
                            addButton.setBackground(new Color(60, 60, 60));
                            addButton.setForeground(Color.WHITE);
                            addButton.setFocusPainted(false);
                            
                            // Add action listener to add button
                            final User finalFoundUser = foundUser;
                            addButton.addActionListener(new ActionListener() {
                                @Override
                                public void actionPerformed(ActionEvent e) {
                                    // Add friend using UserController
                                    boolean success = UserController.addFriend(newUser.getUserId(), finalFoundUser.getUserId());
                                    
                                    if (success) {
                                        // Show success message
                                        JOptionPane.showMessageDialog(addFriendDialog,
                                            finalFoundUser.getUsername() + " has been added to your friends!",
                                            "Friend Added",
                                            JOptionPane.INFORMATION_MESSAGE);
                                        
                                        // Close the dialog
                                        addFriendDialog.dispose();
                                        
                                        // Refresh the friends panel
                                        refreshFriendsPanel();
                                    } else {
                                        // Show error message
                                        JOptionPane.showMessageDialog(addFriendDialog,
                                            "Failed to add friend. Please try again.",
                                            "Error",
                                            JOptionPane.ERROR_MESSAGE);
                                    }
                                }
                            });
                            
                            // Add components to user info panel
                            userInfoPanel.add(userCircle);
                            userInfoPanel.add(nameLabel);
                            userInfoPanel.add(addButton);
                            
                            // Add user info panel to result panel
                            resultPanel.add(userInfoPanel);
                        }
                    } else {
                        // User not found
                        JLabel resultLabel = new JLabel("No user found with ID: " + userId);
                        resultLabel.setForeground(Color.ORANGE);
                        resultLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
                        resultPanel.add(resultLabel);
                    }
                } catch (NumberFormatException ex) {
                    // Invalid input - not a number
                    JLabel resultLabel = new JLabel("Please enter a valid user ID.");
                    resultLabel.setForeground(Color.ORANGE);
                    resultLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
                    resultPanel.add(resultLabel);
                }
                
                // Refresh the result panel
                resultPanel.revalidate();
                resultPanel.repaint();
            }
        });
        
        // Add cancel button at the bottom
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(darkBackground);
        
        JButton cancelButton = new JButton("Cancel");
        cancelButton.setBackground(new Color(60, 60, 60));
        cancelButton.setForeground(Color.WHITE);
        cancelButton.setFocusPainted(false);
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addFriendDialog.dispose();
            }
        });
        
        buttonPanel.add(cancelButton);
        
        // Add panels to dialog
        addFriendDialog.add(mainPanel, BorderLayout.CENTER);
        addFriendDialog.add(buttonPanel, BorderLayout.SOUTH);
        
        // Show dialog
        addFriendDialog.setVisible(true);
    }
    
    /**
     * Method to refresh the friends panel
     */
    private void refreshFriendsPanel() {
        // Remove existing components
        friendsPanel.removeAll();
        
        // Friends header with label on left, add friend button on right
        JPanel friendsHeaderPanel = new JPanel(new BorderLayout());
        friendsHeaderPanel.setBackground(darkBackground);
        friendsHeaderPanel.setBorder(BorderFactory.createEmptyBorder(30, 0, 10, 0));
        
        JPanel friendsTitlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        friendsTitlePanel.setBackground(darkBackground);
        friendsTitlePanel.add(friendsLabel);
        
        JPanel addFriendButtonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        addFriendButtonPanel.setBackground(darkBackground);
        addFriendButtonPanel.add(addFriendButton);
        
        friendsHeaderPanel.add(friendsTitlePanel, BorderLayout.WEST);
        friendsHeaderPanel.add(addFriendButtonPanel, BorderLayout.EAST);
        
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
        
        // Add panels to friends panel
        friendsPanel.add(friendsHeaderPanel, BorderLayout.NORTH);
        friendsPanel.add(friendsRowPanel, BorderLayout.CENTER);
        
        // Refresh the UI
        friendsPanel.revalidate();
        friendsPanel.repaint();
        mainPanel.revalidate();
        mainPanel.repaint();
    }
    
    /**
     * Tries to load an image file using multiple methods
     */
    private BufferedImage tryLoadImage(String imagePath) {
        BufferedImage image = null;
        
        // 1. Direct file path loading
        try {
            File file = new File(imagePath);
            if (file.exists() && file.canRead()) {
                image = ImageIO.read(file);
                if (image != null) {
                    System.out.println("Image loaded directly from file path: " + imagePath);
                    return image;
                }
            }
        } catch (Exception e) {
            System.err.println("Failed to load with direct file path: " + e.getMessage());
        }
        
        // 2. Loading as a resource
        try {
            URL resourceUrl = getClass().getResource("/" + imagePath);
            if (resourceUrl != null) {
                image = ImageIO.read(resourceUrl);
                if (image != null) {
                    System.out.println("Image loaded as resource: " + imagePath);
                    return image;
                }
            }
        } catch (Exception e) {
            System.err.println("Failed to load as resource: " + e.getMessage());
        }
        
        // 3. Loading using ImageIcon
        try {
            ImageIcon icon = new ImageIcon(imagePath);
            if (icon.getIconWidth() > 0) {
                // Convert ImageIcon to BufferedImage
                image = new BufferedImage(
                    icon.getIconWidth(),
                    icon.getIconHeight(),
                    BufferedImage.TYPE_INT_ARGB
                );
                Graphics g = image.createGraphics();
                icon.paintIcon(null, g, 0, 0);
                g.dispose();
                System.out.println("Image loaded with ImageIcon: " + imagePath);
                return image;
            }
        } catch (Exception e) {
            System.err.println("Failed to load with ImageIcon: " + e.getMessage());
        }
        
        // 4. Try alternative paths
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
                        System.out.println("Image loaded with alternative path: " + path);
                        return image;
                    }
                }
            } catch (Exception e) {
                // Try the next path
            }
        }
        
        // No method worked, return null
        System.err.println("Could not load image with any method: " + imagePath);
        return null;
    }
    
    /**
     * Helper method to get posterPath from Movie
     */
    private String getPosterPathFromMovie(Movie movie) {
        if (movie == null) {
            return null;
        }
        
        try {
            // Try to access posterPath through getMovie() method
            Map<String, Object> movieData = movie.getMovie();
            if (movieData != null && movieData.containsKey("posterPath")) {
                return (String) movieData.get("posterPath");
            }
        } catch (Exception e) {
            System.err.println("Error accessing through getMovie() method: " + e.getMessage());
        }
        
        // Return null if above doesn't work
        return null;
    }
    
    /**
     * Circular picture panel - For drawing circular profile pictures
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
            // Important: Make panel background transparent and set layout manager to null
            setOpaque(false);
            setBackground(new Color(0, 0, 0, 0)); // Completely transparent
        }
        
        /**
         * Method to update the image
         */
        public void updateImage(BufferedImage newImage) {
            this.image = newImage;
        }
        
        @Override
        protected void paintComponent(Graphics g) {
            // Completely transparent background
            g.setColor(new Color(0, 0, 0, 0));
            g.fillRect(0, 0, getWidth(), getHeight());
            
            Graphics2D g2d = (Graphics2D) g.create();
            
            // Anti-aliasing for smooth edges
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            
            if (image != null) {
                // Create circular clipping area
                g2d.setClip(new java.awt.geom.Ellipse2D.Float(0, 0, diameter, diameter));
                
                // Fit image to circle
                g2d.drawImage(image, 0, 0, diameter, diameter, null);
            } else {
                // Show gray circle if no image
                g2d.setColor(Color.LIGHT_GRAY);
                g2d.fillOval(0, 0, diameter, diameter);
                
                // If label exists, show first letter
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
            
            // Get movie poster path
            String posterPath = movie != null ? getPosterPathFromMovie(movie) : null;
            
            // Movie poster panel
            JPanel poster;
            
            if (posterPath != null && !posterPath.isEmpty()) {
                // Load poster image
                final BufferedImage posterImage = tryLoadImage(posterPath);
                
                poster = new JPanel() {
                    @Override
                    protected void paintComponent(Graphics g) {
                        super.paintComponent(g);
                        
                        if (posterImage != null) {
                            // Draw poster image
                            g.drawImage(posterImage, 0, 0, 120, 180, this);
                        } else {
                            // Show dark gray background with title if image not loaded
                            g.setColor(Color.DARK_GRAY);
                            g.fillRect(0, 0, 120, 180);
                            
                            // Show movie title
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
                // Show gray rectangle if no movie poster
                poster = new JPanel() {
                    @Override
                    protected void paintComponent(Graphics g) {
                        super.paintComponent(g);
                        g.setColor(Color.DARK_GRAY);
                        g.fillRect(0, 0, 120, 180);
                        
                        // Show title if movie exists
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