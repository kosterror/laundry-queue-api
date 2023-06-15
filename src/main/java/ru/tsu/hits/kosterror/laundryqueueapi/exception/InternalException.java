package ru.tsu.hits.kosterror.laundryqueueapi.exception;

public class InternalException extends AbstractCustomException {


    public InternalException(int code) {
        super(code);
    }

    public InternalException(int code, String message) {
        super(code, message);
    }

    public InternalException(int code, String message, Throwable cause) {
        super(code, message, cause);
    }

    public InternalException(int code, Throwable cause) {
        super(code, cause);
    }

}
