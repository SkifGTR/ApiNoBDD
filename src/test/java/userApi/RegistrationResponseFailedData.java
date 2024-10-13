package userApi;

public class RegistrationResponseFailedData {
    private String error;

    public RegistrationResponseFailedData() {
    }

    public RegistrationResponseFailedData(String error) {
        this.error = error;
    }

    public String getError() {
        return error;
    }
}
