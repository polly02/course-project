package com.example.pricemanager.controller;

import com.example.pricemanager.entity.User;
import com.example.pricemanager.message.Action;
import com.example.pricemanager.message.Status;
import com.example.pricemanager.service.RegistrationService;
import com.example.pricemanager.service.Service;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class RegistrationController implements Controller {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button backButton;

    @FXML
    private Button completeRegistrationButton;

    @FXML
    private TextField loginField;

    @FXML
    private TextField passwordField;

    @FXML
    private PasswordField repeatedPasswordField;

    @FXML
    void initialize() {

    }

    RegistrationService registrationService = new RegistrationService();

    @FXML
    void onClickBackButton(ActionEvent event) {
        Service.changeScene(backButton, "login.fxml");
    }

    @FXML
    void onClickCompleteRegistrationButton(ActionEvent event) {
        if (isDataCorrectToRegistration()) {
            client.writeObject(Action.REGISTRATION);
            client.writeObject(new User(loginField.getText(), passwordField.getText()));
            if (registrationService.registrate((Status) client.readObject())) {
                Service.showAlert("Вы были успешно зарегистрированы! Войдите в систему.");
                Service.changeScene(completeRegistrationButton, "login.fxml");
            }
        }
    }

    private boolean isDataCorrectToRegistration() {
        if (!loginField.getText().matches("[a-zA-Z\\d]{3,40}")) {
            Service.showAlert("Логин должен быть не короче 3 символов, но и не более 40.\n" +
                    "Можно использовать только английские буквы (a-z и A-Z) и цифры (0-9).");
            return false;
        }
        if (!passwordField.getText().matches("[a-zA-Z\\d]{6,}")) {
            Service.showAlert("Пароль должен состоять как минимум из 6 символов.\n" +
                    "Можно использовать только английские буквы (a-z и A-Z) и цифры (0-9).");
            return false;
        }
        if (!passwordField.getText().equals(repeatedPasswordField.getText())) {
            Service.showAlert("Введённые пароли не совпадают. Попробуйте снова.");
            passwordField.setText("");
            repeatedPasswordField.setText("");
            return false;
        }
        return true;
    }
}
