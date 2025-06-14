import Services.DatabaseConnector;
import Services.Validator;
import Services.RoundedButton;

import javax.swing.*;
import java.awt.*;

public class RegisterPanel extends JFrame {
    private JTextField ImieField;
    private JTextField NazwiskoField;
    private JTextField loginFieldTextField;
    private JPasswordField passwordField;
    private JPasswordField checkpasswordField;
    private JTextField emailField;
    private RoundedButton zarejestrujSięButton;
    private RoundedButton wyjścieButton;

    public RegisterPanel() {
        super("Rejestracja");

        // Kolory
        Color czerwony = Color.decode("#E63946");
        Color niebieski = Color.decode("#457B9D");
        Color granat = Color.decode("#1D3557");

        // Panel i layout
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(26, 26, 34));
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(6, 16, 6, 16);
        c.fill = GridBagConstraints.NONE;
        c.anchor = GridBagConstraints.CENTER;
        c.gridx = 0;
        c.weightx = 1;

        // Tytuł
        JLabel title = new JLabel("Utwórz konto", SwingConstants.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, 24));
        title.setForeground(czerwony);
        c.gridy = 0;
        panel.add(title, c);

        // Pola
        ImieField = createField(panel, c, "Imię:");
        NazwiskoField = createField(panel, c, "Nazwisko:");
        loginFieldTextField = createField(panel, c, "Login:");
        emailField = createField(panel, c, "Email:");
        passwordField = createPassword(panel, c, "Hasło:");
        checkpasswordField = createPassword(panel, c, "Powtórz hasło:");

        // Przyciski
        zarejestrujSięButton = new RoundedButton("Zarejestruj się", niebieski, 20);
        wyjścieButton = new RoundedButton("Wróć", granat, 20);

        Dimension buttonSize = new Dimension(180, 32);
        zarejestrujSięButton.setPreferredSize(buttonSize);
        wyjścieButton.setPreferredSize(buttonSize);

        c.gridy++;
        panel.add(zarejestrujSięButton, c);
        c.gridy++;
        panel.add(wyjścieButton, c);

        // Okno
        setContentPane(panel);
        pack();
        setMinimumSize(new Dimension(380, 500));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        setVisible(true);


        wyjścieButton.addActionListener(e -> {
            dispose();
            new LoginPanel();
        });

        ImieField.addActionListener(e -> NazwiskoField.requestFocus());
        NazwiskoField.addActionListener(e -> loginFieldTextField.requestFocus());
        loginFieldTextField.addActionListener(e -> emailField.requestFocus());
        emailField.addActionListener(e -> passwordField.requestFocus());
        passwordField.addActionListener(e -> checkpasswordField.requestFocus());
        checkpasswordField.addActionListener(e -> zarejestrujSięButton.doClick());


        zarejestrujSięButton.addActionListener(e -> {



            String imie = ImieField.getText();
            String nazwisko = NazwiskoField.getText();
            String login = loginFieldTextField.getText();
            String email = emailField.getText();
            String password = new String(passwordField.getPassword());
            String checkPassword = new String(checkpasswordField.getPassword());

            if (!Validator.isValidEmail(email)) {
                JOptionPane.showMessageDialog(this, "Podaj poprawny adres e-mail!", "Błąd", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (DatabaseConnector.loginExists(login)) {
                JOptionPane.showMessageDialog(this, "Login już istnieje! Proszę użyć innego loginu.", "Błąd", JOptionPane.ERROR_MESSAGE);
                return;
            }


            if (!password.equals(checkPassword)) {
                JOptionPane.showMessageDialog(this, "Hasła się nie zgadzają!", "Błąd", JOptionPane.ERROR_MESSAGE);
                return;
            }

            boolean success = DatabaseConnector.registerUser(imie, nazwisko, login, password, email);
            if (success) {
                JOptionPane.showMessageDialog(this, "Rejestracja zakończona sukcesem!", "Sukces", JOptionPane.INFORMATION_MESSAGE);
                dispose();
                new LoginPanel();
            } else {
                JOptionPane.showMessageDialog(this, "Błąd rejestracji.", "Błąd", JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    private JTextField createField(JPanel panel, GridBagConstraints c, String label) {
        JLabel l = new JLabel(label);
        l.setFont(new Font("SansSerif", Font.PLAIN, 13));
        l.setForeground(new Color(180, 180, 180));
        c.gridy++;
        panel.add(l, c);

        JTextField field = new JTextField();
        field.setFont(new Font("SansSerif", Font.PLAIN, 13));
        field.setForeground(Color.WHITE);
        field.setCaretColor(Color.WHITE);
        field.setBackground(new Color(36, 36, 46));
        field.setPreferredSize(new Dimension(240, 32));
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(70, 70, 80)),
                BorderFactory.createEmptyBorder(6, 10, 6, 10)
        ));
        c.gridy++;
        panel.add(field, c);

        return field;
    }

    private JPasswordField createPassword(JPanel panel, GridBagConstraints c, String label) {
        JLabel l = new JLabel(label);
        l.setFont(new Font("SansSerif", Font.PLAIN, 13));
        l.setForeground(new Color(180, 180, 180));
        c.gridy++;
        panel.add(l, c);

        JPasswordField field = new JPasswordField();
        field.setFont(new Font("SansSerif", Font.PLAIN, 13));
        field.setForeground(Color.WHITE);
        field.setCaretColor(Color.WHITE);
        field.setBackground(new Color(36, 36, 46));
        field.setPreferredSize(new Dimension(240, 32));
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(70, 70, 80)),
                BorderFactory.createEmptyBorder(6, 10, 6, 10)
        ));
        c.gridy++;
        panel.add(field, c);

        return field;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(RegisterPanel::new);
    }
}
