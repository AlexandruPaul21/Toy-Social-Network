package com.example.map_toysocialnetwork_javafx.domain;

import java.util.Objects;

/**
 * Class for User
 */
public class User extends Entity<Long>{
    private final String firstname;
    private final String lastname;
    private final String email;
    private final String password;

    /**
     * Basic constructor
     * @param firstname the first name
     * @param lastname the last name
     * @param email the email of the user
     * @param password the password of the user
     */
    public User(String firstname, String lastname, String email, String password) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    /**
     * Getter for first name
     * @return the first name
     */
    public String getFirstname() {
        return firstname;
    }

    /**
     * Returns the last name
     * @return the last name
     */
    public String getLastname() {
        return lastname;
    }


    /**
     *
     * @return the hashcode
     */
    @Override
    public int hashCode() {
        return Objects.hash(getFirstname(),getLastname(),getEmail(),getPassword());
    }

    /**
     * Verifies the equality
     * @param obj the object with which is verified
     * @return true or false
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof User that)) return false;
        return (getFirstname().equals(that.getFirstname()) &&
            getLastname().equals(that.getLastname()) &&
            getEmail().equals(that.getEmail()) &&
            getPassword().equals(that.getPassword()));
    }

    /**
     * Makes it printable
     * @return a string with all the valuable information
     */
    @Override
    public String toString() {
        return "Id: " + getId() + " firstname: " + firstname + " lastname: " + lastname + " email: " + email + "\n";
    }

    public String writeable() {
        return getId() + ";" + getFirstname() + ";" + getLastname() + ";" + getEmail() + ";" + getPassword() + "\n";
    }
}
