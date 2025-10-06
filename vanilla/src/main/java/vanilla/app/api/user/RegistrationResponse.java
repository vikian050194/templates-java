package vanilla.app.api.user;

public class RegistrationResponse {

    int id;

    public RegistrationResponse() {

    }

    public RegistrationResponse(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int value) {
        id = value;
    }
}
