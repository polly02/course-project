package com.example.pricemanager.controller;

import com.example.pricemanager.dto.ChangePasswordDto;
import com.example.pricemanager.entity.User;
import com.example.pricemanager.message.Action;
import com.example.pricemanager.message.Status;
import com.example.pricemanager.service.PersonalService;
import com.example.pricemanager.service.Service;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.text.Text;

public class PersonalTabController implements Controller {
    @FXML
    private Button changePasswordButton;

    @FXML
    private Text loginArea;

    @FXML
    private PasswordField newPasswordField;

    @FXML
    private PasswordField oldPasswordField;

    @FXML
    private PasswordField repeatedNewPasswordField;

    private PersonalService personalService = new PersonalService();

    @FXML
    void initialize() {
        loginArea.setText(user.getLogin());
    }

    @FXML
    void onClickChangePasswordButton(ActionEvent event) {
        if (isDataCorrectToChangePassword()) {
            client.writeObject(Action.CHANGE_PASSWORD);
            client.writeObject(new ChangePasswordDto(user.getLogin(),oldPasswordField.getText(), newPasswordField.getText()));
            if (personalService.isPasswordChanged((Status) client.readObject())) {
                Service.showAlert("Вы успешно сменили пароль!");
                newPasswordField.setText("");
                repeatedNewPasswordField.setText("");
                oldPasswordField.setText("");
            }
        }
    }

    private boolean isDataCorrectToChangePassword() {
        if (!newPasswordField.getText().matches("[a-zA-Z\\d]{6,}")) {
            Service.showAlert("Пароль должен состоять как минимум из 6 символов.\n" +
                    "Можно использовать только английские буквы (a-z и A-Z) и цифры (0-9).");
            return false;
        }
        if (!newPasswordField.getText().equals(repeatedNewPasswordField.getText())) {
            Service.showAlert("Введённые пароли не совпадают. Попробуйте снова.");
            newPasswordField.setText("");
            repeatedNewPasswordField.setText("");
            return false;
        }
        if (newPasswordField.getText().equals(oldPasswordField.getText())) {
            Service.showAlert("Новый и старый пароли совпадают. Придумайте другой пароль.");
            newPasswordField.setText("");
            repeatedNewPasswordField.setText("");
            oldPasswordField.setText("");
            return false;
        }
        return true;
    }
}
