package com.sahil.assignment.exception;

public class BotCapExceededException extends RuntimeException{

    public BotCapExceededException(String message){
        super(message);
    }
}
