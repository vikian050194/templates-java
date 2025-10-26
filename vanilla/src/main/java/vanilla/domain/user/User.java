package vanilla.domain.user;

// TODO what is the best way to separate data layer entity from business logic entity?
public class User {

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public User(int id, String username, String password) {
        this.id = id;
        this.username = username;
        this.password = password;
    }

    public int id;

    public String username;
    
    public String password;
}
