package com.example.pricemanager.service;

import com.example.pricemanager.message.Status;

public class PersonalService {
    public boolean isPasswordChanged(Status status){
        switch (status){
            case SUCCESS:{
                return true;
            }
            case INVALID_PASSWORD:{
                Service.showAlert("Неверно введён текущий пароль.");
                break;
            }
        }
        return false;
    }
}
