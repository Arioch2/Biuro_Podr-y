import Services.DatabaseConnector;

import javax.swing.*;
import java.awt.*;

public class StatisticPanel extends JFrame {
    private final JLabel totalReservationsLabel = new JLabel();
    private final JLabel totalUsersLabel = new JLabel();
    private final JLabel totalRevenueLabel = new JLabel();

    public StatisticPanel() {
        setTitle("Statystyki i raporty");

        //  Styl
        Color tło = new Color(26, 26, 34);
        Color tekst = Color.WHITE;
        Color akcent = new Color(42, 157, 143);
        Font naglowekFont = new Font("SansSerif", Font.BOLD, 22);
        Font wartoscFont = new Font("SansSerif", Font.PLAIN, 18);

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(tło);
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(20, 20, 20, 20);
        c.gridx = 0;
        c.fill = GridBagConstraints.HORIZONTAL;

        //  Nagłówek
        JLabel header = new JLabel("📊 Statystyki systemowe");
        header.setForeground(akcent);
        header.setFont(naglowekFont);
        header.setHorizontalAlignment(SwingConstants.CENTER);
        c.gridy = 0;
        c.gridwidth = 2;
        panel.add(header, c);

        //  Rezerwacje
        c.gridy++;
        totalReservationsLabel.setFont(wartoscFont);
        totalReservationsLabel.setForeground(tekst);
        panel.add(totalReservationsLabel, c);

        //  Użytkownicy
        c.gridy++;
        totalUsersLabel.setFont(wartoscFont);
        totalUsersLabel.setForeground(tekst);
        panel.add(totalUsersLabel, c);

        //  Przychód
        c.gridy++;
        totalRevenueLabel.setFont(wartoscFont);
        totalRevenueLabel.setForeground(tekst);
        panel.add(totalRevenueLabel, c);


        // Okno
        setContentPane(panel);
        pack();
        setMinimumSize(new Dimension(430, 450));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        setVisible(true);




        loadStats();
    }

    private void loadStats() {
        int total = DatabaseConnector.countReservations();
        int users = DatabaseConnector.countUniqueUsers();
        double revenue = DatabaseConnector.sumPaidReservations();

        totalReservationsLabel.setText("🔹 Liczba rezerwacji: " + total);
        totalUsersLabel.setText("🔹 Liczba użytkowników: " + users);
        totalRevenueLabel.setText("🔹 Suma opłaconych rezerwacji: " + String.format("%.2f PLN", revenue));
    }
}
