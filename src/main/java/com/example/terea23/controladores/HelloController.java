package com.example.terea23.controladores;

import com.example.terea23.dao.CompanyDao;
import com.example.terea23.modelos.Company;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.cell.PropertyValueFactory;


/**
 * Esta clase representa el controlador de la vista principal,
 * que contiene información sobre los componentes de la vista
 * y funcionalidad de estos.
 *
 * @author Angel Muñoz
 * @version 1.0
 * @since 1.0
 */
public class HelloController {

  /**
  * Constructor sin parámetros de entrada.
  */
  public HelloController() {}

  @FXML
  private TableColumn<Company, String> tcName;
  @FXML
  private TableColumn<Company, Integer> tcId;
  @FXML
  private TableColumn<Company, Integer> tcPropietario;
  @FXML
  private TableColumn<Company, Integer> tcMoneda;
  @FXML
  private TableView<Company> tvDatos;
  @FXML
  private TextField tfNombre;

  private ObservableList<Company> datos = FXCollections.observableArrayList();

  /**
   * Método que inicializa los componentes de la vista y el
   * método necesario para ver todas las empresas que hay en
   * la base de datos.
   *
   * @see Company
   * @see CompanyDao
   * @throws SQLException excepción en caso de que no se pueda contactar con la base de datos.
   */
  public void initialize() throws SQLException {

    tcName.setCellValueFactory(new PropertyValueFactory<>("name"));
    tcId.setCellValueFactory(new PropertyValueFactory<>("id"));
    tcPropietario.setCellValueFactory(new PropertyValueFactory<>("partnerid"));
    tcMoneda.setCellValueFactory(new PropertyValueFactory<>("currencyid"));

    List<Company> companies = CompanyDao.buscarCompaniesNombre("");
    datos = FXCollections.observableList(CompanyDao.buscarCompaniesNombre(""));
    tvDatos.setItems(datos);

    tfNombre.textProperty().addListener((observable, oldValue, newValue) -> {
      new Thread(() -> {
        List<Company> results = null;
        try {
          results = CompanyDao.buscarCompaniesNombre(newValue);
        } catch (SQLException e) {
          throw new RuntimeException(e);
        }

        List<Company> finalResults = results;
        Platform.runLater(() -> datos.setAll(finalResults));
      }).start();
    });

  }

  /**
  * Método para el botón de buscar que permite buscar
  * compañías en la base de datos dependiendo de lo que
  * pongamos en tfNombre.
  *
  * @param actionEvent variable que gestiona el evento realizado.
  * @see Company
  * @see CompanyDao
  */
  @FXML
  public void onBtnBuscar(ActionEvent actionEvent) {
    try {
      List<Company> companies = CompanyDao.buscarCompaniesNombre(tfNombre.getText());
      datos.setAll(companies);
      //tvDatos.setItems(datos);
    } catch (SQLException e) {
      System.err.println("Error de SQL al consultar: " + e.getMessage());
      showAlert("Error", "Error de SQL al consultar: " + e.getMessage());
    } catch (Exception e) {
      System.err.println("Error de SQL al consultar: " + e.getMessage());
    }
  }

  /**
  * Método para el botón añadir que nos permite añadir una
  * compañía a nuestra base de datos.
  *
  * @param actionEvent variable que gestiona el evento realizado.
  * @see Company
  * @see CompanyDao
  */
  public void onBtnAnadir(ActionEvent actionEvent) {
    try {

      TextInputDialog dialogName = new TextInputDialog();
      Company company = new Company();
      company.setName(tfNombre.getText());
      String comprobador = tfNombre.getText();

      while (comprobador.isEmpty()) {
        dialogName.setTitle("Selección nombre empresa.");
        dialogName.setHeaderText("Seleccione un nombre para la empresa :");
        Optional<String> result1 = dialogName.showAndWait();
        if (result1.isPresent()) {
          company.setName(result1.get());
          comprobador = company.getName();
        }
      }

      TextInputDialog dialogPropietario = new TextInputDialog();
      dialogPropietario.setTitle("Selección ID de Socio.");
      dialogPropietario.setHeaderText("Seleccione un ID de socio :");
      Optional<String> result3 = dialogPropietario.showAndWait();
      if (result3.isPresent()) {
        company.setPartnerid(Integer.parseInt(result3.get()));
      } else {
        company.setPartnerid(0);
      }

      TextInputDialog dialogCurrencyid = new TextInputDialog();
      dialogCurrencyid.setTitle("Selección ID de tipo de moneda.");
      dialogCurrencyid.setHeaderText("Seleccione un ID de tipo de moneda:");
      Optional<String> result2 = dialogCurrencyid.showAndWait();
      if (result2.isPresent()) {
        company.setCurrencyid(Integer.parseInt(result2.get()));
      } else {
        company.setCurrencyid(0);
      }

      CompanyDao.insertarCompany(company);

      datos.add(company);
      onBtnBuscar(actionEvent);

    } catch (Exception e) {
      System.err.println("Error de SQL al consultar: " + e.getMessage());
    }
  }

