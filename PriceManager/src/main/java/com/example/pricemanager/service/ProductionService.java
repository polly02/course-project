package com.example.pricemanager.service;

import com.example.pricemanager.message.Status;

public class ProductionService {
    public boolean isOperationAccepted(Status status) {
        switch (status) {
            case SUCCESS: {
                return true;
            }
            case NOT_ENOUGH_PRODUCTS: {
                Service.showAlert("Вы не можете провести эту операцию, ведь количество товаров станет отрицательным. Вероятно вы уже продали этот товар.");
                break;
            }
        }
        return false;
    }
}
