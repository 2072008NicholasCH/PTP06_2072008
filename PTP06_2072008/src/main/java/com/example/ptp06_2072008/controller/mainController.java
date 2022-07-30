package com.example.ptp06_2072008.controller;

import com.example.ptp06_2072008.HelloApplication;
import com.example.ptp06_2072008.model.Category;
import com.example.ptp06_2072008.model.Item;
import com.example.ptp06_2072008.util.utilConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import com.example.ptp06_2072008.dao.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.view.JasperViewer;

import java.io.IOException;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

public class mainController {
    public MenuBar menuBar;
    @FXML
    public TableView<Item> tableView;
    @FXML
    public TableColumn<Object, Object> colId;
    @FXML
    public TableColumn<Object, Object> colName;
    @FXML
    public TableColumn<Object, Object> colPrice;
    @FXML
    public TableColumn<Object, Object> colKat;
    @FXML
    public TextField txtId;
    @FXML
    public TextField txtName;
    @FXML
    public TextField txtPrice;
    @FXML
    public TextArea txtaDesc;
    @FXML
    public ComboBox<Category> cBoxKat;
    @FXML
    public Button btnSave;
    @FXML
    public Button btnReset;
    public Button btnUpdate;
    public Button btnDel;
    ObservableList <Category> katList;
    ObservableList <Item> itemList;
    categoryDao katDao = new categoryDao();
    itemDao itemDao = new itemDao();
    @FXML
    private Stage stage;
    @FXML
    private FXMLLoader fxmlLoader;
    private ObservableList<Category> namaKategori;

    public void initialize() throws IOException {
        fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("category-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 400);
        stage = new Stage();
        stage.setTitle("Category Management");
        stage.setScene(scene);
        stage.initModality(Modality.APPLICATION_MODAL);
        btnDel.setDisable(true);
        btnUpdate.setDisable(true);
        tampilan();
    }

    public void getSelectedItem() {
        txtId.setText(String.valueOf(tableView.getSelectionModel().getSelectedItem().getId()));
        txtName.setText(tableView.getSelectionModel().getSelectedItem().getNama());
        txtPrice.setText(String.valueOf(tableView.getSelectionModel().getSelectedItem().getPrice()));
        txtaDesc.setText(tableView.getSelectionModel().getSelectedItem().getDesc());
        cBoxKat.setValue(tableView.getSelectionModel().getSelectedItem().getCategory());
        txtId.setDisable(true);
        btnSave.setDisable(true);
        btnDel.setDisable(false);
        btnUpdate.setDisable(false);
    }

    public void addData(ActionEvent actionEvent) {

        if ((txtId.getText().equals("")) || (txtPrice.getText().equals("")) || (txtName.getText().equals("")) || (txtaDesc.getText().equals("")) || (cBoxKat.getValue() == null)){
            alert();
        } else {
            int id = Integer.parseInt(txtId.getText());
            String nama = txtName.getText();
            double price = Double.parseDouble(txtPrice.getText());
            String desc = txtaDesc.getText();
            Category cBox = cBoxKat.getValue();
            itemDao.setData(new Item(id, nama, price, desc, cBox));
            reset();
        }
        tampilan();

    }

    public void alert (){
        Alert alert = new Alert(Alert.AlertType.ERROR, "Please Fill the Field Properly", ButtonType.OK);
        alert.showAndWait();
    }

    public void reset() {
        txtaDesc.clear();
        txtId.clear();
        txtName.clear();
        txtPrice.clear();
        cBoxKat.setValue(null);
        btnSave.setDisable(false);
        txtId.setDisable(false);
        btnDel.setDisable(true);
        btnUpdate.setDisable(true);
    }

    public void upData(ActionEvent actionEvent) {
        int id = Integer.parseInt(txtId.getText());
        String nama = txtName.getText();
        double price = Double.parseDouble(txtPrice.getText());
        String desc = txtaDesc.getText();
        Category cBox = cBoxKat.getValue();
        itemDao.upData(new Item(id, nama, price, desc, cBox));
        tampilan();
        reset();
    }

    public void delData(ActionEvent actionEvent) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure want to delete this selected data?", ButtonType.OK, ButtonType.CANCEL);
        alert.showAndWait();
        if (alert.getResult() == ButtonType.OK) {
            itemDao.delData(tableView.getSelectionModel().getSelectedItem());
            tampilan();
            reset();
        }
    }

    public void tampilan(){
        itemList = itemDao.getData();
        tableView.setItems(itemList);
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colName.setCellValueFactory(new PropertyValueFactory<>("nama"));
        colPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        colKat.setCellValueFactory(new PropertyValueFactory<>("category"));
        katList = katDao.getData();
        cBoxKat.setItems(katList);
    }

    public void gotoKategori() {
        stage.showAndWait();
        kategoriController kControl = fxmlLoader.getController();
        katList = kControl.getKatList();
        namaKategori = FXCollections.observableArrayList();
        cBoxKat.setItems(namaKategori);
        cBoxKat.getSelectionModel().select(0);
        tampilan();
    }

    public void gotoClose(ActionEvent actionEvent) {
        txtaDesc.getScene().getWindow().hide();
    }

    public void gotoSpRep(ActionEvent actionEvent) {
        Connection conn = utilConnection.getConnection();
        JasperPrint jp;
        Map param = new HashMap();
        try {
            jp = JasperFillManager.fillReport("report/SimpleGroup_PTP06.jasper", param, conn);
            JasperViewer jv = new JasperViewer(jp, false);
            jv.setTitle("Simple Report");
            jv.setVisible(true);
        } catch (JRException e) {
            throw new RuntimeException(e);
        }
    }

    public void gotoGrRep(ActionEvent actionEvent) {
        Connection conn = utilConnection.getConnection();
        JasperPrint jp;
        Map param = new HashMap();
        try {
            jp = JasperFillManager.fillReport("report/GroupReport_PTP06.jasper", param, conn);
            JasperViewer jv = new JasperViewer(jp, false);
            jv.setTitle("Group Report");
            jv.setVisible(true);
        } catch (JRException e) {
            throw new RuntimeException(e);
        }
    }
}
