package com.example.pricemanager.service;

import com.example.pricemanager.message.Status;

public class CompanyService {
    public boolean isCompanyAdded(Status status){
        switch (status){
            case SUCCESS:{
                return true;
            }
            case COMPANY_ALREADY_EXISTS:{
                Service.showAlert("Предприятие с таким названием уже существует. Придумайте другое.");
                break;
            }
        }
        return false;
    }
}
