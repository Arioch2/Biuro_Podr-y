import Services.DatabaseConnector;
import Services.RoundedButton;

import javax.swing.*;
import java.awt.*;

public class DeleteAccountPanel extends JFrame {
    private final JTextField loginField = new JTextField(20);
    private final JPasswordField passwordField = new JPasswordField(20);
    private final JPasswordField confirmPasswordField = new JPasswordField(20);
    private final UsersMenuPanel usersMenuPanel;

    public DeleteAccountPanel() {
        super("Usuń konto");

        //  Styl
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

        //  Login
        c.gridx = 0;
        c.gridy = 0;
        panel.add(label("Login:", tekst, font), c);
        stylujPole(loginField, pole, tekst, font);
        c.gridx = 1;
        panel.add(loginField, c);

        //  Hasło
        c.gridy++;
        c.gridx = 0;
        panel.add(label("Hasło:", tekst, font), c);
        stylujPole(passwordField, pole, tekst, font);
        c.gridx = 1;
        panel.add(passwordField, c);

        //  Potwierdzenie
        c.gridy++;
        c.gridx = 0;
        panel.add(label("Potwierdź hasło:", tekst, font), c);
        stylujPole(confirmPasswordField, pole, tekst, font);
        c.gridx = 1;
        panel.add(confirmPasswordField, c);

        //  Przyciski
        RoundedButton deleteButton = new RoundedButton("Usuń konto", czerwony, 20);
        RoundedButton cancelButton = new RoundedButton("Anuluj", zielony, 20);
        deleteButton.setPreferredSize(new Dimension(150, 40));
        cancelButton.setPreferredSize(new Dimension(150, 40));

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        buttons.setBackground(tło);
        buttons.add(cancelButton);
        buttons.add(deleteButton);

        c.gridy++;
        c.gridx = 0;
        c.gridwidth = 2;
        panel.add(buttons, c);

        //  Okno
        setContentPane(panel);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(500, 300);
        setLocationRelativeTo(null);
        setResizable(false);
        setVisible(true);


        deleteButton.addActionListener(e -> deleteAccount());

        cancelButton.addActionListener(e -> {
            dispose();
            new UsersMenuPanel();
        });

        usersMenuPanel = null;
    }

    private void deleteAccount() {
        String login = loginField.getText();
        String password = new String(passwordField.getPassword());
        String confirmPassword = new String(confirmPasswordField.getPassword());

        boolean success = DatabaseConnector.deleteUserAccount(login, password, confirmPassword);
        if (success) {
            dispose();
            new LoginPanel();
        } else {
            JOptionPane.showMessageDialog(this, "Nieprawidłowe dane lub konto już usunięte.", "Błąd", JOptionPane.ERROR_MESSAGE);
        }
    }

    private JLabel label(String text, Color fg, Font font) {
        JLabel l = new JLabel(text);
        l.setForeground(fg);
        l.setFont(font);
        return l;
    }

    private void stylujPole(JTextField field, Color bg, Color fg, Font font) {
        field.setBackground(bg);
        field.setForeground(fg);
        field.setCaretColor(fg);
        field.setFont(font);
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(fg.darker()),
                BorderFactory.createEmptyBorder(6, 8, 6, 8)
        ));
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
