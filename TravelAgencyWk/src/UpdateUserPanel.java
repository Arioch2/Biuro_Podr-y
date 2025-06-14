import Services.DatabaseConnector;
import Services.Validator;
import Services.RoundedButton;

import javax.swing.*;
import java.awt.*;

public class UpdateUserPanel extends JFrame {
    private final JTextField changeImieField = new JTextField(20);
    private final JTextField changeNazwiskoField = new JTextField(20);
    private final JTextField changeEmailField = new JTextField(20);
    private final JLabel checkLoginField = new JLabel();

    private final String currentLogin;

    public UpdateUserPanel(String login) {
        super("Aktualizacja danych");
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

        // Login info
        checkLoginField.setText("Twój login: " + currentLogin);
        checkLoginField.setForeground(zielony);
        checkLoginField.setFont(new Font("SansSerif", Font.BOLD, 15));
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 2;
        panel.add(checkLoginField, c);

        // Imię
        c.gridy++;
        c.gridwidth = 1;
        panel.add(label("Imię:", tekst, font), c);
        stylujPole(changeImieField, pole, tekst, font);
        c.gridx = 1;
        panel.add(changeImieField, c);

        // Nazwisko
        c.gridy++;
        c.gridx = 0;
        panel.add(label("Nazwisko:", tekst, font), c);
        stylujPole(changeNazwiskoField, pole, tekst, font);
        c.gridx = 1;
        panel.add(changeNazwiskoField, c);

        // Email
        c.gridy++;
        c.gridx = 0;
        panel.add(label("Email:", tekst, font), c);
        stylujPole(changeEmailField, pole, tekst, font);
        c.gridx = 1;
        panel.add(changeEmailField, c);

        // Przyciski
        RoundedButton aktualizujDaneButton = new RoundedButton("Aktualizuj", zielony, 20);
        RoundedButton anulujButton = new RoundedButton("Anuluj", czerwony, 20);
        aktualizujDaneButton.setPreferredSize(new Dimension(150, 40));
        anulujButton.setPreferredSize(new Dimension(150, 40));

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        buttons.setBackground(tło);
        buttons.add(anulujButton);
        buttons.add(aktualizujDaneButton);

        c.gridy++;
        c.gridx = 0;
        c.gridwidth = 2;
        panel.add(buttons, c);

        //  Okno
        setContentPane(panel);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(500, 320);
        setLocationRelativeTo(null);
        setResizable(false);
        setVisible(true);


        anulujButton.addActionListener(e -> {
            dispose();
            new UsersMenuPanel();
        });

        aktualizujDaneButton.addActionListener(e -> {
            String newImie = changeImieField.getText();
            String newNazwisko = changeNazwiskoField.getText();
            String newEmail = changeEmailField.getText();

            if (!Validator.isValidEmail(newEmail)) {
                JOptionPane.showMessageDialog(this, "Podaj poprawny adres e-mail!", "Błąd", JOptionPane.ERROR_MESSAGE);
                return;
            }

            boolean success = DatabaseConnector.updateUser(currentLogin, newImie, newNazwisko, newEmail);
            if (success) {
                JOptionPane.showMessageDialog(this, "Dane zostały zaktualizowane!", "Sukces", JOptionPane.INFORMATION_MESSAGE);
                dispose();
                new UsersMenuPanel();
            } else {
                JOptionPane.showMessageDialog(this, "Błąd aktualizacji danych!", "Błąd", JOptionPane.ERROR_MESSAGE);
            }
        });
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
}
