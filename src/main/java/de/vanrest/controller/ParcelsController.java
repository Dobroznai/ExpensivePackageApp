package de.vanrest.controller;

import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class ParcelsController {

    @FXML
    private ListView<String> inputList;

    @FXML
    private ListView<String> scannedList;

    private final ObservableList<String> inputItems = FXCollections.observableArrayList();
    private final ObservableList<String> scannedItems = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        // Прив'язуємо списки до ListView
        inputList.setItems(inputItems);
        scannedList.setItems(scannedItems);

        // Прикладові дані
        inputItems.addAll("DE123456", "DE654321", "DE111222");
    }

    // Метод для сканування (наприклад, переносить перший елемент з input до scanned)
    public void scanNextParcel() {
        if (!inputItems.isEmpty()) {
            String parcel = inputItems.remove(0);
            scannedItems.add(parcel);
        }
    }

    // Інші методи можна додати за потребою (очищення списків, додавання посилок тощо)
}