package bank;

import bank.ui.LoginScreen;
import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        // 1. Initialize the Facade
        // This 'uiManager' will be passed to every screen so they can talk to the backend.
        UIManager uiManager = new UIManager();

        // 2. Launch the GUI
        // We use 'invokeLater' to ensure the UI runs on the correct thread.
        SwingUtilities.invokeLater(() -> {
            // Create and show the login screen
            LoginScreen loginScreen = new LoginScreen(uiManager);
            loginScreen.setVisible(true);
        });
    }
}