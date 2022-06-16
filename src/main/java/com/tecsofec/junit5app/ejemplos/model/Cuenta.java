package com.tecsofec.junit5app.ejemplos.model;

import java.math.BigDecimal;

/**
 * @author EVER C.R
 */
public class Cuenta {

    private String persona;
    private BigDecimal saldo;

    public String getPersona() {
        return persona;
    }

    public void setPersona(String persona) {
        this.persona = persona;
    }
}
