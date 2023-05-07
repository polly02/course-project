package com.example.pricemanager.controller;

import com.example.pricemanager.dto.ChartDto;
import com.example.pricemanager.message.Action;
import com.example.pricemanager.service.Service;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;

import java.util.List;

public class StatisticTabController implements Controller {

    @FXML
    private LineChart<String, Double> chart;

    @FXML
    private Button costButton;

    @FXML
    private Button priceButton;

    @FXML
    private AnchorPane workPlace;

    @FXML
    void initialize() {
        chart.setLegendVisible(false);
        chart.setAnimated(false);
        onClickPriceButton();
    }

    @FXML
    void onClickCostButton() {
        client.writeObject(Action.GET_DATA_FOR_COST_CHART);
        client.writeObject(currentProduct.getId());
        List<ChartDto> chartDtos = (List<ChartDto>) client.readObject();
        initChart(chartDtos);
    }

    @FXML
    void onClickPriceButton() {
        client.writeObject(Action.GET_DATA_FOR_PRICE_CHART);
        client.writeObject(currentProduct.getId());
        List<ChartDto> chartDtos = (List<ChartDto>) client.readObject();
        initChart(chartDtos);
    }

    private void initChart(List<ChartDto> chartDtos) {
        chart.getData().clear();
        XYChart.Series valueSeries = new XYChart.Series();
        for (int i = 0; i < chartDtos.size(); i += 1) {
            valueSeries.getData().add(new XYChart.Data(String.valueOf(chartDtos.get(i).getDate()), chartDtos.get(i).getPrice()));
        }
        chart.getData().add(valueSeries);
    }

    @FXML
    void onClickSaveButton() {
        Service.printToPDF(chart);
    }

}
