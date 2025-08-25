package com.example.demo.Exception;

public class NotFoundException extends RuntimeException{

    public NotFoundException(Long id, String entity){
        super ( entity + " with ID: " + id + " not found!");
    }
}
