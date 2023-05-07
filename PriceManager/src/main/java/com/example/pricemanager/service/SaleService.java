package com.example.pricemanager.service;

import com.example.pricemanager.message.Status;

public class SaleService {
    public boolean isOperationAccepted(Status status) {
        switch (status) {
            case SUCCESS: {
                return true;
            }
            case NOT_ENOUGH_PRODUCTS: {
                Service.showAlert("Ваша компания не имеет достаточного количества товаров для проведения данной операции.");
                break;
            }
        }
        return false;
    }
}
