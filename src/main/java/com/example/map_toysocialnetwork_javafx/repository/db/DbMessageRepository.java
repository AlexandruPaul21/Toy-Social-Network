package com.example.map_toysocialnetwork_javafx.repository.db;

import com.example.map_toysocialnetwork_javafx.domain.Message;
import com.example.map_toysocialnetwork_javafx.domain.validators.Validator;
import com.example.map_toysocialnetwork_javafx.repository.memory.MemoryRepository;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class DbMessageRepository extends MemoryRepository<Long, Message> {
    private final String urlDb;
    private final String usernameDb;
    private final String passwordDb;

    public DbMessageRepository(Validator<Message> validator, String urlDb, String usernameDb, String passwordDb) {
        super(validator);
        this.urlDb = urlDb;
        this.usernameDb = usernameDb;
        this.passwordDb = passwordDb;
    }

    public List<Message> getMessages(Long idFrom, Long idTo) {
        String sql = "SELECT * FROM messages WHERE (id_from = ? AND id_to = ?) OR (id_from = ? AND id_to = ?) ORDER BY msg_date";
        List<Message> messages = new ArrayList<>();
        try(
                Connection connection = DriverManager.getConnection(urlDb, usernameDb, passwordDb);
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                ) {
            preparedStatement.setLong(1,idFrom);
            preparedStatement.setLong(2,idTo);
            preparedStatement.setLong(3,idTo);
            preparedStatement.setLong(4,idFrom);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Long id = resultSet.getLong("id");
                Long idFr = resultSet.getLong("id_from");
                Long idT = resultSet.getLong("id_to");
                String message = resultSet.getString("msg");
                LocalDateTime dateTime = resultSet.getTimestamp("msg_date").toLocalDateTime();
                Message msg = new Message(message, idFr, idT, dateTime);
                msg.setId(id);

                messages.add(msg);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return messages;
    }

    public Long add(Message message) {
        String sql = "INSERT INTO messages(id, msg, id_from, id_to, msg_date) VALUES (?,?,?,?,?)";
        try(
                Connection connection = DriverManager.getConnection(urlDb, usernameDb, passwordDb);
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                )
        {
            preparedStatement.setLong(1, message.getId());
            preparedStatement.setString(2, message.getMessage());
            preparedStatement.setLong(3, message.getIdFrom());
            preparedStatement.setLong(4, message.getIdTo());
            preparedStatement.setTimestamp(5, Timestamp.valueOf(message.getSendTime()));

            preparedStatement.executeUpdate();
            return message.getId();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Long getLowestFreeId() {
        String sql = "SELECT id FROM messages ORDER BY id DESC";
        Long id = null;
        try(
                Connection connection = DriverManager.getConnection(urlDb, usernameDb, passwordDb);
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
        )
        {
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                id = resultSet.getLong("id");
            }
            if (id == null) {
                id = 1L;
            }
            return id;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return id;
    }
}
