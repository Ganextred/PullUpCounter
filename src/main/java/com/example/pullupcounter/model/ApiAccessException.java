package com.example.pullupcounter.model;

public class ApiAccessException extends Exception {
    public final String gameName;
    public ApiAccessException(Throwable cause, String gameName) {
        super(cause);
        this.gameName = gameName;
    }
}
