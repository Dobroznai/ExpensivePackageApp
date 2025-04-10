package de.vanrest.controller;

import de.vanrest.model.Parcel;
import de.vanrest.readers.BarcodeScanner;
import de.vanrest.readers.ExcelReader;
import de.vanrest.readers.TXTReader;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.List;
import java.util.Objects;

public class Controller {

    private static final Logger log = LoggerFactory.getLogger(Controller.class);
    private final ObservableList<Parcel> inputList = FXCollections.observableArrayList();
    private final ObservableList<Parcel> scannedList = FXCollections.observableArrayList();

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

    @FXML private TextField newParcel;
    @FXML private Button getData;

    @FXML public void initialize() {
        BarcodeScanner barcodeScanner = new BarcodeScanner(inputList, scannedList);
        Thread scannerThread = new Thread(barcodeScanner);
        scannerThread.start();

        getData.setOnAction(event -> {
            String getUserParcel = newParcel.getText().trim();
            String[] parts = getUserParcel.split("\\s+");
            if (parts.length == 3) {
                Parcel parcel = new Parcel(parts[0], parts[1], parts[2]);
                if (!inputList.contains(parcel))
                    inputList.add(parcel);
                else
                    log.warn("Duplicate - {}", parcel);
            }
        });

        idColumn1.setCellValueFactory(new PropertyValueFactory<>("idParcel"));
        trackingNumberColumn1.setCellValueFactory(new PropertyValueFactory<>("trackingNumber"));
        gibitNumberColumn1.setCellValueFactory(new PropertyValueFactory<>("gibitNumber"));
        tourNumberColumn1.setCellValueFactory(new PropertyValueFactory<>("tourNumber"));
        inputParcelsTable1.setItems(inputList);

        idColumn2.setCellValueFactory(new PropertyValueFactory<>("idParcel"));
        trackingNumberColumn2.setCellValueFactory(new PropertyValueFactory<>("trackingNumber"));
        gibitNumberColumn2.setCellValueFactory(new PropertyValueFactory<>("gibitNumber"));
        tourNumberColumn2.setCellValueFactory(new PropertyValueFactory<>("tourNumber"));
        scannedParcelsTable2.setItems(scannedList);
    }

    @FXML private void onTxtFileBtnClicked() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("TXT Files", "*.txt"));
        File file = fileChooser.showOpenDialog(null);

        if (file != null) {
            TXTReader txtReader = new TXTReader();
            List<Parcel> inputParcels = txtReader.read(file.getPath());
            inputList.addAll(inputParcels);
        }
    }

    @FXML private void onExcelFileBtnClicked() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Excel Files", "*.xlsx"));
        File file = fileChooser.showOpenDialog(null);

        if (file != null) {
            ExcelReader excelReader = new ExcelReader();
            List<Parcel> inputParcels = excelReader.read(file.getPath());
            inputList.addAll(inputParcels);
        }
    }
}