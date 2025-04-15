package de.vanrest.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Objects;

public class TourNumberController {

    private static final Logger log = LoggerFactory.getLogger(TourNumberController.class);
    @FXML private Label tourNumberLabel;
    private static Stage dialodStage = null;
    private static TourNumberController controller = null;

    public void setTourNumber(String tourNumber) {
        tourNumberLabel.setText(tourNumber);
    }

    public static void show(String tourNumber) {
        if (dialodStage != null && dialodStage.isShowing()) {
            controller.setTourNumber(tourNumber);
            return;
        } try {
            FXMLLoader loader = new FXMLLoader(TourNumberController.class.getResource("/fxml/TourNumber.fxml"));
            Parent root = loader.load();

            controller = loader.getController();
            controller.setTourNumber(tourNumber);

            dialodStage = new Stage();
            dialodStage.setTitle("Tour Number");
            Scene scene = new Scene(root, 250, 183);
            dialodStage.setScene(scene);
            scene.getStylesheets().add(Objects.requireNonNull(TourNumberController.class.getResource("/css/style.css")).toExternalForm());
            dialodStage.setResizable(false);

            Image icon = new Image(Objects.requireNonNull(TourNumberController.class.getResourceAsStream("/images/icon.png")));
            dialodStage.getIcons().add(icon);

            dialodStage.setOnHidden(event -> {
                dialodStage = null;
                controller = null;
            });

            dialodStage.show();

        } catch (IOException e) {
            log.error("Error displaying window for showing tour number");
        }
    }
}
