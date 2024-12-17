package com.example.demo.exception;

public class InvalidPasswordException extends RuntimeException{
    public InvalidPasswordException() {
        super("You have entered the wrong password");
    }
}
