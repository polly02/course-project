package com.example.pricemanager.controller;

import com.example.pricemanager.connection.Client;
import com.example.pricemanager.dto.UserDto;
import com.example.pricemanager.entity.Company;
import com.example.pricemanager.entity.Product;
import com.example.pricemanager.entity.User;
import com.example.pricemanager.message.Action;
import com.example.pricemanager.service.Service;
import javafx.event.EventHandler;
import javafx.stage.WindowEvent;

public interface Controller {
    Client client = Service.getClient("127.0.0.1", 8000);
    User user = new User();
    Company currentCompany = new Company();
    Product currentProduct = new Product();

    EventHandler<WindowEvent> closeEventHandler = windowEvent -> {
        client.writeObject(Action.EXIT);
        client.disConnect();
        System.exit(0);
    };

    static void updateUserInfo() {
        client.writeObject(Action.GET_USER_INFO);
        client.writeObject(user.getLogin());
        UserDto userInfo = ((UserDto) client.readObject());
        user.setId(userInfo.getId());
        user.setLogin(userInfo.getLogin());
        user.setUserStatus(userInfo.getUserStatus());
        user.setUserRole(userInfo.getUserRole());
    }
}
