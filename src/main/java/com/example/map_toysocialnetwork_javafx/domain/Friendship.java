package com.example.map_toysocialnetwork_javafx.domain;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Friendship extends Entity<Long>{
    private final Long id1;
    private final Long id2;
    private LocalDate friendsFrom;
    private String status;

    public Friendship(Long id1, Long id2) {
        this.id1 = id1;
        this.id2 = id2;
        this.friendsFrom = LocalDate.now();
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDate getFriendsFrom() {
        return friendsFrom;
    }

    public void setFriendsFrom(LocalDate friendsFrom) {
        this.friendsFrom = friendsFrom;
    }

    public Long getId1() {
        return id1;
    }

    public Long getId2() {
        return id2;
    }

    public String writeable() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return getId() + ";" + getId1() + ";" + getId2() + ";" + dtf.format(getFriendsFrom()) + "\n";
    }

    @Override
    public String toString() {
        return "Friendship{" +
                "id1=" + id1 +
                ", id2=" + id2 +
                ", friendsFrom=" + friendsFrom +
                ", status='" + status + '\'' +
                '}';
    }
}
