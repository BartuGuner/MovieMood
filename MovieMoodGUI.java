import java.awt.*;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class MovieMoodGUI extends JFrame {
    private ArrayList<Comment> commentsList;
    private Comment userComment = null; 
    private int selectedRating = -1; 

    static class Comment {
        String username;
        String commentText;

        public Comment(String username, String commentText) {
            this.username = username;
            this.commentText = commentText;
        }
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
                JOptionPane.showMessageDialog(this, "You rated: " + selectedRating);
                //userrate method??
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

    public static void main(String[] args) {
        SwingUtilities.invokeLater(MovieMoodGUI::new);
    }
}
