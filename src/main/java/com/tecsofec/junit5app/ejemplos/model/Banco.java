package com.tecsofec.junit5app.ejemplos.model;

import java.math.BigDecimal;

/**
 * @author EVER C.R
 */
public class Banco {
    private String nombre;

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void transferir(Cuenta origen, Cuenta destino, BigDecimal monto){

    }
}
