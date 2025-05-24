module com.example.sportify {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires com.google.zxing;
    requires com.google.zxing.javase;
    requires twilio;
    requires org.controlsfx.controls;
    requires org.apache.pdfbox;
    requires java.desktop;
    requires javafx.swing;
    requires org.json;

    opens entities to javafx.base;
    opens com.example.sportify to javafx.fxml;
    exports com.example.sportify;
}