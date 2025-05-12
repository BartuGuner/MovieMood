import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;

public class MyListPanel extends JFrame {
    
    private JPanel mainPanel;
    private JPanel listsContainer;
    private Map<String, JPanel> movieLists;
    
    public MyListPanel() {
        movieLists = new HashMap<>();
        
        setTitle("Movie Mood");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 800);
        setLocationRelativeTo(null);
        
        initComponents();
        
        setVisible(true);
    }
    
    private void initComponents() {
        mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBackground(Color.BLACK);
        
        createHeader();
        createListsPanel();
        
        add(mainPanel);
    }
    
    private void createHeader() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(Color.BLACK);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        
        // Logo
        JLabel logoLabel = new JLabel("Movie Mood");
        logoLabel.setFont(new Font("Arial", Font.BOLD, 24));
        logoLabel.setForeground(Color.RED);
        headerPanel.add(logoLabel, BorderLayout.WEST);
        
        // Navigation buttons
        JPanel navPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        navPanel.setOpaque(false);
        
        String[] navItems = {"Home", "Explore", "My List", "Movies", "My Profile"};
        for (String item : navItems) {
            JButton navButton = new JButton(item);
            styleButton(navButton, item.equals("My List"));
            navPanel.add(navButton);
        }
        headerPanel.add(navPanel, BorderLayout.CENTER);
        
        // Chat button
        JButton chatButton = new JButton("💬");
        chatButton.setFont(new Font("Dialog", Font.PLAIN, 20));
        styleButton(chatButton, false);
        chatButton.setBackground(Color.WHITE);
        chatButton.setForeground(Color.BLACK);
        chatButton.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        
        JPanel chatPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        chatPanel.setOpaque(false);
        chatPanel.add(chatButton);
        headerPanel.add(chatPanel, BorderLayout.EAST);
        
        mainPanel.add(headerPanel, BorderLayout.NORTH);
    }
    
    private void createListsPanel() {
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BorderLayout());
        contentPanel.setBackground(Color.BLACK);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Create button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setOpaque(false);
        
        JButton createListButton = new JButton("Create New List");
        createListButton.setBackground(Color.RED);
        createListButton.setForeground(Color.WHITE);
        createListButton.setFocusPainted(false);
        createListButton.setContentAreaFilled(true);
        createListButton.setOpaque(true);
        createListButton.setBorderPainted(false);
        createListButton.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        createListButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        createListButton.addActionListener(e -> showCreateListDialog());
        buttonPanel.add(createListButton);
        
        // Lists container
        listsContainer = new JPanel();
        listsContainer.setLayout(new BoxLayout(listsContainer, BoxLayout.Y_AXIS));
        listsContainer.setBackground(Color.BLACK);
        
        // Add empty state message
        JPanel emptyPanel = new JPanel(new GridBagLayout());
        emptyPanel.setBackground(Color.BLACK);
        
        JLabel emptyLabel = new JLabel("No lists yet. Click 'Create New List' to get started.");
        emptyLabel.setForeground(Color.WHITE);
        emptyLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        emptyPanel.add(emptyLabel);
        
        listsContainer.add(emptyPanel);
        
        JScrollPane scrollPane = new JScrollPane(listsContainer);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(Color.BLACK);
        
        contentPanel.add(buttonPanel, BorderLayout.NORTH);
        contentPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(contentPanel, BorderLayout.CENTER);
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
    
    private void showCreateListDialog() {
        // Create custom dialog for list creation
        CreateListDialog dialog = new CreateListDialog(this);
        dialog.setVisible(true);
        
        // Get the list name if a list was created
        String listName = dialog.getListName();
        if (listName != null && !listName.trim().isEmpty()) {
            // Check if there's an empty state message and remove it
            if (listsContainer.getComponentCount() == 1 && 
                listsContainer.getComponent(0) instanceof JPanel && 
                ((JPanel)listsContainer.getComponent(0)).getComponentCount() == 1 &&
                ((JPanel)listsContainer.getComponent(0)).getComponent(0) instanceof JLabel) {
                listsContainer.removeAll();
            }
            
            addMovieList(listName);
            listsContainer.revalidate();
            listsContainer.repaint();
        }
    }
    
    private void addMovieList(String listName) {
        // List header panel
        JPanel listPanel = new JPanel(new BorderLayout());
        listPanel.setBackground(Color.BLACK);
        listPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 30, 0));
        
        JPanel listHeaderPanel = new JPanel(new BorderLayout());
        listHeaderPanel.setBackground(Color.BLACK);
        listHeaderPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        
        JLabel listNameLabel = new JLabel(listName);
        listNameLabel.setFont(new Font("Arial", Font.BOLD, 22));
        listNameLabel.setForeground(Color.WHITE);
        listHeaderPanel.add(listNameLabel, BorderLayout.WEST);
        
        // Butonlar için bir panel oluştur
        JPanel buttonWrapperPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttonWrapperPanel.setOpaque(false);
        buttonWrapperPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 30)); // Sağdan boşluk
        
        // Delete List butonu (yeni)
        JButton deleteButton = new JButton("Delete List");
        deleteButton.setBackground(Color.RED);
        deleteButton.setForeground(Color.WHITE);
        deleteButton.setFocusPainted(false);
        deleteButton.setContentAreaFilled(true);
        deleteButton.setOpaque(true);
        deleteButton.setBorderPainted(false);
        deleteButton.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        deleteButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        deleteButton.addActionListener(e -> deleteList(listName));
        
        // Manage List butonu
        JButton manageButton = new JButton("Manage List");
        manageButton.setBackground(Color.RED);
        manageButton.setForeground(Color.WHITE);
        manageButton.setFocusPainted(false);
        manageButton.setContentAreaFilled(true);
        manageButton.setOpaque(true);
        manageButton.setBorderPainted(false); 
        manageButton.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        manageButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        manageButton.addActionListener(e -> manageList(listName));
        
        // Önce Delete butonu sonra Manage butonu ekle
        buttonWrapperPanel.add(deleteButton);
        buttonWrapperPanel.add(Box.createHorizontalStrut(10)); // Butonlar arası boşluk
        buttonWrapperPanel.add(manageButton);
        
        listHeaderPanel.add(buttonWrapperPanel, BorderLayout.EAST);
        
        listPanel.add(listHeaderPanel, BorderLayout.NORTH);
        
        // Horizontal scrollable movie panel
        JPanel moviePanel = new JPanel();
        moviePanel.setLayout(new FlowLayout(FlowLayout.LEFT, 15, 10));
        moviePanel.setBackground(Color.BLACK);
        
        // Add empty message to the movie panel
        JLabel emptyMoviesLabel = new JLabel("No movies in this list yet. Use 'Manage List' to add movies.");
        emptyMoviesLabel.setForeground(Color.LIGHT_GRAY);
        emptyMoviesLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        emptyMoviesLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        moviePanel.add(emptyMoviesLabel);
        
        JScrollPane movieScrollPane = new JScrollPane(moviePanel);
        movieScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        movieScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
        movieScrollPane.setBorder(BorderFactory.createEmptyBorder());
        movieScrollPane.getViewport().setBackground(Color.BLACK);
        
        // Set a fixed height for the scrollpane
        movieScrollPane.setPreferredSize(new Dimension(getWidth() - 50, 240));
        
        listPanel.add(movieScrollPane, BorderLayout.CENTER);
        
        // Store reference to the movie panel
        movieLists.put(listName, moviePanel);
        
        // Add to the lists container
        listsContainer.add(listPanel);
    }
    
    // YENİ: Liste silme metodu
    private void deleteList(String listName) {
        // Silme onayı için popup göster
        int choice = JOptionPane.showConfirmDialog(
            this,
            "Are you sure you want to delete the list '" + listName + "'?",
            "Confirm Delete",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE
        );
        
        // Kullanıcı Evet derse listeyi sil
        if (choice == JOptionPane.YES_OPTION) {
            // Movie List panel referansını movieLists Map'inden al ve sil
            movieLists.remove(listName);
            
            // Liste panelini UI'dan kaldır
            for (Component comp : listsContainer.getComponents()) {
                if (comp instanceof JPanel) {
                    JPanel listPanel = (JPanel) comp;
                    Component headerComp = ((BorderLayout)listPanel.getLayout()).getLayoutComponent(BorderLayout.NORTH);
                    
                    if (headerComp instanceof JPanel) {
                        JPanel headerPanel = (JPanel) headerComp;
                        Component labelComp = ((BorderLayout)headerPanel.getLayout()).getLayoutComponent(BorderLayout.WEST);
                        
                        if (labelComp instanceof JLabel && ((JLabel)labelComp).getText().equals(listName)) {
                            listsContainer.remove(listPanel);
                            break;
                        }
                    }
                }
            }
            
            // UI'yı güncelle
            listsContainer.revalidate();
            listsContainer.repaint();
            
            // Eğer hiç liste kalmadıysa, boş mesajı göster
            if (listsContainer.getComponentCount() == 0) {
                JPanel emptyPanel = new JPanel(new GridBagLayout());
                emptyPanel.setBackground(Color.BLACK);
                
                JLabel emptyLabel = new JLabel("No lists yet. Click 'Create New List' to get started.");
                emptyLabel.setForeground(Color.WHITE);
                emptyLabel.setFont(new Font("Arial", Font.PLAIN, 16));
                emptyPanel.add(emptyLabel);
                
                listsContainer.add(emptyPanel);
                listsContainer.revalidate();
                listsContainer.repaint();
            }
        }
    }
    
    // UPDATED: This method now opens the ManageListDialog
    private void manageList(String listName) {
        ManageListDialog dialog = new ManageListDialog(this, listName);
        dialog.setVisible(true);
    }
    
    private JPanel createMovieCard(String title) {
        JPanel card = new JPanel();
        card.setLayout(new BorderLayout());
        card.setBackground(new Color(30, 30, 30));
        card.setPreferredSize(new Dimension(140, 200));
        card.setBorder(BorderFactory.createEmptyBorder());
        card.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Create placeholder image
        JPanel posterPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();
                
                g2d.setColor(new Color(20, 20, 20));
                g2d.fillRect(0, 0, getWidth(), getHeight());
                
                g2d.setColor(new Color(60, 60, 60));
                g2d.setFont(new Font("Arial", Font.BOLD, 30));
                FontMetrics fm = g2d.getFontMetrics();
                String text = "□";  // Square character
                int textWidth = fm.stringWidth(text);
                int textHeight = fm.getHeight();
                
                g2d.drawString(text, (getWidth() - textWidth) / 2, 
                              (getHeight() - textHeight) / 2 + fm.getAscent());
                
                g2d.dispose();
            }
        };
        posterPanel.setPreferredSize(new Dimension(140, 180));
        
        JLabel titleLabel = new JLabel(title, SwingConstants.CENTER);
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
        
        card.add(posterPanel, BorderLayout.CENTER);
        card.add(titleLabel, BorderLayout.SOUTH);
        
        return card;
    }
    
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        SwingUtilities.invokeLater(() -> new MyListPanel());
    }
    
    // Custom dialog for create list options - INNER CLASS
    private class CreateListDialog extends JDialog {
        private String listName = null;
        private JTextField manualNameField;
        private JTextField generateNameField;
        
        public CreateListDialog(JFrame parent) {
            super(parent, "Create List", true);
            setSize(700, 550);
            setLocationRelativeTo(parent);
            setResizable(false);
            
            initDialog();
        }
        
        private void initDialog() {
            JPanel mainPanel = new JPanel(new BorderLayout());
            mainPanel.setBackground(new Color(220, 220, 220)); // Light gray background
            
            // Header panel (dark gray with back button)
            JPanel headerPanel = new JPanel(new BorderLayout());
            headerPanel.setBackground(new Color(40, 40, 40)); // Dark gray
            headerPanel.setPreferredSize(new Dimension(700, 60));
            
            // Back button panel (dark red)
            JPanel backPanel = new JPanel(new BorderLayout());
            backPanel.setBackground(new Color(128, 0, 0)); // Dark red
            backPanel.setPreferredSize(new Dimension(150, 60));
            
            JButton backButton = new JButton("BACK");
            backButton.setFont(new Font("Arial", Font.BOLD, 18));
            backButton.setForeground(Color.WHITE);
            backButton.setBackground(null);
            backButton.setBorder(null);
            backButton.setContentAreaFilled(false);
            backButton.setFocusPainted(false);
            backButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
            backButton.addActionListener(e -> dispose());
            
            backPanel.add(backButton, BorderLayout.CENTER);
            headerPanel.add(backPanel, BorderLayout.WEST);
            
            // Title
            JLabel titleLabel = new JLabel("Create List");
            titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
            titleLabel.setForeground(Color.WHITE);
            titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 0));
            headerPanel.add(titleLabel, BorderLayout.CENTER);
            
            // Right side panel (empty, no X button)
            headerPanel.add(new JPanel() {
                {
                    setOpaque(false);
                }
            }, BorderLayout.EAST);
            
            mainPanel.add(headerPanel, BorderLayout.NORTH);
            
            // Content panel
            JPanel contentPanel = new JPanel(new BorderLayout());
            contentPanel.setBackground(new Color(220, 220, 220)); // Light gray
            contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
            
            // Select one label
            JLabel selectLabel = new JLabel("Select one:");
            selectLabel.setFont(new Font("Arial", Font.BOLD, 20));
            selectLabel.setForeground(Color.WHITE);
            selectLabel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
            selectLabel.setOpaque(true);
            selectLabel.setBackground(new Color(128, 0, 0)); // Dark red
            
            JPanel selectPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            selectPanel.setOpaque(false);
            selectPanel.add(selectLabel);
            
            contentPanel.add(selectPanel, BorderLayout.NORTH);
            
            // Options panel with two columns
            JPanel optionsPanel = new JPanel(new GridLayout(1, 2, 10, 0));
            optionsPanel.setOpaque(false);
            
            // Manual Selection Panel
            JPanel manualPanel = new JPanel();
            manualPanel.setLayout(new BoxLayout(manualPanel, BoxLayout.Y_AXIS));
            manualPanel.setBackground(new Color(60, 20, 20)); // Very dark red
            manualPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
            
            // Title
            JLabel manualTitle = new JLabel("Manual Selection");
            manualTitle.setFont(new Font("Arial", Font.BOLD, 24));
            manualTitle.setForeground(Color.WHITE);
            manualTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
            manualPanel.add(manualTitle);
            
            manualPanel.add(Box.createVerticalStrut(20));
            
            // Description
            JLabel manualDesc = new JLabel("Create your own list!");
            manualDesc.setFont(new Font("Arial", Font.PLAIN, 16));
            manualDesc.setForeground(Color.WHITE);
            manualDesc.setAlignmentX(Component.CENTER_ALIGNMENT);
            manualPanel.add(manualDesc);
            
            // Create button
            
            JButton manualCreateButton = new JButton("Create");
            manualCreateButton.setFont(new Font("Arial", Font.BOLD, 16));
            manualCreateButton.setForeground(Color.BLACK);
            manualCreateButton.setBackground(Color.WHITE);
            manualCreateButton.setFocusPainted(false);
            manualCreateButton.setBorder(BorderFactory.createEmptyBorder(10, 30, 10, 30));
            manualCreateButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
            manualCreateButton.setAlignmentX(Component.CENTER_ALIGNMENT);
            manualCreateButton.addActionListener(e -> {
                if (manualNameField.getText().trim().isEmpty()) {
                    JOptionPane.showMessageDialog(this, 
                        "Please enter a list name", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                listName = manualNameField.getText().trim();
                dispose();
            });
            
            JPanel manualButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
            manualButtonPanel.setOpaque(false);
            manualButtonPanel.add(manualCreateButton);
            
            manualPanel.add(Box.createVerticalStrut(40));
            manualPanel.add(manualButtonPanel);
            
            // Text field for manual entry
            manualNameField = new JTextField();
            manualNameField.setMaximumSize(new Dimension(300, 40));
            manualNameField.setPreferredSize(new Dimension(300, 40));
            manualNameField.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
            
            JPanel manualFieldPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
            manualFieldPanel.setOpaque(false);
            manualFieldPanel.add(manualNameField);
            
            manualPanel.add(Box.createVerticalStrut(20));
            manualPanel.add(manualFieldPanel);
            
            // Generate List Panel
            JPanel generatePanel = new JPanel();
            generatePanel.setLayout(new BoxLayout(generatePanel, BoxLayout.Y_AXIS));
            generatePanel.setBackground(new Color(60, 20, 20)); // Very dark red
            generatePanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
            
            // Title
            JLabel generateTitle = new JLabel("Generate List");
            generateTitle.setFont(new Font("Arial", Font.BOLD, 24));
            generateTitle.setForeground(Color.WHITE);
            generateTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
            generatePanel.add(generateTitle);
            
            generatePanel.add(Box.createVerticalStrut(20));
            
            // Description
            JLabel generateDesc = new JLabel("<html><div style='text-align: center;'>Will be automatically generated exclusively based on your favorites!</div></html>");
            generateDesc.setFont(new Font("Arial", Font.PLAIN, 16));
            generateDesc.setForeground(Color.WHITE);
            generateDesc.setAlignmentX(Component.CENTER_ALIGNMENT);
            generatePanel.add(generateDesc);
            
            // Create button for generate
            JButton generateCreateButton = new JButton("Create");
            generateCreateButton.setFont(new Font("Arial", Font.BOLD, 16));
            generateCreateButton.setForeground(Color.BLACK);
            generateCreateButton.setBackground(Color.WHITE);
            generateCreateButton.setFocusPainted(false);
            generateCreateButton.setBorder(BorderFactory.createEmptyBorder(10, 30, 10, 30));
            generateCreateButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
            generateCreateButton.setAlignmentX(Component.CENTER_ALIGNMENT);
            generateCreateButton.addActionListener(e -> {
                if (generateNameField.getText().trim().isEmpty()) {
                    JOptionPane.showMessageDialog(this, 
                        "Please enter a list name", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                listName = generateNameField.getText().trim();
                dispose();
            });
            
            JPanel generateButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
            generateButtonPanel.setOpaque(false);
            generateButtonPanel.add(generateCreateButton);
            
            generatePanel.add(Box.createVerticalStrut(40));
            generatePanel.add(generateButtonPanel);
            
            // Enter name label and field
            JLabel nameLabel = new JLabel("Enter Name:");
            nameLabel.setForeground(Color.WHITE);
            nameLabel.setFont(new Font("Arial", Font.PLAIN, 14));
            nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            
            generateNameField = new JTextField();
            generateNameField.setMaximumSize(new Dimension(300, 40));
            generateNameField.setPreferredSize(new Dimension(300, 40));
            generateNameField.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
            
            JPanel generateFieldPanel = new JPanel();
            generateFieldPanel.setLayout(new BoxLayout(generateFieldPanel, BoxLayout.Y_AXIS));
            generateFieldPanel.setOpaque(false);
            generateFieldPanel.add(nameLabel);
            generateFieldPanel.add(Box.createVerticalStrut(5));
            
            JPanel textFieldPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
            textFieldPanel.setOpaque(false);
            textFieldPanel.add(generateNameField);
            generateFieldPanel.add(textFieldPanel);
            
            generatePanel.add(Box.createVerticalStrut(20));
            generatePanel.add(generateFieldPanel);
            
            optionsPanel.add(manualPanel);
            optionsPanel.add(generatePanel);
            
            contentPanel.add(optionsPanel, BorderLayout.CENTER);
            
            mainPanel.add(contentPanel, BorderLayout.CENTER);
            
            add(mainPanel);
        }
        
        public String getListName() {
            return listName;
        }
    }
    
    private class ManageListDialog extends JDialog {
        private JPanel myListPanel;
        private JPanel otherMoviesPanel;
        private List<String> selectedMoviesToRemove = new ArrayList<>();
        private List<String> selectedMoviesToAdd = new ArrayList<>();
        private String listName;
        private JTextField listNameField; // New: field for editing list name
        
        public ManageListDialog(JFrame parent, String listName) {
            super(parent, "Manage List", true);
            this.listName = listName;
            setSize(1200, 900);
            setLocationRelativeTo(parent);
            setResizable(false);
            
            initDialog();
        }
        
        private void initDialog() {
            JPanel mainPanel = new JPanel(new BorderLayout());
            mainPanel.setBackground(new Color(220, 220, 220)); // Light gray background
            
            // Header panel
            JPanel headerPanel = new JPanel(new BorderLayout());
            headerPanel.setBackground(new Color(40, 40, 40)); // Dark gray
            headerPanel.setPreferredSize(new Dimension(800, 60));
            
            // Back button panel
            JPanel backPanel = new JPanel(new BorderLayout());
            backPanel.setBackground(new Color(128, 0, 0)); // Dark red
            backPanel.setPreferredSize(new Dimension(150, 60));
            
            JButton backButton = new JButton("BACK");
            backButton.setFont(new Font("Arial", Font.BOLD, 18));
            backButton.setForeground(Color.WHITE);
            backButton.setBackground(null);
            backButton.setBorder(null);
            backButton.setContentAreaFilled(false);
            backButton.setFocusPainted(false);
            backButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
            backButton.addActionListener(e -> dispose());
            
            backPanel.add(backButton, BorderLayout.CENTER);
            headerPanel.add(backPanel, BorderLayout.WEST);
            
            // Title
            JLabel titleLabel = new JLabel("Manage List");
            titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
            titleLabel.setForeground(Color.WHITE);
            titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 0));
            headerPanel.add(titleLabel, BorderLayout.CENTER);
            
            mainPanel.add(headerPanel, BorderLayout.NORTH);
            
            // Content panel
            JPanel contentPanel = new JPanel();
            contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
            contentPanel.setBackground(new Color(220, 220, 220)); // Light gray
            contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
            
            // List name panel - MODIFIED to include editable field and save button
            JPanel listNamePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
            listNamePanel.setOpaque(false);
            listNamePanel.setAlignmentX(Component.LEFT_ALIGNMENT);
            
            // List name label
            JLabel listNameLabel = new JLabel("List name:");
            listNameLabel.setFont(new Font("Arial", Font.BOLD, 30));
            listNameLabel.setForeground(new Color(180, 0, 0)); // Dark red
            
            // Editable text field for list name
            listNameField = new JTextField(listName);
            listNameField.setFont(new Font("Arial", Font.BOLD, 30));
            listNameField.setForeground(new Color(180, 0, 0));
            listNameField.setBackground(new Color(240, 240, 240));
            listNameField.setPreferredSize(new Dimension(300, 40));
            listNameField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(180, 0, 0), 2),
                BorderFactory.createEmptyBorder(2, 5, 2, 5)
            ));
            
            // Save button for list name
            JButton saveNameButton = new JButton("Save");
            saveNameButton.setFont(new Font("Arial", Font.BOLD, 16));
            saveNameButton.setForeground(Color.WHITE);
            saveNameButton.setBackground(new Color(255, 0, 0)); // Bright red (kıpkırmızı)
            saveNameButton.setOpaque(true);
            saveNameButton.setBorderPainted(false);
            saveNameButton.setFocusPainted(false);
            saveNameButton.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
            saveNameButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
            saveNameButton.addActionListener(e -> saveListName());
            
            listNamePanel.add(listNameLabel);
            listNamePanel.add(Box.createHorizontalStrut(10));
            listNamePanel.add(listNameField);
            listNamePanel.add(Box.createHorizontalStrut(10));
            listNamePanel.add(saveNameButton);
            
            contentPanel.add(listNamePanel);
            contentPanel.add(Box.createVerticalStrut(20));
            
            // Add a label to show the current list contents
            JLabel listContentsLabel = new JLabel("List contents:");
            listContentsLabel.setFont(new Font("Arial", Font.BOLD, 20));
            listContentsLabel.setForeground(new Color(180, 0, 0));
            listContentsLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
            contentPanel.add(listContentsLabel);
            contentPanel.add(Box.createVerticalStrut(10));
            
            // My list movies panel
            JPanel myListContainer = new JPanel(new BorderLayout());
            myListContainer.setOpaque(false);
            myListContainer.setAlignmentX(Component.LEFT_ALIGNMENT);
            
            myListPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
            myListPanel.setBackground(new Color(220, 220, 220)); // Light gray
            
            // DÜZELTME: Listedeki mevcut filmleri göster
            JPanel currentMoviePanel = movieLists.get(listName);
            boolean hasMovies = false;
            
            if (currentMoviePanel != null) {
                // Mevcut panelde boş mesaj var mı kontrol et
                if (currentMoviePanel.getComponentCount() == 1 && 
                    currentMoviePanel.getComponent(0) instanceof JLabel && 
                    ((JLabel)currentMoviePanel.getComponent(0)).getText().startsWith("No movies")) {
                    // Boş mesaj var, film yok
                    JLabel emptyLabel = new JLabel("No movies in this list yet. Add some movies below.");
                    emptyLabel.setForeground(Color.BLACK);
                    emptyLabel.setFont(new Font("Arial", Font.PLAIN, 16));
                    myListPanel.add(emptyLabel);
                } else {
                    // Filmleri göster
                    hasMovies = true;
                    
                    // Mevcut filmler panelindeki tüm bileşenleri kontrol et
                    for (Component comp : currentMoviePanel.getComponents()) {
                        if (comp instanceof JPanel) {
                            // Bu bir film kartı
                            Component[] cardComponents = ((JPanel) comp).getComponents();
                            for (Component cardComp : cardComponents) {
                                if (cardComp instanceof JLabel) {
                                    // Film adını bul
                                    String movieTitle = ((JLabel) cardComp).getText();
                                    // Film adını kullanarak bir film kartı oluştur (silme butonu ile)
                                    myListPanel.add(createMovieCardWithRemoveButton(movieTitle));
                                    break;
                                }
                            }
                        }
                    }
                    
                    // Eğer hiç film ekleyemediyse, belki hiç film yoktur
                    if (myListPanel.getComponentCount() == 0) {
                        JLabel emptyLabel = new JLabel("No movies in this list yet. Add some movies below.");
                        emptyLabel.setForeground(Color.BLACK);
                        emptyLabel.setFont(new Font("Arial", Font.PLAIN, 16));
                        myListPanel.add(emptyLabel);
                        hasMovies = false;
                    }
                }
            } else {
                // Liste bulunamadı - bu durumda boş mesaj göster
                JLabel emptyLabel = new JLabel("No movies in this list yet. Add some movies below.");
                emptyLabel.setForeground(Color.BLACK);
                emptyLabel.setFont(new Font("Arial", Font.PLAIN, 16));
                myListPanel.add(emptyLabel);
            }
            
            JScrollPane myListScrollPane = new JScrollPane(myListPanel);
            myListScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
            myListScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
            myListScrollPane.setBorder(BorderFactory.createEmptyBorder());
            myListScrollPane.getViewport().setBackground(new Color(220, 220, 220)); // Light gray
            myListScrollPane.setPreferredSize(new Dimension(700, 220));
            
            myListContainer.add(myListScrollPane, BorderLayout.CENTER);
            contentPanel.add(myListContainer);
            
            // Remove movie button - sadece filmler varsa göster
            if (hasMovies) {
                JPanel removePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
                removePanel.setOpaque(false);
                removePanel.setAlignmentX(Component.LEFT_ALIGNMENT);
                
                JLabel removeLabel = new JLabel("REMOVE MOVIE");
                removeLabel.setFont(new Font("Arial", Font.BOLD, 20));
                removeLabel.setForeground(Color.BLACK);
                
                JButton submitRemoveButton = new JButton("SUBMIT");
                submitRemoveButton.setFont(new Font("Arial", Font.BOLD, 16));
                submitRemoveButton.setForeground(Color.WHITE);
                submitRemoveButton.setBackground(new Color(255, 0, 0)); // Bright red (kıpkırmızı)
                submitRemoveButton.setOpaque(true);
                submitRemoveButton.setBorderPainted(false);
                submitRemoveButton.setFocusPainted(false);
                submitRemoveButton.setBorder(BorderFactory.createEmptyBorder(10, 25, 10, 25));
                submitRemoveButton.addActionListener(e -> {
                    if (!selectedMoviesToRemove.isEmpty()) {
                        // Show the notification dialog
                        showNotification("MOVIE REMOVED!!!");
                        
                        // Seçili filmleri listeden kaldır
                        JPanel moviePanel = movieLists.get(listName);
                        if (moviePanel != null) {
                            for (String movieTitle : selectedMoviesToRemove) {
                                // Bu başlıkla eşleşen film kartlarını bul ve kaldır
                                Component[] components = moviePanel.getComponents();
                                for (Component comp : components) {
                                    if (comp instanceof JPanel) {
                                        Component[] cardComponents = ((JPanel) comp).getComponents();
                                        for (Component cardComp : cardComponents) {
                                            if (cardComp instanceof JLabel && 
                                                ((JLabel) cardComp).getText().equals(movieTitle)) {
                                                moviePanel.remove(comp);
                                                break;
                                            }
                                        }
                                    }
                                }
                            }
                            
                            // Filmler kaldırıldıktan sonra panel boşsa, boş mesajı göster
                            if (moviePanel.getComponentCount() == 0) {
                                JLabel emptyLabel = new JLabel("No movies in this list yet. Use 'Manage List' to add movies.");
                                emptyLabel.setForeground(Color.LIGHT_GRAY);
                                emptyLabel.setFont(new Font("Arial", Font.PLAIN, 14));
                                emptyLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
                                moviePanel.add(emptyLabel);
                            }
                            
                            // Arayüzü güncelle
                            moviePanel.revalidate();
                            moviePanel.repaint();
                        }
                    }
                });
                
                removePanel.add(removeLabel);
                removePanel.add(Box.createHorizontalStrut(50));
                removePanel.add(submitRemoveButton);
                
                contentPanel.add(Box.createVerticalStrut(10));
                contentPanel.add(removePanel);
            }
            
            contentPanel.add(Box.createVerticalStrut(30));
            
            // Other movies label
            JLabel otherMoviesLabel = new JLabel("Other movies :");
            otherMoviesLabel.setFont(new Font("Arial", Font.BOLD, 30));
            otherMoviesLabel.setForeground(new Color(180, 0, 0)); // Dark red
            otherMoviesLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
            contentPanel.add(otherMoviesLabel);
            contentPanel.add(Box.createVerticalStrut(20));
            
            // Other movies panel
            JPanel otherMoviesContainer = new JPanel(new BorderLayout());
            otherMoviesContainer.setOpaque(false);
            otherMoviesContainer.setAlignmentX(Component.LEFT_ALIGNMENT);
            
            otherMoviesPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
            otherMoviesPanel.setBackground(new Color(220, 220, 220)); // Light gray
            
            // Add random movies (10 movies instead of 6)
            String[] randomMovies = {
                "Scream", "Avatar", "Baby Driver", "Split", "Pride & Prejudice", 
                "A Quiet Place", "Inception", "The Matrix", "Interstellar", "Titanic"
            };
            
            for (String movie : randomMovies) {
                otherMoviesPanel.add(createMovieCardWithAddButton(movie));
            }
            
            // Set a preferred size to ensure horizontal scrolling is visible
            otherMoviesPanel.setPreferredSize(new Dimension(1500, 220));
            
            JScrollPane otherMoviesScrollPane = new JScrollPane(otherMoviesPanel);
            otherMoviesScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS); // Always show horizontal scrollbar
            otherMoviesScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
            otherMoviesScrollPane.setBorder(BorderFactory.createEmptyBorder());
            otherMoviesScrollPane.getViewport().setBackground(new Color(220, 220, 220)); // Light gray
            otherMoviesScrollPane.setPreferredSize(new Dimension(700, 240)); // Increased height to accommodate scrollbar
            
            otherMoviesContainer.add(otherMoviesScrollPane, BorderLayout.CENTER);
            contentPanel.add(otherMoviesContainer);
            
            // Add movie button
            JPanel addPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
            addPanel.setOpaque(false);
            addPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
            
            JLabel addLabel = new JLabel("ADD MOVIE");
            addLabel.setFont(new Font("Arial", Font.BOLD, 20));
            addLabel.setForeground(Color.BLACK);
            
            JButton submitAddButton = new JButton("SUBMIT");
            submitAddButton.setFont(new Font("Arial", Font.BOLD, 16));
            submitAddButton.setForeground(Color.WHITE);
            submitAddButton.setBackground(new Color(255, 0, 0)); // Bright red (kıpkırmızı)
            submitAddButton.setOpaque(true);
            submitAddButton.setBorderPainted(false);
            submitAddButton.setFocusPainted(false);
            submitAddButton.setBorder(BorderFactory.createEmptyBorder(10, 25, 10, 25));
            submitAddButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
            
            // GÜNCELLENDİ: "MOVIE ADDED!!!" bildiriminde filmleri listeye ekle
            submitAddButton.addActionListener(e -> {
                if (!selectedMoviesToAdd.isEmpty()) {
                    // Alternatif bildirimler arasında değişiklik göstermek için
                    int randomChoice = 0;
                    //int randomChoice = (int)(Math.random() * 3);
                    
                    if (randomChoice == 0) {
                        // Film eklendiğinde bildirimi göster
                        showNotification("MOVIE ADDED!!!");
                        
                        // Filmi listeye ekleme kodunu buraya ekliyoruz
                        JPanel moviePanel = movieLists.get(listName);
                        
                        if (moviePanel != null) {
                            // Eğer boş mesaj varsa kaldır
                            if (moviePanel.getComponentCount() == 1 && 
                                moviePanel.getComponent(0) instanceof JLabel &&
                                ((JLabel)moviePanel.getComponent(0)).getText().startsWith("No movies")) {
                                moviePanel.removeAll();
                            }
                            
                            // Seçilen her filmi listeye ekle
                            for (String movieTitle : selectedMoviesToAdd) {
                                JPanel movieCard = MyListPanel.this.createMovieCard(movieTitle);
                                moviePanel.add(movieCard);
                            }
                            
                            // Arayüzü güncelle
                            moviePanel.revalidate();
                            moviePanel.repaint();
                        }
                    } else if (randomChoice == 1) {
                        showNotification("MOVIE ALREADY EXISTS IN YOUR LIST!!!");
                    } else {
                        showNotification("MOVIE NOT FOUND!!!");
                    }
                    
                    // İşlem bittikten sonra seçimi temizle
                    selectedMoviesToAdd.clear();
                }
            });
            
            addPanel.add(addLabel);
            addPanel.add(Box.createHorizontalStrut(80));
            addPanel.add(submitAddButton);
            
            contentPanel.add(Box.createVerticalStrut(10));
            contentPanel.add(addPanel);
            
            JScrollPane mainScrollPane = new JScrollPane(contentPanel);
            mainScrollPane.setBorder(BorderFactory.createEmptyBorder());
            mainScrollPane.getViewport().setBackground(new Color(220, 220, 220));
            
            mainPanel.add(mainScrollPane, BorderLayout.CENTER);
            
            add(mainPanel);
        }
        
        // NEW: Method to save the list name change
        private void saveListName() {
            String newListName = listNameField.getText().trim();
            
            // Validate the new name
            if (newListName.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                    "List name cannot be empty.",
                    "Invalid Name",
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // Check if the name already exists (except for the current name)
            if (!newListName.equals(listName) && movieLists.containsKey(newListName)) {
                JOptionPane.showMessageDialog(this,
                    "A list with this name already exists. Please choose a different name.",
                    "Duplicate Name",
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // If the name has changed, update it
            if (!newListName.equals(listName)) {
                // Get the movie panel from the old name
                JPanel moviePanel = movieLists.get(listName);
                
                // Remove the old name from the map
                movieLists.remove(listName);
                
                // Add the panel with the new name
                movieLists.put(newListName, moviePanel);
                
                // Update the lists UI by finding and updating the label
                for (Component comp : listsContainer.getComponents()) {
                    if (comp instanceof JPanel) {
                        JPanel listPanel = (JPanel) comp;
                        Component headerComp = ((BorderLayout)listPanel.getLayout()).getLayoutComponent(BorderLayout.NORTH);
                        
                        if (headerComp instanceof JPanel) {
                            JPanel headerPanel = (JPanel) headerComp;
                            Component labelComp = ((BorderLayout)headerPanel.getLayout()).getLayoutComponent(BorderLayout.WEST);
                            
                            if (labelComp instanceof JLabel && ((JLabel)labelComp).getText().equals(listName)) {
                                ((JLabel)labelComp).setText(newListName);
                                break;
                            }
                        }
                    }
                }
                
                // Update the current list name
                String oldListName = listName;
                listName = newListName;
                
                // Show success notification
                showNotification("List renamed from \"" + oldListName + "\" to \"" + newListName + "\"");
            }
        }
        
        private JPanel createMovieCardWithRemoveButton(String title) {
            JPanel container = new JPanel(new BorderLayout());
            container.setOpaque(false);
            
            JPanel card = new JPanel();
            card.setLayout(new BorderLayout());
            card.setBackground(new Color(30, 30, 30));
            card.setPreferredSize(new Dimension(120, 160));
            card.setBorder(BorderFactory.createEmptyBorder());
            card.setCursor(new Cursor(Cursor.HAND_CURSOR));
            
            // In a real app, you would fetch the movie poster from a backend service
            JPanel posterPanel = new JPanel();
            posterPanel.setBackground(new Color(20, 20, 20));
            posterPanel.setPreferredSize(new Dimension(120, 140));
            
            JLabel titleLabel = new JLabel(title, SwingConstants.CENTER);
            titleLabel.setForeground(Color.WHITE);
            titleLabel.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
            titleLabel.setFont(new Font("Arial", Font.PLAIN, 10));
            
            card.add(posterPanel, BorderLayout.CENTER);
            card.add(titleLabel, BorderLayout.SOUTH);
            
            // Create a remove button
            JButton removeButton = new JButton("−");
            removeButton.setFont(new Font("Arial", Font.BOLD, 18));
            removeButton.setForeground(Color.WHITE);
            removeButton.setBackground(null);
            removeButton.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.WHITE, 2),
                BorderFactory.createEmptyBorder(0, 6, 3, 6)
            ));
            removeButton.setContentAreaFilled(false);
            removeButton.setFocusPainted(false);
            removeButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
            removeButton.addActionListener(e -> {
                if (selectedMoviesToRemove.contains(title)) {
                    selectedMoviesToRemove.remove(title);
                    removeButton.setBackground(null);
                    removeButton.setOpaque(false);
                } else {
                    selectedMoviesToRemove.add(title);
                    removeButton.setBackground(Color.WHITE);
                    removeButton.setForeground(Color.BLACK);
                    removeButton.setOpaque(true);
                }
            });
            
            container.add(card, BorderLayout.CENTER);
            
            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
            buttonPanel.setOpaque(false);
            buttonPanel.add(removeButton);
            container.add(buttonPanel, BorderLayout.SOUTH);
            
            return container;
        }
        
        private JPanel createMovieCardWithAddButton(String title) {
            JPanel container = new JPanel(new BorderLayout());
            container.setOpaque(false);
            
            JPanel card = new JPanel();
            card.setLayout(new BorderLayout());
            card.setBackground(new Color(30, 30, 30));
            card.setPreferredSize(new Dimension(120, 160));
            card.setBorder(BorderFactory.createEmptyBorder());
            card.setCursor(new Cursor(Cursor.HAND_CURSOR));
            
            // In a real app, you would fetch the movie poster from a backend service
            JPanel posterPanel = new JPanel();
            posterPanel.setBackground(new Color(20, 20, 20));
            posterPanel.setPreferredSize(new Dimension(120, 140));
            
            JLabel titleLabel = new JLabel(title, SwingConstants.CENTER);
            titleLabel.setForeground(Color.WHITE);
            titleLabel.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
            titleLabel.setFont(new Font("Arial", Font.PLAIN, 10));
            
            card.add(posterPanel, BorderLayout.CENTER);
            card.add(titleLabel, BorderLayout.SOUTH);
            
            // Create an add button
            JButton addButton = new JButton("+");
            addButton.setFont(new Font("Arial", Font.BOLD, 18));
            addButton.setForeground(Color.WHITE);
            addButton.setBackground(null);
            addButton.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.WHITE, 2),
                BorderFactory.createEmptyBorder(0, 6, 3, 6)
            ));
            addButton.setContentAreaFilled(false);
            addButton.setFocusPainted(false);
            addButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
            addButton.addActionListener(e -> {
                if (selectedMoviesToAdd.contains(title)) {
                    selectedMoviesToAdd.remove(title);
                    addButton.setBackground(null);
                    addButton.setOpaque(false);
                } else {
                    selectedMoviesToAdd.add(title);
                    addButton.setBackground(Color.WHITE);
                    addButton.setForeground(Color.BLACK);
                    addButton.setOpaque(true);
                }
            });
            
            container.add(card, BorderLayout.CENTER);
            
            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
            buttonPanel.setOpaque(false);
            buttonPanel.add(addButton);
            container.add(buttonPanel, BorderLayout.SOUTH);
            
            return container;
        }
        
        private void showNotification(String message) {
            NotificationDialog dialog = new NotificationDialog(this, message);
            dialog.setVisible(true);
        }
    }
    
    // Notification dialog for messages - INNER CLASS
    private class NotificationDialog extends JDialog {
        public NotificationDialog(JDialog parent, String message) {
            super(parent, "Notification", true);
            setSize(600, 400);
            setLocationRelativeTo(parent);
            setResizable(false);
            
            JPanel mainPanel = new JPanel(new BorderLayout());
            mainPanel.setBackground(new Color(220, 220, 220)); // Light gray
            
            // Header panel
            JPanel headerPanel = new JPanel(new BorderLayout());
            headerPanel.setBackground(new Color(40, 40, 40)); // Dark gray
            headerPanel.setPreferredSize(new Dimension(600, 60));
            
            // Back button panel
            JPanel backPanel = new JPanel(new BorderLayout());
            backPanel.setBackground(new Color(128, 0, 0)); // Dark red
            backPanel.setPreferredSize(new Dimension(150, 60));
            
            JButton backButton = new JButton("BACK");
            backButton.setFont(new Font("Arial", Font.BOLD, 18));
            backButton.setForeground(Color.WHITE);
            backButton.setBackground(null);
            backButton.setBorder(null);
            backButton.setContentAreaFilled(false);
            backButton.setFocusPainted(false);
            backButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
            backButton.addActionListener(e -> dispose());
            
            backPanel.add(backButton, BorderLayout.CENTER);
            headerPanel.add(backPanel, BorderLayout.WEST);
            
            mainPanel.add(headerPanel, BorderLayout.NORTH);
            
            // Message panel
            JPanel messagePanel = new JPanel(new GridBagLayout());
            messagePanel.setBackground(new Color(220, 220, 220)); // Light gray
            
            JLabel messageLabel = new JLabel(message);
            messageLabel.setFont(new Font("Arial", Font.BOLD, 36));
            messageLabel.setForeground(new Color(180, 0, 0)); // Dark red
            
            messagePanel.add(messageLabel);
            
            mainPanel.add(messagePanel, BorderLayout.CENTER);
            
            add(mainPanel);
        }
    }
}