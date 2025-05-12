import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

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
    public MovieMoodGUI(Movie movie, User user) {
        this.movie = movie;
        this.user = user;
        setTitle("Movie Mood - Details");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        add(createNavBar(), BorderLayout.NORTH);

        JPanel topPanel = new JPanel(new GridLayout(1, 2));
        topPanel.setPreferredSize(new Dimension(900, 450));

        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setBackground(Color.BLACK);
        leftPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        //change
        JLabel imdbLabel = createLabel("IMDB RATING: 8.8", Color.RED);
        JLabel userLabel = createLabel("OUR USERS RATE: 9.2", Color.WHITE);

        JButton seeComments = createMainButton("Click to See Comments");
        JButton addRating = createMainButton("Add Rating");
        JButton addComment = createMainButton("Add A Comment");

        seeComments.addActionListener(e -> showCommentsList());
        addComment.addActionListener(e -> showAddCommentDialog());
        addRating.addActionListener(e -> showRatingDialog());

        JLabel listLabel = createLabel("You have the movie at these lists:", Color.LIGHT_GRAY);
        JLabel listLabel2 = createLabel("Click to remove from:", Color.LIGHT_GRAY);

        //change 
        ArrayList<String> userLists = new ArrayList<>();
        userLists.add("MovieList A");
        userLists.add("MovieList B");

        leftPanel.add(imdbLabel);
        leftPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        leftPanel.add(userLabel);
        leftPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        leftPanel.add(seeComments);
        leftPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        leftPanel.add(addRating);
        leftPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        leftPanel.add(addComment);
        leftPanel.add(Box.createRigidArea(new Dimension(0, 30)));
        leftPanel.add(listLabel);
        leftPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        leftPanel.add(listLabel2);

        //remove from list
        for (String listName : userLists) {
            JButton listBtn = createListButton(listName);

            // stub listener:
            listBtn.addActionListener(e -> {
                JOptionPane.showMessageDialog(
                    this,
                    "Removed from \"" + listName + "\"",
                    "Stub: Remove",
                    JOptionPane.INFORMATION_MESSAGE
                );
                //removeMovieFromList(movieId, listName);
            });

            leftPanel.add(Box.createRigidArea(new Dimension(0, 10)));
            leftPanel.add(listBtn);
        }


        JButton addToList = createAddListButton("+ Add this movie to your list");
        addToList.addActionListener(e -> {
            // example
            String newList = JOptionPane.showInputDialog(
                this,
                "Enter list name to add this movie:",
                "Stub: Add to List",
                JOptionPane.PLAIN_MESSAGE
            );
            if (newList != null && !newList.trim().isEmpty()) {
                JOptionPane.showMessageDialog(
                    this,
                    "Added to \"" + newList + "\"",
                    "Stub: Add",
                    JOptionPane.INFORMATION_MESSAGE
                );
                //addMovieToList(movieId, newList);
            }
        });

        leftPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        leftPanel.add(addToList);

        JPanel rightPanel = new JPanel();
        rightPanel.setBackground(Color.BLACK);
        //example image 
        ImageIcon posterIcon = new ImageIcon("mouse (1).png");
        Image scaledImage = posterIcon.getImage().getScaledInstance(350, 400, Image.SCALE_SMOOTH);
        JLabel posterLabel = new JLabel(new ImageIcon(scaledImage));
        rightPanel.add(posterLabel);

        topPanel.add(leftPanel);
        topPanel.add(rightPanel);

        JPanel bottomPanel = new JPanel(new GridLayout(1, 4));
        bottomPanel.setPreferredSize(new Dimension(900, 150));
        bottomPanel.setBackground(Color.DARK_GRAY);

        //it will change
        bottomPanel.add(createInfoPanel("       MOVIE NAME:", "     INCEPTION"));
        bottomPanel.add(createInfoPanel("DIRECTOR:", "Christopher Nolan"));
        bottomPanel.add(createInfoPanel("RELEASE YEAR:", "2010"));
        bottomPanel.add(createInfoPanel("GENRE:", "SCI-FI"));

        add(topPanel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    private void showAddCommentDialog() {
        JDialog dialog = new JDialog(this, "Add Comment", true);
        dialog.setSize(500, 500);
        dialog.setLayout(new BorderLayout());

        // HEADER
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(Color.DARK_GRAY);
        JButton back = new JButton("BACK");
        back.setBackground(Color.RED);
        back.setForeground(Color.WHITE);
        back.addActionListener(e -> dialog.dispose());
        JLabel title = new JLabel("ADD COMMENT:", SwingConstants.CENTER);
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Arial", Font.BOLD, 16));
        header.add(back, BorderLayout.WEST);
        header.add(title, BorderLayout.CENTER);

        // CENTER
        JPanel center = new JPanel();
        center.setLayout(new BoxLayout(center, BoxLayout.Y_AXIS));
        center.setBackground(Color.LIGHT_GRAY);
        center.setBorder(new EmptyBorder(20, 30, 20, 30));

        JLabel label = new JLabel("COMMENT:");
        label.setAlignmentX(Component.LEFT_ALIGNMENT);

        JTextArea area = new JTextArea();
        area.setLineWrap(true);
        area.setWrapStyleWord(true);
        area.setPreferredSize(new Dimension(400, 80));
        area.setMaximumSize(new Dimension(400, 80));
        area.setText("");

        JButton submit = new JButton("SUBMIT");
        submit.setAlignmentX(Component.LEFT_ALIGNMENT);
        submit.setBackground(Color.RED);
        submit.setForeground(Color.WHITE);

        submit.addActionListener(e -> {
            String text = area.getText().trim();
            if (!text.isEmpty()) {
                Comment newComment = new Comment("CurrentUser", text);
                commentsList.add(newComment);
                dialog.dispose();
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(brightRed);
            }
        });

        center.add(label);
        center.add(Box.createRigidArea(new Dimension(0, 5)));
        center.add(area);
        center.add(Box.createRigidArea(new Dimension(0, 10)));
        center.add(submit);

        // Eğer kullanıcı daha önce yorum yaptıysa --> kendi yorumlarını görür
        //change 
        boolean hasOwnComments = false;
        for (Comment comment : commentsList) {
            if (comment.username.equals("CurrentUser")) {
                if (!hasOwnComments) {
                    center.add(Box.createRigidArea(new Dimension(0, 20)));
                    JLabel previousLabel = new JLabel("YOUR PREVIOUS COMMENTS:");
                    previousLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
                    previousLabel.setFont(new Font("Arial", Font.PLAIN, 12));
                    center.add(previousLabel);
                    center.add(Box.createRigidArea(new Dimension(0, 10)));
                    hasOwnComments = true;
                }

                JPanel ownCommentPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
                ownCommentPanel.setBackground(Color.LIGHT_GRAY);

                JLabel commentText = new JLabel(comment.commentText);
                commentText.setFont(new Font("Arial", Font.BOLD, 12));

                JButton editBtn = new JButton("EDIT");
                editBtn.setBackground(Color.RED);
                editBtn.setForeground(Color.WHITE);

                editBtn.addActionListener(e2 -> {
                    dialog.dispose();
                    showEditDialog(comment);
                });

                ownCommentPanel.add(commentText);
                ownCommentPanel.add(editBtn);
                center.add(Box.createRigidArea(new Dimension(0, 5)));
                center.add(ownCommentPanel);
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

         for (Comment comment : FilmController.getCommentsForMovie(movie)) {
            JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            JLabel name = new JLabel(comment.getAuthor().getUsername() + ": ");
            name.setFont(new Font("Arial", Font.BOLD, 14));
            name.setForeground(Color.RED);
            JLabel text = new JLabel(comment.getText());
            text.setForeground(Color.BLACK);
            panel.setBackground(Color.LIGHT_GRAY);
            panel.add(name);
            panel.add(text);
            center.add(panel);
        }

        JScrollPane scrollPane = new JScrollPane(center);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        dialog.add(header, BorderLayout.NORTH);
        dialog.add(scrollPane, BorderLayout.CENTER);
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }


    private void showEditDialog(Comment commentToEdit) {
        JDialog dialog = new JDialog(this, "Edit Comment", true);
        dialog.setSize(500, 300);
        dialog.setLayout(new BorderLayout());

        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(Color.DARK_GRAY);
        JButton back = new JButton("BACK");
        back.setBackground(Color.RED);
        back.setForeground(Color.WHITE);
        back.addActionListener(e -> dialog.dispose());
        JLabel title = new JLabel("EDIT COMMENT:", SwingConstants.CENTER);
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Arial", Font.BOLD, 16));
        header.add(back, BorderLayout.WEST);
        header.add(title, BorderLayout.CENTER);

        JPanel center = new JPanel();
        center.setLayout(new BoxLayout(center, BoxLayout.Y_AXIS));
        center.setBackground(Color.LIGHT_GRAY);
        center.setBorder(new EmptyBorder(20, 30, 20, 30));

        JLabel editLabel = new JLabel("EDIT COMMENT:");
        editLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JTextArea area = new JTextArea(commentToEdit.commentText);
        area.setLineWrap(true);
        area.setWrapStyleWord(true);
        area.setPreferredSize(new Dimension(400, 80));
        area.setMaximumSize(new Dimension(400, 80));

        JButton submit = new JButton("SUBMIT");
        submit.setBackground(Color.RED);
        submit.setForeground(Color.WHITE);
        submit.addActionListener(e -> {
            commentToEdit.commentText = area.getText().trim();
            dialog.dispose();
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

        //ratinging methodla 
        JButton submit = new JButton("Submit");
        submit.setBackground(Color.RED);
        submit.setForeground(Color.WHITE);
        submit.addActionListener(e -> {
            if (selectedRating > 0) {
                JOptionPane.showMessageDialog(this, "You rated: " + selectedRating);
                //userrate method??
                dialog.dispose();
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

    public static void main(String[] args) {
        SwingUtilities.invokeLater(MovieMoodGUI::new);
    }
}
