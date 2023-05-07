package com.example.pricemanager.service;

import com.example.pricemanager.message.Status;

public class LoginService {

    public boolean login(Status status) {
        switch (status){
            case SUCCESS:{
                return true;
            }
            case INVALID_PASSWORD:{
                Service.showAlert("Неверно введён логин или пароль. Попробуйте снова!");
                break;
            }
            case USER_BANNED: {
                Service.showAlert("Ваша учётная запись была заблокирована администратором.");
                break;
            }
        }
        return false;
    }
}
