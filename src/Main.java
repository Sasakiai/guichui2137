/**
 * Launches the Pac-Man game.
 */
import controller.MainMenuController;
import view.MainMenuWindow;
import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        // ensure GUI is created on the Swing event thread
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                MainMenuWindow mainMenu = new MainMenuWindow();
                new MainMenuController(mainMenu);
                mainMenu.setVisible(true);
            }
        });
    }
}

