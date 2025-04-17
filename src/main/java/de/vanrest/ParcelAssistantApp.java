package de.vanrest;

import de.vanrest.controllers.RootController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.util.Objects;

public class ParcelAssistantApp extends Application {

    @Override
    public void start(Stage stage) throws Exception {
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/fxml/RootView.fxml")));
            Scene scene = new Scene(root);
            stage.setScene(scene);
            scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/css/style.css")).toExternalForm());
            stage.setTitle("Parcel Assistant (ver. 1.0)");
            Image icon = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/icon.png")));
            stage.getIcons().add(icon);
            stage.setResizable(false);
            stage.show();
            RootController.setupScannerHandler(scene);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
