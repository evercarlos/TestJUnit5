package com.tecsofec.junit5app.ejemplos.model;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author EVER C.R
 */
class CuentaTest {

    @Test
    void testNombreCuenta(){
        Cuenta cuenta = new Cuenta("Ever", new BigDecimal("1000.32467567")); // "" por tema de precisión
        // cuenta.setPersona("Ever");
        String esperando = "Ever"; // expected
        String actual = cuenta.getPersona();
        assertEquals(esperando, actual); // asertar si son iguales con el esperado
        assertTrue(actual.equals("Ever")); // asertar si es verdadero
    }

    @Test
    void testSaldoCuenta() {
        Cuenta cuenta = new Cuenta("Ever", new BigDecimal("1000.34544"));
        assertEquals(1000.34544, cuenta.getSaldo().doubleValue());
        // compareTo: -1: saldo menor que cero 0: mayor que el saldo
        assertFalse(cuenta.getSaldo().compareTo(BigDecimal.ZERO)<0); // comprando cuenta.getSaldo().compareTo(BigDecimal.ZERO), 0
        assertTrue(cuenta.getSaldo().compareTo(BigDecimal.ZERO)>0); //true
    }
}