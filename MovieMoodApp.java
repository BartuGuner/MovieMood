import javax.swing.*;

public class MovieMoodApp {
    public static void main(String[] args) {
        try {
            UserController.register("Mert", "Ahmet", "Mehmet","123" );
            // Set system look and feel
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> new MovieMoodLoginUI());
    }
}
