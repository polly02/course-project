package com.example.pricemanager.controller;

import com.example.pricemanager.service.Service;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;

public class CalculatorTabController implements Controller {

    @FXML
    private Button costCalcButton;

    @FXML
    private Button priceCalcButton;

    @FXML
    private AnchorPane workPlace;

    @FXML
    void initialize() {
        onClickPriceCalcButton();
    }

    @FXML
    void onClickCostCalcButton() {
        changeTab("costCalculator.fxml");
    }

    @FXML
    void onClickPriceCalcButton() {
        changeTab("priceCalculator.fxml");
    }

    @FXML
    void onClickCostsCalcHistoryButton() {
        changeTab("costCalculatorHistory.fxml");
    }

    @FXML
    void onClickPriceCalcHistoryButton() {
        changeTab("priceCalculatorHistory.fxml");
    }


    public void changeTab(String fxlFileName) {
        Service.changeTab(workPlace, fxlFileName);
    }
}
