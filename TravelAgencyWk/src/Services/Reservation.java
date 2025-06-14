package Services;
import java.sql.Date;
import java.time.LocalDate;

public class Reservation {
    private int reservationId;
    private int offerId;
    private int userId;
    private String offerName;
    private String offerDescription;
    private java.sql.Date reservationDate;
    private double price;
    private String paymentStatus;
    private String firstName;
    private String lastName;


    public Reservation(int reservationId, int offerId, int userId, String firstName, String lastName,
                       String offerName, String offerDescription, Date reservationDate, double price, String paymentStatus) {
        this.reservationId = reservationId;
        this.offerId = offerId;
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.offerName = offerName;
        this.offerDescription = offerDescription;
        this.reservationDate = reservationDate;
        this.price = price;
        this.paymentStatus = paymentStatus;
    }

    public int getReservationId() { return reservationId; }

    public String getPaymentStatus() { return paymentStatus; }


    public int getOfferId() {
        return offerId;
    }
    public String getOfferName() { return offerName; }
    public LocalDate getReservationDate() {
        return reservationDate.toLocalDate();
        }

    public String getOfferDescription() {return offerDescription;}

    public double getPrice() { return price; }

    public int getUserId() {
        return userId;
    }

    public String getFirstName() { return firstName; }

    public String getLastName() { return lastName; }

    public int getId() {
        return reservationId;
    }



    @Override
    public String toString() {
        return offerName + " (Zarezerwowano: " + reservationDate + ", Status: " + paymentStatus + ")";
    }



}
