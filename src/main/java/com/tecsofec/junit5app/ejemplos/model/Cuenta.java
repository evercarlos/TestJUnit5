package com.tecsofec.junit5app.ejemplos.model;

import java.math.BigDecimal;

/**
 * @author EVER C.R
 */
public class Cuenta {

    private String persona;
    private BigDecimal saldo;


    public Cuenta(String persona, BigDecimal saldo) {
        this.persona = persona;
        this.saldo = saldo;
    }

    public BigDecimal getSaldo() {
        return saldo;
    }

    public void setSaldo(BigDecimal saldo) {
        this.saldo = saldo;
    }

    public String getPersona() {
        return persona;
    }

    public void setPersona(String persona) {
        this.persona = persona;
    }

    // Agregado para comparar los objetos
    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof  Cuenta)){ // obj == null ||
            return  false;
        }
        Cuenta cuenta = (Cuenta) obj;
        if(this.persona == null || this.saldo == null) {
            return  false;
        }
        return this.persona.equals(cuenta.getPersona()) && this.saldo.equals(cuenta.getSaldo());
    }

}
