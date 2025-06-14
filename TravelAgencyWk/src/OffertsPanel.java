import Services.DatabaseConnector;
import Services.Offer;
import Services.RoundedButton;
import com.github.lgooddatepicker.components.DatePicker;

import javax.swing.*;
import java.awt.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class OffertsPanel extends JFrame {
    private JList<String> offersList;
    private DefaultListModel<String> model;
    private List<Offer> allOffers;
    private DatePicker startDatePickerField;
    private DatePicker endDatePickerField;
    private JTextField minPriceField = new JTextField(6);
    private JTextField maxPriceField = new JTextField(6);


    public OffertsPanel() {
        super("Dostępne oferty");

        //  Kolory
        Color granat = Color.decode("#1D3557");
        Color niebieski = Color.decode("#3A86FF");
        Color zielony = Color.decode("#2A9D8F");

        //  Layout
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(26, 26, 34));
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(8, 16, 8, 16);
        c.gridx = 0;
        c.anchor = GridBagConstraints.CENTER;
        c.fill = GridBagConstraints.NONE;

        //  Nagłówek
        JLabel label = new JLabel("Dostępne oferty");
        label.setFont(new Font("SansSerif", Font.BOLD, 24));
        label.setForeground(niebieski);
        c.gridy = 0;
        panel.add(label, c);


        //  Panel dat
        JPanel datePanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 16, 0));
        datePanel.setBackground(new Color(26, 26, 34));
        startDatePickerField = new DatePicker();
        endDatePickerField = new DatePicker();

        datePanel.add(new JLabel("Od:"));
        datePanel.add(startDatePickerField);
        datePanel.add(new JLabel("Do:"));
        datePanel.add(endDatePickerField);
        c.gridy++;
        panel.add(datePanel, c);

        datePanel.add(new JLabel("Cena od:"));
        datePanel.add(minPriceField);
        datePanel.add(new JLabel("do:"));
        datePanel.add(maxPriceField);

        minPriceField.setText("0");
        maxPriceField.setText("99999");


        //  Przycisk wyszukiwania
        RoundedButton searchButton = new RoundedButton("Szukaj", niebieski, 20);
        searchButton.setPreferredSize(new Dimension(200, 40));
        c.gridy++;
        panel.add(searchButton, c);

        RoundedButton clearFiltersButton = new RoundedButton("Wyczyść filtry", Color.GRAY, 20);
        clearFiltersButton.setPreferredSize(new Dimension(200, 40));
        c.gridy++;
        panel.add(clearFiltersButton, c);


        //  Lista ofert
        model = new DefaultListModel<>();
        offersList = new JList<>(model);
        offersList.setFont(new Font("SansSerif", Font.BOLD, 15));
        offersList.setFixedCellHeight(28);
        offersList.setForeground(Color.WHITE);
        offersList.setBackground(new Color(36, 36, 46));
        offersList.setSelectionBackground(niebieski.darker());

        JScrollPane scrollPane = new JScrollPane(offersList);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        scrollPane.setPreferredSize(new Dimension(680, 340));
        scrollPane.setBorder(BorderFactory.createLineBorder(niebieski.darker()));
        c.gridy++;
        c.fill = GridBagConstraints.BOTH;
        c.weightx = 1;
        c.weighty = 1;
        panel.add(scrollPane, c);

        c.fill = GridBagConstraints.NONE;
        c.weighty = 0;

        //  Przyciski akcji
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        btnPanel.setBackground(new Color(26, 26, 34));
        RoundedButton zarezerwujButton = new RoundedButton("Zarezerwuj", zielony, 20);
        RoundedButton zamknijButton = new RoundedButton("Zamknij", granat, 20);
        zarezerwujButton.setPreferredSize(new Dimension(200, 40));
        zamknijButton.setPreferredSize(new Dimension(200, 40));
        btnPanel.add(zarezerwujButton);
        btnPanel.add(zamknijButton);
        c.gridy++;
        panel.add(btnPanel, c);

        //  Setup
        setContentPane(panel);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH); // pełny ekran na start
        setMinimumSize(new Dimension(800, 600)); // ale można zmniejszyć
        setLocationRelativeTo(null);
        setVisible(true);


        searchButton.addActionListener(e -> filterOffersByDate());
        zarezerwujButton.addActionListener(e -> reserveOffer());
        zamknijButton.addActionListener(e -> {
            dispose();
            new UsersMenuPanel();
        });

        clearFiltersButton.addActionListener(e -> {
            startDatePickerField.clear();
            endDatePickerField.clear();
            minPriceField.setText("0");
            maxPriceField.setText("99999");
            loadOffers();
        });


        loadOffers();
    }

    private void loadOffers() {
        allOffers = DatabaseConnector.getAllOffers();
        model.clear();
        allOffers.forEach(offer -> {
            String formatted = String.format("%s | %s | %s - %s | Cena: %.2f PLN",
                    offer.getName(), offer.getDescription(), offer.getStartDate(), offer.getEndDate(), offer.getPrice());
            model.addElement(formatted);
        });
    }

    private void filterOffersByDate() {


        LocalDate start = startDatePickerField.getDate();
        LocalDate end = endDatePickerField.getDate();
        boolean filterByDate = (start != null && end != null);

        BigDecimal min;
        BigDecimal max;

        try {
            String minText = minPriceField.getText().trim();
            String maxText = maxPriceField.getText().trim();
            min = minText.isEmpty() ? BigDecimal.ZERO : new BigDecimal(minText);
            max = maxText.isEmpty() ? BigDecimal.valueOf(Double.MAX_VALUE) : new BigDecimal(maxText);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Nieprawidłowy format ceny!", "Błąd", JOptionPane.ERROR_MESSAGE);
            return;
        }

        final BigDecimal finalMin = min;
        final BigDecimal finalMax = max;


        List<Offer> filtered = allOffers.stream()
                .filter(o -> !filterByDate || (!o.getStartDate().isBefore(start) && !o.getEndDate().isAfter(end)))
                .filter(o -> o.getPrice().compareTo(finalMin) >= 0 && o.getPrice().compareTo(finalMax) <= 0)
                .toList();



        model.clear();
        if (filtered.isEmpty()) {
            model.addElement("Brak ofert w wybranym zakresie dat.");
        } else {
            filtered.forEach(o -> model.addElement(
                    String.format("%s | %s | %s - %s | Cena: %.2f PLN",
                            o.getName(), o.getDescription(), o.getStartDate(), o.getEndDate(), o.getPrice())));
        }
    }

    private void reserveOffer() {
        String selectedText = offersList.getSelectedValue();
        if (selectedText == null) {
            JOptionPane.showMessageDialog(this, "Wybierz ofertę przed rezerwacją!", "Błąd", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Offer selected = allOffers.stream()
                .filter(o -> selectedText.contains(o.getName()))
                .findFirst()
                .orElse(null);

        if (selected != null) {
            int userId = DatabaseConnector.getCurrentUserId();
            DatabaseConnector.reserveOffer(userId, selected.getId());

            JOptionPane.showMessageDialog(this,
                    "Zarezerwowano: " + selected.getName(),
                    "Sukces", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(OffertsPanel::new);
    }
}
