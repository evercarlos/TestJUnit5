package com.tecsofec.junit5app.ejemplos.model;

import com.tecsofec.junit5app.ejemplos.exception.DineroInsuficienteException;
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
        assertNotNull(actual);
        assertEquals(esperando, actual); // asertar si son iguales con el esperado
        assertTrue(actual.equals("Ever")); // asertar si es verdadero
    }

    @Test
    void testSaldoCuenta() {
        Cuenta cuenta = new Cuenta("Ever", new BigDecimal("1000.34544"));
        assertNotNull(cuenta.getSaldo());
        assertEquals(1000.34544, cuenta.getSaldo().doubleValue());
        // compareTo: -1: saldo menor que cero 0: mayor que el saldo
        assertFalse(cuenta.getSaldo().compareTo(BigDecimal.ZERO)<0); // comprando cuenta.getSaldo().compareTo(BigDecimal.ZERO), 0
        assertTrue(cuenta.getSaldo().compareTo(BigDecimal.ZERO)>0); //true
    }

    // test Driven Development

    @Test
    void testReferenciaCuenta() {
        // instancias distintas por memoria, sale error: Para ello se implementa el método equals
        Cuenta cuenta = new Cuenta("Ever", new BigDecimal("1000.458458"));
        Cuenta cuenta2 = new Cuenta("Ever", new BigDecimal("1000.458458"));
        // assertNotEquals(cuenta2, cuenta);
        assertEquals(cuenta2, cuenta);
    }

    @Test
    void testDebitoCuenta() {
        Cuenta c = new Cuenta("Ever", new BigDecimal("1000.12345"));
        c.debito(new BigDecimal(100));
        assertNotNull(c.getSaldo());
        assertEquals(900, c.getSaldo().intValue());// ya qu es decimal se agrega intValue
        assertEquals("900.12345", c.getSaldo().toPlainString());// string plano
    }

    @Test
    void testCreditoCuenta() {
        Cuenta c = new Cuenta("Ever", new BigDecimal("1000.12345"));
        c.credito(new BigDecimal(100));
        assertNotNull(c.getSaldo());
        assertEquals(1100, c.getSaldo().intValue());// ya qu es decimal se agrega intValue
        assertEquals("1100.12345", c.getSaldo().toPlainString());// string plano
    }

    @Test
    void testDineroInsuficienteException(){
        Cuenta cuenta = new Cuenta("Ever", new BigDecimal("1000.12345"));
        Exception exception = assertThrows(DineroInsuficienteException.class, ()-> {
            cuenta.debito(new BigDecimal(1500));
        });
       String actual = exception.getMessage();
       String esperado = "Dinero Insuficiente";
       assertEquals(esperado, actual);
    }

    @Test
    void testTransferirDineroCuentas() {
        Cuenta cuenta1 = new Cuenta("Ever", new BigDecimal("2500"));
        Cuenta cuenta2 = new Cuenta("Maria", new BigDecimal("1500.8989"));

        Banco banco =  new Banco();
        banco.setNombre("BCP");
        banco.transferir(cuenta2, cuenta1, new BigDecimal(500));// origen(retirar 500 de esta cuenta), destino (depositar 500 a esta cuenta)
        assertEquals("1000.8989", cuenta2.getSaldo().toPlainString()); // retirar : 1500.8989-->1000.8989
        assertEquals("3000", cuenta1.getSaldo().toPlainString());// depositar
    }

    @Test
    void testRelacionBancoCuentas() {
        Cuenta cuenta1 = new Cuenta("Ever", new BigDecimal("2500"));
        Cuenta cuenta2 = new Cuenta("Maria", new BigDecimal("1500.8989"));

        Banco banco =  new Banco();
        banco.addCuenta(cuenta1);
        banco.addCuenta(cuenta2);

        banco.setNombre("BCP");
        banco.transferir(cuenta2, cuenta1, new BigDecimal(500));
        assertEquals("1000.8989", cuenta2.getSaldo().toPlainString());
        assertEquals("3000", cuenta1.getSaldo().toPlainString());

        assertEquals(2, banco.getCuentas().size());
        assertEquals("BCP", cuenta1.getBanco().getNombre());

    }
}







