package com.example.pricemanager.controller;

import com.example.pricemanager.entity.Sale;
import com.example.pricemanager.message.Action;
import com.example.pricemanager.message.Status;
import com.example.pricemanager.service.SaleService;
import com.example.pricemanager.service.Service;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;

import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

public class SaleTabController implements Controller {
    private ObservableList<Sale> tableData = FXCollections.observableArrayList();

    @FXML
    private TableView<Sale> table;

    @FXML
    private TableColumn<?, ?> col_amount;

    @FXML
    private TableColumn<?, ?> col_date;

    @FXML
    private TableColumn<?, ?> col_id;

    @FXML
    private TableColumn<?, ?> col_total_price;

    @FXML
    private Text currentProductField;

    @FXML
    private DatePicker date_field;

    @FXML
    private TextField amount_field;

    @FXML
    private TextField total_price_field;

    @FXML
    private Button updateButton;

    @FXML
    private Button addButton;

    @FXML
    private Button clearButton;

    @FXML
    private Button deleteButton;

    @FXML
    private Button saveButton;

    @FXML
    private TextField search_field;

    private SaleService saleService = new SaleService();

    @FXML
    void initialize() {
        currentProductField.setText(currentProduct.getId() + ". \"" + currentProduct.getName() + "\"");

        col_id.setCellValueFactory(new PropertyValueFactory<>("id"));
        col_amount.setCellValueFactory(new PropertyValueFactory<>("amount"));
        col_total_price.setCellValueFactory(new PropertyValueFactory<>("totalPrice"));
        col_date.setCellValueFactory(new PropertyValueFactory<>("date"));

        loadDataFromDB();
    }

    @FXML
    void onClickAddButton(ActionEvent event) {
        if (isInputDataCorrect()) {
            Sale sale = getSaleFromInputData();
            sale.setProductId(currentProduct.getId());
            client.writeObject(Action.ADD_NEW_SALE);
            client.writeObject(sale);
            if (saleService.isOperationAccepted((Status) client.readObject())) {
                loadDataFromDB();
                Service.showAlert("Вы успешно добавили новую продажу.");
            }
        }
    }

    @FXML
    void onClickClearButton(ActionEvent event) {
        date_field.setValue(null);
        amount_field.clear();
        total_price_field.clear();
    }

    @FXML
    void onClickSaveButton() {
        Service.printToPDF(table);
    }

    @FXML
    void onClickDeleteButton(ActionEvent event) {
        if (table.getSelectionModel().getSelectedItem() != null) {
            client.writeObject(Action.DELETE_SALE);
            client.writeObject(table.getSelectionModel().getSelectedItem().getId());
            loadDataFromDB();
            Service.showAlert("Вы успешно удалили продажу.");
        } else {
            Service.showAlert("Для выполнения этой операции выберите продажу из таблицы.");
        }
    }

    @FXML
    void onClickUpdateButton(ActionEvent event) {
        if (table.getSelectionModel().getSelectedItem() != null) {
            if (isInputDataCorrect()) {
                Sale sale = getSaleFromInputData();
                sale.setId(table.getSelectionModel().getSelectedItem().getId());
                sale.setProductId(currentProduct.getId());
                client.writeObject(Action.UPDATE_SALE);
                client.writeObject(sale);
                if (saleService.isOperationAccepted((Status) client.readObject())) {
                    loadDataFromDB();
                    Service.showAlert("Вы успешно обновили данные по продаже.");
                }
            }
        } else {
            Service.showAlert("Для выполнения этой операции выберите продажу из таблицы, а затем отредактируйте необходимые поля.");
        }
    }

    @FXML
    void onSelectOneRecord(MouseEvent event) {
        if (table.getSelectionModel().getSelectedItem() != null) {
            Sale selectedSale = table.getSelectionModel().getSelectedItem();
            amount_field.setText(String.valueOf(selectedSale.getAmount()));
            total_price_field.setText(String.valueOf(selectedSale.getTotalPrice()));
            date_field.setValue(selectedSale.getDate());
        }
    }

    private void loadDataFromDB() {
        tableData.clear();
        client.writeObject(Action.GET_ALL_PRODUCT_SALES);
        client.writeObject(currentProduct.getId());
        List<Sale> tempList = (List<Sale>) client.readObject();
        tableData = FXCollections.observableArrayList(tempList);
        table.setItems(tableData);
        routesSearch();
    }

    private void routesSearch() {
        FilteredList<Sale> filteredList = new FilteredList<>(tableData, b -> true);
        search_field.textProperty().addListener((observable, oldValue, newValue) -> filteredList.setPredicate(sale -> {
            if (newValue == null || newValue.isEmpty()) {
                return true;
            }
            String lowerCaseFilter = newValue.toLowerCase();
            return String.valueOf(sale.getId()).contains(lowerCaseFilter) ||
                    sale.getDate().toString().toLowerCase().contains(lowerCaseFilter);
        }));
        SortedList<Sale> sortedList = new SortedList<>(filteredList);
        sortedList.comparatorProperty().bind(table.comparatorProperty());
        table.setItems(sortedList);
    }

    private Sale getSaleFromInputData() {
        Sale sale = new Sale();
        sale.setAmount(!amount_field.getText().equals("") ? Integer.parseInt(amount_field.getText()) : 1);
        sale.setTotalPrice(!total_price_field.getText().equals("") ? Double.parseDouble(total_price_field.getText().replace(",", ".")) : 0f);
        sale.setDate(date_field != null ? date_field.getValue() : LocalDate.now());
        return sale;
    }

    private boolean isInputDataCorrect() {
        boolean flag = true;
        if (!new Scanner(amount_field.getText()).hasNextInt() || Integer.parseInt(amount_field.getText()) <= 0) {
            amount_field.setText("10");
            flag = false;
            Service.showAlert("Введены некорректные данные. Используйте целое число большее 0 для количества товаров в продаже.");
        }
        if (!new Scanner(total_price_field.getText().replace(".", ",")).hasNextDouble()
                || Double.parseDouble(total_price_field.getText().replace(",", ".")) <= 0) {
            total_price_field.setText("1000.0");
            flag = false;
            Service.showAlert("Введены некорректные данные. Используйте вещественное (либо целое) число большее 0 для общей выручки.");
        }
        if (date_field.getValue() == null) {
            date_field.setValue(LocalDate.now());
            flag = false;
            Service.showAlert("Вы не указали дату. Нажмите на календарь рядом с полем для выбора даты.");
        }

        return flag;
    }
}
