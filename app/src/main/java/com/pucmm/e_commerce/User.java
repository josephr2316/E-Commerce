package com.pucmm.e_commerce;

public class User {

    private String name;
    private String user;
    public String email;
    private String password;
    private String telephoneNumber;
    private boolean isAdmin;

    public User() {
    }

    public User(String name, String user, String email, String password, String telephoneNumber, boolean isAdmin) {
        this.name = name;
        this.user = user;
        this.email = email;
        this.password = password;
        this.telephoneNumber = telephoneNumber;
        this.isAdmin = isAdmin;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
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

    public String getTelephoneNumber() {
        return telephoneNumber;
    }

    public void setTelephoneNumber(String telephoneNumber) {
        this.telephoneNumber = telephoneNumber;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }
}
