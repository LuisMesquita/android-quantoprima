package br.com.mobiclub.quantoprima.core.service.api.exception;

public class AppBlockedException extends RuntimeException {
    public AppBlockedException(String message) {
        super(message);
    }
}