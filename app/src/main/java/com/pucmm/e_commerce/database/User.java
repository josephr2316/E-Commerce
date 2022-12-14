package com.pucmm.e_commerce.database;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;

import java.io.Serializable;
import java.util.UUID;

public class User implements Serializable {

    private String name;
    private String user;
    public String email;
    private String password;
    private String telephoneNumber;
    private boolean isAdmin;

    private String imagen;

    public User() {
    }

    public User(String name, String user, String email, String password, String telephoneNumber, boolean isAdmin, String imagen) {
        this.name = name;
        this.user = user;
        this.email = email;
        this.password = password;
        this.telephoneNumber = telephoneNumber;
        this.isAdmin = isAdmin;
        this.imagen = imagen;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }
    public void generarImagen(){
        this.imagen = UUID.randomUUID().toString();
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
    public void castFirebaseUser(FirebaseUser firebaseUser){
        this.name = firebaseUser.getDisplayName();

    }
}
