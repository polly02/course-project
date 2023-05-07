package com.example.pricemanager.service;

import com.example.pricemanager.message.Status;

public class AdminService {
    public boolean isUserUpdated(Status status){
        switch (status){
            case SUCCESS:{
                return true;
            }
            case UNABLE_TO_BAN_ADMIN:{
                Service.showAlert("Вы не можете заблокировать администратора.");
                break;
            }
            case UNABLE_TO_DEMOTE_ADMIN:{
                Service.showAlert("Вы не можете понизить администратора до пользователя.");
                break;
            }
        }
        return false;
    }
}
