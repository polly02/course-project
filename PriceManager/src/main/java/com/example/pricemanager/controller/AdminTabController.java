package com.example.pricemanager.controller;

import com.example.pricemanager.dto.UserDto;
import com.example.pricemanager.entity.Product;
import com.example.pricemanager.entity.User;
import com.example.pricemanager.message.Action;
import com.example.pricemanager.message.Status;
import com.example.pricemanager.service.AdminService;
import com.example.pricemanager.service.Service;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;

import java.util.List;

public class AdminTabController implements Controller {
    private ObservableList<UserDto> tableData = FXCollections.observableArrayList();

    @FXML
    private TableColumn<?, ?> col_id;

    @FXML
    private TableColumn<?, ?> col_login;

    @FXML
    private TableColumn<?, ?> col_role;

    @FXML
    private TableColumn<?, ?> col_status;

    @FXML
    private Button doActiveButton;

    @FXML
    private Button doBannedButton;

    @FXML
    private TextField search_field;

    @FXML
    private TableView<UserDto> table;

    @FXML
    private Button upToAdminButton;

    private AdminService adminService = new AdminService();

    @FXML
    void initialize() {
        col_id.setCellValueFactory(new PropertyValueFactory<>("id"));
        col_login.setCellValueFactory(new PropertyValueFactory<>("login"));
        col_status.setCellValueFactory(new PropertyValueFactory<>("userStatus"));
        col_role.setCellValueFactory(new PropertyValueFactory<>("userRole"));

        loadDataFromDB();
    }

    @FXML
    void onClickDoActiveButton(ActionEvent event) {
        if (table.getSelectionModel().getSelectedItem() != null) {
            UserDto selectedUser = table.getSelectionModel().getSelectedItem();
            selectedUser.setUserStatus(User.UserStatus.ACTIVE);
            updateUser(selectedUser);
        } else {
            Service.showAlert("Выберите пользователя для этой операции.");
        }
    }

    @FXML
    void onClickDoBannedButton(ActionEvent event) {
        if (table.getSelectionModel().getSelectedItem() != null) {
            UserDto selectedUser = table.getSelectionModel().getSelectedItem();
            selectedUser.setUserStatus(User.UserStatus.BANNED);
            updateUser(selectedUser);
        } else {
            Service.showAlert("Выберите пользователя для этой операции.");
        }
    }

    @FXML
    void onClickUpToAdminButton(ActionEvent event) {
        if (table.getSelectionModel().getSelectedItem() != null) {
            UserDto selectedUser = table.getSelectionModel().getSelectedItem();
            selectedUser.setUserRole(User.UserRole.ADMIN_ROLE);
            updateUser(selectedUser);
        } else {
            Service.showAlert("Выберите пользователя для этой операции.");
        }
    }

    @FXML
    void onClickSaveButton() {
        Service.printToPDF(table);
    }

    private void loadDataFromDB() {
        tableData.clear();
        client.writeObject(Action.GET_ALL_USERS);
        List<UserDto> tempList = (List<UserDto>) client.readObject();
        tableData = FXCollections.observableArrayList(tempList);
        table.setItems(tableData);
        routesSearch();
    }

    private void routesSearch() {
        FilteredList<UserDto> filteredList = new FilteredList<>(tableData, b -> true);
        search_field.textProperty().addListener((observable, oldValue, newValue) -> filteredList.setPredicate(userDto -> {
            if (newValue == null || newValue.isEmpty()) {
                return true;
            }
            String lowerCaseFilter = newValue.toLowerCase();
            return String.valueOf(userDto.getId()).contains(lowerCaseFilter) ||
                    userDto.getLogin().toLowerCase().contains(lowerCaseFilter);
        }));
        SortedList<UserDto> sortedList = new SortedList<>(filteredList);
        sortedList.comparatorProperty().bind(table.comparatorProperty());
        table.setItems(sortedList);
    }

    private void updateUser(UserDto user) {
        client.writeObject(Action.UPDATE_USER);
        client.writeObject(user);
        if (adminService.isUserUpdated((Status) client.readObject())) {
            Service.showAlert("Вы успешно обновили состояние пользователя.");
            loadDataFromDB();
        }
    }
}
