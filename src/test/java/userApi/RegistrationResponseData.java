package userApi;

public class RegistrationResponseData extends RegistrationRequestData {
    private Integer id;
    private String token;

    public RegistrationResponseData() {
    }

    public RegistrationResponseData(Integer id, String token) {
        this.id = id;
        this.token = token;
    }

    public Integer getId() {
        return id;
    }

    public String getToken() {
        return token;
    }
}
