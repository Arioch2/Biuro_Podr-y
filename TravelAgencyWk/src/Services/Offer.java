package Services;

import java.sql.Date;
import java.math.BigDecimal;
import java.time.LocalDate;

public class Offer {
    private int id;
    private String name;
    private String description;
    private BigDecimal price;
    private String imageUrl;
    private LocalDate startDate;
    private LocalDate endDate;


    public Offer(int id, String name, String description, BigDecimal price, String imageUrl, LocalDate startDate, LocalDate endDate) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.imageUrl = imageUrl;
        this.startDate = startDate;
        this.endDate = endDate;


    }

    public Offer(String name, String description, BigDecimal price) {
        this.name = name;
        this.description = description;
        this.price = price;
    }


    public int getId() { return id; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public LocalDate getStartDate() { return startDate; }
    public LocalDate getEndDate() { return endDate; }
    public BigDecimal getPrice() {return price;}


    @Override
    public String toString() {
        return name + " (" + startDate + " - " + endDate + ")\n" + description;
    }
}
