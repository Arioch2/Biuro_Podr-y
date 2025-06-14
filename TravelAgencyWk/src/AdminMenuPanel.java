import Services.RoundedButton;

import javax.swing.*;
import java.awt.*;

public class AdminMenuPanel extends JFrame {

    public AdminMenuPanel() {
        super("Panel Administratora");

        // Kolory i styl
        Color tło = new Color(26, 26, 34);
        Color tekst = Color.WHITE;
        Color granat = new Color(29, 53, 87);
        Color zielony = new Color(42, 157, 143);
        Color czerwony = new Color(230, 57, 70);
        Font font = new Font("SansSerif", Font.BOLD, 16);

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(tło);
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(15, 0, 15, 0);
        c.gridx = 0;
        c.fill = GridBagConstraints.HORIZONTAL;

        //  Przyciski
        RoundedButton btnUzytkownicy = new RoundedButton("Zarządzanie Użytkownikami", granat, 20);
        RoundedButton btnOferty = new RoundedButton("Zarządzanie Ofertami", granat, 20);
        RoundedButton btnRezerwacje = new RoundedButton("Zarządzanie Rezerwacjami", granat, 20);
        RoundedButton btnStatystyki = new RoundedButton("Statystyki i Raporty", granat, 20);
        RoundedButton btnWyloguj = new RoundedButton("Wyloguj", zielony, 20);
        RoundedButton btnWyjscie = new RoundedButton("Wyjście", czerwony, 20);

        Dimension btnSize = new Dimension(280, 50);
        for (JButton b : new JButton[]{btnUzytkownicy, btnOferty, btnRezerwacje, btnStatystyki, btnWyloguj, btnWyjscie}) {
            b.setPreferredSize(btnSize);
        }


        btnUzytkownicy.addActionListener(e -> {
            dispose();
            new userManagment();
        });

        btnOferty.addActionListener(e -> {
            dispose();
            new OffersManagementPanel();
        });

        btnRezerwacje.addActionListener(e -> {
            dispose();
            new ReservationsManagement();
        });

        btnStatystyki.addActionListener(e -> {
            dispose();
            new StatisticPanel();
        });

        btnWyloguj.addActionListener(e -> {
            dispose();
            new LoginPanel();
        });

        btnWyjscie.addActionListener(e -> dispose());

        //  Dodaje przyciski do panelu
        RoundedButton[] przyciski = {
                btnUzytkownicy,
                btnOferty,
                btnRezerwacje,
                btnStatystyki,
                btnWyloguj,
                btnWyjscie
        };

        for (int i = 0; i < przyciski.length; i++) {
            c.gridy = i;
            panel.add(przyciski[i], c);
        }

        //  Setup
        setContentPane(panel);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(400, 450);
        setLocationRelativeTo(null);
        setResizable(false);
        setVisible(true);
    }
}
