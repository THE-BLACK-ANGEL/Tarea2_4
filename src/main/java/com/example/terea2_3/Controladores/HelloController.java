package com.example.terea2_3.Controladores;

import com.example.terea2_3.Modelos.Company;
import com.example.terea2_3.DAO.CompanyDAO;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class HelloController {

    @FXML
    private TableColumn<Company, String> tcName;
    @FXML
    private TableColumn<Company, Integer> tcID;
    @FXML
    private TableColumn<Company,Integer> tcPropietario;
    @FXML
    private TableColumn<Company, Integer> tcMoneda;

    @FXML
    private TableView<Company> tvDatos;

    @FXML
    private TextField tfNombre;

    private ObservableList<Company> datos = FXCollections.observableArrayList();

    public void initialize() throws SQLException {

        //Inicializamos los valores de las columnas de la tabla asignando a cada columna los valores de los registros
        //que se han almacenado en las variables de los objetos de la clase Company.
        tcName.setCellValueFactory(new PropertyValueFactory<>("name"));
        tcID.setCellValueFactory(new PropertyValueFactory<>("id"));
        tcPropietario.setCellValueFactory(new PropertyValueFactory<>("partner_id"));
        tcMoneda.setCellValueFactory(new PropertyValueFactory<>("currency_id"));
        //CARGAR DATOS DESDE EL INICIO DEL PROGRAMA DE LA BASE DE DATOS

        List<Company> companies = CompanyDAO.buscarCompaniesNombre("");
        datos = FXCollections.observableList(CompanyDAO.buscarCompaniesNombre(""));
        tvDatos.setItems(datos);

        tfNombre.textProperty().addListener((observable, oldValue, newValue) -> {

            //Realizar la búsqueda en un hilo
            new Thread(() -> {
                List<Company> results = null;
                try {
                    results = CompanyDAO.buscarCompaniesNombre(newValue);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }

                //Actualizar la interfaz gráfica en el hilo principal
                List<Company> finalResults = results;
                Platform.runLater(() -> datos.setAll(finalResults));
            }).start();
        });


    }

    //Metodo del boton para buscar
    @FXML
    public void onBtnBuscar(ActionEvent actionEvent) {
        try {

            //Declaramos una lista que almacenara objetos de tipo Company obtenidos a partir de la lectura y recogida de la informacion de los registros de
            //la tabla res_company gracias al metodo de la clase CompanyDAO (interfaz que hace de intermedaria entre la base de datos de odoo y el controlador
            //de la aplicacion) de buscar companies por el nombre.
            List<Company> companies = CompanyDAO.buscarCompaniesNombre(tfNombre.getText());
            //Lo siguiente que haremos es decirle a la tabla que muestre los datos de la lista asignando a la lista observable los datos encontrados tras la busqueda
            datos.setAll(companies);
            //tvDatos.setItems(datos);

        } catch (SQLException e) {
            System.err.println("Error de SQL al consultar: " + e.getMessage());
            showAlert("Error", "Error de SQL al consultar: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Error de SQL al consultar: " + e.getMessage());
        }
    }

    //Metodo del boton para añadir
    public void onBtnAnadir(ActionEvent actionEvent) {
        //Creamos los dialogos para asignarle valores al objeto al que insertaremos mas tarde en la base de datos
        try {
            TextInputDialog dialogCurrency_id = new TextInputDialog();
            TextInputDialog dialogPropietario = new TextInputDialog();
            TextInputDialog dialogName = new TextInputDialog();
            Company company = new Company();
            //Establecemos como el nombre de la compañia al nombre que hayamos introducido el textField y si no hay nada en este pedimos que se introduzca un nombre
            company.setName(tfNombre.getText());
            //Creamos una variable de control para que mientras no haya asignado un nombre de empresa siga pidiendolo
            String comprobador = tfNombre.getText();
            //Bucle que se realizara mientras el nombre
            while(comprobador.isEmpty()){
                dialogName.setTitle("Selección nombre empresa.");
                dialogName.setHeaderText("Seleccione un nombre para la empresa :");
                Optional<String> result1 = dialogName.showAndWait();
                if (result1.isPresent()) {
                    company.setName(result1.get());
                    comprobador = company.getName();
                }
            }

            //Establecemos los titulos y las cabeceras del dialog para el ID de moneda
            dialogCurrency_id.setTitle("Selección ID de tipo de moneda.");
            dialogCurrency_id.setHeaderText("Seleccione un ID de tipo de moneda:");
            //Pedimos el ID de moneda
            Optional<String> result2 = dialogCurrency_id.showAndWait();
            if (result2.isPresent()) {
                company.setCurrency_id(Integer.parseInt(result2.get()));
            } else {
                company.setCurrency_id(0);
            }

            //Pedimos el id de socio y establecemos un titulo y cabecera para este dialog
            dialogPropietario.setTitle("Selección ID de Socio.");
            dialogPropietario.setHeaderText("Seleccione un ID de socio :");
            Optional<String> result3 = dialogPropietario.showAndWait();
            if (result3.isPresent()) {
                company.setPartner_id(Integer.parseInt(result3.get()));
            }
            else {
                company.setPartner_id(0);
            }
            CompanyDAO.insertarCompany(company);

            datos.add(company);
            onBtnBuscar(actionEvent);

        }catch (Exception e) {
            System.err.println("Error de SQL al consultar: " + e.getMessage());
        }
    }

    //Metodo del boton de eliminar
    public void onBtnEliminar(ActionEvent actionEvent) {
        //Obtener la compañía seleccionada en la tabla
        Company selectedCompany = tvDatos.getSelectionModel().getSelectedItem();
        //Enviar una alerta por codigo si no se ha seleccionado ninguna compañia
        if (selectedCompany == null) {
            showAlert("Error", "Por favor, selecciona una compañía para eliminar.");
            return;
        }

        //Mostrar alerta de confirmación
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmación de Eliminación");
        alert.setHeaderText("¿Estás seguro de que deseas eliminar la compañía?");
        alert.setContentText("Se eliminará la compañía: " + selectedCompany.getName());

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                //metodo de eliminacion en DAO
                CompanyDAO.eliminarCompany(selectedCompany.getId());
                showAlert("Éxito", "Compañía eliminada exitosamente.");

                //Actualizar la tabla después de eliminar
                datos.remove(selectedCompany);
                //Esto recarga los datos de la tabla ya que llamamos al metodo del boton buscar sin nada dentro de forma que muestra toda la informacion de la tabla
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
    public void onBtnEditar(ActionEvent actionEvent) {
        //Obtener la compañía seleccionada en la tabla
        Company selectedCompany = tvDatos.getSelectionModel().getSelectedItem();
        if (selectedCompany != null) {
            try {
                Integer companyID = selectedCompany.getId();

                //Diálogo para cambiar el nombre de la empresa
                TextInputDialog dialogName = new TextInputDialog();
                dialogName.setTitle("Selección nombre nuevo de empresa.");
                dialogName.setHeaderText("Seleccione un nombre nuevo para la empresa :");
                Optional<String> result1 = dialogName.showAndWait();

                if (result1.isPresent() && !result1.get().trim().isEmpty()) {
                    selectedCompany.setName(result1.get().trim());
                }

                //Diálogo para cambiar el ID de moneda
                TextInputDialog dialogCurrency_id = new TextInputDialog();
                dialogCurrency_id.setTitle("Selección ID de tipo de moneda.");
                dialogCurrency_id.setHeaderText("Seleccione un ID de tipo de moneda:");
                Optional<String> result2 = dialogCurrency_id.showAndWait();

                if (result2.isPresent() && !result2.get().trim().isEmpty()) {
                    try {
                        selectedCompany.setCurrency_id(Integer.parseInt(result2.get().trim()));
                    } catch (NumberFormatException e) {
                        showAlert("Error", "El ID de moneda debe ser un número válido.");
                        return; //Salimos del metodo en caso de haber alguna excepcion
                    }
                }

                //Diálogo para cambiar el ID del socio
                TextInputDialog dialogPropietario = new TextInputDialog();
                dialogPropietario.setTitle("Selección ID de Socio.");
                dialogPropietario.setHeaderText("Seleccione un ID de socio :");
                Optional<String> result3 = dialogPropietario.showAndWait();

                if (result3.isPresent() && !result3.get().trim().isEmpty()) {
                    try {
                        selectedCompany.setPartner_id(Integer.parseInt(result3.get().trim()));
                    } catch (NumberFormatException e) {
                        showAlert("Error", "El ID de socio debe ser un número válido.");
                        return;
                    }
                }

                //Llamar al metodo para editar la compañia en la base de dato
                CompanyDAO.editarCompany(selectedCompany, companyID);

                //Recargar la tabla para reflejar los cambios
                onBtnBuscar(actionEvent); //Esto recargará la tabla con los datos actualizados

            } catch (SQLException e) {
                System.err.println("Error al actualizar la compañía: " + e.getMessage());
                showAlert("Error", "Error al actualizar la compañía: " + e.getMessage());
            }
        } else {
            showAlert("Advertencia", "Por favor, selecciona una compañía para editar.");
        }
    }
    //Alertas del programa en caso de un error SQLException
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}