package com.seatlock.seatlock.common;

public class InvalidCredentialsException extends RuntimeException{

    public  InvalidCredentialsException(){
        super("Invalid email or password");
    }
}
