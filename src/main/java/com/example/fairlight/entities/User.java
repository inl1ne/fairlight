package com.example.fairlight.entities;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;

/**
 * Created by ilewis on 2/12/14.
 */
@Entity
public class User {
    @Id
    Long id;

    private String username;

    public User() {}

    public User(String username) {
        this.username = username;
    }

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }
}
