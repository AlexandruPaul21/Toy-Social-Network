package com.example.map_toysocialnetwork_javafx.gui;

import com.example.map_toysocialnetwork_javafx.domain.validators.ArgumentException;
import com.example.map_toysocialnetwork_javafx.domain.validators.ValidationException;
import com.example.map_toysocialnetwork_javafx.service.FriendshipService;
import com.example.map_toysocialnetwork_javafx.service.MessageService;
import com.example.map_toysocialnetwork_javafx.service.UserService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;

public class RegisterWindow {
    private UserService userService;
    private FriendshipService friendshipService;
    private MessageService messageService;

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    public void setFriendshipService(FriendshipService friendshipService) {
        this.friendshipService = friendshipService;
    }

    public void setMessageService(MessageService messageService) {
        this.messageService = messageService;
    }

    @FXML
    private TextField firstName;

    @FXML
    private TextField lastName;

    @FXML
    private TextField email;

    @FXML
    private TextField pass;

    @FXML
    private TextField passConf;

    @FXML
    private Label errorLabel;


    public void registerPressed() throws IOException {
        if (!pass.getText().equals(passConf.getText())) {
            errorLabel.setText("The passwords does not match");
            pass.clear();
            passConf.clear();
            return;
        }

        try {
            userService.add(firstName.getText(), lastName.getText(), email.getText(), pass.getText());
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/com/example/map_toysocialnetwork_javafx/login.fxml"));
            VBox root = fxmlLoader.load();
            Login login = fxmlLoader.getController();
            login.setService(userService, friendshipService, messageService);
            login.setInitMsg("User created. Now login");

            Stage stage = new Stage();
            stage.setScene(new Scene(root));

            stage.setTitle("Login!");
            stage.show();

            closeWindow();

        } catch (ValidationException | ArgumentException validationException) {
            errorLabel.setText(validationException.getMessage());
        }
    }

    private void closeWindow() {
        Stage thisStage = (Stage) lastName.getScene().getWindow();
        thisStage.close();
    }
}
