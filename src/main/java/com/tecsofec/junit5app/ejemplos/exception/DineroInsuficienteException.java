package com.tecsofec.junit5app.ejemplos.exception;

/**
 * @author EVER C.R
 */
public class DineroInsuficienteException  extends  RuntimeException{

    public DineroInsuficienteException(String message) {
        super(message);
    }
}
