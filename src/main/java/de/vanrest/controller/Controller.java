package de.vanrest.controller;

import de.vanrest.model.Parcel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;

public class Controller {

    @FXML private TableView<Parcel> inputParcelsTable1;
    @FXML private TableColumn<Parcel, Integer> idColumn1;
    @FXML private TableColumn<Parcel, String> trackingNumberColumn1;
    @FXML private TableColumn<Parcel, String> gibitNumberColumn1;
    @FXML private TableColumn<Parcel, String> tourNumberColumn1;

    @FXML private TableView<Parcel> scannedParcelsTable2;
    @FXML private TableColumn<Parcel, Integer> idColumn2;
    @FXML private TableColumn<Parcel, String> trackingNumberColumn2;
    @FXML private TableColumn<Parcel, String> gibitNumberColumn2;
    @FXML private TableColumn<Parcel, String> tourNumberColumn2;

    @FXML private Text inputText1;
    @FXML private Text scannedText2;

    @FXML public void initialize() {
        idColumn1.setCellValueFactory(new PropertyValueFactory<>("idParcel"));
        trackingNumberColumn1.setCellValueFactory(new PropertyValueFactory<>("trackingNumber"));
        gibitNumberColumn1.setCellValueFactory(new PropertyValueFactory<>("gibitNumber"));
        tourNumberColumn1.setCellValueFactory(new PropertyValueFactory<>("tourNumber"));

        idColumn2.setCellValueFactory(new PropertyValueFactory<>("idParcel"));
        trackingNumberColumn2.setCellValueFactory(new PropertyValueFactory<>("trackingNumber"));
        gibitNumberColumn2.setCellValueFactory(new PropertyValueFactory<>("gibitNumber"));
        tourNumberColumn2.setCellValueFactory(new PropertyValueFactory<>("tourNumber"));


        ObservableList<Parcel> inputParcels = FXCollections.observableArrayList(
                new Parcel("h1004838907454542", "80-31", "362"),
                new Parcel("h1004838907490837", "80-32", "646")
        );

        ObservableList<Parcel> scannedParcels = FXCollections.observableArrayList(
                new Parcel("h1004838956334644", "80-32", "362")
        );

        inputParcelsTable1.setItems(inputParcels);
        scannedParcelsTable2.setItems(scannedParcels);
    }
}