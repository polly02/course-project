module com.example.pricemanager {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.pricemanager to javafx.fxml;
    exports com.example.pricemanager;
    exports com.example.pricemanager.controller;
    opens com.example.pricemanager.controller to javafx.fxml;
    exports com.example.pricemanager.service;
    opens com.example.pricemanager.service to javafx.fxml;
    opens com.example.pricemanager.entity to javafx.base;
    opens com.example.pricemanager.dto to javafx.base;
}