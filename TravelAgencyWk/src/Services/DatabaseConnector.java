package Services;
import javax.swing.JOptionPane;
import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class DatabaseConnector {
    private static final String URL = "jdbc:postgresql://localhost:5432/postgres";
    private static final String USER = "postgres";
    private static final String PASSWORD = "qwerty";
    private static String currentUserLogin; // Przechowywanie loginu

    public static Connection connect() {
        try {
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static boolean registerUser(String imie, String nazwisko, String login, String password, String email) {
        String sql = "INSERT INTO users (imie, nazwisko, login, password, email) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, imie);
            stmt.setString(2, nazwisko);
            stmt.setString(3, login);
            stmt.setString(4, password);
            stmt.setString(5, email);

            int rowsInserted = stmt.executeUpdate();
            return rowsInserted > 0;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public static boolean loginUser(String login, String password) {
        String sql = "SELECT * FROM users WHERE login = ? AND password = ?";

        try (Connection conn = connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, login);
            stmt.setString(2, password);

            ResultSet rs = stmt.executeQuery();
            boolean loginSuccess = rs.next();
            if (loginSuccess) {
                setCurrentUserLogin(login);
            }
            return loginSuccess;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public static boolean updateUser(String currentLogin, String newImie, String newNazwisko, String newEmail) {
        String sql = "UPDATE users SET imie = ?, nazwisko = ?, email = ? WHERE login = ?";

        try (Connection conn = connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, newImie);
            stmt.setString(2, newNazwisko);
            stmt.setString(3, newEmail);
            stmt.setString(4, currentLogin);

            int rowsUpdated = stmt.executeUpdate();
            return rowsUpdated > 0;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public static void setCurrentUserLogin(String login) {
        currentUserLogin = login;
    }

    public static String getCurrentUserLogin() {
        return currentUserLogin;
    }

    public static boolean changePassword(String login, String oldPassword, String newPassword) {
        String sql = "UPDATE users SET password = ? WHERE login = ? AND password = ?";

        try (Connection conn = connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, newPassword);
            stmt.setString(2, login);
            stmt.setString(3, oldPassword);

            int rowsUpdated = stmt.executeUpdate();
            return rowsUpdated > 0;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public static List<Offer> getOffers() {
        List<Offer> offers = new ArrayList<>();
        String sql = "SELECT id, name, description, price, image_url, start_date, end_date FROM offers";

        try (Connection conn = connect();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                offers.add(new Offer(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("description"),
                        rs.getBigDecimal("price"),
                        rs.getString("image_url"),
                        rs.getDate("start_date").toLocalDate(), //  Konwersja na LocalDate
                        rs.getDate("end_date").toLocalDate() //  Konwersja na LocalDate
                ));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return offers;
    }


    public static List<Offer> searchOffersByDateRange(Date startDate, Date endDate) {
        List<Offer> offers = new ArrayList<>();
        String sql = "SELECT id, name, description, price, image_url, start_date, end_date FROM offers WHERE start_date >= ? AND end_date <= ?";

        try (Connection conn = connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setDate(1, startDate);
            stmt.setDate(2, endDate);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                offers.add(new Offer(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("description"),
                        rs.getBigDecimal("price"),
                        rs.getString("image_url"),
                        rs.getDate("start_date").toLocalDate(),
                        rs.getDate("end_date").toLocalDate()
                ));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return offers;
    }

    public static List<Offer> getAllOffers() {
        List<Offer> offers = new ArrayList<>();
        String sql = "SELECT id, name, description, price, location, start_date, end_date FROM offers";

        try (Connection conn = connect();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Offer offer = new Offer(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("description"),
                        rs.getBigDecimal("price"), //  Pobieranie ceny z bazy
                        rs.getString("location"),
                        rs.getDate("start_date") != null ? rs.getDate("start_date").toLocalDate() : LocalDate.now(),
                        rs.getDate("end_date") != null ? rs.getDate("end_date").toLocalDate() : LocalDate.now().plusDays(7)
                );


                System.out.println("🔹 Pobieram ofertę: " + offer.getName());
                offers.add(offer);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return offers;
    }


    public static int getCurrentUserId() {
        String sql = "SELECT id FROM users WHERE login = ?";
        int userId = -1;

        try (Connection conn = connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, getCurrentUserLogin()); // Pobieranie loginu aktualnego użytkownika
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                userId = rs.getInt("id");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }



        return userId;
    }




    public static List<Reservation> getUserReservations(int userId) {
        String sql = "SELECT r.id AS reservation_id, r.offer_id, r.user_id, " +
                "u.imie AS first_name, u.nazwisko AS last_name, " +
                "o.name AS offer_name, o.description AS offer_description, " +
                "r.reservation_date, r.payment_status, o.price " +
                "FROM reservations r " +
                "JOIN offers o ON r.offer_id = o.id " +
                "JOIN users u ON r.user_id = u.id " +
                "WHERE r.user_id = ?";


        List<Reservation> reservations = new ArrayList<>();
        try (Connection conn = connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                System.out.println("🔹 Pobieram dane: offer_name = " + rs.getString("offer_name"));
                System.out.println("🔹 Pobieram dane: offer_description = " + rs.getString("offer_description"));
                System.out.println("🔹 Pobieram dane: price = " + rs.getDouble("price"));

                reservations.add(new Reservation(
                        rs.getInt("reservation_id"),
                        rs.getInt("offer_id"),
                        rs.getInt("user_id"),
                        rs.getString("first_name"),
                        rs.getString("last_name"),
                        rs.getString("offer_name"),
                        rs.getString("offer_description"),
                        rs.getDate("reservation_date") != null ? rs.getDate("reservation_date") : java.sql.Date.valueOf(LocalDate.now()),
                        rs.getDouble("price"),
                        rs.getString("payment_status")
                ));

            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return reservations;
    }







    public static void reserveOffer(int userId, int offerId) {
        String sql = "INSERT INTO reservations (user_id, offer_id, reservation_date) VALUES (?, ?, (SELECT start_date FROM offers WHERE id = ?))";

        try (Connection conn = connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);
            stmt.setInt(2, offerId);
            stmt.setInt(3, offerId); // Pobiera datę startową wycieczki
            stmt.executeUpdate();

            System.out.println("Rezerwacja dodana dla użytkownika ID: " + userId);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }




    public static void cancelReservation(int offerId) {
        String sql = "DELETE FROM reservations WHERE offer_id = ? AND user_id = ?";

        try (Connection conn = connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, offerId);
            stmt.setInt(2, getCurrentUserId());

            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Rezerwacja została anulowana!");
            } else {
                JOptionPane.showMessageDialog(null, "Nie znaleziono rezerwacji do usunięcia!", "Błąd", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public static boolean deleteUserAccount(String login, String password, String confirmPassword) {
        if (!password.equals(confirmPassword)) {
            JOptionPane.showMessageDialog(null, "Hasła nie pasują!", "Błąd", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        String sql = "DELETE FROM users WHERE login = ? AND password = ?";
        try (Connection conn = connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, login);
            stmt.setString(2, password);

            int rowsDeleted = stmt.executeUpdate();
            if (rowsDeleted > 0) {
                JOptionPane.showMessageDialog(null, "Konto zostało usunięte! Nastąpi wylogowanie.", "Sukces", JOptionPane.INFORMATION_MESSAGE);
                setCurrentUserLogin(null);  //  Resetowanie aktualnego użytkownika
                return true;
            } else {
                JOptionPane.showMessageDialog(null, "Błędne dane logowania!", "Błąd", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }


    public static boolean simulatePayment(int userId, int offerId, double amount) {
        System.out.println("🔹 Symulacja płatności dla użytkownika: " + userId + " | Oferta: " + offerId + " | Kwota: " + amount);

        String sql = "UPDATE reservations SET payment_status = 'Opłacona' WHERE user_id = ? AND offer_id = ?";
        try (Connection conn = connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            stmt.setInt(2, offerId);
            int rowsAffected = stmt.executeUpdate();

            return rowsAffected > 0; //  Zwraca `true`, jeśli płatność została zarejestrowana
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }



    public static boolean isUserAdmin(String login) {
        String sql = "SELECT is_admin FROM users WHERE login = ?";

        try (Connection conn = connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, login);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getBoolean("is_admin");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return false;
    }


    public static List<String> getAllUsers() {
        List<String> users = new ArrayList<>();
        String sql = "SELECT login FROM users";

        try (Connection conn = connect();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                String user = rs.getString("login");
                System.out.println("🔹 Pobieram użytkownika: " + user);
                users.add(user);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return users;
    }



    public static void updateAdminStatus(String login, boolean isAdmin) {
        String sql = "UPDATE users SET is_admin = ? WHERE login = ?";

        try (Connection conn = connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setBoolean(1, isAdmin);
            stmt.setString(2, login);
            stmt.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }


    public static void deleteUser(String login) {
        String sqlDeletePayments = "DELETE FROM payments WHERE user_id = (SELECT id FROM users WHERE login = ?)";
        String sqlDeleteUser = "DELETE FROM users WHERE login = ?";

        try (Connection conn = connect();
             PreparedStatement stmtPayments = conn.prepareStatement(sqlDeletePayments);
             PreparedStatement stmtUser = conn.prepareStatement(sqlDeleteUser)) {

            stmtPayments.setString(1, login);
            stmtPayments.executeUpdate();

            stmtUser.setString(1, login);
            stmtUser.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }



    public static void addOffer(String name, String description, BigDecimal price, LocalDate startDate, LocalDate endDate) {
        String sql = "INSERT INTO offers (name, description, price, start_date, end_date) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, name);
            stmt.setString(2, description);
            stmt.setBigDecimal(3, price);
            stmt.setDate(4, java.sql.Date.valueOf(startDate)); //  Konwersja `LocalDate` na SQL `Date`
            stmt.setDate(5, java.sql.Date.valueOf(endDate));
            stmt.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }




    public static void updateOffer(String oldName, String newName, String newDescription, BigDecimal newPrice, LocalDate newStartDate, LocalDate newEndDate) {
        String sql = "UPDATE offers SET name = ?, description = ?, price = ?, start_date = ?, end_date = ? WHERE name = ?";

        try (Connection conn = connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, newName);
            stmt.setString(2, newDescription);
            stmt.setBigDecimal(3, newPrice);
            stmt.setDate(4, java.sql.Date.valueOf(newStartDate)); //  Konwersja `LocalDate` na SQL `Date`
            stmt.setDate(5, java.sql.Date.valueOf(newEndDate));
            stmt.setString(6, oldName);
            stmt.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }




    public static void deleteOffer(String name) {
        String sql = "DELETE FROM offers WHERE name = ?";

        try (Connection conn = connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, name);
            stmt.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public static Offer getOfferByName(String name) {
        String sql = "SELECT * FROM offers WHERE name = ?";
        try (Connection conn = connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, name);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Offer(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("description"),
                        rs.getBigDecimal("price"),
                        rs.getString("location"),
                        rs.getDate("start_date") != null ? rs.getDate("start_date").toLocalDate() : LocalDate.now(),
                        rs.getDate("end_date") != null ? rs.getDate("end_date").toLocalDate() : LocalDate.now().plusDays(7)
                );
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }


    public static void addUser(String firstName, String lastName, String login, String email, String password, boolean isAdmin) {
        String sql = "INSERT INTO users (imie, nazwisko, login, email, password, is_admin) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, firstName);
            stmt.setString(2, lastName);
            stmt.setString(3, login);
            stmt.setString(4, email);
            stmt.setString(5, password);
            stmt.setBoolean(6, isAdmin);
            stmt.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }


    public static boolean addReservation(int userId, int offerId) {
        String checkSql = "SELECT COUNT(*) FROM reservations WHERE user_id = ? AND offer_id = ?";
        String insertSql = "INSERT INTO reservations (user_id, offer_id, reservation_date, payment_status) VALUES (?, ?, ?, ?)";

        try (Connection conn = connect();
             PreparedStatement checkStmt = conn.prepareStatement(checkSql);
             PreparedStatement insertStmt = conn.prepareStatement(insertSql)) {

            // Sprawdzenie czy rezerwacja już istnieje
            checkStmt.setInt(1, userId);
            checkStmt.setInt(2, offerId);
            ResultSet rs = checkStmt.executeQuery();

            if (rs.next() && rs.getInt(1) > 0) {
                System.out.println("🔹 Użytkownik już ma tę ofertę w rezerwacjach!");
                return false; // Jeśli oferta już istnieje, nie dodawaj jej ponownie
            }

            // Jeśli nie istnieje, dodaj rezerwację
            insertStmt.setInt(1, userId);
            insertStmt.setInt(2, offerId);
            insertStmt.setDate(3, java.sql.Date.valueOf(LocalDate.now()));
            insertStmt.setString(4, "Nieopłacona");
            insertStmt.executeUpdate();
            return true;

        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }


    public static List<Reservation> getAllReservations() {
        List<Reservation> reservations = new ArrayList<>();

        String sql = "SELECT r.id AS reservation_id, r.offer_id, r.user_id, u.imie AS first_name, u.nazwisko AS last_name, " +
                "o.name AS offer_name, o.description AS offer_description, r.reservation_date, " +
                "r.payment_status, o.price " +
                "FROM reservations r " +
                "JOIN offers o ON r.offer_id = o.id " +
                "JOIN users u ON r.user_id = u.id " +
                "ORDER BY r.reservation_date";




        try (Connection conn = connect();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                reservations.add(new Reservation(
                        rs.getInt("reservation_id"),
                        rs.getInt("offer_id"),
                        rs.getInt("user_id"),
                        rs.getString("first_name"),
                        rs.getString("last_name"),
                        rs.getString("offer_name"),
                        rs.getString("offer_description"),
                        rs.getDate("reservation_date") != null ? rs.getDate("reservation_date") : java.sql.Date.valueOf(LocalDate.now()),
                        rs.getDouble("price"),
                        rs.getString("payment_status")
                ));

            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return reservations;
    }


    public static void updatePaymentStatus(int offerId, String newStatus) {
        String sql = "UPDATE reservations SET payment_status = ? WHERE offer_id = ?";

        try (Connection conn = connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, newStatus);
            stmt.setInt(2, offerId);
            stmt.executeUpdate();
            System.out.println("🔹 Status płatności zmieniony na: " + newStatus);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }


    public static void cancelReservationById(int reservationId) {
        String sql = "DELETE FROM reservations WHERE id = ?";
        try (Connection conn = connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, reservationId);
            stmt.executeUpdate();
            System.out.println("🔹 Rezerwacja usunięta (ID: " + reservationId + ")");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }


    public static int countReservations() {
        String sql = "SELECT COUNT(*) FROM reservations";
        try (Connection conn = connect();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            return rs.next() ? rs.getInt(1) : 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public static int countUniqueUsers() {
        String sql = "SELECT COUNT(DISTINCT user_id) FROM reservations";
        try (Connection conn = connect();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            return rs.next() ? rs.getInt(1) : 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public static double sumPaidReservations() {
        String sql = "SELECT SUM(price) FROM reservations WHERE payment_status = 'Opłacona'";
        try (Connection conn = connect();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            return rs.next() ? rs.getDouble(1) : 0.0;
        } catch (SQLException e) {
            e.printStackTrace();
            return 0.0;
        }
    }


    public static boolean loginExists(String login) {
        try (Connection conn = connect();
             PreparedStatement stmt = conn.prepareStatement("SELECT COUNT(*) FROM users WHERE login = ?")) {
            stmt.setString(1, login);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static List<User> getAllUserObjects() {
        List<User> users = new ArrayList<>();
        String query = "SELECT login, imie, nazwisko, email, is_admin FROM users";

        try (Connection conn = connect();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                String login = rs.getString("login");
                String imie = rs.getString("imie");
                String nazwisko = rs.getString("nazwisko");
                String email = rs.getString("email");
                boolean isAdmin = rs.getBoolean("is_admin");

                users.add(new User(login, imie, nazwisko, email, isAdmin));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return users;
    }


}



