package com.example.map_toysocialnetwork_javafx.service;

import com.example.map_toysocialnetwork_javafx.domain.Message;
import com.example.map_toysocialnetwork_javafx.repository.db.DbMessageRepository;

import java.time.LocalDateTime;
import java.util.List;

public class MessageService {
    private final DbMessageRepository repository;

    public MessageService(DbMessageRepository repository) {
        this.repository = repository;
    }

    public List<Message> getMessages(Long idFrom, Long idTo) {
        return repository.getMessages(idFrom, idTo);
    }

    public void add(String message, Long idFrom, Long idTo, LocalDateTime dateTime) {
        Message msg = new Message(message, idFrom, idTo, dateTime);
        msg.setId(repository.getLowestFreeId()+1);
        repository.add(msg);
    }
}
