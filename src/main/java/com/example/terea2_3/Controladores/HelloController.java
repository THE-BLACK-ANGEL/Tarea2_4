package com.example.terea2_3.Controladores;

import com.example.terea2_3.Modelos.Company;
import com.example.terea2_3.DAO.CompanyDAO;
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
    private TableColumn<Company, String> tcID;
    @FXML
    private TableColumn<Company,Integer> tcPropietario;
    @FXML
    private TableColumn<Company, Integer> tcMoneda;

    @FXML
    private TableView<Company> tvDatos;

    @FXML
    private TextField tfNombre;

    public void initialize() throws SQLException {

        //Inicializamos los valores de las columnas de la tabla asignando a cada columna los valores de los registros
        //que se han almacenado en las variables de los objetos de la clase Company.
        tcName.setCellValueFactory(new PropertyValueFactory<>("name"));
        tcID.setCellValueFactory(new PropertyValueFactory<>("id"));
        tcPropietario.setCellValueFactory(new PropertyValueFactory<>("partner_id"));
        tcMoneda.setCellValueFactory(new PropertyValueFactory<>("currency_id"));
        //CARGAR DATOS DESDE EL INICIO DEL PROGRAMA DE LA BASE DE DATOS

        List<Company> companies = CompanyDAO.buscarCompaniesNombre("");
        ObservableList<Company> datos = FXCollections.observableList(companies);
        tvDatos.setItems(datos);

    }

    //Metodo del boton para buscar
    @FXML
    public void onBtnBuscar(ActionEvent actionEvent) {
        try {

            //Declaramos una lista que almacenara objetos de tipo Company obtenidos a partir de la lectura y recogida de la informacion de los registros de
            //la tabla res_company gracias al metodo de la clase CompanyDAO (interfaz que hace de intermedaria entre la base de datos de odoo y el controlador
            //de la aplicacion) de buscar companies por el nombre.
            List<Company> companies = CompanyDAO.buscarCompaniesNombre(tfNombre.getText());

            //Permitimos que se puedan ver los datos la lista companies y que esta refleje los cambios que se realizen de forma automatica
            ObservableList<Company> datos = FXCollections.observableArrayList(companies);

            //Lo siguiente que haremos es decirle a la tabla que muestre los datos de la lista
            tvDatos.setItems(datos);

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
            //Llamamos al metodo buscar para que se vean los cambios realizados ya que al buscar sin indicar ningun filtrado, nos muestra todos los elementos de la base de datos de forma actualizada
            onBtnBuscar(actionEvent);

        }catch (Exception e) {
            System.err.println("Error de SQL al consultar: " + e.getMessage());
        }
    }

    //Metodo del boton de eliminar
    public void onBtnEliminar(ActionEvent actionEvent) {
        // Obtener la compañía seleccionada en la tabla
        Company selectedCompany = tvDatos.getSelectionModel().getSelectedItem();
        //Enviar una alerta por codigo si no se ha seleccionado ninguna compañia
        if (selectedCompany == null) {
            showAlert("Error", "Por favor, selecciona una compañía para eliminar.");
            return;
        }

        // Mostrar alerta de confirmación
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

                // Actualizar la tabla después de eliminar
                onBtnBuscar(actionEvent); // Esto recargará los datos de la tabla
            } catch (SQLException e) {
                System.err.println("Error de SQL al eliminar: " + e.getMessage());
                showAlert("Error", "Error de SQL al eliminar: " + e.getMessage());
            } catch (Exception e) {
                System.err.println("Error al eliminar: " + e.getMessage());
                showAlert("Error", "Error al eliminar: " + e.getMessage());
            }
        }
    }

    @FXML
    public void onBtnEditar(ActionEvent actionEvent) {
        // Obtener la compañía seleccionada en la tabla
        Company selectedCompany = tvDatos.getSelectionModel().getSelectedItem();
        if (selectedCompany != null) {
            try {
                String companyNombreCopia = selectedCompany.getName();
                //Declaramos el dialogo para cambiar el valor de name y en caso de que no se introduzca nada el valor seguira siendo el mismo
                TextInputDialog dialogName = new TextInputDialog();
                //Variable de control
                dialogName.setTitle("Selección nombre nuevo de empresa.");
                dialogName.setHeaderText("Seleccione un nombre nuevo para la empresa :");
                Optional<String> result1 = dialogName.showAndWait();

                if (result1.isPresent()) {
                    selectedCompany.setName(result1.get());
                }
                else{
                selectedCompany.setName(selectedCompany.getName());
                }

                //Declaramos el dialogo para cambiar el valor de id de moneda y en caso de que no se introduzca nada el valor seguira siendo el mismo
                TextInputDialog dialogCurrency_id = new TextInputDialog();
                //Variable control
                dialogCurrency_id.setTitle("Selección ID de tipo de moneda.");
                dialogCurrency_id.setHeaderText("Seleccione un ID de tipo de moneda:");
                //Pedimos el ID de moneda
                Optional<String> result2 = dialogCurrency_id.showAndWait();

                if (result2.isPresent()) {
                    selectedCompany.setCurrency_id(Integer.parseInt(result2.get()));
                } else {
                    selectedCompany.setId(selectedCompany.getId());
                }

                //Declaramos el dialogo para cambiar el valor de la id del socio y en caso de que no se introduzca nada el valor seguira siendo el mismo
                TextInputDialog dialogPropietario = new TextInputDialog();
                dialogPropietario.setTitle("Selección ID de Socio.");
                dialogPropietario.setHeaderText("Seleccione un ID de socio :");
                Optional<String> result3 = dialogPropietario.showAndWait();
                if (result3.isPresent()) {
                    selectedCompany.setPartner_id(Integer.parseInt(result3.get()));
                }
                else {
                    selectedCompany.setPartner_id(selectedCompany.getId());
                }

                // Llamar al metodo de actualizarCompany en el DAO
                CompanyDAO.editarCompany(selectedCompany,companyNombreCopia);

                // Recargar la tabla para reflejar los cambios
                onBtnBuscar(actionEvent); // Esto recargará la tabla con los datos actualizados

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