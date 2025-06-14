import Services.DatabaseConnector;
import Services.Offer;
import Services.RoundedButton;

import javax.swing.*;
import java.awt.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;

public class OffersManagementPanel extends JFrame {
    private final DefaultListModel<String> model = new DefaultListModel<>();
    private final JList<String> offersList = new JList<>(model);

    public OffersManagementPanel() {
        super("Zarządzanie ofertami");

        //  Kolory i styl
        Color tło = new Color(26, 26, 34);
        Color pole = new Color(36, 36, 46);
        Color tekst = Color.WHITE;
        Color granat = new Color(29, 53, 87);
        Color zielony = new Color(42, 157, 143);
        Color czerwony = new Color(230, 57, 70);
        Font font = new Font("SansSerif", Font.PLAIN, 14);

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(tło);
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(16, 16, 16, 16);
        c.fill = GridBagConstraints.HORIZONTAL;

        //  Nagłówek
        JLabel naglowek = new JLabel("Lista ofert");
        naglowek.setForeground(tekst);
        naglowek.setFont(new Font("SansSerif", Font.BOLD, 20));
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 2;
        panel.add(naglowek, c);

        //  Lista ofert
        offersList.setFont(font);
        offersList.setBackground(pole);
        offersList.setForeground(tekst);
        offersList.setSelectionBackground(zielony.darker());
        offersList.setSelectionForeground(Color.WHITE);
        JScrollPane scrollPane = new JScrollPane(offersList);
        scrollPane.setPreferredSize(new Dimension(900, 400));

        c.gridy++;
        c.gridwidth = 2;
        c.weightx = 1;
        c.weighty = 1;
        c.fill = GridBagConstraints.BOTH;
        panel.add(scrollPane, c);

        //  Przyciski
        RoundedButton addBtn = new RoundedButton("Dodaj ofertę", zielony, 20);
        RoundedButton editBtn = new RoundedButton("Edytuj ofertę", granat, 20);
        RoundedButton deleteBtn = new RoundedButton("Usuń ofertę", czerwony, 20);
        RoundedButton exitBtn = new RoundedButton("Wróć", granat, 20);

        Dimension btnSize = new Dimension(180, 40);
        for (JButton b : new JButton[]{addBtn, editBtn, deleteBtn, exitBtn}) {
            b.setPreferredSize(btnSize);
        }

        JPanel przyciski = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        przyciski.setBackground(tło);
        przyciski.add(addBtn);
        przyciski.add(editBtn);
        przyciski.add(deleteBtn);
        przyciski.add(exitBtn);

        c.gridy++;
        c.gridwidth = 2;
        c.weighty = 0;
        c.fill = GridBagConstraints.NONE;
        panel.add(przyciski, c);

        //  Okno
        setContentPane(panel);
        setExtendedState(JFrame.MAXIMIZED_BOTH); // ⬛ domyślnie fullscreen
        setResizable(true);                       // 🔁 skalowalne
        setMinimumSize(new Dimension(1500, 800));  // 📏 minimalny rozmiar
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);


        addBtn.addActionListener(e -> addOffer());
        editBtn.addActionListener(e -> editOffer());
        deleteBtn.addActionListener(e -> deleteOffer());
        exitBtn.addActionListener(e -> {
            dispose();
            new AdminMenuPanel();
        });

        loadOffers();
    }

    private void loadOffers() {
        model.clear();
        List<Offer> offers = DatabaseConnector.getAllOffers();
        offers.forEach(o -> model.addElement(String.format(
                "%s | %s | %s - %s | Cena: %.2f PLN",
                o.getName(), o.getDescription(), o.getStartDate(), o.getEndDate(), o.getPrice()
        )));
    }

    private void addOffer() {
        String name = JOptionPane.showInputDialog("Podaj nazwę nowej oferty:");
        String description = JOptionPane.showInputDialog("Podaj opis oferty:");
        String priceStr = JOptionPane.showInputDialog("Podaj cenę oferty:");
        String start = JOptionPane.showInputDialog("Data rozpoczęcia (YYYY-MM-DD):");
        String end = JOptionPane.showInputDialog("Data zakończenia (YYYY-MM-DD):");

        if (name != null && description != null && priceStr != null && start != null && end != null) {
            try {
                BigDecimal price = new BigDecimal(priceStr);
                LocalDate startDate = LocalDate.parse(start);
                LocalDate endDate = LocalDate.parse(end);
                DatabaseConnector.addOffer(name, description, price, startDate, endDate);
                JOptionPane.showMessageDialog(this, "Oferta dodana!", "Sukces", JOptionPane.INFORMATION_MESSAGE);
                loadOffers();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Nieprawidłowe dane wejściowe!", "Błąd", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void editOffer() {
        String selected = offersList.getSelectedValue();
        if (selected == null) {
            JOptionPane.showMessageDialog(this, "Wybierz ofertę do edycji!", "Błąd", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String oldName = selected.split(" \\| ")[0];
        Offer offer = DatabaseConnector.getOfferByName(oldName);
        if (offer == null) {
            JOptionPane.showMessageDialog(this, "Nie znaleziono oferty!", "Błąd", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String newName = JOptionPane.showInputDialog("Nowa nazwa:", offer.getName());
        String newDesc = JOptionPane.showInputDialog("Nowy opis:", offer.getDescription());
        String newPriceStr = JOptionPane.showInputDialog("Nowa cena:", offer.getPrice().toString());
        String newStart = JOptionPane.showInputDialog("Nowa data rozpoczęcia (YYYY-MM-DD):", offer.getStartDate().toString());
        String newEnd = JOptionPane.showInputDialog("Nowa data zakończenia (YYYY-MM-DD):", offer.getEndDate().toString());

        if (newName != null && newDesc != null && newPriceStr != null && newStart != null && newEnd != null) {
            try {
                BigDecimal newPrice = new BigDecimal(newPriceStr);
                LocalDate startDate = LocalDate.parse(newStart);
                LocalDate endDate = LocalDate.parse(newEnd);
                DatabaseConnector.updateOffer(oldName, newName, newDesc, newPrice, startDate, endDate);
                JOptionPane.showMessageDialog(this, "Oferta zaktualizowana!", "Sukces", JOptionPane.INFORMATION_MESSAGE);
                loadOffers();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Nieprawidłowe dane!", "Błąd", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void deleteOffer() {
        String selected = offersList.getSelectedValue();
        if (selected == null) {
            JOptionPane.showMessageDialog(this, "Wybierz ofertę do usunięcia!", "Błąd", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String offerName = selected.split(" \\| ")[0];
        int confirm = JOptionPane.showConfirmDialog(this, "Czy na pewno chcesz usunąć ofertę?", "Potwierdzenie", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            DatabaseConnector.deleteOffer(offerName);
            JOptionPane.showMessageDialog(this, "Oferta usunięta!", "Sukces", JOptionPane.INFORMATION_MESSAGE);
            loadOffers();
        }
    }
}
