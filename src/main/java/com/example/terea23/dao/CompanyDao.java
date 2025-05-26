package com.example.terea23.dao;

import com.example.terea23.modelos.Company;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
* Clase en la que realizamos las operaciones para
* obtener, editar y eliminar las compañías a trabes de
* la interacción con la base de datos.
*
* @author Angel Muñoz
* @version 1.0
* @since 1.0
*/
public class CompanyDao {

  /**
  * Constructor sin parámetros de entrada.
  */
  public CompanyDao() {}

  /**
  * Método para buscar compañías en la base de datos.
  *
  * @param nombre nombre de compañía que vamos a buscar.
  * @see Company
  * @return companies -> lista de compañías obtenidas después
  de consultar la base de datos.
  * @throws SQLException excepción en caso de no poder realizar
  la conexión con la base de datos.
  */
  public static List<Company> buscarCompaniesNombre(String nombre) throws SQLException {

    //Lista que almacenara las compañías que vamos a buscar.
    List<Company> companies = new ArrayList<>();

    //Establecemos conexión con la base de datos que vamos a utilizar
    try (Connection conexion = ConexionBaseDatos.getConnection();
         PreparedStatement statement =
            conexion.prepareStatement(
            "SELECT id,name,partner_id,currency_id FROM res_company WHERE name LIKE ? ORDER BY id"
            )
         ) {
      statement.setString(1, "%" + nombre + "%");
      ResultSet resultSet = statement.executeQuery();

      while (resultSet.next()) {

        Company company = new Company();
        company.setId(resultSet.getInt("id"));
        company.setName(resultSet.getString("name"));
        company.setPartnerid(resultSet.getInt("partner_id"));
        company.setCurrencyid(resultSet.getInt("currency_id"));
        companies.add(company);
      }
    }

    return companies;

  }

  /**
   * Método para eliminar una compañía de la base de datos.
   *
   * @param id id de la compañía que vamos a eliminar.
   * @see Company
   * @throws SQLException excepción en caso de no poder realizar
    la conexión con la base de datos.
   */
  public static void eliminarCompany(Integer id) throws SQLException {

    String sql = "DELETE FROM res_company WHERE id = ?";

    try (Connection conexion = ConexionBaseDatos.getConnection();
         PreparedStatement preparedStatement = conexion.prepareStatement(sql)) {
      preparedStatement.setInt(1, id);
      preparedStatement.executeUpdate();
    }

  }

  /**
   * Método para insertar una compañía en la base de datos.
   *
   * @param company compañía que vamos a añadir en la base de datos.
   * @see Company
   * @throws SQLException excepción en caso de no poder realizar
   la conexión con la base de datos.
   */
  public static void insertarCompany(Company company) throws SQLException {

    String sql = "INSERT "
            + "INTO res_company (name, partner_id, currency_id,create_date) "
            + "VALUES (?, ?, ?, CURRENT_DATE)";

    try (Connection conexion = ConexionBaseDatos.getConnection();) {
      PreparedStatement preparedStatement = conexion.prepareStatement(sql);
      preparedStatement.setString(1, company.getName());
      preparedStatement.setInt(2, company.getPartnerid());
      preparedStatement.setInt(3, company.getCurrencyid());

      //Insertamos el nuevo registro
      preparedStatement.executeUpdate();

    }

  }

  /**
   * Método para editar la compañía por su ID.
   *
   * @param company compañía con los datos ya modificados.
   * @param companyCopiaId id de la compañía a modificar.
   * @see Company
   * @throws SQLException excepción en caso de no poder realizar
   la conexión con la base de datos.
   */
  public static void editarCompany(Company company, Integer companyCopiaId) throws SQLException {
    String sql = "UPDATE res_company "
            + "SET name = ?,"
            + " partner_id = ?,"
            + " currency_id = ? "
            + "WHERE id = ?";

    try (Connection conexion = ConexionBaseDatos.getConnection();
         PreparedStatement preparedStatement = conexion.prepareStatement(sql)) {
      preparedStatement.setString(1, company.getName());
      preparedStatement.setInt(2, company.getPartnerid());
      preparedStatement.setInt(3, company.getCurrencyid());
      preparedStatement.setInt(4, companyCopiaId);
      preparedStatement.executeUpdate();
    }

  }

}

