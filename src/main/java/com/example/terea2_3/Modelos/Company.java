package com.example.terea2_3.Modelos;

public class Company {
    private Integer id ;
    private String name ;
    private Integer partner_id ;
    private Integer currency_id ;

    //GETTER & SETTERS

    public void setName(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }

    public void setId(Integer id) {
        this.id = id;
    }
    public Integer getId() {
        return id;
    }

    public void setPartner_id(Integer partner_id) {
        this.partner_id = partner_id;
    }
    public Integer getPartner_id() {
        return partner_id;
    }

    public void setCurrency_id(Integer currency_id) {
        this.currency_id = currency_id;
    }
    public Integer getCurrency_id() {
        return currency_id;
    }
}
