import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class MoviesPage extends JFrame {

    public MoviesPage() {
        setTitle("Movie Mood - Movies");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        add(createNavBar(), BorderLayout.NORTH);

        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setBackground(Color.BLACK);

        // Movies by Genre
        JLabel genreLabel = new JLabel("Movies by Genre");
        genreLabel.setForeground(Color.WHITE);
        genreLabel.setFont(new Font("Arial", Font.BOLD, 16));
        genreLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        genreLabel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 0));
        centerPanel.add(genreLabel);

        JPanel genreRow = new JPanel(new GridLayout(1, 3));
        genreRow.setBackground(Color.BLACK);
        genreRow.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));

        //when knowing all genres this can be change 
        genreRow.add(createCategoryPanel("Sci-Fi"));
        genreRow.add(createCategoryPanel("Action"));
        genreRow.add(createCategoryPanel("Romance"));
        centerPanel.add(genreRow);

        // Movies by Year
        JLabel yearLabel = new JLabel("Movies by Release Year");
        yearLabel.setForeground(Color.WHITE);
        yearLabel.setFont(new Font("Arial", Font.BOLD, 16));
        yearLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        yearLabel.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 0));
        centerPanel.add(yearLabel);

        JPanel yearRow = new JPanel(new GridLayout(1, 3));
        yearRow.setBackground(Color.BLACK);
        yearRow.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));

        yearRow.add(createCategoryPanel("2000-"));
        yearRow.add(createCategoryPanel("1990-2000"));
        yearRow.add(createCategoryPanel("1900-1990"));
        centerPanel.add(yearRow);

        JScrollPane scrollPane = new JScrollPane(centerPanel);
        add(scrollPane, BorderLayout.CENTER);

        setVisible(true);
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

            btn.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    dispose();
                    if (item.equals("Home")) {
                        new HomePage();
                    } else if (item.equals("Movies")) {
                        new MoviesPage();
                    } else {
                        JOptionPane.showMessageDialog(null, item + " page is under development.");
                    }
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

    private JPanel createCategoryPanel(String categoryName) {
        JPanel categoryPanel = new JPanel();
        categoryPanel.setLayout(new BorderLayout());
        categoryPanel.setBackground(Color.BLACK);

        JButton titleButton = new JButton(categoryName);
        titleButton.setFont(new Font("Arial", Font.BOLD, 14));
        titleButton.setForeground(Color.WHITE);
        titleButton.setBackground(Color.BLACK);
        titleButton.setFocusPainted(false);
        titleButton.setBorderPainted(false);
        titleButton.addActionListener(e -> {
            dispose();
            new GenrePage(categoryName);
        });

        JPanel posters = new JPanel();
        posters.setLayout(new OverlayLayout(posters));
        posters.setBackground(Color.BLACK);
        int offset = 25;

        //example posters 
        for (int i = 0; i < 4; i++) {
            JLabel poster = new JLabel();
            //arraylist 
            ImageIcon imageIcon = new ImageIcon("mouse (1).png");
            Image scaledImage = imageIcon.getImage().getScaledInstance(100, 150, Image.SCALE_SMOOTH);
            poster.setIcon(new ImageIcon(scaledImage));
            poster.setAlignmentX(0.0f);
            poster.setAlignmentY(0.0f);
            poster.setBorder(BorderFactory.createEmptyBorder(0, i * offset, 0, 0));

            final String movieTitle = "INCEPTION"; // change
            poster.addMouseListener(new MouseAdapter() {
                public void mouseClicked(MouseEvent e) {
                    dispose();
                    new MovieMoodGUI(); // Movie detail page 
                }
            });

            posters.add(poster);
        }

        categoryPanel.add(titleButton, BorderLayout.NORTH);
        categoryPanel.add(posters, BorderLayout.CENTER);
        return categoryPanel;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(MoviesPage::new);
    }
}
