package vanilla.app.api.user;

public class RegistrationRequest {

    String login;
    String password;

    public RegistrationRequest() {

    }

    public RegistrationRequest(String login, String password) {
        this.login = login;
        this.password = password;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String value) {
        login = value;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String value) {
        password = value;
    }
}
