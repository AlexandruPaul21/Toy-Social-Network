package com.example.map_toysocialnetwork_javafx.gui;

import com.example.map_toysocialnetwork_javafx.domain.User;
import com.example.map_toysocialnetwork_javafx.domain.UserDTO;
import com.example.map_toysocialnetwork_javafx.domain.validators.ArgumentException;
import com.example.map_toysocialnetwork_javafx.service.FriendshipService;
import com.example.map_toysocialnetwork_javafx.service.UserService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class MainWindow {
    ObservableList<UserDTO> modelUser = FXCollections.observableArrayList();

    @FXML
    private TextField searchBar;

    @FXML
    private Button buttonTrue;

    @FXML
    private Button buttonFalse;

    @FXML
    private TableColumn<UserDTO, String> columnFrom;

    @FXML
    private TableColumn<UserDTO, String> columnFirstname;

    @FXML
    private TableColumn<UserDTO, String> columnLastname;

    @FXML
    private TableColumn<UserDTO, String> columnMail;

    @FXML
    private TableView<UserDTO> mainTable;
    @FXML
    private Label welcomeText;

    private UserService userService;
    private FriendshipService friendshipService;
    private User loggedUser;

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    public void setFriendshipService(FriendshipService friendshipService) {
        this.friendshipService = friendshipService;
    }

    public void setLoggedUser(User loggedUser) {
        this.loggedUser = loggedUser;
    }

    public void setAll(UserService userService, FriendshipService friendshipService, User loggedUser) {
        setUserService(userService);
        setFriendshipService(friendshipService);
        setLoggedUser(loggedUser);
        welcomeText.setText("Welcome " + loggedUser.getFirstname());
    }

    private List<UserDTO> getFriendsDto(String status) {
        List<Long> idList = friendshipService.getRequests(loggedUser.getId(), status);

        return idList.stream().map(x -> userService.findOne(x)).toList()
                .stream()
                .map(x -> new UserDTO(x.getId(),x.getFirstname(), x.getLastname(), x.getEmail(),
                        friendshipService.getFriendsFrom(x.getId(), loggedUser.getId())))
                .collect(Collectors.toList());
    }

    private List<UserDTO> getUsersNotFriends() {
        List<Long> idList = friendshipService.getDiscover(loggedUser.getId(), userService.findAll().size());

        return idList.stream().map(x -> userService.findOne(x)).toList()
                .stream()
                .map(x -> new UserDTO(x.getId(),x.getFirstname(), x.getLastname(), x.getEmail()))
                .collect(Collectors.toList());
    }



    @FXML
    private void initialize() {
        columnFirstname.setCellValueFactory(new PropertyValueFactory<>("firstname"));
        columnLastname.setCellValueFactory(new PropertyValueFactory<>("lastname"));
        columnMail.setCellValueFactory(new PropertyValueFactory<>("email"));
        columnFrom.setCellValueFactory(new PropertyValueFactory<>("friendsFrom"));

        mainTable.setItems(modelUser);
        buttonTrue.setText("Delete Friend");
        buttonFalse.setVisible(false);
        disableButtons();
    }

    public void populateFriends() {
        modelUser.clear();
        columnFrom.setVisible(true);
        modelUser.setAll(getFriendsDto("accepted"));

        buttonTrue.setText("Delete Friend");
        buttonFalse.setVisible(false);
        searchBar.setVisible(false);
        if (mainTable.getSelectionModel().isEmpty()) {
            disableButtons();
        }
    }

    public void populateRequests() {
        modelUser.clear();
        columnFrom.setVisible(true);
        modelUser.setAll(getFriendsDto("sent"));

        buttonFalse.setVisible(true);
        searchBar.setVisible(false);
        buttonTrue.setText("Accept");
        buttonFalse.setText("Decline");
        if (mainTable.getSelectionModel().isEmpty()) {
            disableButtons();
        }
    }

    public void populateDiscover() {
        modelUser.clear();
        columnFrom.setVisible(false);
        modelUser.setAll(getUsersNotFriends());

        searchBar.setVisible(true);
        buttonFalse.setVisible(false);
        buttonTrue.setText("Add new friend");

        if (mainTable.getSelectionModel().isEmpty()) {
            disableButtons();
        }
    }

    public void selectedItemFromTable() {
        buttonTrue.setDisable(false);
        buttonFalse.setDisable(false);
    }

    private void disableButtons() {
        buttonTrue.setDisable(true);
        buttonFalse.setDisable(true);
    }

    public void onTrueButtonPressed() {
        UserDTO wantUser = modelUser.get(mainTable.getSelectionModel().getFocusedIndex());
        if (buttonTrue.getText().equals("Accept")) {
            friendshipService.confirmFriends(wantUser.getId(), loggedUser.getId());
            populateRequests();
        } else if (buttonTrue.getText().equals("Add new friend")) {
            friendshipService.addFriend(loggedUser.getId(), wantUser.getId());
            populateDiscover();
        } else {
            try {
                friendshipService.removeFriend(loggedUser.getId(), wantUser.getId());
                friendshipService.removeFriend(wantUser.getId(), loggedUser.getId());
            } catch(ArgumentException ignore){}
            populateFriends();
        }
    }

    public void onFalseButtonPressed() {
        if (buttonFalse.getText().equals("Decline")) {
            UserDTO wantUser = modelUser.get(mainTable.getSelectionModel().getFocusedIndex());
            friendshipService.removeFriend(wantUser.getId(), loggedUser.getId());
            populateRequests();
        }
    }

    public void logOut() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("/com/example/map_toysocialnetwork_javafx/login.fxml"));
        VBox vBox = fxmlLoader.load();
        Login login = fxmlLoader.getController();
        login.setService(userService, friendshipService);
        login.setInitMsg("Successfully logged out");

        Stage stage = new Stage();
        stage.setScene(new Scene(vBox));

        stage.setTitle("Login");
        stage.show();

        closeWindow();
    }

    private void closeWindow() {
        Stage thisStage = (Stage) mainTable.getScene().getWindow();
        thisStage.close();
    }
}
