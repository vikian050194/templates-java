package vanilla.app.api.user;

public class RegistrationRequest {

    String username;
    String password;

    public RegistrationRequest() {

    }

    public RegistrationRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getLogin() {
        return username;
    }

    public void setLogin(String value) {
        username = value;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String value) {
        password = value;
    }
}
