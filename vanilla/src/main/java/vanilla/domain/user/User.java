package vanilla.domain.user;

// TODO what is the best way to separate data layer entity from business logic entity?
public class User {

    public User(String login, String password) {
        this.login = login;
        this.password = password;
    }

    public User(int id, String login, String password) {
        this.id = id;
        this.login = login;
        this.password = password;
    }

    public int id;

    public String login;
    
    public String password;
}
