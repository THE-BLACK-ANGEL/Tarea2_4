package com.example.terea23.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Clase que funciona como conector de nuestra
 * aplicación con la base de datos.
 *
 * @author Angel Muñoz
 * @version 1.0
 * @since 1.0
 */
public class ConexionBaseDatos {

  /**
  * Constructor sin parámetros de entrada.
  */
  public ConexionBaseDatos() {}

  /**
  * Variable que almacena la dirección física y verifica la
  * conexión con la base de datos.
  */
  public static Connection connection = null;

  /**
   * Método en el que obtenemos la conexión con la
   base de datos.
   *
   * @return connection -> variable que contiene la
   conexión realizada con la base de datos.
   */
  public static Connection getConnection() {

    String dbName = "Angel";
    String dbPort = "5432";
    String dbUser  = "odoo";
    String dbPass = "odoo";

    try {
      Class.forName("org.postgresql.Driver");
      String url = "jdbc:postgresql://localhost:" + dbPort + "/" + dbName;
      connection = DriverManager.getConnection(url, dbUser, dbPass);
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
      System.exit(-1);
    } catch (SQLException e) {
      e.printStackTrace();
      System.exit(-1);
    }

    return connection;

  }
}
