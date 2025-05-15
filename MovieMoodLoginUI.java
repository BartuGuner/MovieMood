import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.border.*;

public class MovieMoodLoginUI extends JFrame {
    // Components
    private JPanel leftPanel, rightPanel, formPanel;
    private JLabel titleLabel, questionLabel;
    private JTextField emailField;
    private JPasswordField passwordField;
    private JButton signInButton, signUpButton;
    
    // Colors
    private Color darkRed = new Color(102, 0, 0);
    private Color darkGray = new Color(25, 25, 25);
    private Color brightRed = new Color(230, 0, 0);
    
    public MovieMoodLoginUI() {
        setTitle("Movie Mood");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(1, 2));
        
        initComponents();
        layoutComponents();
        
        setVisible(true);
    }
    
    private void initComponents() {
        // Panels
        leftPanel = new JPanel(new BorderLayout());
        leftPanel.setBackground(darkRed);
        
        rightPanel = new JPanel(new BorderLayout());
        rightPanel.setBackground(darkGray);
        
        formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBackground(darkRed);
        formPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        
        //labels
        titleLabel = new JLabel("Movie Mood");
        titleLabel.setForeground(brightRed);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 36));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        questionLabel = new JLabel("DON'T HAVE AN ACCOUNT?");
        questionLabel.setForeground(brightRed);
        questionLabel.setFont(new Font("Arial", Font.BOLD, 24));
        questionLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        //Text fields
        emailField = new JTextField(20);
        emailField.setMaximumSize(new Dimension(300, 40));
        emailField.setAlignmentX(Component.CENTER_ALIGNMENT);
        emailField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.LIGHT_GRAY),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)));
        
        passwordField = new JPasswordField(20);
        passwordField.setMaximumSize(new Dimension(300, 40));
        passwordField.setAlignmentX(Component.CENTER_ALIGNMENT);
        passwordField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.LIGHT_GRAY),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)));
        
        //Buttons
        signInButton = new JButton("Sign In");
        signInButton.setMaximumSize(new Dimension(300, 40));
        signInButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        signInButton.setBackground(darkGray);
        signInButton.setForeground(Color.WHITE);
        signInButton.setFocusPainted(false);
        signInButton.setBorderPainted(false);
        
        signUpButton = new JButton("Sign Up");
        signUpButton.setMaximumSize(new Dimension(300, 40));
        signUpButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        signUpButton.setBackground(darkGray);
        signUpButton.setForeground(Color.WHITE);
        signUpButton.setFocusPainted(false);
        signUpButton.setBorderPainted(false);
        
        // Add hover effects
        addHoverEffect(signInButton, darkGray.brighter());
        addHoverEffect(signUpButton, darkGray.brighter());
        
        // Add action listener to sign in button
        signInButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String mail = emailField.getText();
                char[] password = passwordField.getPassword();
                
                try {
                    // For testing purposes - using a simple test credential
                    User newUser = UserController.login(mail,new String(password));
                    if (newUser!=null) {
                        FrontendStaticUser.frontEndStaticUser = newUser;
                        JOptionPane.showMessageDialog(MovieMoodLoginUI.this, 
                                "Login successful!", 
                                "Success", 
                                JOptionPane.INFORMATION_MESSAGE);
                        
                        setVisible(false); // Hide the login frame
                        new ProfileFrame(newUser);
                        emailField.setText("");
                        passwordField.setText("");
                    } else {
                        JOptionPane.showMessageDialog(MovieMoodLoginUI.this, 
                                "No user exists with these credentials", 
                                "Login Failed", 
                                JOptionPane.ERROR_MESSAGE);
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(MovieMoodLoginUI.this,
                            "Error during login: " + ex.getMessage(),
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                } finally {
                    // Clear password from memory (Güvenlik için önemli)
                    java.util.Arrays.fill(password, '0');
                }
            }
        });
        
        signUpButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new SignUpFrame();
            }
        });
    }
    
    private void layoutComponents() {
        // Left panel with form
        JLabel emailLabel = new JLabel("Email");
        emailLabel.setForeground(Color.WHITE);
        emailLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel passwordLabel = new JLabel("Password");
        passwordLabel.setForeground(Color.WHITE);
        passwordLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        formPanel.add(Box.createVerticalGlue());
        formPanel.add(titleLabel);
        formPanel.add(Box.createRigidArea(new Dimension(0, 40)));
        formPanel.add(emailLabel);
        formPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        formPanel.add(emailField);
        formPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        formPanel.add(passwordLabel);
        formPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        formPanel.add(passwordField);
        formPanel.add(Box.createRigidArea(new Dimension(0, 30)));
        formPanel.add(signInButton);
        formPanel.add(Box.createVerticalGlue());
        
        leftPanel.add(formPanel, BorderLayout.CENTER);
        
        JPanel rightContentPanel = new JPanel();
        rightContentPanel.setLayout(new BoxLayout(rightContentPanel, BoxLayout.Y_AXIS));
        rightContentPanel.setBackground(darkGray);
        rightContentPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        
        rightContentPanel.add(Box.createVerticalGlue());
        rightContentPanel.add(questionLabel);
        rightContentPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        rightContentPanel.add(signUpButton);
        rightContentPanel.add(Box.createVerticalGlue());
        
        rightPanel.add(rightContentPanel, BorderLayout.CENTER);
        
        add(leftPanel);
        add(rightPanel);
    }
    
    private void addHoverEffect(JButton button, Color hoverColor) {
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(hoverColor);
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(darkGray);
            }
        });
    }
    public boolean signInSuccsesfully(String name,char[] password){
        return true;
    }
  
}
