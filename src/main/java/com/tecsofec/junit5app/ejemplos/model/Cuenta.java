package com.tecsofec.junit5app.ejemplos.model;

import com.tecsofec.junit5app.ejemplos.exception.DineroInsuficienteException;

import java.math.BigDecimal;

/**
 * @author EVER C.R
 */
public class Cuenta {

    private String persona;
    private BigDecimal saldo;
    private Banco banco;


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

    public Banco getBanco() {
        return banco;
    }

    public void setBanco(Banco banco) {
        this.banco = banco;
    }

    public String getPersona() {
        return persona;
    }

    public void setPersona(String persona) {
        this.persona = persona;
    }

    public void debito(BigDecimal monto) {
      BigDecimal nuevoSaldo=  this.saldo.subtract(monto);
      if (nuevoSaldo.compareTo(BigDecimal.ZERO)< 0){
          throw new DineroInsuficienteException("Dinero Insuficiente");
      }
      this.saldo = nuevoSaldo;
    }

    public void credito(BigDecimal monto){
       this.saldo= this.saldo.add(monto);
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
