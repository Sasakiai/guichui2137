package view;

import model.ScoreEntry;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * Window that lists stored high scores.
 */
public class HighScoresWindow extends JFrame {

    /**
     * Shows high scores in a scrollable list.
     */
    public HighScoresWindow(List<ScoreEntry> scores) {
        setTitle("High Scores: Pac-Man");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // CHECK
        setSize(800, 600);
        setLocationRelativeTo(null);
        setResizable(true);

        ImageIcon iconPac = new ImageIcon(getClass().getResource("/iconPac.png"));
        setIconImage(iconPac.getImage());

        Color backgroundColor = new Color(0xFFF9E6);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(backgroundColor);

        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setBackground(backgroundColor);
        titlePanel.setPreferredSize(new Dimension(800, 180));

        ImageIcon highScoresTitle = scaleImage("/highScoresTitle.png", 500, 80);
        JLabel highScoresLabel = new JLabel(highScoresTitle);
        highScoresLabel.setHorizontalAlignment(SwingConstants.CENTER);

        titlePanel.add(highScoresLabel, BorderLayout.CENTER);
        titlePanel.setPreferredSize(new Dimension(800, 200));

        mainPanel.add(titlePanel, BorderLayout.NORTH);

        DefaultListModel<String> listModel = new DefaultListModel<String>();
        int rank = 1;
        for (ScoreEntry entry : scores) {
            listModel.addElement(rank++ + ". " + entry.getName() + " - " + entry.getScore());
        }

        JList<String> list = new JList<>(listModel);
        list.setBackground(backgroundColor);
        list.setForeground(new Color(0xF08080));
        list.setFont(new Font("Monospaced", Font.BOLD, 20));

        JScrollPane scroll = new JScrollPane(list);
        scroll.getViewport().setBackground(backgroundColor);
        scroll.setBorder(BorderFactory.createEmptyBorder());

        mainPanel.add(scroll, BorderLayout.CENTER);

        add(mainPanel);
        setVisible(true);
    }

    /**
     * Utility helper for image scaling.
     */
    private ImageIcon scaleImage(String imagePath, int width, int height) {
        ImageIcon icon = new ImageIcon(getClass().getResource(imagePath));
        Image scaledImage = icon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
        return new ImageIcon(scaledImage);
    }
}

