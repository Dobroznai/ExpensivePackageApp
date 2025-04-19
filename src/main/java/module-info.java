module de.vanrest.parcelassistant {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.slf4j;
    requires ch.qos.logback.classic;
    requires org.apache.poi.poi;
    requires org.apache.poi.ooxml;
    requires java.desktop;

    opens de.vanrest.controllers to javafx.fxml;
    exports de.vanrest;
    exports de.vanrest.models;
    opens de.vanrest.utils to javafx.fxml;
}