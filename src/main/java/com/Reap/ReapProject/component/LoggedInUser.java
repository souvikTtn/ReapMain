package com.Reap.ReapProject.component;


public class LoggedInUser {
    private String email;
    private String password;
    private Integer id;

    public Integer getId() {
        return id;
    }

    @Override
    public String toString() {
        return "LoggedInUser{" +
                "email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", id=" + id +
                '}';
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
