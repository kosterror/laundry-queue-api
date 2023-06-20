package ru.tsu.hits.kosterror.laundryqueueapi.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.tsu.hits.kosterror.laundryqueueapi.dto.api.ApiError;
import ru.tsu.hits.kosterror.laundryqueueapi.exception.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ControllerAdvice
@Slf4j
public class ExceptionHandlingController {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleInvalidRequestBody(HttpServletRequest request,
                                                             MethodArgumentNotValidException exception
    ) {
        logError(request, exception);
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
        Map<String, List<String>> messages = new HashMap<>();

        exception
                .getBindingResult()
                .getAllErrors()
                .forEach(error -> {
                    String fieldName = ((FieldError) error).getField();
                    String message = error.getDefaultMessage();

                    if (message != null) {
                        if (messages.containsKey(fieldName)) {
                            messages.get(fieldName).add(message);
                        } else {
                            List<String> newMessageList = new ArrayList<>();
                            newMessageList.add(message);

                            messages.put(fieldName, newMessageList);
                        }
                    }

                });

        ApiError error = new ApiError(
                httpStatus.value(),
                "Тело запроса не прошло валидацию",
                messages
        );

        return new ResponseEntity<>(error, httpStatus);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ApiError> handleBadRequestException(HttpServletRequest request,
                                                              BadRequestException exception
    ) {
        logError(request, exception);
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;

        ApiError error = new ApiError(
                httpStatus.value(),
                exception.getMessage()
        );

        return new ResponseEntity<>(error, httpStatus);
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ApiError> handleUnauthorizedException(HttpServletRequest request,
                                                                UnauthorizedException exception
    ) {
        logError(request, exception);
        HttpStatus httpStatus = HttpStatus.UNAUTHORIZED;

        ApiError error = new ApiError(
                httpStatus.value(),
                exception.getMessage()
        );

        return new ResponseEntity<>(error, httpStatus);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ApiError> handleNotFoundException(HttpServletRequest request,
                                                            NotFoundException exception
    ) {
        logError(request, exception);
        HttpStatus httpStatus = HttpStatus.NOT_FOUND;

        ApiError error = new ApiError(
                httpStatus.value(),
                exception.getMessage()
        );

        return new ResponseEntity<>(error, httpStatus);
    }

    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<ApiError> handleConflictException(HttpServletRequest request,
                                                            ConflictException exception
    ) {
        logError(request, exception);
        HttpStatus httpStatus = HttpStatus.CONFLICT;

        ApiError error = new ApiError(
                httpStatus.value(),
                exception.getMessage()
        );

        return new ResponseEntity<>(error, httpStatus);
    }

    @ExceptionHandler(InternalServerException.class)
    public ResponseEntity<ApiError> handleInternalServerException(HttpServletRequest request,
                                                                  InternalServerException exception
    ) {
        logError(request, exception);
        HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;

        ApiError error = new ApiError(
                httpStatus.value(),
                "Внутренняя ошибка сервера"
        );

        return new ResponseEntity<>(error, httpStatus);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleException(HttpServletRequest request,
                                                    Exception exception
    ) {
        logError(request, exception);
        HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;

        ApiError error = new ApiError(
                httpStatus.value(),
                "Непредвиденная внутренняя ошибка сервера"
        );

        return new ResponseEntity<>(error, httpStatus);
    }

    private void logError(HttpServletRequest request, Exception exception) {
        log.error("Возникла ошибка при запросе: {} {}.",
                request.getMethod(),
                request.getRequestURL(),
                exception
        );
    }

}
