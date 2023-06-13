package ru.tsu.hits.kosterror.laundryqueueapi.exception;

public class EmailAlreadyUsedException extends AbstractCustomException {

    public EmailAlreadyUsedException(int code) {
        super(code);
    }

    public EmailAlreadyUsedException(int code, String message) {
        super(code, message);
    }

    public EmailAlreadyUsedException(int code, String message, Throwable cause) {
        super(code, message, cause);
    }

    public EmailAlreadyUsedException(int code, Throwable cause) {
        super(code, cause);
    }

}
