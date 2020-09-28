package com.example.inclass12_group7;

import java.io.Serializable;

public class UserDetails implements Serializable {
    String email;

    public UserDetails(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "UserDetails{" +
                "email='" + email + '\'' +
                '}';
    }
}
