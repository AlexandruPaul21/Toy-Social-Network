package com.example.map_toysocialnetwork_javafx.repository.db;


import com.example.map_toysocialnetwork_javafx.domain.User;
import com.example.map_toysocialnetwork_javafx.domain.validators.Validator;
import com.example.map_toysocialnetwork_javafx.repository.memory.MemoryUserRepository;

import java.sql.*;

public class DbUserRepository extends MemoryUserRepository {
    private final String urlDb;
    private final String usernameDb;
    private final String passwordDb;

    public DbUserRepository(Validator<User> validator, String urlDb, String usernameDb, String passwordDb) {
        super(validator);
        this.urlDb = urlDb;
        this.usernameDb = usernameDb;
        this.passwordDb = passwordDb;
        loadData();
    }

    @Override
    public User save(User entity) {
        User user = super.save(entity);
        if (user == null) {
            String sql = "INSERT INTO users(id,firstname,lastname,email,pass,salt) VALUES (?,?,?,?,?,?)";
            try (
                    Connection connection = DriverManager.getConnection(urlDb, usernameDb, passwordDb);
                    PreparedStatement preparedStatement = connection.prepareStatement(sql)
                    ) {
                preparedStatement.setLong(1, entity.getId());
                preparedStatement.setString(2, entity.getFirstname());
                preparedStatement.setString(3, entity.getLastname());
                preparedStatement.setString(4, entity.getEmail());
                preparedStatement.setString(5, entity.getPassword());
                preparedStatement.setString(6, entity.getSalt());
                preparedStatement.executeUpdate();
            } catch (SQLException sqlException) {
                sqlException.printStackTrace();
            }
        }
        return user;
    }

    @Override
    public User delete(Long aLong) {
        User user = super.delete(aLong);
        if (user != null) {
            String sql = "DELETE FROM users WHERE id = ?";
            try (
                    Connection connection = DriverManager.getConnection(urlDb, usernameDb, passwordDb);
                    PreparedStatement preparedStatement = connection.prepareStatement(sql)
                    ) {
                preparedStatement.setLong(1, aLong);
                preparedStatement.executeUpdate();
            } catch (SQLException sqlException) {
                sqlException.printStackTrace();
            }
        }
        return user;
    }

    @Override
    public User update(User entity) {
        User user = super.update(entity);
        if (user == null) {
            String sql = "UPDATE users SET " +
                    "firstname = ?," +
                    "lastname = ?," +
                    "email = ?," +
                    "pass = ? WHERE id = ?;";
            try (
                    Connection connection = DriverManager.getConnection(urlDb, usernameDb, passwordDb);
                    PreparedStatement preparedStatement = connection.prepareStatement(sql)
                    ) {
                preparedStatement.setString(1, entity.getFirstname());
                preparedStatement.setString(2, entity.getLastname());
                preparedStatement.setString(3, entity.getEmail());
                preparedStatement.setString(4, entity.getPassword());
                preparedStatement.setLong(5, entity.getId());
                preparedStatement.executeUpdate();
            } catch (SQLException sqlException) {
                sqlException.printStackTrace();
            }
        }
        return user;
    }

    public String getSaltWEmail(String email) {
        String sql = "SELECT salt FROM users WHERE email = ?";
        String salt = null;
        try (
                Connection connection = DriverManager.getConnection(urlDb, usernameDb, passwordDb);
                PreparedStatement preparedStatement = connection.prepareStatement(sql)
                ) {
            preparedStatement.setString(1, email);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                salt = resultSet.getString("salt");
            }
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
        return salt;
    }

    public User findByEmailPass(String email, String password) {
        try (
                Connection connection = DriverManager.getConnection(urlDb, usernameDb, passwordDb);
                PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM users WHERE " +
                        "users.email = ? AND users.pass = ?")
                ){
            preparedStatement.setString(1, email);
            preparedStatement.setString(2, password);
            ResultSet resultSet = preparedStatement.executeQuery();
            User res = null;
            while (resultSet.next()) {
                Long id = resultSet.getLong("id");
                res = findOne(id);
            }
            return res;
        } catch (SQLException sqlException) {
            sqlException.getStackTrace();
        }
        return null;
    }

    private void loadData() {
        try (
                Connection connection = DriverManager.getConnection(urlDb,usernameDb,passwordDb);
                PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM users");
                ResultSet resultSet = preparedStatement.executeQuery()
                ) {
            while (resultSet.next()) {
                Long id = resultSet.getLong("id");
                String firstname = resultSet.getString("firstname");
                String lastname = resultSet.getString("lastname");
                String email = resultSet.getString("email");
                String pass = resultSet.getString("pass");
                String salt = resultSet.getString("salt");
                User user = new User(firstname, lastname, email, pass, salt);
                user.setId(id);
                super.save(user);
            }
            System.out.println("Success");
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
    }
}
