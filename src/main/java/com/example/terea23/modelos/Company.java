package com.example.terea23.modelos;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 * Clase compañía que usaremos para almacenar
 * las compañías que obtengamos de la base de datos
 * y para realizar las operaciones necesarias.
 *
 * @author Angel Muñoz
 * @version 1.0
 * @since 1.0
 */
public class Company {

  private SimpleIntegerProperty id;
  private SimpleStringProperty name;
  private SimpleIntegerProperty partnerid;
  private SimpleIntegerProperty currencyid;

  /**
  * Constructor sin parámetros de una compañía.
  */
  public Company() {
    this.id = new SimpleIntegerProperty();
    this.name = new SimpleStringProperty();
    this.partnerid = new SimpleIntegerProperty();
    this.currencyid = new SimpleIntegerProperty();
  }

  /**
  * Método para devolver el id de la compañía.
  *
  * @return id -> identificador de la compañía.
  *
  */
  public Integer getId() {
    return id.get();
  }

  /**
  * Método para asignar id a la compañía.
  *
  * @param id id a asignar a la compañía.
  */
  public void setId(Integer id) {
    this.id.set(id);
  }

  /**
  * Método para devolver el nombre de la compañía.
  *
  * @return name -> nombre de la compañía.
  */
  public String getName() {
    return name.get();
  }

  /**
  * Método para asignar nombre a la compañía.
  *
  * @param name nombre a asignar a la compañía.
  */
  public void setName(String name) {
    this.name.set(name);
  }

  /**
  * Método para devolver el id de socio de la compañía.
  *
  * @return partnerid -> id de socio de la compañía.
  */
  public Integer getPartnerid() {
    return partnerid.get();
  }

  /**
  * Método para asignar el id de socio a la compañía.
  *
  * @param partnerid id de socio a asignar a la compañía.
  */
  public void setPartnerid(Integer partnerid) {
    this.partnerid.set(partnerid);
  }

  /**
  * Método para devolver el id de la moneda de la compañía.
  *
  * @return currencyid -> id de moneda de la compañía.
  */
  public Integer getCurrencyid() {
    return currencyid.get();
  }

  /**
  * Método para asignar el id de la moneda a la compañía.
  *
  * @param currencyid id de moneda a asignar a la compañía.
  */
  public void setCurrencyid(Integer currencyid) {
    this.currencyid.set(currencyid);
  }
}
