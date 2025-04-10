package de.vanrest;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.util.Objects;

public class ParcelAssistantApp extends Application{

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/ParcelsView.fxml")));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Parcel Assistant (ver. 1.0)");
        Image icon = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/image.png")));
        stage.getIcons().add(icon);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

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
