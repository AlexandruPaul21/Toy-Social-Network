package com.example.map_toysocialnetwork_javafx.domain;

public class UserDTO {
    private Long id;
    private final String firstname;
    private final String lastname;
    private final String email;
    private final String friendsFrom;

    public UserDTO(Long id, String firstname, String lastname, String email, String date) {
        this.id = id;
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.friendsFrom = date;
    }

    public UserDTO(Long id, String firstname, String lastname, String email) {
        this.id = id;
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.friendsFrom = null;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public String getEmail() {
        return email;
    }

    public String getFriendsFrom() {
        return friendsFrom;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
