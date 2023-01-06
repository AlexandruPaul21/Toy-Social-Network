package com.example.map_toysocialnetwork_javafx.repository.db;

import com.example.map_toysocialnetwork_javafx.domain.Friendship;
import com.example.map_toysocialnetwork_javafx.domain.validators.ArgumentException;
import com.example.map_toysocialnetwork_javafx.domain.validators.Validator;
import com.example.map_toysocialnetwork_javafx.repository.memory.MemoryFriendshipRepository;


import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class DbFriendshipRepository extends MemoryFriendshipRepository {
    private final String urlDb;
    private final String usernameDb;
    private final String passwordDb;

    public DbFriendshipRepository(Validator<Friendship> validator, String urlDb, String usernameDb, String passwordDb) {
        super(validator);
        this.urlDb = urlDb;
        this.usernameDb = usernameDb;
        this.passwordDb = passwordDb;
        loadData();
    }

    @Override
    public Friendship save(Friendship entity) {
        Friendship friendship = super.save(entity);
        if (friendship == null) {
            String sql = "INSERT INTO friendships(id,id1,id2,start) VALUES(?,?,?,?)";
            try (
                    Connection connection = DriverManager.getConnection(urlDb,usernameDb,passwordDb);
                    PreparedStatement preparedStatement = connection.prepareStatement(sql)
                    ){
                preparedStatement.setLong(1, entity.getId());
                preparedStatement.setLong(2, entity.getId1());
                preparedStatement.setLong(3, entity.getId2());
                preparedStatement.setDate(4, Date.valueOf(entity.getFriendsFrom()));
                preparedStatement.executeUpdate();
            } catch (SQLException sqlException) {
                sqlException.printStackTrace();
            }
        }
        return friendship;
    }

    @Override
    public Long delete(Long id1, Long id2) {
        Long id = null;
        try {
            id = super.delete(id1,id2);
        } catch (ArgumentException argumentException) {
            argumentException.printStackTrace();
        }
        if (id != null) {
            String sql = "DELETE FROM friendships WHERE id = ?";
            try (
                    Connection connection = DriverManager.getConnection(urlDb,usernameDb,passwordDb);
                    PreparedStatement preparedStatement = connection.prepareStatement(sql)
                    ) {
                preparedStatement.setLong(1, id);
                preparedStatement.executeUpdate();
            } catch (SQLException sqlException) {
                sqlException.printStackTrace();
            }
        }
        return id;
    }

    private void loadData() {
        try (
                Connection connection = DriverManager.getConnection(urlDb,usernameDb,passwordDb);
                PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM friendships");
                ResultSet resultSet = preparedStatement.executeQuery()
                ) {
            while (resultSet.next()) {
                Long id = resultSet.getLong("id");
                Long id1 = resultSet.getLong("id1");
                Long id2 = resultSet.getLong("id2");
                LocalDate localDate = resultSet.getDate("start").toLocalDate();
                String status = resultSet.getString("status");
                Friendship friendship = new Friendship(id1, id2);
                friendship.setId(id);
                friendship.setFriendsFrom(localDate);
                friendship.setStatus(status);
                super.save(friendship);
            }
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
    }

    public List<Long> getRequests(Long id, String status) {
        List<Long> res = new ArrayList<>();
        try (
                Connection connection = DriverManager.getConnection(urlDb, usernameDb, passwordDb);
                PreparedStatement preparedStatement = connection.prepareStatement(
                        "SELECT id1, id2 FROM friendships WHERE status = ? AND (id1 = ? OR id2 = ?)"
                )
                ) {
            preparedStatement.setString(1, status);
            preparedStatement.setLong(2, id);
            preparedStatement.setLong(3, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                if (resultSet.getLong("id1") != id && status.equals("accepted")) {
                    res.add(resultSet.getLong("id1"));
                }
                if (resultSet.getLong("id2") != id && status.equals("accepted")) {
                    res.add(resultSet.getLong("id2"));
                }
                if (resultSet.getLong("id2") == id && status.equals("sent")) {
                    res.add(resultSet.getLong("id1"));
                }
            }
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
        return res;
    }

    public List<Long> getFriendRequestsSent(Long id) {
        List<Long> reqIds = new ArrayList<>();

        String sql = "SELECT * FROM friendships WHERE id1 = ? AND status = 'sent' ";

        try (
                Connection connection = DriverManager.getConnection(urlDb, usernameDb, passwordDb);
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                ) {
                preparedStatement.setLong(1, id);
                ResultSet resultSet = preparedStatement.executeQuery();

                while (resultSet.next()) {
                    reqIds.add(resultSet.getLong("id2"));
                }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return reqIds;
    }

    public List<Long> discoverNew(Long id, long maxSize) {
        List<Long> res = new ArrayList<>(getRequests(id, "accepted"));
        res.addAll(getRequests(id, "sent"));

        String sql = "SELECT id2 FROM friendships WHERE id1 = ? AND status = 'sent'";

        try (
                Connection connection = DriverManager.getConnection(urlDb, usernameDb, passwordDb);
                PreparedStatement preparedStatement = connection.prepareStatement(sql)
                ) {
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                res.add(resultSet.getLong("id2"));
            }

        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }

        List<Long> ans = new ArrayList<>();

        for (Long i = 1L; i <= maxSize; ++i){
            if (!res.contains(i) && !i.equals(id) && !i.equals(6L)) {
                ans.add(i);
            }
        }
        return ans;
    }

    public void setConfirm(Long id1, Long id2) {
        String sql = "UPDATE friendships SET status = 'accepted' WHERE id1 = ? AND id2 = ?";
        try (
                Connection connection = DriverManager.getConnection(urlDb, usernameDb, passwordDb);
                PreparedStatement preparedStatement = connection.prepareStatement(sql)
                ) {
                preparedStatement.setLong(1, id1);
                preparedStatement.setLong(2, id2);
                preparedStatement.executeUpdate();
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
    }

    public LocalDate getFriendsFrom(Long id1, Long id2) {
        String sql = "SELECT start FROM friendships WHERE id1 = ? AND id2 = ?";

        try (
                Connection connection = DriverManager.getConnection(urlDb, usernameDb, passwordDb);
                PreparedStatement preparedStatement = connection.prepareStatement(sql)
        ) {
            preparedStatement.setLong(1, id1);
            preparedStatement.setLong(2, id2);
            ResultSet resultSet = preparedStatement.executeQuery();

            LocalDate localDate = null;

            while (resultSet.next()) {
                localDate = resultSet.getDate("start").toLocalDate();
            }

            return localDate;

        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
        return null;
    }
}
