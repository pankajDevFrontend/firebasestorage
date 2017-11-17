package com.pankaj.firebase.storage.utils;

/**
 * Created by pankaj at com.pankaj.firebase.storage.utils on 09/11/17.
 */

public class User {

    public String password;
    public String email;

    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
