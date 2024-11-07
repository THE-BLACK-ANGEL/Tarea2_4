package com.example.terea2_3.DAO;

import com.example.terea2_3.Modelos.Company;

import javax.swing.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CompanyDAO {
    //Metodo para buscar la compañia en la base de datos
    public static List<Company> buscarCompaniesNombre(String nombre) throws SQLException {

        //Lista que almacenara las compañias que vamos a buscar.
        List<Company> companies = new ArrayList<>();

        //Establecemos conexion con la base de datos que vamos a utilizar
        try (Connection conexion = ConexionBaseDatos.getConnection();

             //Realizamos una consulta para conseguir los datos de los registros que nos interese, para mas tarde, asignar estos datos
             //a variables de objetos de tipo Company y añadir de esta forma los distintos registros de la tabla de la base de datos
             //a la lista de tipo Company ("companies") en forma de objetos del mismo tipo que la lista (Company).
             //IMPORTANTE : Hemos realizado la consulta asegurando que nos se pueda realizar inyeccion SQL gracias a ? y mas tarde
             PreparedStatement statement = conexion.prepareStatement("SELECT id, name, partner_id, currency_id FROM res_company WHERE name LIKE ? ORDER BY id");
             ) {
                statement.setString(1, "%" + nombre + "%");
                ResultSet resultSet = statement.executeQuery();

            //Mientras hayan registros en la tabla a la que accedemos se almacenaran los datos de los registros de la siguiente manera
            //a un objeto temporal de tipo Company para finalmente añadir estos a la lista de tipo Company "companies".
            while (resultSet.next()) {

                //Creamos un objeto tipo Company que mas tarde añadiremos a la lista de este tipo creada anteriormente.
                Company company = new Company();

                //Establecemos el valor de la variable id con el valor de la columna "id" del registro correspondiente de la tabla de la base de datos.
                company.setId(resultSet.getInt("id"));

                //Hacemos lo mismo con las demás columnas de las que nos interesa almacenar el valor en el objeto.
                company.setName(resultSet.getString("name"));
                company.setPartner_id(resultSet.getInt("partner_id"));
                company.setCurrency_id(resultSet.getInt("currency_id"));

                //añadimos el objeto a la lista una vez realizado la asignacion de valores de las variables de este.
                companies.add(company);
            }
        }


        //Devolvemos por parametro la lista para ser usada en otras clases (En nuestro caso desde controlador/es).
        return companies;
    }
    //Metodo para eliminar compañia de la base de datos
    public static void eliminarCompany(Integer id) throws SQLException {
        //Consulta para eliminar una compañia por si ID
        String sql = "DELETE FROM res_company WHERE id = ?";

        try (Connection conexion = ConexionBaseDatos.getConnection();
             PreparedStatement preparedStatement = conexion.prepareStatement(sql)) {
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();

        }
    }
    //Metodo para insertar una compañia en la base de datos
    public static void insertarCompany(Company company) throws SQLException {
        //Consulta para insertar un registro con informacion que luego le determinaremos la informacion que le vamos a meter por las posiciones en las que se le quiere dar un valor a estos
        String sql = "INSERT INTO res_company (name, partner_id, currency_id,create_date) VALUES (?, ?, ?, CURRENT_DATE)";

        try (Connection conexion = ConexionBaseDatos.getConnection();){
            PreparedStatement preparedStatement = conexion.prepareStatement(sql);
            preparedStatement.setString(1, company.getName());
            preparedStatement.setInt(2, company.getPartner_id());
            preparedStatement.setInt(3, company.getCurrency_id());

            //Insertamos el nuevo registro
            preparedStatement.executeUpdate();

        }

    }
    //Metodo para editar la informacion de una compañia de la base de datos
    public static void editarCompany(Company company,String companyCopia) throws SQLException {
        String sql = "UPDATE res_company SET name = ?, partner_id = ?, currency_id = ? WHERE name =" + "'"+companyCopia+"'";

        try (Connection conexion = ConexionBaseDatos.getConnection();
             PreparedStatement preparedStatement = conexion.prepareStatement(sql)) {

            preparedStatement.setString(1, company.getName());
            preparedStatement.setInt(2, company.getPartner_id());
            preparedStatement.setInt(3, company.getCurrency_id());

            preparedStatement.executeUpdate();
        }
    }

}

