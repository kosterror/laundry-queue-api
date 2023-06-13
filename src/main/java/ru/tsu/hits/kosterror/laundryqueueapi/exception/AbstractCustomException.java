package ru.tsu.hits.kosterror.laundryqueueapi.exception;

public abstract class AbstractCustomException extends RuntimeException {

    private final int code;

    protected AbstractCustomException(int code) {
        super();
        this.code = code;
    }

    protected AbstractCustomException(int code, String message) {
        super(message);
        this.code = code;
    }

    protected AbstractCustomException(int code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
    }

    protected AbstractCustomException(int code, Throwable cause) {
        super(cause);
        this.code = code;
    }

    public int getCode() {
        return code;
    }

}
