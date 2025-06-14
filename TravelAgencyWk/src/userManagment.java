import Services.DatabaseConnector;
import Services.RoundedButton;
import Services.User;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.stream.Collectors;

public class userManagment extends JFrame {
    private final DefaultListModel<String> model = new DefaultListModel<>();
    private final JList<String> usersList = new JList<>(model);
    private final JCheckBox adminOnlyBox = new JCheckBox("Tylko administratorzy");
    private final JTextField emailSearchField = new JTextField(20);


    public userManagment() {
        super("Panel administratora");

        //  Styl
        Color tło = new Color(26, 26, 34);
        Color pole = new Color(36, 36, 46);
        Color tekst = Color.WHITE;
        Color zielony = new Color(42, 157, 143);
        Color czerwony = new Color(230, 57, 70);
        Color granat = new Color(29, 53, 87);
        Font font = new Font("SansSerif", Font.PLAIN, 14);

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(tło);
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(10, 20, 10, 20);
        c.fill = GridBagConstraints.HORIZONTAL;

        //  Nagłówek
        JLabel naglowek = new JLabel("Lista użytkowników");
        naglowek.setForeground(granat);
        naglowek.setFont(new Font("SansSerif", Font.BOLD, 18));
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 2;
        panel.add(naglowek, c);

        //  Checkbox filtru
        adminOnlyBox.setForeground(tekst);
        adminOnlyBox.setBackground(tło);
        adminOnlyBox.setFont(font);
        JPanel filtrPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        filtrPanel.setBackground(tło);
        filtrPanel.add(adminOnlyBox);

        emailSearchField.setFont(font);
        emailSearchField.setBackground(pole);
        emailSearchField.setForeground(tekst);
        emailSearchField.setCaretColor(tekst);
        emailSearchField.setBorder(BorderFactory.createLineBorder(granat.darker()));
        emailSearchField.setToolTipText("Szukaj po e-mailu");
        filtrPanel.add(new JLabel("Email:")).setForeground(tekst);
        filtrPanel.add(emailSearchField);

        emailSearchField.addCaretListener(e -> loadUsers());
        adminOnlyBox.addActionListener(e -> loadUsers());

        c.gridy++;
        panel.add(filtrPanel, c);
        adminOnlyBox.addActionListener(e -> loadUsers());

        //  Lista
        usersList.setBackground(pole);
        usersList.setForeground(tekst);
        usersList.setFont(font);
        usersList.setSelectionBackground(granat.darker());
        usersList.setSelectionForeground(Color.WHITE);
        JScrollPane scrollPane = new JScrollPane(usersList);
        scrollPane.setPreferredSize(new Dimension(850, 400));

        c.gridy++;
        c.weightx = 1;
        c.weighty = 1;
        c.fill = GridBagConstraints.BOTH;
        panel.add(scrollPane, c);

        //  Przyciski
        RoundedButton addUserButton = new RoundedButton("Dodaj użytkownika", zielony, 20);
        RoundedButton setAdminButton = new RoundedButton("Nadaj admina", granat, 20);
        RoundedButton removeAdminButton = new RoundedButton("Usuń admina", granat, 20);
        RoundedButton deleteUserButton = new RoundedButton("Usuń użytkownika", czerwony, 20);
        RoundedButton exitButton = new RoundedButton("Wróć", zielony.darker(), 20);

        Dimension btnSize = new Dimension(180, 40);
        for (JButton b : new JButton[]{addUserButton, setAdminButton, removeAdminButton, deleteUserButton, exitButton}) {
            b.setPreferredSize(btnSize);
        }

        JPanel przyciski = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        przyciski.setBackground(tło);
        przyciski.add(addUserButton);
        przyciski.add(setAdminButton);
        przyciski.add(removeAdminButton);
        przyciski.add(deleteUserButton);
        przyciski.add(exitButton);

        c.gridy++;
        c.fill = GridBagConstraints.NONE;
        c.weighty = 0;
        panel.add(przyciski, c);

        //  Okno
        setContentPane(panel);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setResizable(true);
        setMinimumSize(new Dimension(1200, 700));
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);


        loadUsers();

        addUserButton.addActionListener(e -> addUser());
        setAdminButton.addActionListener(e -> changeAdminStatus(true));
        removeAdminButton.addActionListener(e -> changeAdminStatus(false));
        deleteUserButton.addActionListener(e -> deleteUser());
        exitButton.addActionListener(e -> {
            dispose();
            new AdminMenuPanel();
        });
    }

    private void loadUsers() {
        List<User> users = DatabaseConnector.getAllUserObjects();

        if (adminOnlyBox.isSelected()) {
            users = users.stream()
                    .filter(User::isAdmin)
                    .collect(Collectors.toList());
        }

        String mailFragment = emailSearchField.getText().trim().toLowerCase();
        if (!mailFragment.isEmpty()) {
            users = users.stream()
                    .filter(u -> u.getEmail().toLowerCase().contains(mailFragment))
                    .collect(Collectors.toList());
        }

        model.clear();
        for (User user : users) {
            String line = String.format(
                    "login: %s | %s %s | %s | Admin: %s",
                    user.getLogin(),
                    user.getFirstName(),
                    user.getLastName(),
                    user.getEmail(),
                    user.isAdmin() ? "✅" : "❌"
            );
            model.addElement(line);
        }
    }

    private void changeAdminStatus(boolean isAdmin) {
        String selected = usersList.getSelectedValue();
        if (selected == null) {
            JOptionPane.showMessageDialog(this, "Wybierz użytkownika!", "Błąd", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String login = selected.split("\\|")[0].replace("login:", "").trim();
        DatabaseConnector.updateAdminStatus(login, isAdmin);
        JOptionPane.showMessageDialog(this, "Zmieniono uprawnienia administratora!", "Sukces", JOptionPane.INFORMATION_MESSAGE);
        loadUsers();
    }

    private void deleteUser() {
        String selected = usersList.getSelectedValue();
        if (selected == null) {
            JOptionPane.showMessageDialog(this, "Wybierz użytkownika do usunięcia!", "Błąd", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String login = selected.split("\\|")[0].replace("login:", "").trim();
        int confirm = JOptionPane.showConfirmDialog(this, "Na pewno usunąć użytkownika?", "Potwierdzenie", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            DatabaseConnector.deleteUser(login);
            JOptionPane.showMessageDialog(this, "Użytkownik usunięty!", "Sukces", JOptionPane.INFORMATION_MESSAGE);
            loadUsers();
        }
    }

    private void addUser() {
        String firstName = JOptionPane.showInputDialog("Imię:");
        String lastName = JOptionPane.showInputDialog("Nazwisko:");
        String login = JOptionPane.showInputDialog("Login:");
        String email = JOptionPane.showInputDialog("E-mail:");
        String password = JOptionPane.showInputDialog("Hasło:");
        boolean isAdmin = JOptionPane.showConfirmDialog(this, "Czy użytkownik ma być adminem?", "Uprawnienia", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION;

        if (firstName != null && lastName != null && login != null && email != null && password != null) {
            if (DatabaseConnector.loginExists(login)) {
                JOptionPane.showMessageDialog(this, "Login już istnieje! Użyj innego loginu.", "Błąd", JOptionPane.ERROR_MESSAGE);
                return;
            }

            DatabaseConnector.addUser(firstName, lastName, login, email, password, isAdmin);
            JOptionPane.showMessageDialog(this, "Użytkownik dodany!", "Sukces", JOptionPane.INFORMATION_MESSAGE);
            loadUsers();
        }
    }
}
