package com.example.map_toysocialnetwork_javafx.repository.memory;

import com.example.map_toysocialnetwork_javafx.domain.User;
import com.example.map_toysocialnetwork_javafx.domain.validators.Validator;

/**
 * Implements the given interface
 */
public class MemoryUserRepository extends MemoryRepository<Long, User> {
    public MemoryUserRepository(Validator<User> validator) {
        super(validator);
    }

    public Long getLowestFreeId() {
        for (Long i = 1L; ; ++i) {
            if (!entities.containsKey(i)) {
                return i;
            }
        }
    }
}
