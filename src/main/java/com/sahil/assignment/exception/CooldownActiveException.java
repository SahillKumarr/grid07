package com.sahil.assignment.exception;

public class CooldownActiveException extends RuntimeException{

    public CooldownActiveException(String message){
        super(message);
    }
}
