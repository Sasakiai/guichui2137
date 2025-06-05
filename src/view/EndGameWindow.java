package view;

import javax.swing.*;
import java.awt.*;

/**
 * Simple dialog shown when the game ends so the player can enter a name.
 */
public class EndGameWindow extends JFrame {
    private final JTextField nameField = new JTextField();

    public EndGameWindow(int score, final NameSubmitListener listener) {
        setTitle("Game Over");
        setSize(400, 250);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        ImageIcon iconPac = new ImageIcon(getClass().getResource("/iconPac.png"));
        setIconImage(iconPac.getImage());

        Color backgroundColor = new Color(0xFFF9E6);
        getContentPane().setBackground(backgroundColor);

        JPanel panel = new JPanel();
        panel.setBackground(backgroundColor);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JLabel overLabel = new JLabel("Game Over! Score: " + score);
        overLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        overLabel.setFont(new Font("Monospaced", Font.BOLD, 20));
        overLabel.setForeground(new Color(0xF08080));
        panel.add(overLabel);

        panel.add(Box.createVerticalStrut(10));

        JLabel prompt = new JLabel("Enter your name:");
        prompt.setAlignmentX(Component.CENTER_ALIGNMENT);
        prompt.setFont(new Font("Monospaced", Font.BOLD, 18));
        prompt.setForeground(new Color(0xF08080));
        panel.add(prompt);

        nameField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        panel.add(nameField);

        JButton submit = new JButton("Submit");
        submit.setAlignmentX(Component.CENTER_ALIGNMENT);
        submit.setBackground(new Color(0xF08080));
        submit.setForeground(Color.WHITE);
        submit.setFocusPainted(false);
        submit.setFont(new Font("Monospaced", Font.BOLD, 16));
        submit.setBorder(BorderFactory.createEmptyBorder(5, 20, 5, 20));
        submit.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                if (listener != null) {
                    listener.onSubmit(nameField.getText());
                }
                dispose();
            }
        });
        panel.add(Box.createVerticalStrut(10));
        panel.add(submit);

        add(panel);
    }
}

