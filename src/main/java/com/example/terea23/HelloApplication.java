package com.example.terea23;

import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;


/**
 * Clase que contiene la vista de la aplicación.
 *
 * @author Angel Muñoz
 * @version 1.0
 * @since 1.0
 */
public class HelloApplication extends Application {

  /**
   * Constructor sin parámetros de entrada.
  */
  public HelloApplication() {}

  /**
  * Método en el que se asigna el FXMLLoader,
  escena, estilo de la escena, título y
  resolución de la vista de la aplicación.
  *
  * @param stage escena de la aplicación.
  * @throws IOException excepción en caso de cualquier excepción
  en parte o toda la aplicación.
  */

  @Override
  public void start(Stage stage) throws IOException {
    FXMLLoader fxmlLoader;
    fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
    Scene scene = new Scene(fxmlLoader.load(), 600, 480);
    scene.getStylesheets().add(getClass().getResource("estilos.css").toExternalForm());
    stage.setTitle("Interacción base de datos con odoo.");
    stage.setScene(scene);
    stage.show();
  }

  /**
    * Main de la aplicación.
    *
    * @param args lista de cadenas que nos permite recibir
    cadenas desde la línea de comandos al ejecutar la
    aplicación.
    */
  public static void main(String[] args) {
    launch();
  }
}