package br.com.mobiclub.quantoprima.core.service.api.exception;

public class AppOutdatedException extends RuntimeException {
    public AppOutdatedException(String message) {
        super(message);
    }
}