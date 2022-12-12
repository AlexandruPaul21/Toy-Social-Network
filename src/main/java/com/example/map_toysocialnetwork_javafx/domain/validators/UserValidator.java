package com.example.map_toysocialnetwork_javafx.domain.validators;

import com.example.map_toysocialnetwork_javafx.domain.User;

import java.util.regex.Pattern;

/**
 * Custom validator, which implements the Validator Interface
 */
public class UserValidator implements Validator<User>{
    /**
     * Implements the validation function for a user
     * @param entity the entity to be added
     * @throws ValidationException if the user is not valid
     */
    @Override
    public void validate(User entity) throws ValidationException {
        String err = "";
        if (entity.getFirstname().equals("")) {
            err += "Firstname can't be void\n";
        }

        if (entity.getLastname().equals("")) {
            err += "Lastname can't be void\n";
        }

        if (!Pattern.matches("^[a-z0-9]+[._]?[a-z0-9]+@\\w+[.]\\w{2,3}$", entity.getEmail())) {
            err += "Email is not valid\n";
        }

        if (!err.equals("")) {
            throw new ValidationException(err);
        }
    }
}
