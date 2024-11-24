package com.example.demo.exception.notFound;

public class EntityNotFoundException extends RuntimeException {
    public EntityNotFoundException(String str,Long id) {
        super("ERROR :\n"+str+" with ID " + id + " not found.");
    }
}