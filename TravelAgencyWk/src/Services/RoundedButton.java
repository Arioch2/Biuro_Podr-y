package Services;

import javax.swing.*;
import java.awt.*;

public class RoundedButton extends JButton {
    private Color backgroundColor;
    private int radius;

    public RoundedButton(String text, Color backgroundColor, int radius) {
        super(text);
        this.backgroundColor = backgroundColor;
        this.radius = radius;
        setForeground(Color.WHITE);
        setFocusPainted(false);
        setContentAreaFilled(false);
        setBorderPainted(false);
        setFont(new Font("SansSerif", Font.PLAIN, 12));
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                setBackground(backgroundColor.brighter());
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                setBackground(backgroundColor);
            }
        });

        setBackground(backgroundColor);


    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2.setColor(getBackground());
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius);

        super.paintComponent(g);
        g2.dispose();
    }


    @Override
    public void paintBorder(Graphics g) {
        // Brak borderów
    }

    @Override
    public boolean isContentAreaFilled() {
        return false;
    }



}
