package com.tecsofec.junit5app.ejemplos.model;

import com.tecsofec.junit5app.ejemplos.exception.DineroInsuficienteException;
import org.junit.jupiter.api.*;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author EVER C.R
 */
// el ciclo de vida es por método
// agregado cuando se implementó before,after
// @TestInstance(TestInstance.Lifecycle.PER_CLASS)// per clase BeforeAll, afterAll se quita el "static"
class CuentaTest {

    Cuenta cuenta;

    /* second class*/
    @BeforeEach // Se ejecute antes de cada método
    void initMetodoTest(){
        this.cuenta = new Cuenta("Ever", new BigDecimal("1000.34544"));
        System.out.println("iniciando el método");
    }

    @AfterEach // Se ejecuta despúes de cada método
    void tearDown(){
        System.out.println("Finalizando el método de prueba.");
    }

    @BeforeAll
    static void beforeAll() {// si quito static sale error porqe la instancia todavía no fue creado
        // se puede usar para conextar algún recurso
        System.out.println("::::::Unicializando el test::::::");
    }

    @AfterAll
    static void afterAll(){
        System.out.println("::::::::Finalizando el test::::::");
    }

    // POR CADA METODO SE CREA AUTOMATICAMENTE UNA INSTANCIA
    @Test
    @DisplayName("Probando nombre de la cuenta")// al ejecutar en el result remplaza al testNombreCuenta()
    void testNombreCuenta(){
        // Cuenta cuenta = new Cuenta("Ever", new BigDecimal("1000.32467567")); // "" por tema de precisión
        // cuenta.setPersona("Ever");
        String esperando = "Ever"; // expected
        String actual = cuenta.getPersona();
        //assertNotNull(actual);
        //assertNotNull(actual, "La cuenta no puede ser nula");
        assertNotNull(actual, () ->"La cuenta no puede ser nula"); // ()-> para evitar qu se crea el objeto(String actual) cuando no sale error
        assertEquals(esperando, actual, ()->"El nombre de la cuenta no es el que se esperaba: Se esperaba "+esperando+" Sin embargo fue "+actual); // asertar si son iguales con el esperado
        assertTrue(actual.equals("Ever"), ()->"Nombre de la cuenta esperada debe ser a la esperad"); // asertar si es verdadero
    }

    @Test
    @DisplayName("Probando el saldo de la cuenta corriente, que no sea null, mayor que cero, valor esperado")
    void testSaldoCuenta() {
        // cuenta = new Cuenta("Ever", new BigDecimal("1000.34544"));
        assertNotNull(cuenta.getSaldo());
        assertEquals(1000.34544, cuenta.getSaldo().doubleValue());
        // compareTo: -1: saldo menor que cero 0: mayor que el saldo
        assertFalse(cuenta.getSaldo().compareTo(BigDecimal.ZERO)<0); // comprando cuenta.getSaldo().compareTo(BigDecimal.ZERO), 0
        assertTrue(cuenta.getSaldo().compareTo(BigDecimal.ZERO)>0); //true
    }

    // test Driven Development

    @Test
    @DisplayName("Testedando referencias que sean iguales con el método equals")
    void testReferenciaCuenta() {
        // instancias distintas por memoria, sale error: Para ello se implementa el método equals
        cuenta = new Cuenta("Ever", new BigDecimal("1000.458458"));
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
        cuenta = new Cuenta("Ever", new BigDecimal("1000.12345"));
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
    //@Disabled// para qu pase por alto el error(la prueba) // Se muestra en el reporte
    @DisplayName("Probando relaciones entre las cuentas y el banco con assertAll.")
    void testRelacionBancoCuentas() {
        //fail(); // Aseguramos qu falle el método
        Cuenta cuenta1 = new Cuenta("Ever", new BigDecimal("2500"));
        Cuenta cuenta2 = new Cuenta("Maria", new BigDecimal("1500.8989"));

        Banco banco =  new Banco();
        banco.addCuenta(cuenta1);
        banco.addCuenta(cuenta2);

        banco.setNombre("BCP");
        banco.transferir(cuenta2, cuenta1, new BigDecimal(500));
        // para agrupar asserts: al haver un error tambien muestra los demas errores
        assertAll(
                () ->  assertEquals("1000.8989", cuenta2.getSaldo().toPlainString(),
                        ()->"El valor del saldo de la cuenta2 no es el esperado"),// Sin llaves es más limpio
                () -> {
                    assertEquals("3000", cuenta1.getSaldo().toPlainString(),
                            ()->"El valor del saldo de la cuenta1 no es el esperado");
                },
                () -> {
                    assertEquals(2, banco.getCuentas().size(),
                            ()->"el banco no tiene las cuentas esperadas");
                },
                () -> {
                    assertEquals("BCP", cuenta1.getBanco().getNombre());
                },
                () -> {
                    assertEquals("Ever", banco.getCuentas().stream()
                            .filter(c -> c.getPersona().equals("Ever"))
                            .findFirst()
                            .get().getPersona());
                },
                () -> {
                    assertTrue(banco.getCuentas().stream()
                            .anyMatch(c->c.getPersona().equals("Ever")));
                }
        );



        /*assertTrue(banco.getCuentas().stream()
                .filter(c -> c.getPersona().equals("Ever"))
                .findFirst().isPresent());*/
    }
}







