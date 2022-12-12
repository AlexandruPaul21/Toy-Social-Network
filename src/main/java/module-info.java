module com.example.map_toysocialnetwork_javafx {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;
    requires java.sql;

    opens com.example.map_toysocialnetwork_javafx to javafx.fxml;
    exports com.example.map_toysocialnetwork_javafx;
    exports com.example.map_toysocialnetwork_javafx.gui;
    opens com.example.map_toysocialnetwork_javafx.gui to javafx.fxml;
    exports com.example.map_toysocialnetwork_javafx.domain;
}