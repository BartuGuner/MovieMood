import java.awt.*;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class MovieMoodGUI extends JFrame {
    private final Movie movie ;
    private final User user;
    private int selectedRating = -1; 

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

        double imdb = movie.getImdbRating();
        double userAvg = FilmController.getAverageRating(movie);
        //change
        JLabel imdbLabel = createLabel("IMDB RATING: " + imdb, Color.RED);
        JLabel userLabel = createLabel("OUR USERS RATE: " + userAvg, Color.WHITE);

        JButton seeComments = createMainButton("Click to See Comments");
        JButton addRating = createMainButton("Add Rating");
        JButton addComment = createMainButton("Add A Comment");

        seeComments.addActionListener(e -> showCommentsList());
        addComment.addActionListener(e -> showAddCommentDialog());
        addRating.addActionListener(e -> showRatingDialog());

        JLabel listLabel = createLabel("You have the movie at these lists:", Color.LIGHT_GRAY);
        JLabel listLabel2 = createLabel("Click to remove from:", Color.LIGHT_GRAY);

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

//change
        //remove from list 
        //ArrayList<String> userLists = new ArrayList<>();
        //userLists.add("MovieList A");
        //userLists.add("MovieList B");
        List<FilmList> userListsWithMovie = FilmListController.getListsWithMovie(user, movie);

        for (FilmList list : userListsWithMovie) {
            String listName = list.getName();
            JButton listBtn = createListButton(listName);
            listBtn.addActionListener(e -> {
                FilmListController.removeMovieFromList(list, movie);
                JOptionPane.showMessageDialog(
                        this,
                        "Removed from \"" + listName + "\"",
                        "Movie Removed",
                        JOptionPane.INFORMATION_MESSAGE
                );
                // Optionally: refresh UI or remove button dynamically
                listBtn.setEnabled(false); 
            });
            leftPanel.add(Box.createRigidArea(new Dimension(0, 10)));
            leftPanel.add(listBtn);
        }

//Change
        JButton addToList = createAddListButton("+ Add this movie to your list");
        addToList.addActionListener(e -> {
            // example
            String newList = JOptionPane.showInputDialog(
                this,
                "Enter list name to add this movie:",
                "Add to List",
                JOptionPane.PLAIN_MESSAGE
            );
            if (newList != null && !newList.trim().isEmpty()) {
                FilmList targetList = null;
                for (FilmList list : user.getFilmLists()) {
                    if (list.getName().equalsIgnoreCase(newList.trim())) {
                        targetList = list;
                        break;
                    }
                }
                if (targetList == null) {
                    targetList = new FilmList(newList.trim());
                    user.addFilmList(targetList);
                }
                FilmListController.addMovieToList(targetList, movie);
                JOptionPane.showMessageDialog(
                        this,
                        "Added to \"" + newList + "\"",
                        "Movie Added",
                        JOptionPane.INFORMATION_MESSAGE
                );
                // Optionally: refresh UI to show new button
            }
        });



        leftPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        leftPanel.add(addToList);

        JPanel rightPanel = new JPanel();
        rightPanel.setBackground(Color.BLACK);
        ImageIcon posterIcon = loadImageFromURL(movie.getPosterUrl());
        Image scaledImage = posterIcon.getImage().getScaledInstance(350, 400, Image.SCALE_SMOOTH);
        JLabel posterLabel = new JLabel(new ImageIcon(scaledImage));
        rightPanel.add(posterLabel);

        topPanel.add(leftPanel);
        topPanel.add(rightPanel);

        JPanel bottomPanel = new JPanel(new GridLayout(1, 4));
        bottomPanel.setPreferredSize(new Dimension(900, 150));
        bottomPanel.setBackground(Color.DARK_GRAY);

        //it will change
        bottomPanel.add(createInfoPanel("       MOVIE NAME:", movie.getTitle()));
        bottomPanel.add(createInfoPanel("DIRECTOR:","Empty"));
        bottomPanel.add(createInfoPanel("RELEASE YEAR:", String.valueOf(movie.getReleaseDate())));
        bottomPanel.add(createInfoPanel("GENRE:", String.join(", ", movie.getGenres())));

        add(topPanel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    public static ImageIcon loadImageFromURL(String imageUrl) {
    try {
        URL url = new URL(imageUrl);
        BufferedImage image = javax.imageio.ImageIO.read(url);
        return new ImageIcon(image);
    } catch (Exception e) {
        System.err.println("Resim yüklenemedi: " + e.getMessage());
        return new ImageIcon(); // boş bir ikon döner
    }
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
                FilmController.addCommentToMovie(movie, user, text);
                dialog.dispose();
            }
        });

        center.add(label);
        center.add(Box.createRigidArea(new Dimension(0, 5)));
        center.add(area);
        center.add(Box.createRigidArea(new Dimension(0, 10)));
        center.add(submit);

        // Eğer kullanıcı daha önce yorum yaptıysa --> kendi yorumlarını görür
        //change 
        Comment existing = FilmController.getLatestUserComment(movie, user);
        if (existing != null) {
            center.add(Box.createRigidArea(new Dimension(0, 20)));
            JLabel previousLabel = new JLabel("YOUR PREVIOUS COMMENT:");
            previousLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            previousLabel.setFont(new Font("Arial", Font.PLAIN, 12));
            JTextArea oldArea = new JTextArea(existing.getText());
            oldArea.setEditable(false);
            JButton editBtn = new JButton("EDIT");
            editBtn.setBackground(Color.RED);
            editBtn.setForeground(Color.WHITE);
            editBtn.addActionListener(e2 -> {
                dialog.dispose();
                showEditDialog(existing);
            });
            center.add(previousLabel);
            center.add(oldArea);
            center.add(editBtn);
        }

        dialog.add(header, BorderLayout.NORTH);
        dialog.add(new JScrollPane(center), BorderLayout.CENTER);
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
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


    private void showEditDialog(Comment comment) {
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

        JTextArea area = new JTextArea(comment.getText());
        area.setLineWrap(true);
        area.setWrapStyleWord(true);
        area.setPreferredSize(new Dimension(400, 80));
        area.setMaximumSize(new Dimension(400, 80));

        JButton submit = new JButton("SUBMIT");
        submit.setBackground(Color.RED);
        submit.setForeground(Color.WHITE);
        submit.addActionListener(e -> {
            FilmController.editComment(movie, user, comment.getText(), area.getText().trim());
            dialog.dispose();
        });

        center.add(editLabel);
        center.add(Box.createRigidArea(new Dimension(0, 5)));
        center.add(area);
        center.add(Box.createRigidArea(new Dimension(0, 10)));
        center.add(submit);

        dialog.add(header, BorderLayout.NORTH);
        dialog.add(center, BorderLayout.CENTER);
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    private void showRatingDialog() {
        JDialog dialog = new JDialog(this, "Add Rating", true);
        dialog.setSize(500, 250);
        dialog.setLayout(new BorderLayout());

        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(Color.DARK_GRAY);
        JButton back = new JButton("BACK");
        back.setBackground(Color.RED);
        back.setForeground(Color.WHITE);
        back.addActionListener(e -> dialog.dispose());
        JLabel title = new JLabel("ADD RATING:", SwingConstants.CENTER);
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Arial", Font.BOLD, 14));
        header.add(back, BorderLayout.WEST);
        header.add(title, BorderLayout.CENTER);

        JPanel center = new JPanel();
        center.setBackground(Color.LIGHT_GRAY);
        ButtonGroup group = new ButtonGroup();
        JPanel ratingPanel = new JPanel(new FlowLayout());
        for (int i = 1; i <= 10; i++) {
            JButton button = new JButton(String.valueOf(i));
            int finalI = i;
            button.setBackground(Color.RED);
            button.setForeground(Color.WHITE);
            button.addActionListener(e -> selectedRating = finalI);
            ratingPanel.add(button);
        }

        //ratinging methodla 
        JButton submit = new JButton("Submit");
        submit.setBackground(Color.RED);
        submit.setForeground(Color.WHITE);
        submit.addActionListener(e -> {
            if (selectedRating > 0) {
                FilmController.rateMovie(movie, user, selectedRating);
                JOptionPane.showMessageDialog(this, "You rated: " + selectedRating);
                dialog.dispose();
            }
        });

        center.add(ratingPanel);
        center.add(submit);

        dialog.add(header, BorderLayout.NORTH);
        dialog.add(center, BorderLayout.CENTER);
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
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
                dispose(); // mevcut sayfayı kapat
                if (item.equals("Home")) {
                    new HomePage(); // HomePage
                } else if (item.equals("Movies")) {
                    new MoviesPage(); // MoviesPage 
                } else {
                    JOptionPane.showMessageDialog(this, item + " page is under development.");
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

    private JLabel createLabel(String text, Color color) {
        JLabel label = new JLabel(text);
        label.setForeground(color);
        label.setFont(new Font("Arial", Font.BOLD, 14));
        return label;
    }

    private JButton createMainButton(String text) {
        JButton button = new JButton(text);
        button.setMaximumSize(new Dimension(250, 35));
        button.setBackground(Color.RED);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        return button;
    }

    private JButton createListButton(String text) {
        JButton button = new JButton(text);
        button.setMaximumSize(new Dimension(250, 30));
        button.setBackground(Color.DARK_GRAY);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Arial", Font.PLAIN, 13));
        return button;
    }

    private JButton createAddListButton(String text) {
        JButton button = new JButton(text);
        button.setMaximumSize(new Dimension(250, 30));
        button.setBackground(Color.LIGHT_GRAY);
        button.setForeground(Color.BLACK);
        button.setFont(new Font("Arial", Font.BOLD, 13));
        return button;
    }

    private JPanel createInfoPanel(String label, String value) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.BLACK);

        JLabel label1 = new JLabel(label);
        label1.setForeground(Color.LIGHT_GRAY);
        label1.setFont(new Font("Arial", Font.BOLD, 12));

        JLabel label2 = new JLabel(value);
        label2.setForeground(Color.WHITE);
        label2.setFont(new Font("Arial", Font.BOLD, 14));

        panel.add(Box.createVerticalGlue());
        panel.add(label1);
        panel.add(Box.createRigidArea(new Dimension(0, 5)));
        panel.add(label2);
        panel.add(Box.createVerticalGlue());
        return panel;
    }

    //for testing
    public static void main(String[] args) {
    SwingUtilities.invokeLater(() -> {
        // Controller'ları oluştur
        UserController userController = new UserController();
        FilmController filmController = new FilmController();

        // Kullanıcıyı kontrol et – yoksa oluştur
        User alice = userController.login("Alice", "pass1");
        if (alice == null) {
            // Kullanıcı sistemde yoksa manuel oluştur
            alice = new User("Alice", "Smith", "alice@example.com", "pass1", 1);
            
        }

        MovieSeeder.seedMovies(filmController);
        // Örnek bir film bul
        List<Movie> movies = filmController.searchByTitle("Warfare");
        if (!movies.isEmpty()) {
            Movie movie = movies.get(0);
            new MovieMoodGUI(movie, alice); // GUI'yi kullanıcı ile başlat
        } else {
            JOptionPane.showMessageDialog(null, "Film bulunamadı.");
        }
    });
}

}
