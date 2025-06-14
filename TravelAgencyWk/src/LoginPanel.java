import Services.DatabaseConnector;
import Services.RoundedButton;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginPanel extends JFrame {
    private JTextField LoginInput;
    private JPasswordField PasswordInput;
    private JButton zamknjiButton;
    private JButton zalogujButton;
    private JButton rejestracjaButton;

    public LoginPanel() {
        super("Logowanie");

        //  Tło i layout
        JPanel panel = new JPanel();
        panel.setBackground(new Color(26, 26, 34));
        panel.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(8, 12, 8, 12);
        c.gridx = 0;
        c.weightx = 1;

        //  Logo
        JLabel title = new JLabel("TravelAgencyWk", SwingConstants.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, 24));
        title.setForeground(new Color(255, 80, 80));
        c.gridy = 0;
        panel.add(title, c);

        //  Login
        JLabel loginLabel = new JLabel("Login:");
        stylizujLabel(loginLabel);
        c.fill = GridBagConstraints.NONE;
        c.anchor = GridBagConstraints.CENTER;
        c.gridy++;
        panel.add(loginLabel, c);


        LoginInput = new JTextField();
        stylizujPole(LoginInput);
        c.gridy++;
        panel.add(LoginInput, c);
        LoginInput.setPreferredSize(new Dimension(240, 32));

        //  Hasło
        JLabel passLabel = new JLabel("Hasło:");
        stylizujLabel(passLabel);
        c.fill = GridBagConstraints.NONE;
        c.anchor = GridBagConstraints.CENTER;
        c.gridy++;
        panel.add(passLabel, c);


        PasswordInput = new JPasswordField();
        stylizujPole(PasswordInput);
        c.gridy++;
        panel.add(PasswordInput, c);
        PasswordInput.setPreferredSize(new Dimension(240, 32));

        //  Przyciski
        RoundedButton zalogujButton = new RoundedButton("Zaloguj", Color.decode("#E63946"), 20);
        RoundedButton rejestracjaButton = new RoundedButton("Rejestracja", Color.decode("#457B9D"), 20);
        RoundedButton zamknjiButton = new RoundedButton("Zamknij", Color.decode("#1D3557"), 20);



        zalogujButton.setPreferredSize(new Dimension(180, 32));
        zalogujButton.setMaximumSize(new Dimension(180, 32));
        zalogujButton.setMinimumSize(new Dimension(180, 32));

        c.fill = GridBagConstraints.NONE;
        c.anchor = GridBagConstraints.CENTER;
        c.gridy++;
        panel.add(zalogujButton, c);


        rejestracjaButton.setPreferredSize(new Dimension(180, 32));
        rejestracjaButton.setMaximumSize(new Dimension(180, 32));
        rejestracjaButton.setMinimumSize(new Dimension(180, 32));

        c.fill = GridBagConstraints.NONE;
        c.anchor = GridBagConstraints.CENTER;
        c.gridy++;
        panel.add(rejestracjaButton, c);


        zamknjiButton.setPreferredSize(new Dimension(180, 32));
        zamknjiButton.setMaximumSize(new Dimension(180, 32));
        zamknjiButton.setMinimumSize(new Dimension(180, 32));

        c.fill = GridBagConstraints.NONE;
        c.anchor = GridBagConstraints.CENTER;
        c.gridy++;
        panel.add(zamknjiButton, c);


        //  Okno
        setContentPane(panel);
        pack();
        setMinimumSize(new Dimension(380, 420));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        setVisible(true);


        zamknjiButton.addActionListener(e -> dispose());

        zalogujButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String login = LoginInput.getText();
                String password = new String(PasswordInput.getPassword());

                boolean success = DatabaseConnector.loginUser(login, password);
                if (success) {
                    DatabaseConnector.setCurrentUserLogin(login);
                    JOptionPane.showMessageDialog(null, "Logowanie udane!", "Sukces", JOptionPane.INFORMATION_MESSAGE);
                    dispose();
                    if (DatabaseConnector.isUserAdmin(login)) {
                        new AdminMenuPanel();
                    } else {
                        new UsersMenuPanel();
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Niepoprawny login lub hasło.", "Błąd", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        rejestracjaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                new RegisterPanel();
            }
        });

        LoginInput.addActionListener(e -> PasswordInput.requestFocus());
        PasswordInput.addActionListener(e -> zalogujButton.doClick());
    }

    private void stylizLabelFont(JLabel label) {
        label.setFont(new Font("SansSerif", Font.PLAIN, 13));
        label.setForeground(new Color(180, 180, 180));
    }

    private void stylizujLabel(JLabel label) {
        stylizLabelFont(label);
        label.setBorder(BorderFactory.createEmptyBorder(0, 2, 2, 0));
    }

    private void stylizujPole(JTextField field) {
        field.setFont(new Font("SansSerif", Font.PLAIN, 13));
        field.setForeground(Color.WHITE);
        field.setCaretColor(Color.WHITE);
        field.setBackground(new Color(36, 36, 46));
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(70, 70, 80)),
                BorderFactory.createEmptyBorder(6, 10, 6, 10)
        ));
    }

    private void stylizujPrzycisk(JButton btn, Color kolor) {
        btn.setFont(new Font("SansSerif", Font.PLAIN, 12));
        btn.setForeground(Color.WHITE);
        btn.setBackground(kolor);
        btn.setFocusPainted(false);
        btn.setContentAreaFilled(false);
        btn.setOpaque(true);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setBorder(BorderFactory.createEmptyBorder(6, 16, 6, 16));
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(LoginPanel::new);
    }
}
