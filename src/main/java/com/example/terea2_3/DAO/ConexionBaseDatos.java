package com.example.terea2_3.DAO;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionBaseDatos {
    //Creamos la conexion
    public static Connection connection = null ;
    //Metodo para obtener los datos de la conexion
    public static Connection getConnection() {

        String dbName = "Company";
        String dbPort = "5432";
        String dbUser  = "odoo";
        String dbPass = "odoo";

        try {
            Class.forName("org.postgresql.Driver");
            String url = "jdbc:postgresql://localhost:" + dbPort + "/" + dbName;
            // Conseguir dirección de conexión física
            connection = DriverManager.getConnection(url, dbUser , dbPass);

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
