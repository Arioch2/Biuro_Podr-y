import Services.DatabaseConnector;
import Services.RoundedButton;

import javax.swing.*;
import java.awt.*;

public class UsersMenuPanel extends JFrame {
    private RoundedButton ofertyButton;
    private RoundedButton mojeRezerwacjeButton;
    private RoundedButton mojeDaneButton;
    private RoundedButton wylogujButton;
    private RoundedButton wyjścieButton;
    private RoundedButton zmieńHasłoButton;
    private RoundedButton usuńKontoButton;

    public UsersMenuPanel() {
        super("Panel użytkownika");

        // Kolory i styl
        Color czerwony = Color.decode("#E63946");
        Color niebieski = Color.decode("#3A86FF");
        Color granat = Color.decode("#1D3557");

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(26, 26, 34));
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(10, 16, 10, 16);
        c.gridx = 0;
        c.anchor = GridBagConstraints.CENTER;

        // Nagłówek
        JLabel label = new JLabel("Menu użytkownika");
        label.setFont(new Font("SansSerif", Font.BOLD, 24));
        label.setForeground(czerwony);
        c.gridy = 0;
        panel.add(label, c);

        // Przyciski
        ofertyButton = createButton("Zobacz oferty", niebieski);
        mojeRezerwacjeButton = createButton("Moje rezerwacje", niebieski);
        mojeDaneButton = createButton("Moje dane", niebieski);
        zmieńHasłoButton = createButton("Zmień hasło", niebieski);
        usuńKontoButton = createButton("Usuń konto", czerwony);
        wylogujButton = createButton("Wyloguj", granat);
        wyjścieButton = createButton("Wyjście", granat);

        RoundedButton[] guziki = {
                ofertyButton, mojeRezerwacjeButton, mojeDaneButton,
                zmieńHasłoButton, usuńKontoButton, wylogujButton, wyjścieButton
        };

        for (RoundedButton btn : guziki) {
            c.gridy++;
            panel.add(btn, c);
        }

        // Tło i okno
        setContentPane(panel);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(380, 560));
        setLocationRelativeTo(null);
        setResizable(false);
        setVisible(true);


        wylogujButton.addActionListener(e -> {
            dispose();
            new LoginPanel();
        });

        wyjścieButton.addActionListener(e -> dispose());

        mojeDaneButton.addActionListener(e -> {
            String login = DatabaseConnector.getCurrentUserLogin();
            new UpdateUserPanel(login);
            dispose();
        });

        zmieńHasłoButton.addActionListener(e -> {
            String login = DatabaseConnector.getCurrentUserLogin();
            new ChangePasswordPanel(login);
            dispose();
        });

        ofertyButton.addActionListener(e -> {
            new OffertsPanel();
            dispose();
        });

        mojeRezerwacjeButton.addActionListener(e -> {
            new ReservationsPanel();
            dispose();
        });

        usuńKontoButton.addActionListener(e -> {
            new DeleteAccountPanel();
            dispose();
        });
    }

    private RoundedButton createButton(String text, Color color) {
        RoundedButton btn = new RoundedButton(text, color, 20);
        btn.setPreferredSize(new Dimension(220, 34));
        return btn;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(UsersMenuPanel::new);
    }
}
