package at.technikum.application.user;

public class User {

    private int userId;
    private String username;
    private String password;

    public User(int userId, String username, String password) {
        this.userId = userId;
        this.username = username;
        this.password = password;
    }

    public void setId(int userId) {
        this.userId = userId;
    }

    public int getId(){
        return userId;

    }
    public String getUsername(){
        return username;
    }

    public String getPassword(){
        return password;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}