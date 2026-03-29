import javax.swing.*;

public class MainApp {
    public static void main(String[] args) {
        // Set Look and Feel to System Default for a cleaner look
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {}

        // Launch Dashboard
        SwingUtilities.invokeLater(() -> {
            new DashboardUI().setVisible(true);
        });
    }
}
