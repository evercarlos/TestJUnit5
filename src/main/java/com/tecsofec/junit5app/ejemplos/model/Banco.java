package com.tecsofec.junit5app.ejemplos.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * @author EVER C.R
 */
public class Banco {
    private String nombre;
    private List<Cuenta> cuentas;


    public Banco() {
        cuentas = new ArrayList<>();
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public List<Cuenta> getCuentas() {
        return cuentas;
    }

    public void setCuentas(List<Cuenta> cuentas) {
        this.cuentas = cuentas;
    }

    public void transferir(Cuenta origen, Cuenta destino, BigDecimal monto){
        origen.debito(monto);// retira
        destino.credito(monto);//deposita
    }

    public void addCuenta(Cuenta cuenta){
        cuentas.add(cuenta);
        cuenta.setBanco(this); // assertEquals("BCP", cuenta1.getBanco().getNombre());
    }


}
