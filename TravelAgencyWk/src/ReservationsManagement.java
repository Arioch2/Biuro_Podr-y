import Services.Reservation;
import Services.DatabaseConnector;
import Services.RoundedButton;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class ReservationsManagement extends JFrame {
    private final DefaultListModel<String> model = new DefaultListModel<>();
    private final JList<String> reservationsList = new JList<>(model);

    public ReservationsManagement() {
        super("Zarządzanie rezerwacjami");

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
        c.insets = new Insets(12, 20, 12, 20);
        c.fill = GridBagConstraints.HORIZONTAL;

        //  Nagłówek
        JLabel label = new JLabel("Lista rezerwacji");
        label.setForeground(tekst);
        label.setFont(new Font("SansSerif", Font.BOLD, 18));
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 2;
        panel.add(label, c);

        //  Lista
        reservationsList.setFont(font);
        reservationsList.setBackground(pole);
        reservationsList.setForeground(tekst);
        reservationsList.setSelectionBackground(granat.darker());
        reservationsList.setSelectionForeground(Color.WHITE);
        JScrollPane scroll = new JScrollPane(reservationsList);
        scroll.setPreferredSize(new Dimension(950, 450));

        c.gridy++;
        c.weightx = 1;
        c.weighty = 1;
        c.fill = GridBagConstraints.BOTH;
        panel.add(scroll, c);

        //  Przyciski
        RoundedButton deleteButton = new RoundedButton("Usuń rezerwację", czerwony, 20);
        RoundedButton paidButton = new RoundedButton("Oznacz jako opłaconą", zielony, 20);
        RoundedButton closeButton = new RoundedButton("Wróć", granat, 20);
        Dimension btnSize = new Dimension(200, 40);
        deleteButton.setPreferredSize(btnSize);
        paidButton.setPreferredSize(btnSize);
        closeButton.setPreferredSize(btnSize);

        JPanel przyciski = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        przyciski.setBackground(tło);
        przyciski.add(deleteButton);
        przyciski.add(paidButton);
        przyciski.add(closeButton);

        c.gridy++;
        c.gridwidth = 2;
        c.weighty = 0;
        c.fill = GridBagConstraints.NONE;
        panel.add(przyciski, c);

        //  Okno
        setContentPane(panel);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setResizable(true);
        setMinimumSize(new Dimension(1300, 850));
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);


        loadReservations();

        deleteButton.addActionListener(e -> deleteReservation());
        paidButton.addActionListener(e -> markReservationAsPaid());
        closeButton.addActionListener(e -> {
            dispose();
            new AdminMenuPanel();
        });
    }

    private void loadReservations() {
        List<Reservation> reservations = DatabaseConnector.getAllReservations();
        model.clear();
        for (Reservation r : reservations) {
            String res = String.format("%s | Użytkownik: %s %s (ID: %d) | Cena: %.2f PLN | Status: %s | Data: %s",
                    r.getOfferName(), r.getFirstName(), r.getLastName(), r.getUserId(),
                    r.getPrice(), r.getPaymentStatus(), r.getReservationDate());
            model.addElement(res);
        }
        reservationsList.revalidate();
        reservationsList.repaint();
    }

    private void deleteReservation() {
        int index = reservationsList.getSelectedIndex();
        if (index == -1) {
            JOptionPane.showMessageDialog(this, "Wybierz rezerwację!", "Błąd", JOptionPane.ERROR_MESSAGE);
            return;
        }
        int id = DatabaseConnector.getAllReservations().get(index).getReservationId();
        DatabaseConnector.cancelReservationById(id);
        loadReservations();
        JOptionPane.showMessageDialog(this, "Rezerwacja została usunięta!", "Sukces", JOptionPane.INFORMATION_MESSAGE);
    }

    private void markReservationAsPaid() {
        int index = reservationsList.getSelectedIndex();
        if (index == -1) {
            JOptionPane.showMessageDialog(this, "Wybierz rezerwację do oznaczenia!", "Błąd", JOptionPane.ERROR_MESSAGE);
            return;
        }
        int offerId = DatabaseConnector.getAllReservations().get(index).getOfferId();
        DatabaseConnector.updatePaymentStatus(offerId, "Opłacona");
        loadReservations();
        JOptionPane.showMessageDialog(this, "Oznaczono jako opłacona!", "Sukces", JOptionPane.INFORMATION_MESSAGE);
    }
}
