package com.example.map_toysocialnetwork_javafx.service;


import com.example.map_toysocialnetwork_javafx.domain.User;
import com.example.map_toysocialnetwork_javafx.domain.validators.ArgumentException;
import com.example.map_toysocialnetwork_javafx.repository.db.DbUserRepository;
import com.example.map_toysocialnetwork_javafx.utils.Secure;

import java.util.List;

/**
 * Controller for the operations
 */
public class UserService {
    private final DbUserRepository repository;

    /**
     * Basic constructor
     *
     * @param repository the repository
     */
    public UserService(DbUserRepository repository) {
        this.repository = repository;
    }

    /**
     * Returns one element of the list
     *
     * @param id -the id of the entity to be returned
     *           id must not be null
     * @return the entity with the given id
     */
    public User findOne(Long id) {
        return repository.findOne(id);
    }

    /**
     * @return all the values from the list
     */
    public List<User> findAll() {
        return repository.findAll();
    }


    /**
     * Save the entity to the repo
     *
     * @param firstname the first name
     * @param lastname  the last name
     * @throws ArgumentException if the entity is null
     */
    public void add(String firstname, String lastname, String email, String password) {
        byte[] salt = Secure.getSalt();
        String stringSalt = Secure.fromByteToStringHex(salt);
        String hashedPass = Secure.getHashedPassword(password, salt);
        User user = new User(firstname, lastname, email, hashedPass, stringSalt);
        user.setId(repository.getLowestFreeId());
        if (repository.save(user) != null) {
            throw new ArgumentException("Id already taken");
        }
    }

    public User findByEmailPass(String email, String password) {
        String stringSalt = repository.getSaltWEmail(email);

        if (stringSalt == null) {
            return null;
        }

        byte[] salt = Secure.getSaltFromHex(stringSalt);
        String hashPass = Secure.getHashedPassword(password, salt);
        return repository.findByEmailPass(email, hashPass);
    }

    /**
     * Deletes the object from the repo
     *
     * @param id the id of the object to be deleted
     *           id must be not null
     * @throws ArgumentException if the id is null
     */
    public void delete(Long id) {
        if (repository.delete(id) == null) {
            throw new ArgumentException("Deletion failed");
        }
    }

    public void update(Long id, String firstname, String lastname, String email, String password) {
        User user = new User(firstname,lastname,email,password, repository.findOne(id).getSalt());
        user.setId(id);
        repository.update(user);
    }

}