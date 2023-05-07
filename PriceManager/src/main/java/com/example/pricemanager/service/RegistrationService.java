package com.example.pricemanager.service;

import com.example.pricemanager.message.Status;

public class RegistrationService {
    public boolean registrate(Status status){
        switch (status){
            case SUCCESS:{
                return true;
            }
            case LOGIN_ALREADY_EXISTS:{
                Service.showAlert("Пользователь с таким логином уже существует. Придумайте другой.");
                break;
            }
        }
        return false;
    }
}
