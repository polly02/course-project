package com.example.pricemanager.controller;

import com.example.pricemanager.entity.CostCalculation;
import com.example.pricemanager.message.Action;
import com.example.pricemanager.service.Service;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;

import java.util.Scanner;

public class CostCalculatorController implements Controller {
    @FXML
    private Button calculateButton;

    @FXML
    private TextField deprecationField;

    @FXML
    private TextField materialsField;

    @FXML
    private TextField othersField;

    @FXML
    private TextField productionField;

    @FXML
    private Text resultField;

    @FXML
    private TextField salaryField;

    @FXML
    private AnchorPane reportPane;

    @FXML
    void initialize() {

    }

    @FXML
    void onClickSaveButton() {
        Service.printToPDF(reportPane);
    }

    @FXML
    void onClickCalculateButton() {
        if (isInputDataCorrect()) {
            client.writeObject(Action.ADD_NEW_COST_CALCULATION);
            client.writeObject(getCostCalculationFromInputData());
            resultField.setText(String.valueOf(client.readObject()));
        }
    }

    private CostCalculation getCostCalculationFromInputData() {
        CostCalculation costCalculation = new CostCalculation();
        costCalculation.setMaterials(!materialsField.getText().equals("") ? Double.parseDouble(materialsField.getText().replace(",", ".")) : 0f);
        costCalculation.setProduction(!productionField.getText().equals("") ? Double.parseDouble(productionField.getText().replace(",", ".")) : 0f);
        costCalculation.setDeprecation(!deprecationField.getText().equals("") ? Double.parseDouble(deprecationField.getText().replace(",", ".")) : 0f);
        costCalculation.setSalary(!salaryField.getText().equals("") ? Double.parseDouble(salaryField.getText().replace(",", ".")) : 0f);
        costCalculation.setOthers(!othersField.getText().equals("") ? Double.parseDouble(othersField.getText().replace(",", ".")) : 0f);
        costCalculation.setUserId(user.getId());

        return costCalculation;
    }

    private boolean isInputDataCorrect() {
        boolean flag = true;
        if (!new Scanner(materialsField.getText().replace(".", ",")).hasNextDouble()) {
            materialsField.setText("1000.0");
            flag = false;
            Service.showAlert("Введены некорректные данные. Используйте вещественное (либо целое) число для этого поля.");
        } else if (Double.parseDouble(materialsField.getText())<0) {
            materialsField.setText("1000.0");
            flag = false;
            Service.showAlert("Введены некорректные данные. Нельзя использовать отрицательные значения.");
        }
        if (!new Scanner(productionField.getText().replace(".", ",")).hasNextDouble()) {
            productionField.setText("1000.0");
            flag = false;
            Service.showAlert("Введены некорректные данные. Используйте вещественное (либо целое) число для этого поля.");
        }else if (Double.parseDouble(productionField.getText())<0) {
            productionField.setText("1000.0");
            flag = false;
            Service.showAlert("Введены некорректные данные. Нельзя использовать отрицательные значения.");
        }
        if (!new Scanner(deprecationField.getText().replace(".", ",")).hasNextDouble()) {
            deprecationField.setText("1000.0");
            flag = false;
            Service.showAlert("Введены некорректные данные. Используйте вещественное (либо целое) число для этого поля.");
        }else if (Double.parseDouble(deprecationField.getText())<0) {
            deprecationField.setText("1000.0");
            flag = false;
            Service.showAlert("Введены некорректные данные. Нельзя использовать отрицательные значения.");
        }
        if (!new Scanner(salaryField.getText().replace(".", ",")).hasNextDouble()) {
            salaryField.setText("1000.0");
            flag = false;
            Service.showAlert("Введены некорректные данные. Используйте вещественное (либо целое) число для этого поля.");
        }else if (Double.parseDouble(salaryField.getText())<0) {
            salaryField.setText("1000.0");
            flag = false;
            Service.showAlert("Введены некорректные данные. Нельзя использовать отрицательные значения.");
        }
        if (!new Scanner(othersField.getText().replace(".", ",")).hasNextDouble()) {
            othersField.setText("1000.0");
            flag = false;
            Service.showAlert("Введены некорректные данные. Используйте вещественное (либо целое) число для этого поля.");
        }else if (Double.parseDouble(othersField.getText())<0) {
            othersField.setText("1000.0");
            flag = false;
            Service.showAlert("Введены некорректные данные. Нельзя использовать отрицательные значения.");
        }
        return flag;
    }
}
