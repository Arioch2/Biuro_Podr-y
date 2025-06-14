import Services.DatabaseConnector;
import Services.RoundedButton;

import javax.swing.*;
import java.awt.*;

public class ChangePasswordPanel extends JFrame {
    private final JPasswordField oldPasswordField = new JPasswordField(20);
    private final JPasswordField newPasswordField = new JPasswordField(20);
    private final JPasswordField confirmPasswordField = new JPasswordField(20);
    private final String currentLogin;

    public ChangePasswordPanel(String login) {
        super("Zmień hasło");
        this.currentLogin = login;

        //  Kolory
        Color tło = new Color(26, 26, 34);
        Color pole = new Color(36, 36, 46);
        Color tekst = Color.WHITE;
        Color zielony = new Color(42, 157, 143);
        Color czerwony = new Color(230, 57, 70);
        Font font = new Font("SansSerif", Font.PLAIN, 14);

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(tło);
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(10, 20, 10, 20);
        c.fill = GridBagConstraints.HORIZONTAL;

        //  Pola
        c.gridx = 0;
        c.gridy = 0;
        panel.add(label("Stare hasło:", tekst, font), c);
        stylujPole(oldPasswordField, pole, tekst, font);
        c.gridx = 1;
        panel.add(oldPasswordField, c);

        c.gridy++;
        c.gridx = 0;
        panel.add(label("Nowe hasło:", tekst, font), c);
        stylujPole(newPasswordField, pole, tekst, font);
        c.gridx = 1;
        panel.add(newPasswordField, c);

        c.gridy++;
        c.gridx = 0;
        panel.add(label("Powtórz hasło:", tekst, font), c);
        stylujPole(confirmPasswordField, pole, tekst, font);
        c.gridx = 1;
        panel.add(confirmPasswordField, c);

        //  Przyciski
        RoundedButton zmienHasloButton = new RoundedButton("Zmień", zielony, 20);
        RoundedButton anulujButton = new RoundedButton("Anuluj", czerwony, 20);
        zmienHasloButton.setPreferredSize(new Dimension(150, 40));
        anulujButton.setPreferredSize(new Dimension(150, 40));

        JPanel przyciskPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        przyciskPanel.setBackground(tło);
        przyciskPanel.add(anulujButton);
        przyciskPanel.add(zmienHasloButton);

        c.gridy++;
        c.gridx = 0;
        c.gridwidth = 2;
        panel.add(przyciskPanel, c);

        //  Okno
        setContentPane(panel);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(500, 300);
        setLocationRelativeTo(null);
        setResizable(false);
        setVisible(true);


        zmienHasloButton.addActionListener(e -> {
            String oldPassword = new String(oldPasswordField.getPassword());
            String newPassword = new String(newPasswordField.getPassword());
            String confirmPassword = new String(confirmPasswordField.getPassword());

            if (!newPassword.equals(confirmPassword)) {
                JOptionPane.showMessageDialog(this, "Nowe hasło się nie zgadza!", "Błąd", JOptionPane.ERROR_MESSAGE);
                return;
            }

            boolean success = DatabaseConnector.changePassword(currentLogin, oldPassword, newPassword);
            if (success) {
                JOptionPane.showMessageDialog(this, "Hasło zostało zmienione!", "Sukces", JOptionPane.INFORMATION_MESSAGE);
                dispose();
                new UsersMenuPanel();
            } else {
                JOptionPane.showMessageDialog(this, "Błąd zmiany hasła.", "Błąd", JOptionPane.ERROR_MESSAGE);
            }
        });

        anulujButton.addActionListener(e -> {
            dispose();
            new UsersMenuPanel();
        });
    }

    private JLabel label(String text, Color fg, Font font) {
        JLabel l = new JLabel(text);
        l.setForeground(fg);
        l.setFont(font);
        return l;
    }

    private void stylujPole(JPasswordField field, Color bg, Color fg, Font font) {
        field.setBackground(bg);
        field.setForeground(fg);
        field.setCaretColor(fg);
        field.setFont(font);
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(fg.darker()),
                BorderFactory.createEmptyBorder(6, 8, 6, 8)
        ));
    }
}
