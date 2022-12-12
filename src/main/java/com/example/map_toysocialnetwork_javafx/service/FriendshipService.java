package com.example.map_toysocialnetwork_javafx.service;

import com.example.map_toysocialnetwork_javafx.domain.Friendship;
import com.example.map_toysocialnetwork_javafx.domain.validators.ArgumentException;
import com.example.map_toysocialnetwork_javafx.repository.db.DbFriendshipRepository;

import java.time.LocalDate;
import java.util.*;

public class FriendshipService {
    private final DbFriendshipRepository repository;

    public List<Long> getRequests(Long id, String status) {
        return repository.getRequests(id, status);
    }

    public List<Long> getDiscover(Long id, long maxSize) {
        return repository.discoverNew(id, maxSize);
    }

    public FriendshipService(DbFriendshipRepository repository) {
        this.repository = repository;
    }

    /**
     * Creates a friendship relation between the ids
     * @param idUser first user id
     * @param idNewFriend second user id
     * @throws ArgumentException if the operation is not permitted
     */
    public void addFriend(Long idUser, Long idNewFriend) {
        if (idUser == null || idNewFriend == null) {
            throw new ArgumentException("At least one id is not valid");
        }

        Friendship friendship = new Friendship(idUser,idNewFriend);
        friendship.setId(repository.getLowestFreeId());
        repository.save(friendship);
    }

    public void confirmFriends(Long id1, Long id2) {
        repository.setConfirm(id1,id2);
    }

    /**
     * Deletes a friendship relation
     * @param idUser first id
     * @param idNewFriend second id
     * @throws ArgumentException if the operation is not permitted
     */
    public void removeFriend(Long idUser, Long idNewFriend) {
        if (idUser == null || idNewFriend == null) {
            throw new ArgumentException("At least one id is not valid");
        }

        repository.delete(idUser, idNewFriend);
    }

    public String getFriendsFrom(Long id1, Long id2) {
        LocalDate ans1 = repository.getFriendsFrom(id1, id2);
        LocalDate ans2 = repository.getFriendsFrom(id2, id1);
        if (ans1 == null) {
            return ans2.toString();
        }
        return ans1.toString();
    }
}
