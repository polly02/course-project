package com.example.pricemanager.controller;

import com.example.pricemanager.entity.PriceCalculation;
import com.example.pricemanager.message.Action;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.List;

public class PriceCalcHistoryController implements Controller {
    private ObservableList<PriceCalculation> tableData = FXCollections.observableArrayList();

    @FXML
    private TableView<PriceCalculation> table;

    @FXML
    private TableColumn<?, ?> col_average_cost;

    @FXML
    private TableColumn<?, ?> col_increase;

    @FXML
    private TableColumn<?, ?> col_result;

    @FXML
    private TableColumn<?, ?> col_tax;

    @FXML
    void initialize() {
        col_average_cost.setCellValueFactory(new PropertyValueFactory<>("averageCost"));
        col_increase.setCellValueFactory(new PropertyValueFactory<>("increasePerc"));
        col_tax.setCellValueFactory(new PropertyValueFactory<>("taxPerc"));
        col_result.setCellValueFactory(new PropertyValueFactory<>("result"));

        loadDataFromDB();
    }

    @FXML
    void onClickClearHistoryButton() {
        client.writeObject(Action.DELETE_ALL_USER_PRICE_CALCULATIONS);
        client.writeObject(user.getId());
        loadDataFromDB();
    }

    private void loadDataFromDB() {
        tableData.clear();
        client.writeObject(Action.GET_ALL_USER_PRICE_CALCULATIONS);
        client.writeObject(user.getId());
        List<PriceCalculation> tempList = (List<PriceCalculation>) client.readObject();
        tableData = FXCollections.observableArrayList(tempList);
        table.setItems(tableData);
    }
}