  /**
  * Método para el botón eliminar que nos permite eliminar la
  * compañía que seleccionemos de la base de datos.
  *
  * @param actionEvent variable que gestiona el evento realizado.
  * @see Company
  * @see CompanyDao
  */
  public void onBtnEliminar(ActionEvent actionEvent) {
    Company selectedCompany = tvDatos.getSelectionModel().getSelectedItem();
    if (selectedCompany == null) {
      showAlert("Error", "Por favor, selecciona una compañía para eliminar.");
      return;
    }

    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
    alert.setTitle("Confirmación de Eliminación");
    alert.setHeaderText("¿Estás seguro de que deseas eliminar la compañía?");
    alert.setContentText("Se eliminará la compañía: " + selectedCompany.getName());

    Optional<ButtonType> result = alert.showAndWait();
    if (result.isPresent() && result.get() == ButtonType.OK) {
      try {
        CompanyDao.eliminarCompany(selectedCompany.getId());
        showAlert("Éxito", "Compañía eliminada exitosamente.");
        datos.remove(selectedCompany);
        onBtnBuscar(actionEvent);
      } catch (SQLException e) {
        System.err.println("Error de SQL al eliminar: " + e.getMessage());
        showAlert("Error", "Error de SQL al eliminar: " + e.getMessage());
      } catch (Exception e) {
        System.err.println("Error al eliminar: " + e.getMessage());
        showAlert("Error", "Error al eliminar: " + e.getMessage());
      }
    }
  }

  /**
  * Método para botón editar que nos permite editar la
  * información de una compañía de nuestra base de datos.
  *
  * @param actionEvent variable que gestiona el evento realizado.
  * @see Company
  * @see CompanyDao
  */
  public void onBtnEditar(ActionEvent actionEvent) {
    Company selectedCompany = tvDatos.getSelectionModel().getSelectedItem();
    if (selectedCompany != null) {
      try {
        TextInputDialog dialogName = new TextInputDialog();
        dialogName.setTitle("Selección nombre nuevo de empresa.");
        dialogName.setHeaderText("Seleccione un nombre nuevo para la empresa :");
        Optional<String> result1 = dialogName.showAndWait();

        if (result1.isPresent() && !result1.get().trim().isEmpty()) {
          selectedCompany.setName(result1.get().trim());
        }

        TextInputDialog dialogCurrencyid = new TextInputDialog();
        dialogCurrencyid.setTitle("Selección ID de tipo de moneda.");
        dialogCurrencyid.setHeaderText("Seleccione un ID de tipo de moneda:");
        Optional<String> result2 = dialogCurrencyid.showAndWait();
        if (result2.isPresent() && !result2.get().trim().isEmpty()) {
          try {
            selectedCompany.setCurrencyid(Integer.parseInt(result2.get().trim()));
          } catch (NumberFormatException e) {
            showAlert("Error", "El ID de moneda debe ser un número válido.");
            return;
          }
        }

        TextInputDialog dialogPropietario = new TextInputDialog();
        dialogPropietario.setTitle("Selección ID de Socio.");
        dialogPropietario.setHeaderText("Seleccione un ID de socio :");
        Optional<String> result3 = dialogPropietario.showAndWait();

        if (result3.isPresent() && !result3.get().trim().isEmpty()) {
          try {
            selectedCompany.setPartnerid(Integer.parseInt(result3.get().trim()));
          } catch (NumberFormatException e) {
            showAlert("Error", "El ID de socio debe ser un número válido.");
            return;
          }
        }

        Integer companyId = selectedCompany.getId();
        CompanyDao.editarCompany(selectedCompany, companyId);
        onBtnBuscar(actionEvent);

      } catch (SQLException e) {
        System.err.println("Error al actualizar la compañía: " + e.getMessage());
        showAlert("Error", "Error al actualizar la compañía: " + e.getMessage());
      }
    } else {
      showAlert("Advertencia", "Por favor, selecciona una compañía para editar.");
    }
  }

  /**
  * Método que gestiona las alertas de nuestro programa.
  *
  * @param title titulo de la alerta.
  * @param message mensage de la alerta.
  */
  private void showAlert(String title, String message) {
    Alert alert = new Alert(Alert.AlertType.ERROR);
    alert.setTitle(title);
    alert.setHeaderText(null);
    alert.setContentText(message);
    alert.showAndWait();
  }

}