package ru.tsu.hits.kosterror.laundryqueueapi.exception;

public class NotFoundException extends AbstractCustomException {


    public NotFoundException(int code) {
        super(code);
    }

    public NotFoundException(int code, String message) {
        super(code, message);
    }

    public NotFoundException(int code, String message, Throwable cause) {
        super(code, message, cause);
    }

    public NotFoundException(int code, Throwable cause) {
        super(code, cause);
    }

}
