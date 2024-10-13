package userApi;

public class RegistrationRequestData {
    private String email;
    private String password;

    public RegistrationRequestData() {
    }

    public RegistrationRequestData(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}
