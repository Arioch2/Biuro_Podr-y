import javax.swing.*;
import java.awt.*;

public class StyleHelper {
    private static final Font FONT_INPUT = new Font("SansSerif", Font.PLAIN, 14);
    private static final Font FONT_LABEL = new Font("SansSerif", Font.BOLD, 22);
    private static final Font FONT_BUTTON = new Font("SansSerif", Font.BOLD, 14);

    public static void stylizujPanel(JPanel panel) {
        panel.setBackground(new Color(245, 245, 250));
    }

    public static void stylizujNaglowek(JLabel label) {
        if (label != null) {
            label.setFont(FONT_LABEL);
            label.setForeground(new Color(40, 40, 60));
        }
    }

    public static void stylizujPoleTekstowe(JTextField field) {
        field.setFont(FONT_INPUT);
    }

    public static void stylizujPoleHasla(JPasswordField field) {
        field.setFont(FONT_INPUT);
    }

    public static void stylizujPrzyciskNiebieski(JButton btn) {
        stylizujPrzycisk(btn, new Color(30, 144, 255));
    }

    public static void stylizujPrzyciskZielony(JButton btn) {
        stylizujPrzycisk(btn, new Color(40, 167, 69));
    }

    public static void stylizujPrzyciskCzerwony(JButton btn) {
        stylizujPrzycisk(btn, new Color(220, 53, 69));
    }

    private static void stylizujPrzycisk(JButton btn, Color background) {
        btn.setBackground(background);
        btn.setForeground(Color.WHITE);
        btn.setFont(FONT_BUTTON);
        btn.setFocusPainted(false);
        btn.setOpaque(true);
        btn.setBorderPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
    }
}
