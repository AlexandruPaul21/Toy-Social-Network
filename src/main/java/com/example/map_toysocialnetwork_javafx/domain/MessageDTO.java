package com.example.map_toysocialnetwork_javafx.domain;

import java.time.LocalDateTime;
import java.util.Objects;

public class MessageDTO {
    private Long id;
    private String message;
    private LocalDateTime dateTime;

    public MessageDTO(Long id, String message, LocalDateTime dateTime) {
        this.id = id;
        this.message = message;
        this.dateTime = dateTime;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }


    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }
}
