import Services.Reservation;
import Services.DatabaseConnector;
import Services.RoundedButton;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class ReservationsPanel extends JFrame {
    private final DefaultListModel<String> model = new DefaultListModel<>();
    private final JList<String> reservationsList = new JList<>(model);

    public ReservationsPanel() {
        super("Moje rezerwacje");

        Color tło = new Color(26, 26, 34);
        Color granat = new Color(29, 53, 87);
        Color zielony = new Color(42, 157, 143);
        Color czerwony = new Color(230, 57, 70);

        //  Layout główny
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(tło);
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(16, 16, 16, 16);
        c.gridx = 0;

        //  Nagłówek
        JLabel header = new JLabel("Twoje rezerwacje");
        header.setFont(new Font("SansSerif", Font.BOLD, 24));
        header.setForeground(Color.WHITE);
        c.gridy = 0;
        panel.add(header, c);

        //  Lista
        reservationsList.setFont(new Font("SansSerif", Font.PLAIN, 15));
        reservationsList.setForeground(Color.WHITE);
        reservationsList.setBackground(new Color(36, 36, 46));
        reservationsList.setSelectionBackground(new Color(58, 122, 254));
        reservationsList.setSelectionForeground(Color.WHITE);

        JScrollPane scroll = new JScrollPane(reservationsList);
        scroll.setPreferredSize(new Dimension(700, 280));
        c.gridy++;
        c.fill = GridBagConstraints.BOTH;
        c.weightx = 1;
        c.weighty = 1;
        panel.add(scroll, c);

        //  Przyciski
        RoundedButton payButton = new RoundedButton("Opłać", zielony, 20);
        RoundedButton cancelButton = new RoundedButton("Anuluj", czerwony, 20);
        RoundedButton closeButton = new RoundedButton("Zamknij", granat, 20);

        Dimension size = new Dimension(180, 40);
        payButton.setPreferredSize(size);
        cancelButton.setPreferredSize(size);
        closeButton.setPreferredSize(size);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        buttonPanel.setBackground(tło);
        buttonPanel.add(payButton);
        buttonPanel.add(cancelButton);
        buttonPanel.add(closeButton);

        c.gridy++;
        c.weighty = 0;
        c.fill = GridBagConstraints.NONE;
        panel.add(buttonPanel, c);

        //  Ustawienia okna
        setContentPane(panel);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setMinimumSize(new Dimension(800, 600));
        setLocationRelativeTo(null);
        setVisible(true);



        loadReservations();

        cancelButton.addActionListener(e -> cancelReservation());
        payButton.addActionListener(e -> {
            int offerId = getSelectedOfferId();
            if (offerId == -1) {
                JOptionPane.showMessageDialog(this, "Wybierz rezerwację do opłacenia!", "Błąd", JOptionPane.ERROR_MESSAGE);
                return;
            }

            boolean success = DatabaseConnector.simulatePayment(DatabaseConnector.getCurrentUserId(), offerId, 199.99);
            if (success) {
                JOptionPane.showMessageDialog(this, "Rezerwacja została opłacona!", "Sukces", JOptionPane.INFORMATION_MESSAGE);
                loadReservations();
            } else {
                JOptionPane.showMessageDialog(this, "Błąd podczas płatności!", "Błąd", JOptionPane.ERROR_MESSAGE);
            }
        });

        closeButton.addActionListener(e -> {
            dispose();
            new UsersMenuPanel();
        });
    }

    private int getSelectedOfferId() {
        int selectedIndex = reservationsList.getSelectedIndex();
        if (selectedIndex == -1) return -1;

        return DatabaseConnector.getUserReservations(DatabaseConnector.getCurrentUserId())
                .get(selectedIndex).getOfferId();
    }

    private int getSelectedReservationId() {
        int selectedIndex = reservationsList.getSelectedIndex();
        if (selectedIndex == -1) return -1;

        return DatabaseConnector.getUserReservations(DatabaseConnector.getCurrentUserId())
                .get(selectedIndex).getId();
    }

    private void cancelReservation() {
        int resId = getSelectedReservationId();
        if (resId == -1) {
            JOptionPane.showMessageDialog(this, "Nie wybrano rezerwacji!", "Błąd", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, "Na pewno anulować rezerwację?", "Potwierdzenie", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            DatabaseConnector.cancelReservationById(resId);
            loadReservations();
            JOptionPane.showMessageDialog(this, "Rezerwacja anulowana!", "Sukces", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void loadReservations() {
        List<Reservation> reservations = DatabaseConnector.getUserReservations(DatabaseConnector.getCurrentUserId());
        model.clear();
        for (Reservation r : reservations) {
            model.addElement(String.format(
                    "%s | %s | Cena: %.2f PLN | Status: %s",
                    r.getOfferName(), r.getOfferDescription(), r.getPrice(), r.getPaymentStatus()
            ));
        }
    }
}
