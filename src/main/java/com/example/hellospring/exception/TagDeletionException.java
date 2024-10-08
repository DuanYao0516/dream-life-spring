package com.example.hellospring.exception;

public class TagDeletionException extends RuntimeException {
    public TagDeletionException(String message) {
        super(message);
    }
}
