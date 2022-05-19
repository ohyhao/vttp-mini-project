package vttp2022.miniproject.models;

public class User {
    private Integer user_id;
    private String email;
    private String name;
    private String password;

    
    public Integer getUser_id() {
        return user_id;
    }
    public void setUser_id(Integer user_id) {
        this.user_id = user_id;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    
    @Override
    public String toString() {
        return "User [user_id = " + user_id + ", email = " + email + ", name = " + name + ", password = " + password + "]";
    }
}
