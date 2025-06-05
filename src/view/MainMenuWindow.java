package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

/**
 * First screen presented to the player with simple menu options.
 */
public class MainMenuWindow extends JFrame {
    private JButton newGameButton;
    private JButton highScoresButton;
    private JButton exitButton;

    /**
     * Builds the menu window and its components.
     */
    public MainMenuWindow() {
        setTitle("Pac-Man");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);
        setResizable(true);
        initComponents();
        setVisible(true);
    }

    /**
     * Creates and arranges all Swing components.
     */
    private void initComponents() {
        ImageIcon iconPac = new ImageIcon(getClass().getResource("/iconPac.png"));
        setIconImage(iconPac.getImage());

        Color backgroundColor = new Color(0xFFF9E6);

        JPanel mainPanel = new JPanel(new BorderLayout()); //główny panel;
        mainPanel.setBackground(backgroundColor);

        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setBackground(backgroundColor);
        titlePanel.setPreferredSize(new Dimension(800, 180));

        ImageIcon menuTitle = scaleImage("/Title.png", 500, 107);
        JLabel menuTitleLabel = new JLabel(menuTitle);
        menuTitleLabel.setHorizontalAlignment(SwingConstants.CENTER);

        titlePanel.add(menuTitleLabel, BorderLayout.CENTER);
        titlePanel.setPreferredSize(new Dimension(800, 200));

        mainPanel.add(titlePanel, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel();// panel na przyciski
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        buttonPanel.setBackground(backgroundColor);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(30, 150, 50, 150));

        newGameButton = createImageButton("/newGameButton.png", 350,66);
        buttonPanel.add(newGameButton);
        buttonPanel.add(Box.createVerticalStrut(30));

        highScoresButton = createImageButton("/highScoresButton.png", 350,66);
        buttonPanel.add(highScoresButton);
        buttonPanel.add(Box.createVerticalStrut(30));

        exitButton = createImageButton("/exitButton.png", 350,66);
        buttonPanel.add(exitButton);

        mainPanel.add(buttonPanel, BorderLayout.CENTER);

        add(mainPanel);
    }

    private ImageIcon scaleImage(String imagePath, int width, int height) {
        ImageIcon icon = new ImageIcon(getClass().getResource(imagePath));
        Image scaledImage = icon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
        return new ImageIcon(scaledImage);
    }

    private JButton createImageButton(String imagePath, int width, int height) {
        ImageIcon scaledIcon = scaleImage(imagePath, width, height);

        JButton button = new JButton(scaledIcon);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setFocusPainted(false);
        button.setOpaque(false);
        button.setPreferredSize(new Dimension(width, height));
        button.setMaximumSize(new Dimension(width, height));
        button.setMinimumSize(new Dimension(width, height));
        button.setAlignmentX(Component.CENTER_ALIGNMENT);

        return button;
    }

    /**
     * Registers a listener for the "new game" button.
     */
    public void addNewGameListener(ActionListener listener) {
        newGameButton.addActionListener(listener);
    }

    /**
     * Registers a listener for the high scores button.
     */
    public void addHighScoresListener(ActionListener listener) {
        highScoresButton.addActionListener(listener);
    }

    /**
     * Registers a listener for the exit button.
     */
    public void addExitListener(ActionListener listener) {
        exitButton.addActionListener(listener);
    }
}
