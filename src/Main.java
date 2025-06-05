import controller.MainMenuController;
import view.MainMenuWindow;
import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MainMenuWindow mainMenu = new MainMenuWindow();
            new MainMenuController(mainMenu);
            mainMenu.setVisible(true);
        });
    }
}

