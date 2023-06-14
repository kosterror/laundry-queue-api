package ru.tsu.hits.kosterror.laundryqueueapi.exception;

public class UnauthorizedException extends AbstractCustomException {

    public UnauthorizedException(int code) {
        super(code);
    }

    public UnauthorizedException(int code, String message) {
        super(code, message);
    }

    public UnauthorizedException(int code, String message, Throwable cause) {
        super(code, message, cause);
    }

    public UnauthorizedException(int code, Throwable cause) {
        super(code, cause);
    }

}
