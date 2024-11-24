package com.example.demo.exception;

public class EntityNotFoundException extends RuntimeException {
    public EntityNotFoundException(String str,Long id) {
        super(str+" with ID " + id + " not found.");
    }
    public EntityNotFoundException(String str,String name) {
        super(str+" with name " + name + " not found.");
    }
}