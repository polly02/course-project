package com.example.pricemanager.controller;

import com.example.pricemanager.entity.Company;
import com.example.pricemanager.entity.Product;
import com.example.pricemanager.message.Action;
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

import java.util.List;

public class ProductTabController implements Controller {
    private ObservableList<Product> tableData = FXCollections.observableArrayList();

    @FXML
    private TableView<Product> table;

    @FXML
    private TableColumn<?, ?> col_amount;

    @FXML
    private TableColumn<?, ?> col_average_cost;

    @FXML
    private TableColumn<?, ?> col_average_selling_price;

    @FXML
    private TableColumn<?, ?> col_id;

    @FXML
    private TableColumn<?, ?> col_name;

    @FXML
    private Text currentProductArea;

    @FXML
    private TextField name_field;

    @FXML
    private Button addButton;

    @FXML
    private Button chooseCurrentProductButton;

    @FXML
    private Button clearButton;

    @FXML
    private Button deleteButton;

    @FXML
    private Button updateButton;

    @FXML
    private Button saveButton;

    @FXML
    private TextField search_field;

    @FXML
    void initialize() {
        if (currentProduct != null) {
            updateCurrentProductArea();
        }

        col_id.setCellValueFactory(new PropertyValueFactory<>("id"));
        col_name.setCellValueFactory(new PropertyValueFactory<>("name"));
        col_amount.setCellValueFactory(new PropertyValueFactory<>("amount"));
        col_average_cost.setCellValueFactory(new PropertyValueFactory<>("averageCost"));
        col_average_selling_price.setCellValueFactory(new PropertyValueFactory<>("averageSellingPrice"));

        loadDataFromDB();
    }

    @FXML
    void onClickSaveButton() {
        Service.printToPDF(table);
    }

    private void updateCurrentProductArea() {
        if (currentProduct.getId() == 0) {
            currentProductArea.setText("Не выбран.");
        } else {
            currentProductArea.setText(currentProduct.getId() + ". \"" + currentProduct.getName() + "\"");
        }
    }

    @FXML
    void onClickAddButton(ActionEvent event) {
        if (isInputDataCorrect()) {
            Product product = getProductFromInputData();
            product.setCompanyId(currentCompany.getId());
            client.writeObject(Action.ADD_NEW_PRODUCT);
            client.writeObject(product);

            loadDataFromDB();
            Service.showAlert("Вы успешно добавили новый продукт.");
        }
    }

    @FXML
    void onClickChooseCurrentProductButton(ActionEvent event) {
        if (table.getSelectionModel().getSelectedItem() != null) {
            Product product = table.getSelectionModel().getSelectedItem();
            currentProduct.setName(product.getName());
            currentProduct.setId(product.getId());
            currentProduct.setCompanyId(currentCompany.getId());
            currentProduct.setAmount(product.getAmount());
            currentProduct.setAverageCost(product.getAverageCost());
            currentProduct.setAverageSellingPrice(product.getAverageSellingPrice());

            updateCurrentProductArea();
        } else {
            Service.showAlert("Для выполнения этой операции выберите продукт из таблицы.");
        }
    }

    @FXML
    void onClickClearButton(ActionEvent event) {
        name_field.clear();
    }

    @FXML
    void onClickDeleteButton(ActionEvent event) {
        if (table.getSelectionModel().getSelectedItem() != null) {
            client.writeObject(Action.DELETE_PRODUCT);
            client.writeObject(table.getSelectionModel().getSelectedItem().getId());
            if (table.getSelectionModel().getSelectedItem().getId() == currentCompany.getId()) {
                currentProduct.setId(0);
                updateCurrentProductArea();
            }
            loadDataFromDB();
            Service.showAlert("Вы успешно удалили продукт.");
        } else {
            Service.showAlert("Для выполнения этой операции выберите продукт из таблицы.");
        }
    }

    @FXML
    void onClickUpdateButton(ActionEvent event) {
        if (table.getSelectionModel().getSelectedItem() != null) {
            if (isInputDataCorrect()) {
                Product product = getProductFromInputData();
                product.setId(table.getSelectionModel().getSelectedItem().getId());
                product.setCompanyId(currentCompany.getId());
                client.writeObject(Action.UPDATE_PRODUCT);
                client.writeObject(product);
                if (product.getId() == currentProduct.getId()) {
                    currentProduct.setName(product.getName());
                    updateCurrentProductArea();
                }
                loadDataFromDB();
                Service.showAlert("Вы успешно обновили данные о продукте.");
            }
        } else {
            Service.showAlert("Для выполнения этой операции выберите продукт из таблицы, а затем отредактируйте необходимые поля.");
        }
    }

    @FXML
    void onSelectOneRecord(MouseEvent event) {
        if (table.getSelectionModel().getSelectedItem() != null) {
            Product selectedProduct = table.getSelectionModel().getSelectedItem();
            name_field.setText(selectedProduct.getName());
        }
    }

    private void loadDataFromDB() {
        tableData.clear();
        client.writeObject(Action.GET_ALL_COMPANY_PRODUCTS);
        client.writeObject(currentCompany.getId());
        List<Product> tempList = (List<Product>) client.readObject();
        tableData = FXCollections.observableArrayList(tempList);
        table.setItems(tableData);
        routesSearch();
    }

    private void routesSearch() {
        FilteredList<Product> filteredList = new FilteredList<>(tableData, b -> true);
        search_field.textProperty().addListener((observable, oldValue, newValue) -> filteredList.setPredicate(product -> {
            if (newValue == null || newValue.isEmpty()) {
                return true;
            }
            String lowerCaseFilter = newValue.toLowerCase();
            return String.valueOf(product.getId()).contains(lowerCaseFilter) ||
                    product.getName().toLowerCase().contains(lowerCaseFilter);
        }));
        SortedList<Product> sortedList = new SortedList<>(filteredList);
        sortedList.comparatorProperty().bind(table.comparatorProperty());
        table.setItems(sortedList);
    }

    private Product getProductFromInputData() {
        Product product = new Product();
        product.setName(!name_field.getText().equals("") ? name_field.getText() : "");
        return product;
    }

    private boolean isInputDataCorrect() {
        boolean flag = true;
        if (name_field.getText().trim().equals("")) {
            name_field.setText("Цемент");
            flag = false;
            Service.showAlert("Наименование продукта не может быть пустым. Исправьте это.");
        }
        return flag;
    }
}
