package com.example.pricemanager.controller;

import com.example.pricemanager.entity.CostCalculation;
import com.example.pricemanager.entity.PriceCalculation;
import com.example.pricemanager.message.Action;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.List;

public class CostCalcHistoryController implements Controller {
    private ObservableList<CostCalculation> tableData = FXCollections.observableArrayList();

    @FXML
    private TableView<CostCalculation> table;

    @FXML
    private TableColumn<?, ?> col_deprecation;

    @FXML
    private TableColumn<?, ?> col_materials;

    @FXML
    private TableColumn<?, ?> col_others;

    @FXML
    private TableColumn<?, ?> col_production;

    @FXML
    private TableColumn<?, ?> col_result;

    @FXML
    private TableColumn<?, ?> col_salary;

    @FXML
    void initialize() {
        col_materials.setCellValueFactory(new PropertyValueFactory<>("materials"));
        col_production.setCellValueFactory(new PropertyValueFactory<>("production"));
        col_deprecation.setCellValueFactory(new PropertyValueFactory<>("deprecation"));
        col_others.setCellValueFactory(new PropertyValueFactory<>("others"));
        col_salary.setCellValueFactory(new PropertyValueFactory<>("salary"));
        col_result.setCellValueFactory(new PropertyValueFactory<>("result"));

        loadDataFromDB();
    }

    @FXML
    void onClickClearHistoryButton() {
        client.writeObject(Action.DELETE_ALL_USER_COST_CALCULATIONS);
        client.writeObject(user.getId());
        loadDataFromDB();
    }

    private void loadDataFromDB() {
        tableData.clear();
        client.writeObject(Action.GET_ALL_USER_COST_CALCULATIONS);
        client.writeObject(user.getId());
        List<CostCalculation> tempList = (List<CostCalculation>) client.readObject();
        tableData = FXCollections.observableArrayList(tempList);
        table.setItems(tableData);
    }
}
