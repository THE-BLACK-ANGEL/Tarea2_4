package com.example.terea2_3.Modelos;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Company {
    private SimpleIntegerProperty id;
    private SimpleStringProperty name;
    private SimpleIntegerProperty partner_id;
    private SimpleIntegerProperty currency_id;

    public Company() {
        this.id = new SimpleIntegerProperty();
        this.name = new SimpleStringProperty();
        this.partner_id = new SimpleIntegerProperty();
        this.currency_id = new SimpleIntegerProperty();
    }

    public Integer getId() {
        return id.get();
    }

    public void setId(Integer id) {
        this.id.set(id);
    }

    public String getName() {
        return name.get();
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public Integer getPartner_id() {
        return partner_id.get();
    }

    public void setPartner_id(Integer partner_id) {
        this.partner_id.set(partner_id);
    }

    public Integer getCurrency_id() {
        return currency_id.get();
    }

    public void setCurrency_id(Integer currency_id) {
        this.currency_id.set(currency_id);
    }
}
