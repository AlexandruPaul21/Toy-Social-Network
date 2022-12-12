package com.example.map_toysocialnetwork_javafx;

import com.example.map_toysocialnetwork_javafx.domain.Friendship;
import com.example.map_toysocialnetwork_javafx.domain.User;
import com.example.map_toysocialnetwork_javafx.domain.validators.FriendshipValidator;
import com.example.map_toysocialnetwork_javafx.domain.validators.UserValidator;
import com.example.map_toysocialnetwork_javafx.domain.validators.Validator;
import com.example.map_toysocialnetwork_javafx.gui.Login;
import com.example.map_toysocialnetwork_javafx.repository.db.DbFriendshipRepository;
import com.example.map_toysocialnetwork_javafx.repository.db.DbUserRepository;
import com.example.map_toysocialnetwork_javafx.service.FriendshipService;
import com.example.map_toysocialnetwork_javafx.service.UserService;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

//TODO: pending friendships
//TODO: hash for password
public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        Validator<User> userValidator = new UserValidator();
        Validator<Friendship> friendshipValidator = new FriendshipValidator();
        DbUserRepository userRepository = new DbUserRepository(userValidator,
                "jdbc:postgresql://localhost:5432/MAP_ToySocialNetwork","postgres","1234");
        DbFriendshipRepository friendshipRepository = new DbFriendshipRepository(friendshipValidator,
                "jdbc:postgresql://localhost:5432/MAP_ToySocialNetwork","postgres","1234");
        UserService userService = new UserService(userRepository);
        FriendshipService friendshipService = new FriendshipService(friendshipRepository);

        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("login.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Hello world!");
        Login login = fxmlLoader.getController();
        login.setService(userService, friendshipService);
        System.out.println("Service set");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}