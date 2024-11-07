package org.example;

public class Account {
    private String id;
    private String login;
    private String password;
    private String role;
    private boolean isBlocked;

    public Account(String id, String login, String password, String role, boolean isBlocked) {
        this.id = id;
        this.login = login;
        this.password = password;
        this.role = role;
        this.isBlocked = isBlocked;
    }

    public String getId() { return id; }
    public String getLogin() { return login; }
    public String getPassword() { return password; }
    public String getRole() { return role; }
    public boolean isBlocked() { return isBlocked; }
}
