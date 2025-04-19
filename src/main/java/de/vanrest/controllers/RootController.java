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
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
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
    private static BarcodeScanner barcodeScanner;
    private static final StringBuilder builder = new StringBuilder();

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

        setupTables();
        setupScannedParcelHandler();
        setupParcelAddingHandler();

        barcodeScanner.setRescanListener(parcel -> {
            Platform.runLater(() -> description.setText(parcel.getTrackingNumber() + " - already scanned!"));
            PauseTransition pause = new PauseTransition(Duration.seconds(5));
            pause.setOnFinished(event -> description.setText(""));
            pause.play();
        });
    }

    public static void setupScannerHandler(Scene scene) {
        scene.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.ENTER) {
                String scannedCode = builder.toString().trim().toLowerCase();
                if (!scannedCode.isEmpty()) {
                    barcodeScanner.addParcel(scannedCode);
                    builder.setLength(0);
                }
            } else
                builder.append(event.getText());
        });
    }

    private void setupScannedParcelHandler() {
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
            scannedParcelField.clear();
        });
    }

    private void setupParcelAddingHandler() {
        newParcelField.setOnAction(event -> {
            String getUserParcel = newParcelField.getText().trim();
            String[] parts = getUserParcel.split("\\s+");
            if (parts.length == 3 && parts[1].length() == 5 && parts[2].length() == 3) {
                Parcel parcel = new Parcel(parts[0], parts[1], parts[2]);
                if (!inputList.contains(parcel))
                    inputList.add(parcel);
                else
                    log.warn("Duplicate - {}", parcel.getTrackingNumber());
                newParcelField.clear();
            }
        });
    }

    private void setupTables() {
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

        setupCopyHandler();
    }

    private void setupCopyHandler() {
        inputParcelsTable1.getSelectionModel().setCellSelectionEnabled(true);
        inputParcelsTable1.getSelectionModel().setSelectionMode(javafx.scene.control.SelectionMode.MULTIPLE);

        scannedParcelsTable2.getSelectionModel().setCellSelectionEnabled(true);
        scannedParcelsTable2.getSelectionModel().setSelectionMode(javafx.scene.control.SelectionMode.MULTIPLE);

        inputParcelsTable1.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                copySelectedCellToClipboard(inputParcelsTable1);
            }
        });

        scannedParcelsTable2.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                copySelectedCellToClipboard(scannedParcelsTable2);
            }
        });

        inputParcelsTable1.setOnKeyPressed(event -> {
            if (event.isControlDown() && event.getCode().toString().equals("C")) {
                copySelectionToClipboard(inputParcelsTable1);
            }
        });

        scannedParcelsTable2.setOnKeyPressed(event -> {
            if (event.isControlDown() && event.getCode().toString().equals("C")) {
                copySelectionToClipboard(scannedParcelsTable2);
            }
        });
    }

    private void copySelectedCellToClipboard(TableView<?> table) {
        TablePosition<?, ?> position = table.getSelectionModel().getSelectedCells().get(0);
        Object cell = table.getVisibleLeafColumn(position.getColumn()).getCellData(position.getRow());

        if (cell != null) {
            ClipboardContent content = new ClipboardContent();
            content.putString(cell.toString());
            Clipboard.getSystemClipboard().setContent(content);
        }
    }

    private void copySelectionToClipboard(TableView<?> table) {
        StringBuilder clipboardString = new StringBuilder();
        ObservableList<TablePosition> positionList = table.getSelectionModel().getSelectedCells();
        int prevRow = -1;

        for (TablePosition position : positionList) {
            int row = position.getRow();
            Object cell = table.getVisibleLeafColumn(position.getColumn()).getCellData(row);

            if (cell == null)
                cell = "";

            if (prevRow == row)
                clipboardString.append('\t');
            else if (prevRow != -1)
                clipboardString.append('\n');

            clipboardString.append(cell);
            prevRow = row;
        }

        final ClipboardContent clipboardContent = new ClipboardContent();
        clipboardContent.putString(clipboardString.toString());
        Clipboard.getSystemClipboard().setContent(clipboardContent);
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
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
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