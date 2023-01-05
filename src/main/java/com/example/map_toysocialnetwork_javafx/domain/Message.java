package com.example.map_toysocialnetwork_javafx.domain;

import java.time.LocalDateTime;

public class Message extends Entity<Long>{
    private String message;
    private Long idFrom;
    private Long idTo;
    private LocalDateTime sendTime;

    public Message(String message, Long idFrom, Long idTo, LocalDateTime sendTime) {
        this.message = message;
        this.idFrom = idFrom;
        this.idTo = idTo;
        this.sendTime = sendTime;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Long getIdFrom() {
        return idFrom;
    }

    public void setIdFrom(Long idFrom) {
        this.idFrom = idFrom;
    }

    public Long getIdTo() {
        return idTo;
    }

    public void setIdTo(Long idTo) {
        this.idTo = idTo;
    }

    public LocalDateTime getSendTime() {
        return sendTime;
    }

    public void setSendTime(LocalDateTime sendTime) {
        this.sendTime = sendTime;
    }
}
