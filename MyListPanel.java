import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.List;
import javax.imageio.ImageIO;

public class MyListPanel extends JFrame {
    
    private JPanel mainPanel;
    private JPanel listsContainer;
    private Map<String, JPanel> movieListPanels;
    
    // Backend entegrasyonu için gerekli nesneler
    public User currentUser;
    public FilmController filmController;
    public FilmListController filmListController;
    
    public MyListPanel(User frontEndStaticUser) {
        this.currentUser = frontEndStaticUser;
        movieListPanels = new HashMap<>();
        
        // Controller'ları başlat
        filmController = new FilmController();
        filmListController = new FilmListController();
        
        setTitle("Movie Mood");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 800);
        setLocationRelativeTo(null);
        
        initComponents();
        loadUserLists(); // Kullanıcının mevcut listelerini yükle
        
        setVisible(true);
    }
    
    public void initComponents() {
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
    
    // URL'den resim yüklemek için yardımcı metot
    private ImageIcon loadImageFromURL(String imageUrl) {
        try {
            System.out.println("Resim yükleniyor: " + imageUrl);
            URL url = new URL(imageUrl);
            BufferedImage image = ImageIO.read(url);
            return new ImageIcon(image);
        } catch (Exception e) {
            System.err.println("Resim yüklenemedi: " + imageUrl + " - " + e.getMessage());
            // Hata durumunda varsayılan ikon döndür
            return null;
        }
    }
    
    // Resmi yeniden boyutlandırmak için yardımcı metot
    private ImageIcon resizeImageIcon(ImageIcon icon, int width, int height) {
        if (icon == null) return null;
        Image image = icon.getImage();
        Image resizedImage = image.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        return new ImageIcon(resizedImage);
    }
    
    // Kullanıcının film listelerini yükler
    private void loadUserLists() {
        listsContainer.removeAll();
        
        List<FilmList> userLists = filmListController.getAllFilmLists(currentUser);
        System.out.println("Kullanıcı listeleri yükleniyor. Liste sayısı: " + userLists.size());
        
        if (userLists.isEmpty()) {
            // Boş durum mesajını göster
            JPanel emptyPanel = new JPanel(new GridBagLayout());
            emptyPanel.setBackground(Color.BLACK);
            
            JLabel emptyLabel = new JLabel("No lists yet. Click 'Create New List' to get started.");
            emptyLabel.setForeground(Color.WHITE);
            emptyLabel.setFont(new Font("Arial", Font.PLAIN, 16));
            emptyPanel.add(emptyLabel);
            
            listsContainer.add(emptyPanel);
        } else {
            // Her listeyi UI'a ekle
            for (FilmList list : userLists) {
                addMovieListToUI(list);
            }
        }
        
        listsContainer.revalidate();
        listsContainer.repaint();
    }
    
    // Liste oluşturma dialogunu gösterir
    private void showCreateListDialog() {
        CreateListDialog dialog = new CreateListDialog(this);
        dialog.setVisible(true);
        
        String listName = dialog.getListName();
        if (listName != null && !listName.trim().isEmpty()) {
            // Controller'ı kullanarak liste oluştur
            filmListController.createList(currentUser, listName);
            System.out.println("Yeni liste oluşturuldu: " + listName);
            
            // UI'ı yenile
            loadUserLists();
        }
    }
    
    // FilmList'i UI'a ekler
    private void addMovieListToUI(FilmList filmList) {
        System.out.println("Liste UI'a ekleniyor: " + filmList.getName());
        
        // List header panel
        JPanel listPanel = new JPanel(new BorderLayout());
        listPanel.setBackground(Color.BLACK);
        listPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 30, 0));
        
        JPanel listHeaderPanel = new JPanel(new BorderLayout());
        listHeaderPanel.setBackground(Color.BLACK);
        listHeaderPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        
        JLabel listNameLabel = new JLabel(filmList.getName());
        listNameLabel.setFont(new Font("Arial", Font.BOLD, 22));
        listNameLabel.setForeground(Color.WHITE);
        listHeaderPanel.add(listNameLabel, BorderLayout.WEST);
        
        // Butonlar için panel
        JPanel buttonWrapperPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttonWrapperPanel.setOpaque(false);
        buttonWrapperPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 30));
        
        // Delete List butonu
        JButton deleteButton = new JButton("Delete List");
        deleteButton.setBackground(Color.RED);
        deleteButton.setForeground(Color.WHITE);
        deleteButton.setFocusPainted(false);
        deleteButton.setContentAreaFilled(true);
        deleteButton.setOpaque(true);
        deleteButton.setBorderPainted(false);
        deleteButton.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        deleteButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        deleteButton.addActionListener(e -> deleteList(filmList));
        
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
        manageButton.addActionListener(e -> manageList(filmList));
        
        buttonWrapperPanel.add(deleteButton);
        buttonWrapperPanel.add(Box.createHorizontalStrut(10));
        buttonWrapperPanel.add(manageButton);
        
        listHeaderPanel.add(buttonWrapperPanel, BorderLayout.EAST);
        
        listPanel.add(listHeaderPanel, BorderLayout.NORTH);
        
        // Horizontal scrollable movie panel
        JPanel moviePanel = new JPanel();
        moviePanel.setLayout(new FlowLayout(FlowLayout.LEFT, 15, 10));
        moviePanel.setBackground(Color.BLACK);
        
        List<Movie> movies = filmList.getMovies();
        System.out.println("Liste " + filmList.getName() + " içinde " + movies.size() + " film var");
        
        if (movies.isEmpty()) {
            // Add empty message to the movie panel
            JLabel emptyMoviesLabel = new JLabel("No movies in this list yet. Use 'Manage List' to add movies.");
            emptyMoviesLabel.setForeground(Color.LIGHT_GRAY);
            emptyMoviesLabel.setFont(new Font("Arial", Font.PLAIN, 14));
            emptyMoviesLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
            moviePanel.add(emptyMoviesLabel);
        } else {
            // Add movies to the panel
            for (Movie movie : movies) {
                moviePanel.add(createMovieCardFromMovie(movie));
            }
        }
        
        JScrollPane movieScrollPane = new JScrollPane(moviePanel);
        movieScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        movieScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
        movieScrollPane.setBorder(BorderFactory.createEmptyBorder());
        movieScrollPane.getViewport().setBackground(Color.BLACK);
        
        // Set a fixed height for the scrollpane
        movieScrollPane.setPreferredSize(new Dimension(getWidth() - 50, 240));
        
        listPanel.add(movieScrollPane, BorderLayout.CENTER);
        
        // Store reference to the movie panel
        movieListPanels.put(filmList.getName(), moviePanel);
        
        // Add to the lists container
        listsContainer.add(listPanel);
    }
    
    // Listeyi silmek için
    private void deleteList(FilmList filmList) {
        int choice = JOptionPane.showConfirmDialog(
            this,
            "Are you sure you want to delete the list '" + filmList.getName() + "'?",
            "Confirm Delete",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE
        );
        
        if (choice == JOptionPane.YES_OPTION) {
            // Controller ile listeyi sil
            filmListController.removeFilmList(currentUser, filmList.getName());
            System.out.println("Liste silindi: " + filmList.getName());
            
            // UI'ı yenile
            loadUserLists();
        }
    }
    
    // Liste yönetme dialogunu açmak için
    private void manageList(FilmList filmList) {
        ManageListDialog dialog = new ManageListDialog(this, filmList);
        dialog.setVisible(true);
        
        // Dialog kapandıktan sonra listeleri yenile
        loadUserLists();
    }
    
    // Movie nesnesinden film kartı oluşturmak için
    private JPanel createMovieCardFromMovie(Movie movie) {
        JPanel card = new JPanel();
        card.setLayout(new BorderLayout());
        card.setBackground(new Color(30, 30, 30));
        card.setPreferredSize(new Dimension(140, 200));
        card.setBorder(BorderFactory.createEmptyBorder());
        card.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Film posteri için etiket oluştur
        JLabel posterLabel = new JLabel();
        posterLabel.setPreferredSize(new Dimension(140, 180));
        posterLabel.setBackground(new Color(20, 20, 20));
        posterLabel.setHorizontalAlignment(SwingConstants.CENTER);
        posterLabel.setOpaque(true);
        
        // Yükleme sırasında görünecek metin
        posterLabel.setText("Loading...");
        posterLabel.setForeground(Color.LIGHT_GRAY);
        
        // Arka planda resmi yükle
        SwingWorker<ImageIcon, Void> imageLoader = new SwingWorker<ImageIcon, Void>() {
            @Override
            protected ImageIcon doInBackground() throws Exception {
                String posterUrl = movie.getPosterUrl();
                if (posterUrl != null && !posterUrl.isEmpty()) {
                    return loadImageFromURL(posterUrl);
                }
                return null;
            }
            
            @Override
            protected void done() {
                try {
                    ImageIcon poster = get();
                    if (poster != null) {
                        ImageIcon resizedPoster = resizeImageIcon(poster, 140, 180);
                        posterLabel.setIcon(resizedPoster);
                        posterLabel.setText(""); // Yükleme yazısını temizle
                    } else {
                        // Poster yüklenemezse, film adının ilk harfini göster
                        posterLabel.setText(movie.getTitle().substring(0, 1).toUpperCase());
                        posterLabel.setFont(new Font("Arial", Font.BOLD, 48));
                    }
                } catch (Exception e) {
                    System.err.println("Poster gösterilemiyor: " + e.getMessage());
                    posterLabel.setText(movie.getTitle().substring(0, 1).toUpperCase());
                    posterLabel.setFont(new Font("Arial", Font.BOLD, 48));
                }
            }
        };
        imageLoader.execute();
        
        JLabel titleLabel = new JLabel(movie.getTitle(), SwingConstants.CENTER);
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
        
        // Metni kısaltma kontrolü
        if (movie.getTitle().length() > 15) {
            titleLabel.setText(movie.getTitle().substring(0, 12) + "...");
            titleLabel.setToolTipText(movie.getTitle()); // Tam adı tooltip olarak göster
        }
        
        card.add(posterLabel, BorderLayout.CENTER);
        card.add(titleLabel, BorderLayout.SOUTH);
        
        return card;
    }
    
    // Mevcut olmayan filmleri getirmek için yardımcı metot
    private List<Movie> getAvailableMovies(FilmList list) {
        List<Movie> allMovies = filmController.getAllMovies();
        System.out.println("getAvailableMovies - Toplam film sayısı: " + allMovies.size());
        
        List<Movie> availableMovies = new ArrayList<>();
        
        for (Movie movie : allMovies) {
            if (!list.getMovies().contains(movie)) {
                availableMovies.add(movie);
            }
        }
        
        System.out.println("getAvailableMovies - Eklenebilir film sayısı: " + availableMovies.size());
        return availableMovies;
    }
    
    // Controller'ları ayarlamak için setter metotları
    public void setFilmController(FilmController filmController) {
        this.filmController = filmController;
    }
    
    public void setFilmListController(FilmListController filmListController) {
        this.filmListController = filmListController;
    }
    
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        // Test verisini oluştur
        SwingUtilities.invokeLater(() -> {
            try {
                System.out.println("\n=== MovieMood Uygulaması Başlatılıyor ===\n");
                
                // Controller'ları oluştur
                UserController userController = new UserController();
                FilmController filmController = new FilmController();
                FilmListController filmListController = new FilmListController();
                
                // Filmler yükle
                System.out.println("Filmler yükleniyor...");
                MovieSeeder.seedMovies(filmController);
                
                List<Movie> allMovies = filmController.getAllMovies();
                System.out.println("Toplam " + allMovies.size() + " film yüklendi");
                
                // İlk 3 filmi göster
                if (!allMovies.isEmpty()) {
                    System.out.println("İlk 3 film:");
                    for (int i = 0; i < Math.min(3, allMovies.size()); i++) {
                        Movie movie = allMovies.get(i);
                        System.out.println("  - " + movie.getTitle() + " - URL: " + movie.getPosterUrl());
                    }
                }
                
                // Test kullanıcısı oluştur
                userController.register("burakklc@gmail.com", "burak","kilic","1234");
                User testUser = userController.login("TestUser", "password");
                
                if (testUser != null) {
                    // Tek bir liste oluştur
                    filmListController.createList(testUser, "Favorilerim");
                    FilmList favoriteList = filmListController.getFilmListByName(testUser, "Favorilerim");
                    
                    // Listeye sadece bir film ekle
                    if (favoriteList != null && !allMovies.isEmpty()) {
                        Movie movie = allMovies.get(0);
                        filmListController.addMovieToList(favoriteList, movie);
                        System.out.println("Film eklendi: " + movie.getTitle());
                    }
                    
                    // Boş liste oluştur
                    filmListController.createList(testUser, "İzlenecekler");
                    
                    // UI başlat
                    MyListPanel panel = new MyListPanel(testUser);
                    panel.setFilmController(filmController);
                    panel.setFilmListController(filmListController);
                }
                
            } catch (Exception e) {
                System.err.println("Uygulama başlatma hatası:");
                e.printStackTrace();
            }
        });
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
                
                // Yeni liste oluştur
                filmListController.createList(currentUser, listName);
                
                // Yeni oluşturulan listeyi al
                FilmList newList = filmListController.getFilmListByName(currentUser, listName);
                
                // Kullanıcının önerilen filmlerini al
                // Eğer recommendedMovies boş ise, önerileri hesapla
                if (currentUser.getRecommendedMovies().isEmpty()) {
                    currentUser.setRecommendedMovies();
                }
                List<Movie> recommendations = currentUser.getRecommendedMovies();
                
                // Önerilen filmleri yeni listeye ekle
                if (!recommendations.isEmpty()) {
                    for (Movie movie : recommendations) {
                        filmListController.addMovieToList(newList, movie);
                    }
                    
                    JOptionPane.showMessageDialog(this,
                        "List created with " + recommendations.size() + " recommended movies!",
                        "Success", 
                        JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this,
                        "List created, but no recommended movies were found. Try watching more movies first!",
                        "No Recommendations", 
                        JOptionPane.INFORMATION_MESSAGE);
                }
                
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
        private List<Movie> selectedMoviesToRemove = new ArrayList<>();
        private List<Movie> selectedMoviesToAdd = new ArrayList<>();
        private FilmList currentList;
        private JTextField listNameField;
        
        public ManageListDialog(JFrame parent, FilmList filmList) {
            super(parent, "Manage List", true);
            this.currentList = filmList;
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
            
            // List name panel
            JPanel listNamePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
            listNamePanel.setOpaque(false);
            listNamePanel.setAlignmentX(Component.LEFT_ALIGNMENT);
            
            // List name label
            JLabel listNameLabel = new JLabel("List name:");
            listNameLabel.setFont(new Font("Arial", Font.BOLD, 30));
            listNameLabel.setForeground(new Color(180, 0, 0)); // Dark red
            
            // Editable text field for list name
            listNameField = new JTextField(currentList.getName());
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
            saveNameButton.setBackground(new Color(255, 0, 0)); // Bright red
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
            
            // Mevcut filmleri göster
            List<Movie> currentMovies = currentList.getMovies();
            boolean hasMovies = !currentMovies.isEmpty();
            
            if (hasMovies) {
                // Filmleri göster
                for (Movie movie : currentMovies) {
                    myListPanel.add(createMovieCardWithRemoveButton(movie));
                }
            } else {
                // Boş mesaj göster
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
                submitRemoveButton.setBackground(new Color(255, 0, 0)); // Bright red
                submitRemoveButton.setOpaque(true);
                submitRemoveButton.setBorderPainted(false);
                submitRemoveButton.setFocusPainted(false);
                submitRemoveButton.setBorder(BorderFactory.createEmptyBorder(10, 25, 10, 25));
                submitRemoveButton.addActionListener(e -> {
                    if (!selectedMoviesToRemove.isEmpty()) {
                        // Show the notification dialog
                        showNotification("MOVIE REMOVED!!!");
                        
                        // Controller ile filmleri kaldır
                        for (Movie movie : selectedMoviesToRemove) {
                            filmListController.removeMovieFromList(currentList, movie);
                        }
                        
                        // Seçimi temizle
                        selectedMoviesToRemove.clear();
                        
                        // Listeyi yenile
                        refreshMovieList();
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
            
            // Mevcut olmayan filmleri göster
            List<Movie> availableMovies = getAvailableMovies(currentList);
            System.out.println("Dialog - Eklenebilecek film sayısı: " + availableMovies.size());
            
            if (availableMovies.isEmpty()) {
                // Boş durumu göster
                JLabel emptyLabel = new JLabel("No movies available to add");
                emptyLabel.setForeground(Color.BLACK);
                emptyLabel.setFont(new Font("Arial", Font.PLAIN, 16));
                otherMoviesPanel.add(emptyLabel);
            } else {
                // En fazla 10 film göster
                int count = 0;
                for (Movie movie : availableMovies) {
                    System.out.println("Dialog - Film ekleniyor: " + movie.getTitle());
                    otherMoviesPanel.add(createMovieCardWithAddButton(movie));
                    count++;
                    if (count >= 10) break;
                }
            }
            
            // Set a preferred size to ensure horizontal scrolling is visible
            otherMoviesPanel.setPreferredSize(new Dimension(1500, 220));
            
            JScrollPane otherMoviesScrollPane = new JScrollPane(otherMoviesPanel);
            otherMoviesScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
            otherMoviesScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
            otherMoviesScrollPane.setBorder(BorderFactory.createEmptyBorder());
            otherMoviesScrollPane.getViewport().setBackground(new Color(220, 220, 220)); // Light gray
            otherMoviesScrollPane.setPreferredSize(new Dimension(700, 240));
            
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
            submitAddButton.setBackground(new Color(255, 0, 0)); // Bright red
            submitAddButton.setOpaque(true);
            submitAddButton.setBorderPainted(false);
            submitAddButton.setFocusPainted(false);
            submitAddButton.setBorder(BorderFactory.createEmptyBorder(10, 25, 10, 25));
            submitAddButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
            
            submitAddButton.addActionListener(e -> {
                if (!selectedMoviesToAdd.isEmpty()) {
                    // Film eklendiğinde bildirimi göster
                    showNotification("MOVIE ADDED!!!");
                    
                    // Controller ile filmleri ekle
                    for (Movie movie : selectedMoviesToAdd) {
                        filmListController.addMovieToList(currentList, movie);
                    }
                    
                    // İşlem bittikten sonra seçimi temizle
                    selectedMoviesToAdd.clear();
                    
                    // Listeyi yenile
                    refreshMovieList();
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
        
        // Liste adını kaydetme metodu
        private void saveListName() {
            String newListName = listNameField.getText().trim();
            
            // Doğrulama - boş ad olmamalı
            if (newListName.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                    "List name cannot be empty.",
                    "Invalid Name",
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // Düzeltilmiş çakışma kontrolü
            if (!newListName.equals(currentList.getName())) {
                boolean nameExists = false;
                
                // Tüm kullanıcı listelerini al
                List<FilmList> userLists = filmListController.getAllFilmLists(currentUser);
                
                // Debug - listeleri göster
                System.out.println("İsim değişikliği kontrolü: " + currentList.getName() + " -> " + newListName);
                System.out.println("Kullanıcının tüm listeleri: ");
                for (FilmList list : userLists) {
                    System.out.println("  - Liste: " + list.getName());
                }
                
                // Tüm listeleri kontrol et
                for (FilmList existingList : userLists) {
                    // Mevcut liste hariç diğer listeleri kontrol et
                    if (existingList != currentList && 
                        existingList.getName().equalsIgnoreCase(newListName)) { // Büyük-küçük harf duyarsız kontrol
                        nameExists = true;
                        System.out.println("  - ÇAKIŞMA BULUNDU: " + existingList.getName());
                        break;
                    }
                }
                
                if (nameExists) {
                    // Çakışma varsa uyarı göster
                    JOptionPane.showMessageDialog(
                        SwingUtilities.getWindowAncestor(this), // Doğru üst pencereyi al
                        "A list with this name already exists. Please choose a different name.",
                        "Duplicate Name",
                        JOptionPane.ERROR_MESSAGE
                    );
                    return;
                }
                
                System.out.println("Çakışma yok, işlem devam ediyor.");
                
                // İsim değiştiyse güncelle
                // Filmleri kaydet
                List<Movie> movies = new ArrayList<>(currentList.getMovies());
                
                // Eski listeyi sil
                filmListController.removeFilmList(currentUser, currentList.getName());
                
                // Yeni liste oluştur
                filmListController.createList(currentUser, newListName);
                
                // Yeni liste referansını al
                FilmList newList = filmListController.getFilmListByName(currentUser, newListName);
                
                // Filmleri ekle
                for (Movie movie : movies) {
                    filmListController.addMovieToList(newList, movie);
                }
                
                // Güncel liste referansını güncelle
                currentList = newList;
                
                // Başarı bildirimi göster
                showNotification("List renamed to \"" + newListName + "\"");
            }
        }
        
        // Film kartlarını yenilemek için
        private void refreshMovieList() {
            myListPanel.removeAll();
            
            List<Movie> movies = currentList.getMovies();
            
            if (movies.isEmpty()) {
                // Boş mesaj göster
                JLabel emptyLabel = new JLabel("No movies in this list yet. Add some movies below.");
                emptyLabel.setForeground(Color.BLACK);
                emptyLabel.setFont(new Font("Arial", Font.PLAIN, 16));
                myListPanel.add(emptyLabel);
            } else {
                // Filmleri göster
                for (Movie movie : movies) {
                    myListPanel.add(createMovieCardWithRemoveButton(movie));
                }
            }
            
            myListPanel.revalidate();
            myListPanel.repaint();
            
            // Other movies panelini de güncelle
            updateOtherMoviesPanel();
        }
        
        // Other movies panelini güncelleme
        private void updateOtherMoviesPanel() {
            otherMoviesPanel.removeAll();
            
            List<Movie> availableMovies = getAvailableMovies(currentList);
            System.out.println("updateOtherMoviesPanel - Eklenebilecek film sayısı: " + availableMovies.size());
            
            if (availableMovies.isEmpty()) {
                JLabel emptyLabel = new JLabel("No movies available to add");
                emptyLabel.setForeground(Color.BLACK);
                emptyLabel.setFont(new Font("Arial", Font.PLAIN, 16));
                otherMoviesPanel.add(emptyLabel);
            } else {
                int count = 0;
                for (Movie movie : availableMovies) {
                    otherMoviesPanel.add(createMovieCardWithAddButton(movie));
                    count++;
                    if (count >= 10) break;
                }
            }
            
            otherMoviesPanel.revalidate();
            otherMoviesPanel.repaint();
        }
        
        private JPanel createMovieCardWithRemoveButton(Movie movie) {
            JPanel container = new JPanel(new BorderLayout());
            container.setOpaque(false);
            
            JPanel card = new JPanel();
            card.setLayout(new BorderLayout());
            card.setBackground(new Color(30, 30, 30));
            card.setPreferredSize(new Dimension(120, 160));
            card.setBorder(BorderFactory.createEmptyBorder());
            card.setCursor(new Cursor(Cursor.HAND_CURSOR));
            
            // Film posteri için etiket oluştur
            JLabel posterLabel = new JLabel();
            posterLabel.setPreferredSize(new Dimension(120, 140));
            posterLabel.setBackground(new Color(20, 20, 20));
            posterLabel.setHorizontalAlignment(SwingConstants.CENTER);
            posterLabel.setOpaque(true);
            
            // Yükleme sırasında görünecek metin
            posterLabel.setText("Loading...");
            posterLabel.setForeground(Color.LIGHT_GRAY);
            
            // Arka planda resmi yükle
            SwingWorker<ImageIcon, Void> imageLoader = new SwingWorker<ImageIcon, Void>() {
                @Override
                protected ImageIcon doInBackground() throws Exception {
                    String posterUrl = movie.getPosterUrl();
                    if (posterUrl != null && !posterUrl.isEmpty()) {
                        return loadImageFromURL(posterUrl);
                    }
                    return null;
                }
                
                @Override
                protected void done() {
                    try {
                        ImageIcon poster = get();
                        if (poster != null) {
                            ImageIcon resizedPoster = resizeImageIcon(poster, 120, 140);
                            posterLabel.setIcon(resizedPoster);
                            posterLabel.setText(""); // Loading yazısını temizle
                        } else {
                            // Poster yüklenemezse, ilk harfi göster
                            posterLabel.setText(movie.getTitle().substring(0, 1).toUpperCase());
                            posterLabel.setFont(new Font("Arial", Font.BOLD, 48));
                        }
                    } catch (Exception e) {
                        System.err.println("Poster gösterilemiyor: " + e.getMessage());
                        posterLabel.setText(movie.getTitle().substring(0, 1).toUpperCase());
                        posterLabel.setFont(new Font("Arial", Font.BOLD, 48));
                    }
                }
            };
            imageLoader.execute();
            
            JLabel titleLabel = new JLabel(movie.getTitle(), SwingConstants.CENTER);
            titleLabel.setForeground(Color.WHITE);
            titleLabel.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
            titleLabel.setFont(new Font("Arial", Font.PLAIN, 10));
            
            // Metni kısaltma kontrolü
            if (movie.getTitle().length() > 15) {
                titleLabel.setText(movie.getTitle().substring(0, 12) + "...");
                titleLabel.setToolTipText(movie.getTitle()); // Tam adı tooltip olarak göster
            }
            
            card.add(posterLabel, BorderLayout.CENTER);
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
                if (selectedMoviesToRemove.contains(movie)) {
                    selectedMoviesToRemove.remove(movie);
                    removeButton.setBackground(null);
                    removeButton.setOpaque(false);
                    removeButton.setForeground(Color.WHITE);
                } else {
                    selectedMoviesToRemove.add(movie);
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
        
        private JPanel createMovieCardWithAddButton(Movie movie) {
            // Debug için
            System.out.println("Film kartı oluşturuluyor: " + movie.getTitle());
            
            JPanel container = new JPanel(new BorderLayout());
            container.setOpaque(false);
            
            JPanel card = new JPanel();
            card.setLayout(new BorderLayout());
            card.setBackground(new Color(30, 30, 30));
            card.setPreferredSize(new Dimension(120, 160));
            card.setBorder(BorderFactory.createEmptyBorder());
            card.setCursor(new Cursor(Cursor.HAND_CURSOR));
            
            // Film posteri için etiket oluştur
            JLabel posterLabel = new JLabel();
            posterLabel.setPreferredSize(new Dimension(120, 140));
            posterLabel.setBackground(new Color(20, 20, 20));
            posterLabel.setHorizontalAlignment(SwingConstants.CENTER);
            posterLabel.setOpaque(true);
            
            // Yükleme sırasında görünecek bir metin ekle
            posterLabel.setText("Loading...");
            posterLabel.setForeground(Color.LIGHT_GRAY);
            
            // Arka planda resmi yükle
            SwingWorker<ImageIcon, Void> imageLoader = new SwingWorker<ImageIcon, Void>() {
                @Override
                protected ImageIcon doInBackground() throws Exception {
                    String posterUrl = movie.getPosterUrl();
                    System.out.println("Poster URL: " + posterUrl);
                    if (posterUrl != null && !posterUrl.isEmpty()) {
                        return loadImageFromURL(posterUrl);
                    }
                    return null;
                }
                
                @Override
                protected void done() {
                    try {
                        ImageIcon poster = get();
                        if (poster != null) {
                            ImageIcon resizedPoster = resizeImageIcon(poster, 120, 140);
                            posterLabel.setIcon(resizedPoster);
                            posterLabel.setText(""); // Loading yazısını temizle
                            System.out.println("Poster başarıyla yüklendi: " + movie.getTitle());
                        } else {
                            // Poster yüklenemezse, ilk harfi göster
                            posterLabel.setText(movie.getTitle().substring(0, 1).toUpperCase());
                            posterLabel.setFont(new Font("Arial", Font.BOLD, 48));
                            System.out.println("Poster yüklenemedi, ilk harf gösteriliyor: " + movie.getTitle());
                        }
                    } catch (Exception e) {
                        System.err.println("Poster gösterilemiyor: " + e.getMessage());
                        posterLabel.setText(movie.getTitle().substring(0, 1).toUpperCase());
                        posterLabel.setFont(new Font("Arial", Font.BOLD, 48));
                    }
                }
            };
            imageLoader.execute();
            
            JLabel titleLabel = new JLabel(movie.getTitle(), SwingConstants.CENTER);
            titleLabel.setForeground(Color.WHITE);
            titleLabel.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
            titleLabel.setFont(new Font("Arial", Font.PLAIN, 10));
            
            // Metni kısaltma kontrolü (çok uzunsa "..." ekle)
            if (movie.getTitle().length() > 15) {
                titleLabel.setText(movie.getTitle().substring(0, 12) + "...");
                titleLabel.setToolTipText(movie.getTitle()); // Tam adı tooltip olarak göster
            }
            
            card.add(posterLabel, BorderLayout.CENTER);
            card.add(titleLabel, BorderLayout.SOUTH);
            
            // Add butonu oluştur
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
            
            // Ekleme butonu action listener'ı
            addButton.addActionListener(e -> {
                try {
                    // Seçim durumunu değiştir
                    if (selectedMoviesToAdd.contains(movie)) {
                        selectedMoviesToAdd.remove(movie);
                        addButton.setBackground(null);
                        addButton.setOpaque(false);
                        addButton.setForeground(Color.WHITE);
                        System.out.println("Film seçimden kaldırıldı: " + movie.getTitle());
                    } else {
                        selectedMoviesToAdd.add(movie);
                        addButton.setBackground(Color.WHITE);
                        addButton.setForeground(Color.BLACK);
                        addButton.setOpaque(true);
                        System.out.println("Film seçildi: " + movie.getTitle());
                    }
                } catch (Exception ex) {
                    System.err.println("HATA - Buton tıklamasında: " + ex.getMessage());
                    ex.printStackTrace();
                }
            });
            
            // Container'a ekle
            container.add(card, BorderLayout.CENTER);
            
            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
            buttonPanel.setOpaque(false);
            buttonPanel.add(addButton);
            container.add(buttonPanel, BorderLayout.SOUTH);
            
            // Debug için
            System.out.println("Film kartı oluşturuldu: " + movie.getTitle());
            
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