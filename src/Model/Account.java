package Model;

public class Account {
    public static final String TABLE_NAME = "account";

    private String username;
    private String password;

    public static final String COL_USERNAME = "Username";
    public static final String COL_PASSWORD = "Password";

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}