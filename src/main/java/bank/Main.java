package bank;

import javax.swing.SwingUtilities;

import bank.ui.LoginScreen;

/**
 * The entry point for the MyBankUML application.
 * <p>
 * This class is responsible for initializing the core system components (Facade)
 * and launching the Graphical User Interface (GUI) on the appropriate thread.
 * </p>
 */
public class Main {

    /**
     * The main method acts as the application's execution start point.
     *
     * @param args Command-line arguments (not currently used in this application).
     */
    public static void main(String[] args) {
        // 1. Initialize the Core System Facade
        // The UIManager acts as the central bridge between the UI and the Backend (Database/Logic).
        // It is instantiated once here and passed to all UI screens to ensure they share the same state.
        UIManager appController = new UIManager();

        // 2. Launch the User Interface
        // Swing components are not thread-safe and must be initialized on the Event Dispatch Thread (EDT).
        // 'invokeLater' schedules this task to run safely on the EDT to prevent potential freezing or errors.
        SwingUtilities.invokeLater(() -> {
            // Create the initial Login Screen and inject the controller
            LoginScreen loginWindow = new LoginScreen(appController);
            
            // Make the window visible to the user
            loginWindow.setVisible(true);
        });
    }
}