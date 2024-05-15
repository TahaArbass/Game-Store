package com.game_store.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "publisher", uniqueConstraints = {@UniqueConstraint(columnNames = {"Username", "Email"})})
public class Publisher {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PublisherID")
    private int publisherID;
    
    @NotEmpty(message = "Username cannot be empty")
    @Size(min = 3, message = "Username must have at least 3 characters")
    @Column(name = "Username", unique = true)
    private String username;
    
    @NotEmpty(message = "Email cannot be empty")
    @Email(message = "Please provide a valid email")
    @Column(name = "Email", unique = true)
    private String email;
    
    @NotEmpty(message = "Password cannot be empty")
    @Size(min = 8, message = "Password must have at least 8 characters")
    @Column(name = "Password")
    private String password;

    // Constructors
    public Publisher() {
    }

    public Publisher(String username, String email, String password) {
        setUsername(username);
        setEmail(email);
        setPassword(password);
    }

    public Publisher(String username, String password) {
        setUsername(username);
        setPassword(password);
    }

    // Getters and Setters
    public int getPublisherID() {
        return publisherID;
    }

    public void setPublisherID(int publisherID) {
        this.publisherID = publisherID;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

    // toString method (for debugging/logging purposes)
    @Override
    public String toString() {
        return "Publisher{" +
                "publisherID=" + publisherID +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
