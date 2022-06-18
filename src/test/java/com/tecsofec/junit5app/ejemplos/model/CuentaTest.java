package com.tecsofec.junit5app.ejemplos.model;

import com.tecsofec.junit5app.ejemplos.exception.DineroInsuficienteException;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.condition.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.*;

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

    // Si no halla el SO le desabilita, en este caso solo se ejecuta en linux, windows
    // test anidados
    @Nested // test anidados con class
    @DisplayName("Probando atributos de SO")
    class SistemaOperativo {
        @Test
        @EnabledOnOs({OS.LINUX, OS.WINDOWS})
        void testSoloLinuxMac() {

        }

        @Test
        @EnabledOnOs(OS.WINDOWS)
        void testWindows() {

        }

        @Test
        @DisabledOnOs(OS.WINDOWS)
        void testNoWindows() {

        }
    }

    @Nested
    class JavaVersionTest{
        @Test
        @EnabledOnJre(JRE.JAVA_8)// Sólo funciona en JDK8
        void soloJdk8(){

        }

        @Test
        @EnabledOnJre(JRE.JAVA_15)
        void soloJdk15(){

        }

        @Test
        @DisabledOnJre(JRE.JAVA_15)
        void testNoJdk15(){

        }
    }

    @Nested
    class SistemPropertiesTest {

        @Test
        void imprimirSystemProperties() {
            Properties properties = System.getProperties();
            properties.forEach((k, v)-> System.out.println(k+" : "+v));
        }

        @Test
        @EnabledIfSystemProperty(named = "java.version", matches = ".*15.*")
        void testJavaVersion(){

        }

        @Test
        @DisabledIfSystemProperty(named = "os.arch", matches = ".*32.*")
        void testSolo64(){

        }

        @Test
        @EnabledIfSystemProperty(named = "os.arch", matches = ".*32.*")
        void testNo64(){

        }

        @Test
        @EnabledIfSystemProperty(named = "user.name", matches = "1227901")
        void testUsername(){

        }

        // este se crea al dar clic despues del martillo
        @Test
        @EnabledIfSystemProperty(named = "ENV", matches = "dev")
        void testDev(){

        }
    }

    class VariableAmbienteTest{
        // recorremos variable de enviante del SO
        @Test
        void imprimirVariableAmbiente(){
            Map<String, String> getenv = System.getenv();
            getenv.forEach((k,v) -> System.out.println(k+" ="+v));
        }

        @Test
        @EnabledIfEnvironmentVariable(named = "JAVA_HOME", matches = ".*jdk1.8.0_202.*")
        void testJavaHome() {

        }

        @Test
        @EnabledIfEnvironmentVariable(named = "NUMBER_OF_PROCESSORS", matches = "12")
        void testProcesadores(){

        }

        @Test
        @EnabledIfEnvironmentVariable(named = "ENVIRONMENT", matches = "dev")
        void testEnv(){

        }

        @Test
        @DisabledIfEnvironmentVariable(named = "ENVIRONMENT", matches = "prod")
        void testEnvProdDisabled(){

        }
    }

    // assumptions: suposiciones
    //Ejmplos: Si un servidor esta arriba
    @Test
    @DisplayName("Test saldo cuenta Dev")
    void testSaldoCuentaDev() {
        boolean esDev = "dev".equals(System.getProperty("ENV"));// env: configuracion del arranque
        assumeTrue(esDev);// si no se cumple todo  después del assume no se ejecuta(codigo del método)
        assertNotNull(cuenta.getSaldo());
        assertEquals(1000.34544, cuenta.getSaldo().doubleValue());
        // compareTo: -1: saldo menor que cero 0: mayor que el saldo
        assertFalse(cuenta.getSaldo().compareTo(BigDecimal.ZERO)<0); // comprando cuenta.getSaldo().compareTo(BigDecimal.ZERO), 0
        assertTrue(cuenta.getSaldo().compareTo(BigDecimal.ZERO)>0); //true
    }

    @Test
    @DisplayName("Test saldo cuenta Dev 2")
    void testSaldoCuentaDev2() {
        boolean esDev = "dev".equals(System.getProperty("ENV"));// env: configuracion del arranque
        assumingThat(esDev, () ->{// si se cumple pasa por aqui, sino pasa defrente
            assertNotNull(cuenta.getSaldo());
            assertEquals(1000.34544, cuenta.getSaldo().doubleValue());
            // compareTo: -1: saldo menor que cero 0: mayor que el saldo
        });
        assertFalse(cuenta.getSaldo().compareTo(BigDecimal.ZERO)<0); // comprando cuenta.getSaldo().compareTo(BigDecimal.ZERO), 0
        assertTrue(cuenta.getSaldo().compareTo(BigDecimal.ZERO)>0); //true
    }


    // @RepeatedTest(5)// repite 5 veces
    @DisplayName("Probando Debito Cuenta Repetir!")
    @RepeatedTest(value = 5, name = "{displayName} - Repetir número {currentRepetition} de {totalRepetitions}")
    // tambien podemos inyectar
    void testDebitoCuentaRepetir(RepetitionInfo info) {
        if(info.getCurrentRepetition()==3){
            System.out.println("Estamos en la repetición "+info.getCurrentRepetition());
        }
        Cuenta c = new Cuenta("Ever", new BigDecimal("1000.12345"));
        c.debito(new BigDecimal(100));
        assertNotNull(c.getSaldo());
        assertEquals(900, c.getSaldo().intValue());// ya qu es decimal se agrega intValue
        assertEquals("900.12345", c.getSaldo().toPlainString());// string plano
    }


    @ParameterizedTest(name = "Numero {index} ejecutando con valor {0} - {argumentsWithNames}")
    @ValueSource(strings = {"100", "200", "300", "500", "700", "1000"})// tambien puede ser Strings
    void testDebitoCuenta1(String monto) {//inyectando
        Cuenta c = new Cuenta("Ever", new BigDecimal("1000.12345"));

        c.debito(new BigDecimal(monto));

        assertNotNull(c.getSaldo());
        assertTrue(c.getSaldo().compareTo(BigDecimal.ZERO)>0);
    }

    @ParameterizedTest(name = "Numero {index} ejecutando con valor {0} - {argumentsWithNames}")
    // 1, 2,(son los ididices)
    @CsvSource({"1,100", "2,200", "3,300", "4,500", "5,500", "6,1000.11"})// tambien puede ser Strings
    void testDebitoCuentaCsvSource(String index, String monto) {
        Cuenta c = new Cuenta("Ever", new BigDecimal("1000.12345"));

        System.out.println(index+" -> "+monto);
        c.debito(new BigDecimal(monto));

        assertNotNull(c.getSaldo());
        assertTrue(c.getSaldo().compareTo(BigDecimal.ZERO)>0);
    }

    @ParameterizedTest(name = "Numero {index} ejecutando con valor {0} - {argumentsWithNames}")
    @CsvFileSource(resources = "/data.csv")
    void testDebitoCuentaCsvFileSource(String monto) {
        Cuenta c = new Cuenta("Ever", new BigDecimal("1000.12345"));

        c.debito(new BigDecimal(monto));

        assertNotNull(c.getSaldo());
        assertTrue(c.getSaldo().compareTo(BigDecimal.ZERO)>0);
    }

    @ParameterizedTest(name = "Numero {index} ejecutando con valor {0} - {argumentsWithNames}")
    @MethodSource("montoList")
    void testDebitoCuentaMethodSource(String monto) {
        Cuenta c = new Cuenta("Ever", new BigDecimal("1000.12345"));

        c.debito(new BigDecimal(monto));

        assertNotNull(c.getSaldo());
        assertTrue(c.getSaldo().compareTo(BigDecimal.ZERO)>0);
    }

    static List<String> montoList(){
        return Arrays.asList("100", "200", "300", "500", "700", "1000");

    }



}







