package de.vanrest.controllers;

import de.vanrest.models.Parcel;
import de.vanrest.readers.BarcodeScanner;
import de.vanrest.readers.ExcelReader;
import de.vanrest.readers.TXTReader;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.stage.FileChooser;
import javafx.util.Duration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

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
    @FXML private TextField scannedParcelField;
    @FXML private Label description;

    @FXML public void initialize() {
        barcodeScanner = new BarcodeScanner(inputList, scannedList);
        Thread scannerThread = new Thread(barcodeScanner);
        scannerThread.setDaemon(true);
        scannerThread.start();

        barcodeScanner.setListener(parcel ->
            Platform.runLater(() -> TourNumberController.show(parcel.getTourNumber())));

        barcodeScanner.setRescanListener(trackingNumber -> {
            Platform.runLater(() -> description.setText(trackingNumber + " - already scanned!"));
            PauseTransition pause = new PauseTransition(Duration.seconds(5));
            pause.setOnFinished(event -> description.setText(""));
            pause.play();
        });

        scannedParcelField.setOnAction(event -> {
            String inputLine = scannedParcelField.getText().trim();
            Optional<Parcel> optParcel = inputList.stream()
                    .filter(parcel -> parcel.getTrackingNumber().equalsIgnoreCase(inputLine))
                    .findFirst();
            if (optParcel.isPresent()) {
                Parcel parcel = optParcel.get();
                parcel.setTrackingNumber("🔴 " + inputLine);
                TourNumberController.show(parcel.getTourNumber());
                scannedList.add(parcel);
                inputList.remove(parcel);
            } else {
                log.warn("Parcel not found {}", inputLine);
            }
        });

        newParcelField.setOnAction(event -> {
            String getUserParcel = newParcelField.getText().trim();
            String[] parts = getUserParcel.split("\\s+");
            if (parts.length == 3 && parts[1].length() == 5 && parts[2].length() == 3) {
                Parcel parcel = new Parcel(parts[0], parts[1], parts[2]);
                if (!inputList.contains(parcel))
                    inputList.add(parcel);
                else
                    log.warn("Duplicate - {}", parcel.getTrackingNumber());
            }
        });

        idColumn1.setCellValueFactory(new PropertyValueFactory<>("idParcel"));
        trackingNumberColumn1.setCellValueFactory(new PropertyValueFactory<>("trackingNumber"));
        gibitNumberColumn1.setCellValueFactory(new PropertyValueFactory<>("gibitNumber"));
        tourNumberColumn1.setCellValueFactory(new PropertyValueFactory<>("tourNumber"));
        inputParcelsTable1.setItems(inputList);
        inputParcelsTable1.setOnKeyPressed(event -> {
            if (event.isControlDown() && event.getCode().toString().equals("C")) {
                Parcel selected = inputParcelsTable1.getSelectionModel().getSelectedItem();
                String textToCopy = selected.getTrackingNumber();
                ClipboardContent content = new ClipboardContent();
                content.putString(textToCopy);
                Clipboard.getSystemClipboard().setContent(content);
            }
        });

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
                for (Parcel parcel : inputParcels) {
                    if (!inputList.contains(parcel))
                        inputList.add(parcel);
                    else
                        log.warn("Duplicate - {}", parcel.getTrackingNumber());
                }
            } else if (file.getName().endsWith(".xlsx")) {
                ExcelReader excelReader = new ExcelReader();
                List<Parcel> inputParcels = excelReader.read(file.getPath());
                for (Parcel parcel : inputParcels) {
                    if (!inputList.contains(parcel))
                        inputList.add(parcel);
                    else
                        log.warn("Duplicate - {}", parcel.getTrackingNumber());
                }
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
                writer.write("Tracking Number" + "    " + "Gibit Number" + "    " + "Tour Number\n");
                for (Parcel parcel : scannedList) {
                    writer.write(parcel.getTrackingNumber() + "     " + parcel.getGibitNumber() + "           " +
                                        parcel.getTourNumber() + "\n");
                }
            } catch (IOException e) {
                log.error("Error saving file {}", e.getMessage());
            }
        }
    }

    @FXML private void onClearListsBtnClicked() {
        inputList.clear();
        scannedList.clear();
    }

    @FXML private void onLogBtnClicked() {
        try {
            File logFile = new File("logs/app.log");
            if (logFile.exists())
                Desktop.getDesktop().open(logFile);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
}