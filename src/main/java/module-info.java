module com.example.terea2_3 {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires java.sql;
    requires javafx.base;
    requires javafx.graphics;


    opens com.example.terea2_3 to javafx.fxml;
    // Abre el paquete com.example.terea2_3.Modelos al módulo javafx.base, permitiendo que JavaFX acceda a las clases dentro de este paquete.
    opens com.example.terea2_3.Modelos to javafx.base;
    exports com.example.terea2_3;
    // Exporta el paquete com.example.terea2_3.Modelos, permitiendo que otros módulos accedan a las clases públicas dentro de este paquete.
    exports com.example.terea2_3.Modelos;
    opens com.example.terea2_3.Controladores to javafx.fxml; // Permite el acceso a FXML desde la carpeta de controladores

}
