package ru.tsu.hits.kosterror.laundryqueueapi.exception;

public class ForbiddenException extends RuntimeException {

    public ForbiddenException(String message) {
        super(message);
    }
}
