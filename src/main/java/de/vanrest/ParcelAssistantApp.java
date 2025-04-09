package de.vanrest;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ParcelAssistantApp extends Application{

    private static final Logger log = LoggerFactory.getLogger(ParcelAssistantApp.class);

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Parcels.fxml"));
        stage.setScene(new Scene(loader.load(), 992, 700));
        stage.setTitle("Parcel Assistant (ver. 1.0)");
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

//    public static void main(String[] args) {
//        String filePath = "D:\\programming\\Parcels.txt";
//        List<Parcel> parcels = getParcels(filePath);
//
//        if (parcels != null)
//            parcels.forEach(System.out::println);
//
//        BarcodeScanner barcodeScanner = new BarcodeScanner(parcels);
//        Thread scannerThread = new Thread(barcodeScanner);
//        scannerThread.start();
//    }
//
//    public static List<Parcel> getParcels(String filePath) {
//        if (filePath.endsWith(".txt")) {
//            return new TxtReader().read(filePath);
//        } else if (filePath.endsWith(".xlsx")) {
//            return new ExcelReader().read(filePath);
//        } else {
//            System.out.println("File format is not supported");
//            log.error("Input path - {}", filePath);
//            return null;
//        }
//    }
}
