import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class GenrePage extends JFrame {
    public GenrePage(String genre) {
        setTitle("Movies - " + genre);
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(Color.BLACK);
        header.setPreferredSize(new Dimension(900, 40));

        JButton back = new JButton("BACK");
        back.setForeground(Color.WHITE);
        back.setBackground(Color.RED);
        back.addActionListener(e -> {
            dispose();
            new MoviesPage();
        });

        JLabel title = new JLabel(genre.toUpperCase() + " MOVIES", SwingConstants.CENTER);
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Arial", Font.BOLD, 16));

        header.add(back, BorderLayout.WEST);
        header.add(title, BorderLayout.CENTER);
        add(header, BorderLayout.NORTH);

        JPanel moviePanel = new JPanel();
        moviePanel.setBackground(Color.BLACK);
        moviePanel.setLayout(new FlowLayout(FlowLayout.LEFT, 20, 20));


        
        for (int i = 0; i < 6; i++) {
            JLabel poster = new JLabel();
            ImageIcon icon = new ImageIcon("mouse (1).png"); //example 
            Image scaled = icon.getImage().getScaledInstance(120, 180, Image.SCALE_SMOOTH);
            poster.setIcon(new ImageIcon(scaled));

            poster.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)); 
            poster.addMouseListener(new MouseAdapter() {
                public void mouseClicked(MouseEvent e) {
                    dispose();
                    new MovieMoodGUI();
                }
            });

            moviePanel.add(poster);
        }



        JScrollPane scrollPane = new JScrollPane(moviePanel);
        scrollPane.setBorder(null);
        add(scrollPane, BorderLayout.CENTER);

        setVisible(true);
    }
}

