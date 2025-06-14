package Services;

public class User {
    private String login;
    private String firstName;
    private String lastName;
    private String email;
    private boolean admin;

    public User(String login, String firstName, String lastName, String email, boolean admin) {
        this.login = login;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.admin = admin;
    }

    public String getLogin() { return login; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public String getEmail() { return email; }
    public boolean isAdmin() { return admin; }
}
