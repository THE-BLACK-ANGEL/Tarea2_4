/**
 * Módulo principal de la aplicación Tarea2_4.
 */
module com.example.terea23 {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires java.sql;
    requires javafx.base;
    requires javafx.graphics;
    requires org.postgresql.jdbc;

    opens com.example.terea23 to javafx.fxml;
    opens com.example.terea23.modelos to javafx.base;
    exports com.example.terea23;
    exports com.example.terea23.modelos;
    opens com.example.terea23.controladores to javafx.fxml;

}
