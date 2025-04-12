package de.vanrest.controllers;

import de.vanrest.models.Parcel;
import de.vanrest.readers.BarcodeScanner;
import de.vanrest.readers.ExcelReader;
import de.vanrest.readers.TXTReader;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class RootController {

    private static final Logger log = LoggerFactory.getLogger(RootController.class);
    private final ObservableList<Parcel> inputList = FXCollections.observableArrayList();
    private final ObservableList<Parcel> scannedList = FXCollections.observableArrayList();
    BarcodeScanner barcodeScanner;

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

    @FXML private TextField newParcelField;
    @FXML private Button addNewParcel;
    @FXML private Button clearLists;

    @FXML public void initialize() {
        barcodeScanner = new BarcodeScanner(inputList, scannedList);
        Thread scannerThread = new Thread(barcodeScanner);
        scannerThread.start();

        barcodeScanner.setListener(parcel ->
            Platform.runLater(() -> TourNumberController.show(parcel.getTourNumber())));

        addNewParcel.setOnAction(event -> {
            String getUserParcel = newParcelField.getText().trim();
            String[] parts = getUserParcel.split("\\s+");
            if (parts.length == 3) {
                Parcel parcel = new Parcel(parts[0], parts[1], parts[2]);
                if (!inputList.contains(parcel))
                    inputList.add(parcel);
                else
                    log.warn("Duplicate - {}", parcel);
            }
        });

        clearLists.setOnAction(event -> {
            inputList.clear();
            scannedList.clear();
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

    @FXML private void onUploadFileBtnClicked() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("TXT Files", "*.txt"));
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Excel Files", "*.xlsx"));
        File file = fileChooser.showOpenDialog(null);

        if (file != null) {
            if (file.getName().endsWith(".txt")) {
                TXTReader txtReader = new TXTReader();
                List<Parcel> inputParcels = txtReader.read(file.getPath());
                inputList.addAll(inputParcels);
            } else if (file.getName().endsWith(".xlsx")) {
                ExcelReader excelReader = new ExcelReader();
                List<Parcel> inputParcels = excelReader.read(file.getPath());
                inputList.addAll(inputParcels);
            }
        }
    }

    @FXML private void onSaveBtnClicked() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("TXT Files", "*.txt"));
        fileChooser.setInitialFileName("scanned_parcels.txt");
        File file = fileChooser.showSaveDialog(null);
        if (file != null) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))){
                for (Parcel parcel : scannedList) {
                    writer.write(parcel.getTrackingNumber() + "    " + parcel.getGibitNumber() + "    " + parcel.getTourNumber() + System.lineSeparator());
                }
            } catch (IOException e) {
                log.error("Error saving file {}", e.getMessage());
            }
        }
    }
}