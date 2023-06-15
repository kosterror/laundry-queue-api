package ru.tsu.hits.kosterror.laundryqueueapi.exception;

public class BadRequestException extends AbstractCustomException {

    public BadRequestException(int code) {
        super(code);
    }

    public BadRequestException(int code, String message) {
        super(code, message);
    }

    public BadRequestException(int code, String message, Throwable cause) {
        super(code, message, cause);
    }

    public BadRequestException(int code, Throwable cause) {
        super(code, cause);
    }

}
